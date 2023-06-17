package me.coolfish20.easyproduction.blocks;

import me.coolfish20.easyproduction.Registry;
import me.coolfish20.easyproduction.tiles.ResourceMakerTile;
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

public class ResourceMaker extends HorizontalBlock {

    public ResourceMaker(Properties properties) {
        super(properties);
    }

    public VoxelShape makeShapeNS(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0, 0, 0.75, 1, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 1, 0.375, 0.5625, 1.125, 0.5), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.3125, 1.125, 0.25, 0.6875, 1.25, 0.625), IBooleanFunction.OR);

        return shape;
    }


    public VoxelShape makeShapeWE(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0.25, 1, 1, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.375, 1, 0.4375, 0.5, 1.125, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 1.125, 0.3125, 0.625, 1.25, 0.6875), IBooleanFunction.OR);

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
    public void setPlacedBy(World w, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p, ItemStack stack) {
        ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(p_180633_2_);
        if(tile != null) {
            if(stack.getTag() !=null) {
                if (stack.getTag().getAllKeys().contains("fuel_name")) {
                    tile.getTileData().putString("fuel_name", stack.getTag().getString("fuel_name"));
                    tile.setFueled(false);
                }
                if (stack.getTag().getAllKeys().contains("product_name")) {
                    tile.getTileData().putString("product_name", stack.getTag().getString("product_name"));
                }
                tile.setChanged();
            }

        }
        super.setPlacedBy(w, p_180633_2_, p_180633_3_, p, stack);
    }

    @Override
    public void onRemove(BlockState state, World w, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(pos);
        ItemEntity tileentity = new ItemEntity(w,pos.getX(),pos.getY(),pos.getZ());
        ItemStack item = Registry.RESOURCE_ITEM.get().getDefaultInstance();
        tileentity.setItem(item);
        if(tile !=null) {
            item.getOrCreateTag();
            if (tile.getTileData().getAllKeys().contains("fuel_name")) {
                tileentity.getItem().getTag().putString("fuel_name", tile.getTileData().getString("fuel_name"));
            }
            if (tile.getTileData().getAllKeys().contains("product_name")) {
                tileentity.getItem().getTag().putString("product_name", tile.getTileData().getString("product_name"));
            }
            if (tile.getTileData().getAllKeys().contains("hasFuel")) {
            if(tile.getTileData().getBoolean("hasFuel")) {
                ItemEntity fuel = new ItemEntity(w, pos.getX(), pos.getY(), pos.getZ());
                fuel.setItem(tile.getFuel());
                tileentity.getItem().getTag().putBoolean("hasFuel", false);
                w.addFreshEntity(fuel);
            }
            }
        }
        w.addFreshEntity(tileentity);
        super.onRemove(state, w, pos, p_196243_4_, p_196243_5_);
    }

    @Override
    public void onBlockExploded(BlockState state, World w, BlockPos pos, Explosion explosion) {
        ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(pos);
        ItemEntity tileentity = new ItemEntity(w,pos.getX(),pos.getY(),pos.getZ());
        tileentity.setItem(Registry.RESOURCE_ITEM.get().getDefaultInstance());
        if(tile.getTileData().getAllKeys().contains("fuel_name")){
            tileentity.getItem().getTag().putString("fuel_name", tile.getTileData().getString("fuel_name"));
        }
        if(tile.getTileData().getAllKeys().contains("product_name")){
            tileentity.getItem().getTag().putString("product_name", tile.getTileData().getString("product_name"));
        }
        if(tile.getTileData().getAllKeys().contains("hasFuel")){
            ItemEntity fuel = new ItemEntity(w, pos.getX(),pos.getY(),pos.getZ());
            fuel.setItem(tile.getFuel());
            tileentity.getItem().getTag().putBoolean("hasFuel", false);
            w.addFreshEntity(fuel);
        }
        w.addFreshEntity(tileentity);

        super.onBlockExploded(state, w, pos, explosion);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Registry.RESOURCE_TILE.get().create();
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
