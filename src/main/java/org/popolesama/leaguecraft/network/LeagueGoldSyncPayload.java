package org.popolesama.leaguecraft.network;

import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeagueGoldSyncPayload(int gold) implements CustomPacketPayload {
  public static final Type<LeagueGoldSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "gold_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, LeagueGoldSyncPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      LeagueGoldSyncPayload::gold,
      LeagueGoldSyncPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
