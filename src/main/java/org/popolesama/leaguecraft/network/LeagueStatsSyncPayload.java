package org.popolesama.leaguecraft.network;

import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeagueStatsSyncPayload(int attackDamage, int abilityPower) implements CustomPacketPayload {
  public static final Type<LeagueStatsSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "stats_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, LeagueStatsSyncPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      LeagueStatsSyncPayload::attackDamage,
      ByteBufCodecs.VAR_INT,
      LeagueStatsSyncPayload::abilityPower,
      LeagueStatsSyncPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
