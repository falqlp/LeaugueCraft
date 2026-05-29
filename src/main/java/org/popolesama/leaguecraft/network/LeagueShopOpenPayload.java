package org.popolesama.leaguecraft.network;

import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeagueShopOpenPayload(int ignored) implements CustomPacketPayload {
  public static final Type<LeagueShopOpenPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "shop_open"));
  public static final StreamCodec<RegistryFriendlyByteBuf, LeagueShopOpenPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      LeagueShopOpenPayload::ignored,
      LeagueShopOpenPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
