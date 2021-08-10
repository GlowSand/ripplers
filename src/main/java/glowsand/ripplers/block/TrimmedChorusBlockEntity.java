package glowsand.ripplers.block;


import glowsand.ripplers.Ids;
import glowsand.ripplers.Ripplers;
import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class TrimmedChorusBlockEntity extends BlockEntity implements Tickable {
    public UUID rippler = null;
    public UUID player = null;
    public int ticks=0;
    public TrimmedChorusBlockEntity() {
        super(Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY);
    }




    @Override
    public void fromTag(BlockState state,NbtCompound nbt) {
        super.fromTag(state,nbt);
        if (nbt.contains("rippler")){
            rippler = nbt.getUuid("rippler");
        }
        if (nbt.contains("player")){
            player = nbt.getUuid("player");
        }

    }



    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (rippler != null) {
            nbt.putUuid("rippler", rippler);
        }
        if (player!=null){
            nbt.putUuid("player",player);
        }
        return super.writeNbt(nbt);
    }






    @Override
    public void tick() {
        TrimmedChorusBlockEntity trimmedChorusBlockEntity = this;
        if (!trimmedChorusBlockEntity.isRemoved() && trimmedChorusBlockEntity.ticks >= 30 && trimmedChorusBlockEntity.rippler != null && world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld) world;
            trimmedChorusBlockEntity.ticks =0;
            if (trimmedChorusBlockEntity.player!=null){
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) serverWorld.getPlayerByUuid(trimmedChorusBlockEntity.player);
                if (playerEntity!=null&& (!pos.equals(playerEntity.getSpawnPointPosition()) && playerEntity.getSpawnPointPosition() != null)){
                    trimmedChorusBlockEntity.player = null;
                }
            }
            Entity ripplerEntity = serverWorld.getEntity(trimmedChorusBlockEntity.rippler);
            if (ripplerEntity == null){
                trimmedChorusBlockEntity.rippler = null;
            }
            else if (ripplerEntity.getType().equals(Ripplers.RIPPLER_ENTITY_TYPE) && ripplerEntity.isAlive() && ((RipplerEntity)ripplerEntity).chorusBlockPos !=null && ((RipplerEntity)ripplerEntity).chorusWorld !=null && ((RipplerEntity)ripplerEntity).chorusBlockPos.equals(pos) && ((RipplerEntity)ripplerEntity).chorusWorld.equals(world) && ripplerEntity.world.equals(world)){
                if (trimmedChorusBlockEntity.player!=null) {
                    ((RipplerEntity) ripplerEntity).heal(2F);
                    for (int a = 1; a <= 10; a++) {
                        serverWorld.spawnParticles(ParticleTypes.DRAGON_BREATH, Vec3d.ofCenter(pos).getX(), Vec3d.ofCenter(pos).getY(), Vec3d.ofCenter(pos).getZ(), 1, ((RipplerEntity) ripplerEntity).getRandom().nextDouble() * .15, ((RipplerEntity) ripplerEntity).getRandom().nextDouble() * .15, ((RipplerEntity) ripplerEntity).getRandom().nextDouble() * .15, ((RipplerEntity) ripplerEntity).getRandom().nextDouble() * 0.1);
                    }
                    serverWorld.playSound(null, Vec3d.ofCenter(pos).getX(), Vec3d.ofCenter(pos).getY(), Vec3d.ofCenter(pos).getZ(), Registry.SOUND_EVENT.get(Ids.CHORUS_HEART_BEAT), SoundCategory.AMBIENT, 0.5F, 2.0F);
                }
            } else{
                trimmedChorusBlockEntity.rippler = null;
            }
        }
        trimmedChorusBlockEntity.ticks++;
    }
}
