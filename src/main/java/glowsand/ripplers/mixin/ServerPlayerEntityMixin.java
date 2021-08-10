package glowsand.ripplers.mixin;


import glowsand.ripplers.Ripplers;
import glowsand.ripplers.block.TrimmedChorusBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class ServerPlayerEntityMixin  {

    @Shadow @Final private List<ServerPlayerEntity> players;

    @Shadow @Final private Map<UUID, ServerPlayerEntity> playerMap;

    @Shadow public abstract void sendCommandTree(ServerPlayerEntity player);

    @Shadow public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);


    @Shadow @Final private MinecraftServer server;

    @Inject(method = "respawnPlayer",at = @At("HEAD"),cancellable = true)
    public void piss(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir){
        BlockPos pos = player.getSpawnPointPosition();
        ServerWorld spawnWorld = this.server.getWorld(player.getSpawnPointDimension());
        if (spawnWorld != null && pos !=null && alive) {
            BlockEntity blockEntity = spawnWorld.getBlockEntity(pos);
            if (player.world.getRegistryKey().equals(World.END) && spawnWorld.getRegistryKey().equals(World.END) && blockEntity !=null && blockEntity.getType().equals(Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY) && ((TrimmedChorusBlockEntity)blockEntity).rippler !=null) {
                this.players.remove(player);
                player.getServerWorld().removePlayer(player, Entity.RemovalReason.DISCARDED);
                BlockPos blockPos = player.getSpawnPointPosition();
                float f = player.getSpawnAngle();
                boolean bl = player.isSpawnPointSet();
                ServerWorld serverWorld = this.server.getWorld(player.getSpawnPointDimension());
                Optional<Vec3d> optional2;
                if (serverWorld != null && blockPos != null) {
                    optional2 = PlayerEntity.findRespawnPosition(serverWorld, blockPos, f, bl, true);
                } else {
                    optional2 = Optional.empty();
                }

                ServerWorld serverWorld2 = serverWorld != null && optional2.isPresent() ? serverWorld : this.server.getOverworld();
                ServerPlayerEntity serverPlayerEntity = new ServerPlayerEntity(this.server, serverWorld2, player.getGameProfile());
                serverPlayerEntity.networkHandler = player.networkHandler;
                serverPlayerEntity.copyFrom(player, true);
                serverPlayerEntity.setId(player.getId());
                serverPlayerEntity.setMainArm(player.getMainArm());

                for (String string : player.getScoreboardTags()) {
                    serverPlayerEntity.addScoreboardTag(string);
                }

                if (optional2.isPresent()) {
                    BlockState blockState = serverWorld2.getBlockState(blockPos);
                    boolean bl3 = blockState.isOf(Blocks.RESPAWN_ANCHOR);
                    Vec3d vec3d = optional2.get();
                    float h;
                    if (!blockState.isIn(BlockTags.BEDS) && !bl3) {
                        h = f;
                    } else {
                        Vec3d vec3d2 = Vec3d.ofBottomCenter(blockPos).subtract(vec3d).normalize();
                        h = (float) MathHelper.wrapDegrees(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875D - 90.0D);
                    }

                    serverPlayerEntity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, h, 0.0F);
                    serverPlayerEntity.setSpawnPoint(serverWorld2.getRegistryKey(), blockPos, f, bl, false);
                }

                while (!serverWorld2.isSpaceEmpty(serverPlayerEntity) && serverPlayerEntity.getY() < (double) serverWorld2.getTopY()) {
                    serverPlayerEntity.setPosition(serverPlayerEntity.getX(), serverPlayerEntity.getY() + 1.0D, serverPlayerEntity.getZ());
                }
                serverPlayerEntity.setSpawnPoint(RegistryKey.of(Registry.WORLD_KEY,spawnWorld.getRegistryKey().getValue()), pos, 0, false, false);
                WorldProperties worldProperties = serverPlayerEntity.world.getLevelProperties();
                serverPlayerEntity.networkHandler.sendPacket(new PlayerRespawnS2CPacket(serverPlayerEntity.world.getDimension(), serverPlayerEntity.world.getRegistryKey(), BiomeAccess.hashSeed(serverPlayerEntity.getServerWorld().getSeed()), serverPlayerEntity.interactionManager.getGameMode(), serverPlayerEntity.interactionManager.getPreviousGameMode(), serverPlayerEntity.getServerWorld().isDebugWorld(), serverPlayerEntity.getServerWorld().isFlat(), true));
                serverPlayerEntity.networkHandler.requestTeleport(serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
                serverPlayerEntity.networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(serverWorld2.getSpawnPos(), serverWorld2.getSpawnAngle()));
                serverPlayerEntity.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
                serverPlayerEntity.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(serverPlayerEntity.experienceProgress, serverPlayerEntity.totalExperience, serverPlayerEntity.experienceLevel));
                this.sendWorldInfo(serverPlayerEntity, serverWorld2);
                this.sendCommandTree(serverPlayerEntity);
                serverWorld2.onPlayerRespawned(serverPlayerEntity);
                this.players.add(serverPlayerEntity);
                this.playerMap.put(serverPlayerEntity.getUuid(), serverPlayerEntity);
                serverPlayerEntity.onSpawn();
                serverPlayerEntity.setHealth(serverPlayerEntity.getHealth());
                cir.setReturnValue(serverPlayerEntity);
            }
        }
    }


}
