package me.coolfish20.easyproduction.tiles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.coolfish20.easyproduction.Registry;
import me.coolfish20.easyproduction.blocks.ConveyorBelt;
import me.coolfish20.easyproduction.blocks.ResourceMaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ConveyorBeltTile extends TileEntity implements ITickableTileEntity {


    public ConveyorBeltTile() {
        super(Registry.CONVEYOR_TILE.get());
    }

    public void setItem(ItemStack stack){
        this.getTileData().putString("item_name", String.valueOf(stack.getItem().getRegistryName()));
        this.setChanged();
    }

    public void clearItem(){
        this.getTileData().remove("item_name");
        this.setChanged();
    }

    public ItemStack getItem() {
        if (!this.getTileData().getString("item_name").equals("")) {

            String registry_name = getTileData().getString("item_name");

            ResourceLocation location = null;
            try {
                location = ResourceLocation.read(new StringReader(registry_name));
            } catch (CommandSyntaxException e) {
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
        for(String key :this.getTileData().getAllKeys()){
            if(key.equals("item_name")){
                nbt.putString(key, this.getTileData().getString(key));
            }
        }
        return nbt;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT nbt) {
        super.load(p_230337_1_, nbt);
        for(String key :nbt.getAllKeys()){
            if(key.equals("item_name")){
                this.getTileData().putString(key, nbt.getString(key));
            }
        }
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            ConveyorBelt belt = (ConveyorBelt) this.getLevel().getBlockState(worldPosition).getBlock();
            World w = this.getLevel();

            BlockPos east = new BlockPos(this.worldPosition.getX() + 1, this.worldPosition.getY(), this.worldPosition.getZ());
            BlockPos west = new BlockPos(this.worldPosition.getX() - 1, this.worldPosition.getY(), this.worldPosition.getZ());
            BlockPos north = new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() - 1);
            BlockPos south = new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() + 1);
            if(getItem() != null) {
                switch (getLevel().getBlockState(worldPosition).getValue(HorizontalBlock.FACING)) {
                    case SOUTH:
                        if (!w.getBlockState(south).isAir()) {
                            BlockState state = w.getBlockState(south);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(south);
                                if (state.getValue(HorizontalBlock.FACING) != null) {
                                    if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                            .getValue(HorizontalBlock.FACING)) && getItem() != null&& tile.getItem() ==null) {
                                        tile.setItem(getItem());
                                        this.clearItem();
                                    }
                                }

                            } else {
                                if (state.getBlock() instanceof ResourceMaker) {
                                    ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(south);
                                    if (tile.getFuel() != null) {
                                        ItemStack s = getItem();
                                        ItemStack fuel = tile.getFuel();
                                        if (ItemStack.isSame(s,fuel)) {
                                        tile.setFueled(true);
                                        this.clearItem();
                                        }
                                    } else {
                                        ItemEntity e = new ItemEntity(w, south.getX(), south.getY(), south.getZ());
                                        e.setItem(getItem());
                                        w.addFreshEntity(e);
                                        this.clearItem();
                                    }
                                }
                            }

                        } else {
                            if (getItem() != null) {
                                ItemEntity e = new ItemEntity(w, south.getX(), south.getY(), south.getZ());
                                e.setItem(getItem());
                                w.addFreshEntity(e);
                                this.clearItem();
                            }
                        }

                    case NORTH:
                        if (!w.getBlockState(north).isAir()) {
                            BlockState state = w.getBlockState(north);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(north);
                                if (state.getValue(HorizontalBlock.FACING) != null) {
                                    if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                            .getValue(HorizontalBlock.FACING)) && getItem() != null&& tile.getItem() ==null) {
                                        tile.setItem(getItem());
                                        this.clearItem();
                                    }
                                }

                            } else {
                                if (state.getBlock() instanceof ResourceMaker) {
                                    ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(north);
                                    if (tile.getFuel() != null) {
                                        ItemStack s = getItem();
                                        ItemStack fuel = tile.getFuel();
                                        if (ItemStack.isSame(s,fuel)) {
                                        tile.setFueled(true);
                                        this.clearItem();
                                        }
                                    } else {
                                        ItemEntity e = new ItemEntity(w, north.getX(), north.getY(), north.getZ());
                                        e.setItem(getItem());
                                        w.addFreshEntity(e);
                                        this.clearItem();
                                    }
                                }
                            }

                        } else {
                            if (getItem() != null) {
                                ItemEntity e = new ItemEntity(w, north.getX(), north.getY(), north.getZ());
                                e.setItem(getItem());
                                w.addFreshEntity(e);
                                this.clearItem();
                            }
                        }
                    case WEST:
                        if (!w.getBlockState(west).isAir()) {
                            BlockState state = w.getBlockState(west);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(west);
                                if (state.getValue(HorizontalBlock.FACING) != null) {
                                    if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                            .getValue(HorizontalBlock.FACING)) && getItem() != null && tile.getItem() ==null) {
                                        tile.setItem(getItem());
                                        this.clearItem();
                                    }
                                }

                            } else {
                                if (state.getBlock() instanceof ResourceMaker) {
                                    ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(west);
                                    if (tile.getFuel() != null) {
                                        ItemStack s = getItem();
                                        ItemStack fuel = tile.getFuel();
                                        if (ItemStack.isSame(s,fuel)) {
                                            tile.setFueled(true);
                                            this.clearItem();
                                        }
                                    } else {
                                        if (getItem() != null) {
                                            ItemEntity e = new ItemEntity(w, west.getX(), west.getY(), west.getZ());
                                            e.setItem(getItem());
                                            w.addFreshEntity(e);
                                            this.clearItem();
                                        }
                                    }
                                }
                            }

                        } else {
                            if (getItem() != null) {
                                ItemEntity e = new ItemEntity(w, west.getX(), west.getY(), west.getZ());
                                e.setItem(getItem());
                                w.addFreshEntity(e);
                                this.clearItem();
                            }
                        }

                    case EAST:
                        if (!w.getBlockState(east).isAir()) {
                            BlockState state = w.getBlockState(east);
                            if (state.getBlock() instanceof ConveyorBelt) {
                                ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(east);
                                if (state.getValue(HorizontalBlock.FACING) != null) {
                                    if (state.getValue(HorizontalBlock.FACING).equals(getLevel().getBlockState(worldPosition)
                                            .getValue(HorizontalBlock.FACING)) && getItem() != null&& tile.getItem() ==null) {
                                        tile.setItem(getItem());
                                        this.clearItem();
                                    }
                                }

                            } else {
                                if (state.getBlock() instanceof ResourceMaker) {
                                    ResourceMakerTile tile = (ResourceMakerTile) w.getBlockEntity(east);
                                    if (tile.getFuel() != null) {
                                        ItemStack s = getItem();
                                        ItemStack fuel = tile.getFuel();
                                        if (ItemStack.isSame(s,fuel)) {
                                        tile.setFueled(true);
                                        this.clearItem();
                                        }
                                    } else {
                                        ItemEntity e = new ItemEntity(w, east.getX(), east.getY(), east.getZ());
                                        e.setItem(getItem());
                                        w.addFreshEntity(e);
                                        this.clearItem();
                                    }
                                }
                            }

                        } else {
                            if (getItem() != null) {
                                ItemEntity e = new ItemEntity(w, east.getX(), east.getY(), east.getZ());
                                e.setItem(getItem());
                                w.addFreshEntity(e);
                                this.clearItem();
                            }
                        }

                }
            }
        }
    }
}
