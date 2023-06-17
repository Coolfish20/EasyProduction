package me.coolfish20.easyproduction.tiles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.coolfish20.easyproduction.Registry;
import me.coolfish20.easyproduction.blocks.ConveyorBelt;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ResourceMakerTile extends TileEntity implements ITickableTileEntity {

    public ResourceMakerTile() {
        super(Registry.RESOURCE_TILE.get());
    }

    public void setFueled(boolean isFueled){
        this.getTileData().putBoolean("hasFuel",isFueled);
    }

    public void setFuel(ItemStack stack){
        this.getTileData().putString("fuel_name", String.valueOf(stack.getItem().getRegistryName()));
    }
    public ItemStack getFuel(){
        if (!this.getTileData().getString("fuel_name").equals("")) {

            String registry_name = getTileData().getString("fuel_name");

            ResourceLocation location = null;
            try {
                location = ResourceLocation.read(new StringReader(registry_name));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            if(location != null) {
                Item item = ForgeRegistries.ITEMS.getValue(location);
                if (item != null) {
                    return item.getDefaultInstance();
                }
            }
        }
        return null;
    }

    public void setProduct(ItemStack stack){
        this.getTileData().putString("product_name", String.valueOf(stack.getItem().getRegistryName()));
    }

    public ItemStack getProduct(){

        if (!this.getTileData().getString("product_name").equals("")) {
            String registry_name = getTileData().getString("product_name");
            ResourceLocation location = null;
            try {
                location = ResourceLocation.read(new StringReader(registry_name));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            if(location != null) {
                Item item = ForgeRegistries.ITEMS.getValue(location);
                if (item != null) {
                    return item.getDefaultInstance();
                }
            }
        }
        return null;
    }


    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        for(String key : this.getTileData().getAllKeys()){
            if(key.equals("product_name")||
                    key.equals("fuel_name")){
                nbt.putString(key, this.getTileData().getString(key));
            }else {
                if(key.equals("hasFuel")){
                    nbt.putBoolean("fuel_amount", getTileData().getBoolean("hasFuel"));
                }
            }
        }
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        for(String key : nbt.getAllKeys()){
            if(key.equals("product_name") ||
                    key.equals("fuel_name")){
                getTileData().putString(key, nbt.getString(key));
            }else {
                if(key.equals("hasFuel")){
                    getTileData().putBoolean(key, nbt.getBoolean("hasFuel"));
                }
            }
        }
    }

    @Override
    public void tick() {

        if(getTileData().getAllKeys().contains("hasFuel")) {
            if (getTileData().getBoolean("hasFuel") == true) {
            setFueled(false);
                BlockPos east = new BlockPos(this.worldPosition.getX() + 1, this.worldPosition.getY(), this.worldPosition.getZ());
                BlockPos west = new BlockPos(this.worldPosition.getX() - 1, this.worldPosition.getY(), this.worldPosition.getZ());
                BlockPos north = new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() - 1);
                BlockPos south = new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() + 1);

                World w = this.getLevel();
                switch (getLevel().getBlockState(worldPosition).getValue(HorizontalBlock.FACING)) {
                    case EAST:
                        if (!w.getBlockState(east).isAir()) {
                            BlockState state = w.getBlockState(east);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                        .getValue(HorizontalBlock.FACING))) {
                                    ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(east);
                                    if (tile != null) {
                                        tile.setItem(getProduct());
                                    } else {
                                        ItemEntity e = new ItemEntity(w, west.getX(), west.getY(), west.getZ());
                                        e.setItem(getProduct());
                                        w.addFreshEntity(e);
                                    }
                                }
                            } else {
                                ItemEntity e = new ItemEntity(w, west.getX(), west.getY(), west.getZ());
                                e.setItem(getProduct());
                                w.addFreshEntity(e);
                            }
                        }

                    case WEST:
                        if (!w.getBlockState(west).isAir()) {
                            BlockState state = w.getBlockState(west);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                        .getValue(HorizontalBlock.FACING))) {
                                    ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(east);
                                    if (tile != null) {
                                        tile.setItem(getProduct());
                                    } else {
                                        ItemEntity e = new ItemEntity(w, east.getX(), east.getY(), east.getZ());
                                        e.setItem(getProduct());
                                        w.addFreshEntity(e);
                                    }
                                }
                            } else {
                                ItemEntity e = new ItemEntity(w, east.getX(), east.getY(), east.getZ());
                                e.setItem(getProduct());
                                w.addFreshEntity(e);
                            }
                        }

                    case SOUTH:
                        if (!w.getBlockState(south).isAir()) {
                            BlockState state = w.getBlockState(south);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                        .getValue(HorizontalBlock.FACING))) {
                                    ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(east);
                                    if (tile != null) {
                                        tile.setItem(getProduct());
                                    } else {
                                        ItemEntity e = new ItemEntity(w, north.getX(), north.getY(), north.getZ());
                                        e.setItem(getProduct());
                                        w.addFreshEntity(e);
                                    }
                                }
                            } else {
                                ItemEntity e = new ItemEntity(w, north.getX(), north.getY(), north.getZ());
                                e.setItem(getProduct());
                                w.addFreshEntity(e);
                            }
                        }

                    case NORTH:
                        if (!w.getBlockState(north).isAir()) {
                            BlockState state = w.getBlockState(north);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                        .getValue(HorizontalBlock.FACING))) {
                                    ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(east);
                                    if (tile != null) {
                                        tile.setItem(getProduct());
                                    } else {
                                        ItemEntity e = new ItemEntity(w, south.getX(), south.getY(), south.getZ());
                                        e.setItem(getProduct());
                                        w.addFreshEntity(e);
                                    }
                                }
                            } else {
                                ItemEntity e = new ItemEntity(w, south.getX(), south.getY(), south.getZ());
                                e.setItem(getProduct());
                                w.addFreshEntity(e);
                            }
                        }

                }


            }
        }






    }
}
