package glowsand.ripplers.mixin;

import glowsand.ripplers.Ripplers;
import glowsand.ripplers.block.TrimmedChorusBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "findRespawnPosition",at = @At("HEAD"),cancellable = true)
    private static void findRespawnMixin(ServerWorld world, BlockPos pos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3d>> cir){
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity !=null && blockEntity.getType().equals(Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY) && ((TrimmedChorusBlockEntity)blockEntity).rippler !=null && !bl2){
            Optional<Vec3d> optionalVec3d = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER,world,pos);
            if (optionalVec3d.isEmpty()){
                optionalVec3d = BedBlock.findWakeUpPosition(EntityType.PLAYER,world,pos,f);
            }
            if (optionalVec3d.isPresent() && world.random.nextInt(10)==0){
                ServerPlayerEntity serverPlayerEntity = world.getServer().getPlayerManager().getPlayer(((TrimmedChorusBlockEntity) blockEntity).player);
                if (serverPlayerEntity !=null) {
                    serverPlayerEntity.sendMessage(new TranslatableText("ripplers.your.chorus.flower.broke.lol"), false);
                }

                world.breakBlock(pos,false);

            }
            cir.setReturnValue(optionalVec3d);
        }
    }





}
