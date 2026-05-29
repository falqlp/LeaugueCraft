package org.popolesama.leaguecraft;

import org.popolesama.leaguecraft.entity.*;

import org.popolesama.leaguecraft.config.Config;

import org.popolesama.leaguecraft.client.renderer.*;

import org.popolesama.leaguecraft.client.model.*;

import org.popolesama.leaguecraft.block.*;
import org.popolesama.leaguecraft.command.LeagueCommands;
import org.popolesama.leaguecraft.client.LeagueGoldHud;
import org.popolesama.leaguecraft.item.AssassinPearlItem;
import org.popolesama.leaguecraft.item.ClassPotionItem;
import org.popolesama.leaguecraft.item.MageGroundDamageItem;
import org.popolesama.leaguecraft.item.MarksmanQuickShotItem;
import org.popolesama.leaguecraft.item.MarksmanVolleyItem;
import org.popolesama.leaguecraft.item.TankAbsorptionItem;
import org.popolesama.leaguecraft.network.LeagueCraftNetwork;
import org.popolesama.leaguecraft.player.LeagueGold;
import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeaguePlayerStats;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopEffects;
import org.popolesama.leaguecraft.player.LeagueSpellFriendlyFire;
import org.popolesama.leaguecraft.player.LeagueTurretAggro;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(LeagueCraft.MODID)
public class LeagueCraft {
  public static final String MODID = "leaguecraft";

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

  public static final DeferredBlock<Block> NEXUS = registerStructureBlock("nexus", MapColor.COLOR_PURPLE, 5.0F, 1200.0F);
  public static final DeferredBlock<Block> INHIBITOR = registerStructureBlock("inhibitor", MapColor.COLOR_LIGHT_BLUE, 4.0F, 900.0F);
  public static final DeferredBlock<Block> TURRET = registerStructureBlock("turret", MapColor.COLOR_RED, 6.0F, 1600.0F);
  public static final DeferredBlock<LeagueStructureBlock> BLUE_NEXUS = registerTeamStructure("blue_nexus", LeagueStructureKind.NEXUS, LeagueMonster.Team.BLUE, MapColor.COLOR_BLUE);
  public static final DeferredBlock<LeagueStructureBlock> RED_NEXUS = registerTeamStructure("red_nexus", LeagueStructureKind.NEXUS, LeagueMonster.Team.RED, MapColor.COLOR_RED);
  public static final DeferredBlock<LeagueStructureBlock> BLUE_INHIBITOR = registerTeamStructure("blue_inhibitor", LeagueStructureKind.INHIBITOR, LeagueMonster.Team.BLUE, MapColor.COLOR_LIGHT_BLUE);
  public static final DeferredBlock<LeagueStructureBlock> RED_INHIBITOR = registerTeamStructure("red_inhibitor", LeagueStructureKind.INHIBITOR, LeagueMonster.Team.RED, MapColor.COLOR_ORANGE);
  public static final DeferredBlock<LeagueStructureBlock> BLUE_TURRET = registerTeamStructure("blue_turret", LeagueStructureKind.TURRET, LeagueMonster.Team.BLUE, MapColor.COLOR_BLUE);
  public static final DeferredBlock<LeagueStructureBlock> RED_TURRET = registerTeamStructure("red_turret", LeagueStructureKind.TURRET, LeagueMonster.Team.RED, MapColor.COLOR_RED);
  public static final DeferredBlock<TurretPartBlock> BLUE_NEXUS_PART = registerStructurePart("blue_nexus_part", MapColor.COLOR_BLUE);
  public static final DeferredBlock<TurretPartBlock> RED_NEXUS_PART = registerStructurePart("red_nexus_part", MapColor.COLOR_RED);
  public static final DeferredBlock<TurretPartBlock> BLUE_INHIBITOR_PART = registerStructurePart("blue_inhibitor_part", MapColor.COLOR_LIGHT_BLUE);
  public static final DeferredBlock<TurretPartBlock> RED_INHIBITOR_PART = registerStructurePart("red_inhibitor_part", MapColor.COLOR_ORANGE);
  public static final DeferredBlock<TurretPartBlock> TURRET_PART = registerTurretPart("turret_part", MapColor.COLOR_RED);
  public static final DeferredBlock<TurretPartBlock> BLUE_TURRET_PART = registerTurretPart("blue_turret_part", MapColor.COLOR_BLUE);
  public static final DeferredBlock<TurretPartBlock> RED_TURRET_PART = registerTurretPart("red_turret_part", MapColor.COLOR_RED);
  public static final DeferredBlock<LeagueShopBlock> SHOP_BLOCK = BLOCKS.register("shop_block", () -> new LeagueShopBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_YELLOW)
      .strength(3.0F, 8.0F)
      .sound(SoundType.METAL)));
  public static final DeferredBlock<LaneBlock> LANE_BLOCK = BLOCKS.register("lane_block", () -> new LaneBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_YELLOW)
      .strength(1.5F, 6.0F)
      .sound(SoundType.STONE)));

  public static final DeferredItem<BlockItem> NEXUS_ITEM = registerBlockItem("nexus", NEXUS);
  public static final DeferredItem<BlockItem> INHIBITOR_ITEM = registerBlockItem("inhibitor", INHIBITOR);
  public static final DeferredItem<BlockItem> TURRET_ITEM = registerBlockItem("turret", TURRET);
  public static final DeferredItem<BlockItem> BLUE_NEXUS_ITEM = ITEMS.register("blue_nexus", () -> new BlockItem(BLUE_NEXUS.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> RED_NEXUS_ITEM = ITEMS.register("red_nexus", () -> new BlockItem(RED_NEXUS.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> BLUE_INHIBITOR_ITEM = ITEMS.register("blue_inhibitor", () -> new BlockItem(BLUE_INHIBITOR.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> RED_INHIBITOR_ITEM = ITEMS.register("red_inhibitor", () -> new BlockItem(RED_INHIBITOR.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> BLUE_TURRET_ITEM = ITEMS.register("blue_turret", () -> new BlockItem(BLUE_TURRET.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> RED_TURRET_ITEM = ITEMS.register("red_turret", () -> new BlockItem(RED_TURRET.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> SHOP_BLOCK_ITEM = ITEMS.register("shop_block", () -> new BlockItem(SHOP_BLOCK.get(), new Item.Properties()));
  public static final DeferredItem<BlockItem> LANE_BLOCK_ITEM = ITEMS.register("lane_block", () -> new BlockItem(LANE_BLOCK.get(), new Item.Properties()));
  public static final DeferredItem<Item> MAGE_CLASS_ITEM = ITEMS.register(
      "mage_focus",
      () -> new ClassPotionItem(new Item.Properties(), LeaguePlayerClass.Role.MAGE, ClassPotionItem.SpellEffect.DAMAGE, 20 * 18, "Sort de degats lance."));
  public static final DeferredItem<Item> MAGE_GROUND_DAMAGE_ITEM = ITEMS.register(
      "mage_ground_damage",
      () -> new MageGroundDamageItem(new Item.Properties()));
  public static final DeferredItem<Item> SUPPORT_CLASS_ITEM = ITEMS.register(
      "support_focus",
      () -> new ClassPotionItem(new Item.Properties(), LeaguePlayerClass.Role.SUPPORT, ClassPotionItem.SpellEffect.HEAL, 20 * 14, "Sort de soin lance."));
  public static final DeferredItem<Item> SUPPORT_SLOW_ITEM = ITEMS.register(
      "support_slow",
      () -> new ClassPotionItem(new Item.Properties(), LeaguePlayerClass.Role.SUPPORT, ClassPotionItem.SpellEffect.SLOW, 20 * 16, "Sort de ralentissement lance."));
  public static final DeferredItem<Item> MARKSMAN_CLASS_ITEM = ITEMS.register(
      "marksman_quickshot",
      () -> new MarksmanQuickShotItem(new Item.Properties()));
  public static final DeferredItem<Item> MARKSMAN_VOLLEY_ITEM = ITEMS.register(
      "marksman_volley",
      () -> new MarksmanVolleyItem(new Item.Properties()));
  public static final DeferredItem<Item> TANK_CLASS_ITEM = ITEMS.register(
      "tank_bulwark",
      () -> new TankAbsorptionItem(new Item.Properties()));
  public static final DeferredItem<Item> ASSASSIN_CLASS_ITEM = ITEMS.register(
      "assassin_pearl",
      () -> new AssassinPearlItem(new Item.Properties()));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LeagueStructureBlockEntity>> LEAGUE_STRUCTURE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
      "league_structure",
      () -> new BlockEntityType<>(
          LeagueStructureBlockEntity::new,
          java.util.Set.of(BLUE_NEXUS.get(), RED_NEXUS.get(), BLUE_INHIBITOR.get(), RED_INHIBITOR.get(), BLUE_TURRET.get(), RED_TURRET.get()),
          null));

  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> MINION = registerMonster("minion", Minion::new, 0.9F, 1.3F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_MINION = registerMonster("blue_minion", BlueMinion::new, 0.9F, 1.3F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_MINION = registerMonster("red_minion", RedMinion::new, 0.9F, 1.3F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_CASTER_MINION = registerMonster("blue_caster_minion", BlueCasterMinion::new, 0.75F, 1.2F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_CASTER_MINION = registerMonster("red_caster_minion", RedCasterMinion::new, 0.75F, 1.2F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_CANNON_MINION = registerMonster("blue_cannon_minion", BlueCannonMinion::new, 1.15F, 1.25F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_CANNON_MINION = registerMonster("red_cannon_minion", RedCannonMinion::new, 1.15F, 1.25F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_SUPER_MINION = registerMonster("blue_super_minion", BlueSuperMinion::new, 1.2F, 1.65F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_SUPER_MINION = registerMonster("red_super_minion", RedSuperMinion::new, 1.2F, 1.65F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_BUFF = registerMonster("blue_buff", BlueBuff::new, 1.55F, 1.9F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_BUFF = registerMonster("red_buff", RedBuff::new, 1.55F, 1.9F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> VOID_GRUB = registerMonster("void_grub", VoidGrub::new, 1.0F, 1.0F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RIFT_HERALD = registerMonster("rift_herald", RiftHerald::new, 2.3F, 2.6F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> GROMP = registerMonster("gromp", Gromp::new, 1.1F, 1.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> KRUG = registerMonster("krug", Krug::new, 1.2F, 1.6F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RAPTOR = registerMonster("raptor", Raptor::new, 0.8F, 1.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> DRAGON = registerMonster("dragon", Dragon::new, 2.2F, 2.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BARON_NASHOR = registerMonster("baron_nashor", BaronNashor::new, 2.6F, 3.0F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueProjectile>> LEAGUE_PROJECTILE = ENTITY_TYPES.register(
      "league_projectile",
      () -> EntityType.Builder
          .<LeagueProjectile>of(LeagueProjectile::new, MobCategory.MISC)
          .sized(0.25F, 0.25F)
          .clientTrackingRange(8)
          .updateInterval(1)
          .build("league_projectile"));

  public static final DeferredItem<SpawnEggItem> MINION_SPAWN_EGG = registerSpawnEgg("minion_spawn_egg", MINION, 0x3450a4, 0xd6d8ff);
  public static final DeferredItem<SpawnEggItem> BLUE_MINION_SPAWN_EGG = registerSpawnEgg("blue_minion_spawn_egg", BLUE_MINION, 0x1d4ed8, 0x93c5fd);
  public static final DeferredItem<SpawnEggItem> RED_MINION_SPAWN_EGG = registerSpawnEgg("red_minion_spawn_egg", RED_MINION, 0xb91c1c, 0xfca5a5);
  public static final DeferredItem<SpawnEggItem> BLUE_CASTER_MINION_SPAWN_EGG = registerSpawnEgg("blue_caster_minion_spawn_egg", BLUE_CASTER_MINION, 0x2563eb, 0xbfdbfe);
  public static final DeferredItem<SpawnEggItem> RED_CASTER_MINION_SPAWN_EGG = registerSpawnEgg("red_caster_minion_spawn_egg", RED_CASTER_MINION, 0xdc2626, 0xfecaca);
  public static final DeferredItem<SpawnEggItem> BLUE_CANNON_MINION_SPAWN_EGG = registerSpawnEgg("blue_cannon_minion_spawn_egg", BLUE_CANNON_MINION, 0x1e3a8a, 0x64748b);
  public static final DeferredItem<SpawnEggItem> RED_CANNON_MINION_SPAWN_EGG = registerSpawnEgg("red_cannon_minion_spawn_egg", RED_CANNON_MINION, 0x7f1d1d, 0x64748b);
  public static final DeferredItem<SpawnEggItem> BLUE_SUPER_MINION_SPAWN_EGG = registerSpawnEgg("blue_super_minion_spawn_egg", BLUE_SUPER_MINION, 0x0f2f87, 0xd7b55a);
  public static final DeferredItem<SpawnEggItem> RED_SUPER_MINION_SPAWN_EGG = registerSpawnEgg("red_super_minion_spawn_egg", RED_SUPER_MINION, 0x8a1515, 0xd7b55a);
  public static final DeferredItem<SpawnEggItem> BLUE_BUFF_SPAWN_EGG = registerSpawnEgg("blue_buff_spawn_egg", BLUE_BUFF, 0x1f55d8, 0x83d7ff);
  public static final DeferredItem<SpawnEggItem> RED_BUFF_SPAWN_EGG = registerSpawnEgg("red_buff_spawn_egg", RED_BUFF, 0xb83224, 0xffc35c);
  public static final DeferredItem<SpawnEggItem> VOID_GRUB_SPAWN_EGG = registerSpawnEgg("void_grub_spawn_egg", VOID_GRUB, 0x4c1d95, 0xd8b4fe);
  public static final DeferredItem<SpawnEggItem> RIFT_HERALD_SPAWN_EGG = registerSpawnEgg("rift_herald_spawn_egg", RIFT_HERALD, 0x2e1065, 0x67e8f9);
  public static final DeferredItem<SpawnEggItem> GROMP_SPAWN_EGG = registerSpawnEgg("gromp_spawn_egg", GROMP, 0x2f7d62, 0xa9e6bd);
  public static final DeferredItem<SpawnEggItem> KRUG_SPAWN_EGG = registerSpawnEgg("krug_spawn_egg", KRUG, 0x6b5845, 0xc7a36b);
  public static final DeferredItem<SpawnEggItem> RAPTOR_SPAWN_EGG = registerSpawnEgg("raptor_spawn_egg", RAPTOR, 0x8a2b2b, 0xf2b15f);
  public static final DeferredItem<SpawnEggItem> DRAGON_SPAWN_EGG = registerSpawnEgg("dragon_spawn_egg", DRAGON, 0x8e1c1c, 0xf0d06a);
  public static final DeferredItem<SpawnEggItem> BARON_NASHOR_SPAWN_EGG = registerSpawnEgg("baron_nashor_spawn_egg", BARON_NASHOR, 0x5d2f82, 0x9ce3cf);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LEAGUECRAFT_TAB = CREATIVE_MODE_TABS.register(
      "leaguecraft_tab",
      () -> CreativeModeTab.builder()
          .title(Component.translatable("itemGroup.leaguecraft"))
          .withTabsBefore(CreativeModeTabs.COMBAT)
          .icon(() -> NEXUS_ITEM.get().getDefaultInstance())
          .displayItems((parameters, output) -> {
            output.accept(NEXUS_ITEM.get());
            output.accept(INHIBITOR_ITEM.get());
            output.accept(TURRET_ITEM.get());
            output.accept(BLUE_NEXUS_ITEM.get());
            output.accept(RED_NEXUS_ITEM.get());
            output.accept(BLUE_INHIBITOR_ITEM.get());
            output.accept(RED_INHIBITOR_ITEM.get());
            output.accept(BLUE_TURRET_ITEM.get());
            output.accept(RED_TURRET_ITEM.get());
            output.accept(SHOP_BLOCK_ITEM.get());
            output.accept(LANE_BLOCK_ITEM.get());
            output.accept(MAGE_CLASS_ITEM.get());
            output.accept(MAGE_GROUND_DAMAGE_ITEM.get());
            output.accept(SUPPORT_CLASS_ITEM.get());
            output.accept(SUPPORT_SLOW_ITEM.get());
            output.accept(MARKSMAN_CLASS_ITEM.get());
            output.accept(MARKSMAN_VOLLEY_ITEM.get());
            output.accept(TANK_CLASS_ITEM.get());
            output.accept(ASSASSIN_CLASS_ITEM.get());
            output.accept(MINION_SPAWN_EGG.get());
            output.accept(BLUE_MINION_SPAWN_EGG.get());
            output.accept(RED_MINION_SPAWN_EGG.get());
            output.accept(BLUE_CASTER_MINION_SPAWN_EGG.get());
            output.accept(RED_CASTER_MINION_SPAWN_EGG.get());
            output.accept(BLUE_CANNON_MINION_SPAWN_EGG.get());
            output.accept(RED_CANNON_MINION_SPAWN_EGG.get());
            output.accept(BLUE_SUPER_MINION_SPAWN_EGG.get());
            output.accept(RED_SUPER_MINION_SPAWN_EGG.get());
            output.accept(BLUE_BUFF_SPAWN_EGG.get());
            output.accept(RED_BUFF_SPAWN_EGG.get());
            output.accept(VOID_GRUB_SPAWN_EGG.get());
            output.accept(RIFT_HERALD_SPAWN_EGG.get());
            output.accept(GROMP_SPAWN_EGG.get());
            output.accept(KRUG_SPAWN_EGG.get());
            output.accept(RAPTOR_SPAWN_EGG.get());
            output.accept(DRAGON_SPAWN_EGG.get());
            output.accept(BARON_NASHOR_SPAWN_EGG.get());
          })
          .build());

  public LeagueCraft(IEventBus modEventBus, ModContainer modContainer) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    ENTITY_TYPES.register(modEventBus);
    BLOCK_ENTITY_TYPES.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);

    modEventBus.addListener(this::addCreative);
    modEventBus.addListener(this::registerEntityAttributes);
    modEventBus.addListener(LeagueCraftNetwork::register);
    NeoForge.EVENT_BUS.addListener(LeagueCommands::register);
    NeoForge.EVENT_BUS.addListener(LeagueGold::onPlayerLoggedIn);
    NeoForge.EVENT_BUS.addListener(LeagueGold::onPlayerRespawn);
    NeoForge.EVENT_BUS.addListener(LeagueGold::onPlayerChangedDimension);
    NeoForge.EVENT_BUS.addListener(LeagueGold::onPlayerClone);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerClass::onPlayerLoggedIn);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerClass::onPlayerRespawn);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerClass::onPlayerClone);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerStats::onPlayerLoggedIn);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerStats::onPlayerRespawn);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerStats::onPlayerChangedDimension);
    NeoForge.EVENT_BUS.addListener(LeaguePlayerStats::onPlayerClone);
    NeoForge.EVENT_BUS.addListener(LeagueShop::onPlayerClone);
    NeoForge.EVENT_BUS.addListener(LeagueShopEffects::onLivingIncomingDamage);
    NeoForge.EVENT_BUS.addListener(LeagueSpellFriendlyFire::onMobEffectApplicable);
    NeoForge.EVENT_BUS.addListener(LeagueTurretAggro::onLivingIncomingDamage);

    modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }

  private static DeferredBlock<Block> registerStructureBlock(String name, MapColor color, float strength, float resistance) {
    return BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.of()
        .mapColor(color)
        .strength(strength, resistance)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL));
  }

  private static DeferredBlock<LeagueStructureBlock> registerTeamStructure(String name, LeagueStructureKind kind, LeagueMonster.Team team, MapColor color) {
    return BLOCKS.register(name, () -> new LeagueStructureBlock(BlockBehaviour.Properties.of()
        .mapColor(color)
        .strength(5.0F, 1200.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL), kind, team));
  }

  private static DeferredBlock<TurretPartBlock> registerTurretPart(String name, MapColor color) {
    return registerStructurePart(name, color);
  }

  private static DeferredBlock<TurretPartBlock> registerStructurePart(String name, MapColor color) {
    return BLOCKS.register(name, () -> new TurretPartBlock(BlockBehaviour.Properties.of()
        .mapColor(color)
        .strength(5.0F, 1200.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL)));
  }

  public static Block structurePartFor(BlockState controllerState) {
    if (controllerState.is(BLUE_NEXUS.get())) {
      return BLUE_NEXUS_PART.get();
    }
    if (controllerState.is(RED_NEXUS.get())) {
      return RED_NEXUS_PART.get();
    }
    if (controllerState.is(BLUE_INHIBITOR.get())) {
      return BLUE_INHIBITOR_PART.get();
    }
    if (controllerState.is(RED_INHIBITOR.get())) {
      return RED_INHIBITOR_PART.get();
    }
    if (controllerState.is(BLUE_TURRET.get())) {
      return BLUE_TURRET_PART.get();
    }
    if (controllerState.is(RED_TURRET.get())) {
      return RED_TURRET_PART.get();
    }
    if (controllerState.is(TURRET.get())) {
      return TURRET_PART.get();
    }

    return null;
  }

  private static DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<Block> block) {
    return ITEMS.registerSimpleBlockItem(name, block, new Item.Properties());
  }

  private static DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> registerMonster(String name, EntityType.EntityFactory<LeagueMonster> factory, float width, float height) {
    return ENTITY_TYPES.register(name, () -> EntityType.Builder
        .of(factory, MobCategory.MONSTER)
        .sized(width, height)
        .clientTrackingRange(8)
        .build(name));
  }

  private static DeferredItem<SpawnEggItem> registerSpawnEgg(String name, DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> type, int background, int highlight) {
    return ITEMS.register(name, () -> new SpawnEggItem(type.get(), background, highlight, new Item.Properties()));
  }

  private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
      event.accept(MINION_SPAWN_EGG);
      event.accept(BLUE_MINION_SPAWN_EGG);
      event.accept(RED_MINION_SPAWN_EGG);
      event.accept(BLUE_CASTER_MINION_SPAWN_EGG);
      event.accept(RED_CASTER_MINION_SPAWN_EGG);
      event.accept(BLUE_CANNON_MINION_SPAWN_EGG);
      event.accept(RED_CANNON_MINION_SPAWN_EGG);
      event.accept(BLUE_SUPER_MINION_SPAWN_EGG);
      event.accept(RED_SUPER_MINION_SPAWN_EGG);
      event.accept(BLUE_BUFF_SPAWN_EGG);
      event.accept(RED_BUFF_SPAWN_EGG);
      event.accept(VOID_GRUB_SPAWN_EGG);
      event.accept(RIFT_HERALD_SPAWN_EGG);
      event.accept(GROMP_SPAWN_EGG);
      event.accept(KRUG_SPAWN_EGG);
      event.accept(RAPTOR_SPAWN_EGG);
      event.accept(DRAGON_SPAWN_EGG);
      event.accept(BARON_NASHOR_SPAWN_EGG);
    }
  }

  private void registerEntityAttributes(EntityAttributeCreationEvent event) {
    putAttributes(event, MINION.get(), LeagueMonster.Profile.MINION);
    putAttributes(event, BLUE_MINION.get(), LeagueMonster.Profile.BLUE_MINION);
    putAttributes(event, RED_MINION.get(), LeagueMonster.Profile.RED_MINION);
    putAttributes(event, BLUE_CASTER_MINION.get(), LeagueMonster.Profile.BLUE_CASTER_MINION);
    putAttributes(event, RED_CASTER_MINION.get(), LeagueMonster.Profile.RED_CASTER_MINION);
    putAttributes(event, BLUE_CANNON_MINION.get(), LeagueMonster.Profile.BLUE_CANNON_MINION);
    putAttributes(event, RED_CANNON_MINION.get(), LeagueMonster.Profile.RED_CANNON_MINION);
    putAttributes(event, BLUE_SUPER_MINION.get(), LeagueMonster.Profile.BLUE_SUPER_MINION);
    putAttributes(event, RED_SUPER_MINION.get(), LeagueMonster.Profile.RED_SUPER_MINION);
    putAttributes(event, BLUE_BUFF.get(), LeagueMonster.Profile.BLUE_BUFF);
    putAttributes(event, RED_BUFF.get(), LeagueMonster.Profile.RED_BUFF);
    putAttributes(event, VOID_GRUB.get(), LeagueMonster.Profile.VOID_GRUB);
    putAttributes(event, RIFT_HERALD.get(), LeagueMonster.Profile.RIFT_HERALD);
    putAttributes(event, GROMP.get(), LeagueMonster.Profile.GROMP);
    putAttributes(event, KRUG.get(), LeagueMonster.Profile.KRUG);
    putAttributes(event, RAPTOR.get(), LeagueMonster.Profile.RAPTOR);
    putAttributes(event, DRAGON.get(), LeagueMonster.Profile.DRAGON);
    putAttributes(event, BARON_NASHOR.get(), LeagueMonster.Profile.BARON_NASHOR);
  }

  private static void putAttributes(EntityAttributeCreationEvent event, EntityType<? extends Zombie> type, LeagueMonster.Profile profile) {
    AttributeSupplier.Builder attributes = Zombie.createAttributes()
        .add(Attributes.MAX_HEALTH, profile.maxHealth())
        .add(Attributes.ATTACK_DAMAGE, profile.attackDamage())
        .add(Attributes.MOVEMENT_SPEED, profile.movementSpeed())
        .add(Attributes.FOLLOW_RANGE, profile.followRange());
    event.put(type, attributes.build());
  }

  @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
      event.registerAbove(
          VanillaGuiLayers.EXPERIENCE_LEVEL,
          ResourceLocation.fromNamespaceAndPath(MODID, "gold_hud"),
          LeagueGoldHud::render);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
      event.registerLayerDefinition(LeagueMinionRenderer.LAYER, LeagueMinionModel::createBodyLayer);
      event.registerLayerDefinition(CasterMinionRenderer.LAYER, CasterMinionModel::createBodyLayer);
      event.registerLayerDefinition(CannonMinionRenderer.LAYER, CannonMinionModel::createBodyLayer);
      event.registerLayerDefinition(BlueSuperMinionRenderer.LAYER, BlueSuperMinionModel::createBodyLayer);
      event.registerLayerDefinition(RedSuperMinionRenderer.LAYER, RedSuperMinionModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.GROMP_LAYER, GrompModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.KRUG_LAYER, KrugModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.RAPTOR_LAYER, RaptorModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.DRAGON_LAYER, DragonModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.BARON_LAYER, BaronNashorModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.BLUE_BUFF_LAYER, BlueBuffModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.RED_BUFF_LAYER, RedBuffModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.VOID_GRUB_LAYER, VoidGrubModel::createBodyLayer);
      event.registerLayerDefinition(LeagueJungleRenderer.RIFT_HERALD_LAYER, RiftHeraldModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
      event.registerBlockEntityRenderer(LEAGUE_STRUCTURE_BLOCK_ENTITY.get(), LeagueStructureRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
        EntityRenderers.register(MINION.get(), LeagueMinionRenderer::new);
        EntityRenderers.register(BLUE_MINION.get(), LeagueMinionRenderer::new);
        EntityRenderers.register(RED_MINION.get(), LeagueMinionRenderer::new);
        EntityRenderers.register(BLUE_CASTER_MINION.get(), CasterMinionRenderer::new);
        EntityRenderers.register(RED_CASTER_MINION.get(), CasterMinionRenderer::new);
        EntityRenderers.register(BLUE_CANNON_MINION.get(), CannonMinionRenderer::new);
        EntityRenderers.register(RED_CANNON_MINION.get(), CannonMinionRenderer::new);
        EntityRenderers.register(BLUE_SUPER_MINION.get(), BlueSuperMinionRenderer::new);
        EntityRenderers.register(RED_SUPER_MINION.get(), RedSuperMinionRenderer::new);
        EntityRenderers.register(BLUE_BUFF.get(), LeagueJungleRenderer::blueBuff);
        EntityRenderers.register(RED_BUFF.get(), LeagueJungleRenderer::redBuff);
        EntityRenderers.register(VOID_GRUB.get(), LeagueJungleRenderer::voidGrub);
        EntityRenderers.register(RIFT_HERALD.get(), LeagueJungleRenderer::riftHerald);
        EntityRenderers.register(GROMP.get(), LeagueJungleRenderer::gromp);
        EntityRenderers.register(KRUG.get(), LeagueJungleRenderer::krug);
        EntityRenderers.register(RAPTOR.get(), LeagueJungleRenderer::raptor);
        EntityRenderers.register(DRAGON.get(), LeagueJungleRenderer::dragon);
        EntityRenderers.register(BARON_NASHOR.get(), LeagueJungleRenderer::baron);
        EntityRenderers.register(LEAGUE_PROJECTILE.get(), LeagueProjectileRenderer::new);
      });
    }
  }
}
