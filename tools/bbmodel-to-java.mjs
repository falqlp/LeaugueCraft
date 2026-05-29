#!/usr/bin/env node
import fs from "node:fs";
import path from "node:path";

const args = parseArgs(process.argv.slice(2));

if (args.help || !args.input || !args.output || !args.package || !args.class) {
  printHelp();
  process.exit(args.help ? 0 : 1);
}

const bbmodel = JSON.parse(fs.readFileSync(args.input, "utf8"));
const elements = new Map((bbmodel.elements ?? []).filter((element) => element.export !== false).map((element) => [element.uuid, element]));
const groups = new Map((bbmodel.groups ?? []).filter((group) => group.export !== false).map((group) => [group.uuid, group]));
const usedNames = new Map();
const partLines = [];
const rootNames = new Set();

for (const entry of bbmodel.outliner ?? []) {
  emitOutlinerEntry("root", [0, 0, 0], entry);
}

if (args.ensureJungleParts) {
  for (const required of ["head", "right_front_leg", "left_front_leg", "right_back_leg", "left_back_leg", "tail"]) {
    if (!rootNames.has(required)) {
      partLines.push(`    root.addOrReplaceChild("${required}", CubeListBuilder.create(), PartPose.ZERO);`);
    }
  }
}

const width = bbmodel.resolution?.width ?? 64;
const height = bbmodel.resolution?.height ?? 64;
const javaSource = renderJavaSource(width, height);

fs.mkdirSync(path.dirname(args.output), { recursive: true });
fs.writeFileSync(args.output, javaSource);

if (args.copySource) {
  fs.mkdirSync(path.dirname(args.copySource), { recursive: true });
  fs.copyFileSync(args.input, args.copySource);
}

if (args.extractTexture) {
  const texture = (bbmodel.textures ?? []).find((entry) => typeof entry.source === "string" && entry.source.startsWith("data:image/png;base64,"));
  if (texture) {
    fs.mkdirSync(path.dirname(args.extractTexture), { recursive: true });
    fs.writeFileSync(args.extractTexture, Buffer.from(texture.source.replace(/^data:image\/png;base64,/, ""), "base64"));
  }
}

function emitOutlinerEntry(parentVar, parentOriginMc, entry) {
  const uuid = typeof entry === "string" ? entry : entry.uuid;
  if (!uuid) {
    return;
  }

  const group = groups.get(uuid);
  if (group) {
    emitGroup(parentVar, parentOriginMc, group, typeof entry === "object" ? entry.children : undefined);
    return;
  }

  const element = elements.get(uuid);
  if (element) {
    emitElement(parentVar, parentOriginMc, element);
  }
}

function emitGroup(parentVar, parentOriginMc, group, outlinerChildren) {
  const name = uniqueName(cleanName(group.name || "group"));
  if (parentVar === "root") {
    rootNames.add(name);
  }

  const originMc = blockbenchToMinecraftOrigin(group.origin ?? [0, 0, 0]);
  const offset = subtractOrigin(originMc, parentOriginMc);
  const rotation = rotationToMinecraft(group.rotation ?? [0, 0, 0]);
  const varName = cleanName(name);
  partLines.push(`    PartDefinition ${varName} = ${parentVar}.addOrReplaceChild("${name}", CubeListBuilder.create(), ${pose(offset, rotation)});`);

  for (const child of outlinerChildren ?? group.children ?? []) {
    emitOutlinerEntry(varName, originMc, child);
  }
}

function emitElement(parentVar, parentOriginMc, element) {
  const baseName = element.name && element.name !== "cube" ? element.name : `${parentVar}_cube`;
  const name = uniqueName(cleanName(baseName));
  if (parentVar === "root") {
    rootNames.add(name);
  }

  const originMc = blockbenchToMinecraftOrigin(element.origin ?? [0, 0, 0]);
  const offset = subtractOrigin(originMc, parentOriginMc);
  const rotation = rotationToMinecraft(element.rotation ?? [0, 0, 0]);
  const box = cubeFor(element, originMc);
  partLines.push(`    ${parentVar}.addOrReplaceChild("${name}", CubeListBuilder.create()${box}, ${pose(offset, rotation)});`);
}

function cubeFor(element, originMc) {
  const from = element.from ?? [0, 0, 0];
  const to = element.to ?? [0, 0, 0];
  const uv = element.uv_offset ?? [0, 0];
  const x = from[0] - originMc[0];
  const y = 24 - to[1] - originMc[1];
  const z = from[2] - originMc[2];
  const dx = to[0] - from[0];
  const dy = to[1] - from[1];
  const dz = to[2] - from[2];

  return `.texOffs(${uv[0]}, ${uv[1]}).addBox(${float(x)}, ${float(y)}, ${float(z)}, ${float(dx)}, ${float(dy)}, ${float(dz)})`;
}

function blockbenchToMinecraftOrigin(origin) {
  return [origin[0] ?? 0, 24 - (origin[1] ?? 0), origin[2] ?? 0];
}

function subtractOrigin(origin, parentOrigin) {
  return [origin[0] - parentOrigin[0], origin[1] - parentOrigin[1], origin[2] - parentOrigin[2]];
}

function rotationToMinecraft(rotation) {
  const x = degreesToRadians(rotation[0] ?? 0) * (args.flipXRot ? -1 : 1);
  const y = degreesToRadians(rotation[1] ?? 0) * (args.flipYRot ? -1 : 1);
  const z = degreesToRadians(rotation[2] ?? 0) * (args.flipZRot ? -1 : 1);
  return [x, y, z];
}

function pose(offset, rotation) {
  if (rotation.some((value) => Math.abs(value) > 0)) {
    return `PartPose.offsetAndRotation(${float(offset[0])}, ${float(offset[1])}, ${float(offset[2])}, ${float(rotation[0])}, ${float(rotation[1])}, ${float(rotation[2])})`;
  }

  if (offset.every((value) => Math.abs(value) < 0.00000000001)) {
    return "PartPose.ZERO";
  }

  return `PartPose.offset(${float(offset[0])}, ${float(offset[1])}, ${float(offset[2])})`;
}

function renderJavaSource(width, height) {
  const imports = [
    "net.minecraft.client.model.geom.ModelPart",
    "net.minecraft.client.model.geom.PartPose",
    "net.minecraft.client.model.geom.builders.CubeListBuilder",
    "net.minecraft.client.model.geom.builders.LayerDefinition",
    "net.minecraft.client.model.geom.builders.MeshDefinition",
    "net.minecraft.client.model.geom.builders.PartDefinition",
    "net.neoforged.api.distmarker.Dist",
    "net.neoforged.api.distmarker.OnlyIn",
  ];

  return `package ${args.package};

${imports.map((entry) => `import ${entry};`).join("\n")}

@OnlyIn(Dist.CLIENT)
public class ${args.class} extends ${args.extends ?? "LeagueJungleModel"} {
  public ${args.class}(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
${partLines.join("\n")}
    return LayerDefinition.create(mesh, ${width}, ${height});
  }
}
`;
}

function uniqueName(name) {
  const count = usedNames.get(name) ?? 0;
  usedNames.set(name, count + 1);
  return count === 0 ? name : `${name}_${count + 1}`;
}

function cleanName(value) {
  let result = String(value || "part").replace(/[^A-Za-z0-9_]/g, "_");
  if (!/^[A-Za-z_]/.test(result)) {
    result = `_${result}`;
  }
  return result;
}

function degreesToRadians(value) {
  return (value * Math.PI) / 180;
}

function float(value) {
  const normalized = Math.abs(value) < 0.000001 ? 0 : value;
  let text = normalized.toFixed(4).replace(/0+$/, "").replace(/\.$/, "");
  if (!text.includes(".")) {
    text += ".0";
  }
  return `${text}F`;
}

function parseArgs(values) {
  const parsed = {};

  for (let index = 0; index < values.length; index++) {
    const value = values[index];
    switch (value) {
      case "--help":
      case "-h":
        parsed.help = true;
        break;
      case "--input":
      case "-i":
        parsed.input = values[++index];
        break;
      case "--output":
      case "-o":
        parsed.output = values[++index];
        break;
      case "--package":
      case "-p":
        parsed.package = values[++index];
        break;
      case "--class":
      case "-c":
        parsed.class = values[++index];
        break;
      case "--extends":
        parsed.extends = values[++index];
        break;
      case "--copy-source":
        parsed.copySource = values[++index];
        break;
      case "--extract-texture":
        parsed.extractTexture = values[++index];
        break;
      case "--ensure-jungle-parts":
        parsed.ensureJungleParts = true;
        break;
      case "--flip-x-rot":
        parsed.flipXRot = true;
        break;
      case "--flip-y-rot":
        parsed.flipYRot = true;
        break;
      case "--flip-z-rot":
        parsed.flipZRot = true;
        break;
      default:
        throw new Error(`Unknown argument: ${value}`);
    }
  }

  return parsed;
}

function printHelp() {
  console.log(`Usage:
  node tools/bbmodel-to-java.mjs \\
    --input C:/path/model.bbmodel \\
    --output src/main/java/.../MyModel.java \\
    --package org.popolesama.leaguecraft.client.model \\
    --class MyModel \\
    --extends LeagueJungleModel

Options:
  --ensure-jungle-parts   Adds empty head/leg/tail parts if they are missing.
  --copy-source PATH      Copies the .bbmodel beside your assets.
  --extract-texture PATH  Extracts the embedded png texture if the .bbmodel contains one.
  --flip-x-rot            Inverts X rotations when a model uses the opposite convention.
  --flip-y-rot            Inverts Y rotations when needed.
  --flip-z-rot            Inverts Z rotations when needed.
`);
}
