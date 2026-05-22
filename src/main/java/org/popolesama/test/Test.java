package org.popolesama.test;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Test.MODID)
public class Test {
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
  public static final DeferredItem<BlockItem> LANE_BLOCK_ITEM = ITEMS.register("lane_block", () -> new BlockItem(LANE_BLOCK.get(), new Item.Properties()));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LeagueStructureBlockEntity>> LEAGUE_STRUCTURE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
      "league_structure",
      () -> new BlockEntityType<>(
          LeagueStructureBlockEntity::new,
          java.util.Set.of(BLUE_NEXUS.get(), RED_NEXUS.get(), BLUE_INHIBITOR.get(), RED_INHIBITOR.get(), BLUE_TURRET.get(), RED_TURRET.get()),
          null));

  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> MINION = registerMonster("minion", LeagueMonster.Profile.MINION, 0.6F, 1.95F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BLUE_MINION = registerMonster("blue_minion", LeagueMonster.Profile.BLUE_MINION, 0.6F, 1.95F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RED_MINION = registerMonster("red_minion", LeagueMonster.Profile.RED_MINION, 0.6F, 1.95F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> GROMP = registerMonster("gromp", LeagueMonster.Profile.GROMP, 1.1F, 1.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> KRUG = registerMonster("krug", LeagueMonster.Profile.KRUG, 1.2F, 1.6F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> RAPTOR = registerMonster("raptor", LeagueMonster.Profile.RAPTOR, 0.8F, 1.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> DRAGON = registerMonster("dragon", LeagueMonster.Profile.DRAGON, 2.2F, 2.4F);
  public static final DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> BARON_NASHOR = registerMonster("baron_nashor", LeagueMonster.Profile.BARON_NASHOR, 2.6F, 3.0F);

  public static final DeferredItem<SpawnEggItem> MINION_SPAWN_EGG = registerSpawnEgg("minion_spawn_egg", MINION, 0x3450a4, 0xd6d8ff);
  public static final DeferredItem<SpawnEggItem> BLUE_MINION_SPAWN_EGG = registerSpawnEgg("blue_minion_spawn_egg", BLUE_MINION, 0x1d4ed8, 0x93c5fd);
  public static final DeferredItem<SpawnEggItem> RED_MINION_SPAWN_EGG = registerSpawnEgg("red_minion_spawn_egg", RED_MINION, 0xb91c1c, 0xfca5a5);
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
            output.accept(LANE_BLOCK_ITEM.get());
            output.accept(MINION_SPAWN_EGG.get());
            output.accept(BLUE_MINION_SPAWN_EGG.get());
            output.accept(RED_MINION_SPAWN_EGG.get());
            output.accept(GROMP_SPAWN_EGG.get());
            output.accept(KRUG_SPAWN_EGG.get());
            output.accept(RAPTOR_SPAWN_EGG.get());
            output.accept(DRAGON_SPAWN_EGG.get());
            output.accept(BARON_NASHOR_SPAWN_EGG.get());
          })
          .build());

  public Test(IEventBus modEventBus, ModContainer modContainer) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    ENTITY_TYPES.register(modEventBus);
    BLOCK_ENTITY_TYPES.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);

    modEventBus.addListener(this::addCreative);
    modEventBus.addListener(this::registerEntityAttributes);

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

  private static DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<Block> block) {
    return ITEMS.registerSimpleBlockItem(name, block, new Item.Properties());
  }

  private static DeferredHolder<EntityType<?>, EntityType<LeagueMonster>> registerMonster(String name, LeagueMonster.Profile profile, float width, float height) {
    return ENTITY_TYPES.register(name, () -> EntityType.Builder
        .of((EntityType.EntityFactory<LeagueMonster>) (type, level) -> new LeagueMonster(type, level, profile), MobCategory.MONSTER)
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
    public static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
        EntityRenderers.register(MINION.get(), ZombieRenderer::new);
        EntityRenderers.register(BLUE_MINION.get(), ZombieRenderer::new);
        EntityRenderers.register(RED_MINION.get(), ZombieRenderer::new);
        EntityRenderers.register(GROMP.get(), ZombieRenderer::new);
        EntityRenderers.register(KRUG.get(), ZombieRenderer::new);
        EntityRenderers.register(RAPTOR.get(), ZombieRenderer::new);
        EntityRenderers.register(DRAGON.get(), ZombieRenderer::new);
        EntityRenderers.register(BARON_NASHOR.get(), ZombieRenderer::new);
      });
    }
  }
}
