package glowsand.ripplers.block;


import glowsand.ripplers.Ripplers;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrimmedChorusBlock extends BlockWithEntity  {
    public TrimmedChorusBlock(Settings settings) {
        super(settings);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3,0,0.3,.7,.4,.7);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        TrimmedChorusBlockEntity trimmedChorusBlockEntity= (TrimmedChorusBlockEntity) world.getBlockEntity(pos);
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        else if (trimmedChorusBlockEntity != null  && trimmedChorusBlockEntity.rippler != null && trimmedChorusBlockEntity.player==null){
            trimmedChorusBlockEntity.player = player.getUuid();
            ((ServerPlayerEntity)player).setSpawnPoint(world.getRegistryKey(),pos,0f,false,true);
            Ripplers.criterion.trigger(((ServerPlayerEntity)player));
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }


    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }





    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrimmedChorusBlockEntity(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }





    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY,TrimmedChorusBlockEntity::tick);
    }
}
