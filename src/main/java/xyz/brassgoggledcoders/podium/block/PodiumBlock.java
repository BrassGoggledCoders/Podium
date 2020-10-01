package xyz.brassgoggledcoders.podium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import xyz.brassgoggledcoders.podium.content.PodiumBlocks;
import xyz.brassgoggledcoders.podium.tileentity.PodiumTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class PodiumBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape POST_SHAPE = Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    public static final VoxelShape COMMON_SHAPE = VoxelShapes.or(BASE_SHAPE, POST_SHAPE);
    public static final VoxelShape TOP_PLATE_SHAPE = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static final VoxelShape COLLISION_SHAPE = VoxelShapes.or(COMMON_SHAPE, TOP_PLATE_SHAPE);
    public static final VoxelShape WEST_SHAPE = VoxelShapes.or(Block.makeCuboidShape(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.makeCuboidShape(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.makeCuboidShape(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.makeCuboidShape(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.makeCuboidShape(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), COMMON_SHAPE);
    public static final VoxelShape EAST_SHAPE = VoxelShapes.or(Block.makeCuboidShape(15.0D, 10.0D, 0.0D, 10.666667D, 14.0D, 16.0D), Block.makeCuboidShape(10.666667D, 12.0D, 0.0D, 6.333333D, 16.0D, 16.0D), Block.makeCuboidShape(6.333333D, 14.0D, 0.0D, 2.0D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 15.0D, 16.0D, 14.0D, 10.666667D), Block.makeCuboidShape(0.0D, 12.0D, 10.666667D, 16.0D, 16.0D, 6.333333D), Block.makeCuboidShape(0.0D, 14.0D, 6.333333D, 16.0D, 18.0D, 2.0D), COMMON_SHAPE);

    public PodiumBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH)
                .with(HAS_BOOK, false)
                .with(POWERED, false)
        );
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof PodiumTileEntity) {
            PodiumTileEntity podiumTileEntity = (PodiumTileEntity) tileEntity;
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty() && podiumTileEntity.getDisplayItemStack().isEmpty()) {
                podiumTileEntity.setDisplayItemStack(heldItem);
                world.setBlockState(pos, state.with(HAS_BOOK, true));
                return ActionResultType.SUCCESS;
            } else {
                if (player.isSneaking()) {
                    if (heldItem.isEmpty()) {
                        player.setHeldItem(hand, podiumTileEntity.takeDisplayItemStack());
                        world.setBlockState(pos, state.with(HAS_BOOK, false));
                        return ActionResultType.SUCCESS;
                    }
                } else {
                    return podiumTileEntity.activateBehavior(player);
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PodiumTileEntity(PodiumBlocks.PODIUM_TILE_ENTITY.get());
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return COMMON_SHAPE;
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        ItemStack itemstack = context.getItem();
        CompoundNBT compoundnbt = itemstack.getTag();
        PlayerEntity playerentity = context.getPlayer();
        boolean flag = false;
        if (!world.isRemote && playerentity != null && compoundnbt != null && playerentity.canUseCommandBlock() && compoundnbt.contains("BlockEntityTag")) {
            CompoundNBT blockEntityTag = compoundnbt.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("displayItemStack")) {
                flag = true;
            }
        }

        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(HAS_BOOK, flag);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof PodiumTileEntity) {
            return ((PodiumTileEntity) tileEntity).getComparatorSignal();
        } else {
            return 0;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return COMMON_SHAPE;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_BOOK, POWERED);
    }


    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            if (state.get(HAS_BOOK)) {
                this.dropBook(state, world, pos);
            }

            if (state.get(POWERED)) {
                world.notifyNeighborsOfStateChange(pos.down(), this);
            }

            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean allowsMovement(BlockState state, IBlockReader blockReader, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockState, ServerWorld world, BlockPos pos, Random rand) {
        setPowered(world, pos, blockState, false);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean canProvidePower(BlockState blockState) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getWeakPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return blockState.get(POWERED) ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getStrongPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return side == Direction.UP && blockState.get(POWERED) ? 15 : 0;
    }

    public static void pulse(World worldIn, BlockPos pos, BlockState state) {
        setPowered(worldIn, pos, state, true);
        worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 2);
        worldIn.playEvent(1043, pos, 0);
    }

    private static void setPowered(World world, BlockPos pos, BlockState blockState, boolean powered) {
        world.setBlockState(pos, blockState.with(POWERED, powered), 3);
        notifyNeighbors(world, pos, blockState);
    }

    private static void notifyNeighbors(World world, BlockPos pos, BlockState blockState) {
        world.notifyNeighborsOfStateChange(pos.down(), blockState.getBlock());
    }

    private void dropBook(BlockState state, World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof PodiumTileEntity) {
            PodiumTileEntity podiumTileEntity = (PodiumTileEntity) tileentity;
            Direction direction = state.get(FACING);
            ItemStack itemstack = podiumTileEntity.takeDisplayItemStack();
            float f = 0.25F * (float) direction.getXOffset();
            float f1 = 0.25F * (float) direction.getZOffset();
            ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D + (double) f, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5D + (double) f1, itemstack);
            itemEntity.setDefaultPickupDelay();
            world.addEntity(itemEntity);
        }

    }
}
