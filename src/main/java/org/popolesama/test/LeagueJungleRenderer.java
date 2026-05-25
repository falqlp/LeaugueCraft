package org.popolesama.test;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeagueJungleRenderer<M extends EntityModel<LeagueMonster>> extends MobRenderer<LeagueMonster, M> {
  public static final ModelLayerLocation GROMP_LAYER = layer("gromp");
  public static final ModelLayerLocation KRUG_LAYER = layer("krug");
  public static final ModelLayerLocation RAPTOR_LAYER = layer("raptor");
  public static final ModelLayerLocation DRAGON_LAYER = layer("dragon");
  public static final ModelLayerLocation BARON_LAYER = layer("baron_nashor");

  private static final ResourceLocation GROMP_TEXTURE = texture("gromp");
  private static final ResourceLocation KRUG_TEXTURE = texture("krug");
  private static final ResourceLocation RAPTOR_TEXTURE = texture("raptor");
  private static final ResourceLocation DRAGON_TEXTURE = texture("dragon");
  private static final ResourceLocation BARON_TEXTURE = texture("baron_nashor");

  private final ResourceLocation texture;
  private final float modelScale;

  private LeagueJungleRenderer(EntityRendererProvider.Context context, M model, ResourceLocation texture, float shadowRadius, float modelScale) {
    super(context, model, shadowRadius);
    this.texture = texture;
    this.modelScale = modelScale;
  }

  public static LeagueJungleRenderer<GrompModel> gromp(EntityRendererProvider.Context context) {
    return new LeagueJungleRenderer<>(context, new GrompModel(context.bakeLayer(GROMP_LAYER)), GROMP_TEXTURE, 0.75F, 1.0F);
  }

  public static LeagueJungleRenderer<KrugModel> krug(EntityRendererProvider.Context context) {
    return new LeagueJungleRenderer<>(context, new KrugModel(context.bakeLayer(KRUG_LAYER)), KRUG_TEXTURE, 0.85F, 1.1F);
  }

  public static LeagueJungleRenderer<RaptorModel> raptor(EntityRendererProvider.Context context) {
    return new LeagueJungleRenderer<>(context, new RaptorModel(context.bakeLayer(RAPTOR_LAYER)), RAPTOR_TEXTURE, 0.55F, 0.9F);
  }

  public static LeagueJungleRenderer<DragonModel> dragon(EntityRendererProvider.Context context) {
    return new LeagueJungleRenderer<>(context, new DragonModel(context.bakeLayer(DRAGON_LAYER)), DRAGON_TEXTURE, 1.15F, 1.35F);
  }

  public static LeagueJungleRenderer<BaronNashorModel> baron(EntityRendererProvider.Context context) {
    return new LeagueJungleRenderer<>(context, new BaronNashorModel(context.bakeLayer(BARON_LAYER)), BARON_TEXTURE, 1.35F, 1.55F);
  }

  @Override
  public ResourceLocation getTextureLocation(LeagueMonster monster) {
    return texture;
  }

  @Override
  public void render(LeagueMonster monster, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    super.render(monster, entityYaw, partialTick, poseStack, buffer, packedLight);

    poseStack.pushPose();
    poseStack.translate(0.0D, monster.getBbHeight() + 0.45D, 0.0D);
    String healthText = Mth.ceil(monster.getHealth()) + " / " + Mth.ceil(monster.getMaxHealth());
    LeagueHealthBarRenderer.render(poseStack, buffer, entityRenderDispatcher.cameraOrientation(), getFont(), monster.getHealth(), monster.getMaxHealth(), healthText, packedLight);
    poseStack.popPose();
  }

  @Override
  protected void scale(LeagueMonster monster, PoseStack poseStack, float partialTick) {
    poseStack.scale(modelScale, modelScale, modelScale);
  }

  private static ModelLayerLocation layer(String name) {
    return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Test.MODID, name), "main");
  }

  private static ResourceLocation texture(String name) {
    return ResourceLocation.fromNamespaceAndPath(Test.MODID, "textures/entity/jungle/" + name + ".png");
  }
}
