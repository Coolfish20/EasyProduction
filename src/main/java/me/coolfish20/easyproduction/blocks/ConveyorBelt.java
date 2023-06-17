package me.coolfish20.easyproduction.blocks;

import me.coolfish20.easyproduction.Registry;
import me.coolfish20.easyproduction.tiles.ConveyorBeltTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConveyorBelt extends HorizontalBlock {

    public ConveyorBelt(Properties properties) {
        super(properties);
    }


    public VoxelShape makeShapeNS(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.0625, 0, 0, 0.9375, 0.5, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0.5, 0, 0.375, 0.625, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.625, 0.5, 0, 0.75, 0.625, 1), IBooleanFunction.OR);

        return shape;
    }

    public VoxelShape makeShapeWE(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0.0625, 1, 0.5, 0.9375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.5, 0.25, 1, 0.625, 0.375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.5, 0.625, 1, 0.625, 0.75), IBooleanFunction.OR);

        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        if(state.getValue(HorizontalBlock.FACING).equals(Direction.NORTH) ||
                state.getValue(HorizontalBlock.FACING).equals(Direction.SOUTH)){
            return makeShapeNS();
        }
        if(state.getValue(HorizontalBlock.FACING).equals(Direction.EAST) ||
                state.getValue(HorizontalBlock.FACING).equals(Direction.WEST)){
            return makeShapeWE();
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, World w, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(pos);
        if(tile.getTileData().getAllKeys().contains("item_name")){
            ItemEntity e2 = new ItemEntity(w, pos.getX(),pos.getY(),pos.getZ());
            e2.setItem(tile.getItem());
            w.addFreshEntity(e2);
        }
        ItemEntity tilestack = new ItemEntity(w, pos.getX(),pos.getY(),pos.getZ());
        tilestack.setItem(Registry.CONVEYOR_ITEM.get().getDefaultInstance());
        w.addFreshEntity(tilestack);
        super.onRemove(state, w, pos, p_196243_4_,p_196243_5_);
    }

    @Override
    public void onBlockExploded(BlockState state, World w, BlockPos pos, Explosion explosion) {
        ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(pos);
        if(tile.getTileData().getAllKeys().contains("item_name")){
            ItemEntity e2 = new ItemEntity(w, pos.getX(),pos.getY(),pos.getZ());
            e2.setItem(tile.getItem());
            w.addFreshEntity(e2);
        }
        ItemEntity tilestack = new ItemEntity(w, pos.getX(),pos.getY(),pos.getZ());
        tilestack.setItem(Registry.CONVEYOR_ITEM.get().getDefaultInstance());
        w.addFreshEntity(tilestack);

        super.onBlockExploded(state, w, pos, explosion);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Registry.CONVEYOR_TILE.get().create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HorizontalBlock.FACING, context.getHorizontalDirection());
    }
}
