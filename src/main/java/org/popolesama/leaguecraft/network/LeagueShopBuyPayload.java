package org.popolesama.leaguecraft.network;

import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeagueShopBuyPayload(int upgradeOrdinal) implements CustomPacketPayload {
  public static final Type<LeagueShopBuyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "shop_buy"));
  public static final StreamCodec<RegistryFriendlyByteBuf, LeagueShopBuyPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      LeagueShopBuyPayload::upgradeOrdinal,
      LeagueShopBuyPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
