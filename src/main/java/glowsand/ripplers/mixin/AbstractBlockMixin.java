package glowsand.ripplers.mixin;

import glowsand.ripplers.Ids;
import glowsand.ripplers.Ripplers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "onUse",at = @At("HEAD"), cancellable = true)
    public void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        ItemStack handStack = player.getStackInHand(hand);
        if (state.equals(Blocks.CHORUS_FLOWER.getDefaultState()) && handStack.getItem() == Items.SHEARS){
            if (world instanceof ServerWorld ) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.playSound(null, pos.getX(), pos.getY(),pos.getZ(), Registry.SOUND_EVENT.get(Ids.CHORUS_TRIM), SoundCategory.PLAYERS, 1.0F, 1.0F);
                Ripplers.criterion2.trigger((ServerPlayerEntity)player);
            }

            world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + world.random.nextDouble() / 0.5D, pos.getY(), pos.getZ() + world.random.nextDouble() / 0.5D, world.random.nextDouble() / 5.0D, world.random.nextDouble() / 5.0D, world.random.nextDouble() / 5.0D);
            handStack.damage(1, player, (playerx) -> playerx.sendToolBreakStatus(hand));

            world.setBlockState(pos, Ripplers.TRIMMED_CHORUS_BLOCK.getDefaultState());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
