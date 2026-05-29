# Blockbench model converter

Convert a Blockbench `.bbmodel` file into a Minecraft Java `LayerDefinition` model:

```powershell
node tools/bbmodel-to-java.mjs `
  --flip-x-rot `
  --input "C:/Users/leo-paulF/Downloads/BaronNashor.bbmodel" `
  --output "src/main/java/org/popolesama/leaguecraft/client/model/BaronNashorModel.java" `
  --package "org.popolesama.leaguecraft.client.model" `
  --class "BaronNashorModel" `
  --extends "LeagueJungleModel" `
  --ensure-jungle-parts `
  --copy-source "src/main/resources/assets/leaguecraft/models/entity/baron_nashor.bbmodel"
```

By default, the converter does not touch textures. Add `--extract-texture "path/to/texture.png"` only when you want to replace the texture with the one embedded in the `.bbmodel`.

If a model comes out with rotations mirrored, retry with `--flip-x-rot`, `--flip-y-rot`, or `--flip-z-rot`.
