package me.coolfish20.easyproduction.events;

import me.coolfish20.easyproduction.tiles.ConveyorBeltTile;
import me.coolfish20.easyproduction.tiles.ResourceMakerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class RightClickBlockEvent{


    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e){
    PlayerEntity p = e.getPlayer();
    if(e.getEntity() instanceof PlayerEntity && !e.getWorld().isClientSide &&  e.getItemStack() != ItemStack.EMPTY&&
        !p.isCrouching()){
        BlockPos pos = e.getPos();
        World w = e.getWorld();
        if(w.getBlockEntity(pos) != null && e.getItemStack() != ItemStack.EMPTY){
        if(w.getBlockEntity(pos) instanceof ResourceMakerTile){
            ResourceMakerTile tile= (ResourceMakerTile) w.getBlockEntity(pos);
            if(tile.getTileData().get("product_name") == null && !e.getItemStack().getRarity().equals(Rarity.EPIC)
                    &&!e.getItemStack().getRarity().equals(Rarity.RARE)){
            tile.setProduct(e.getItemStack());
            int count =1;
            for(ResourceLocation location : ForgeRegistries.ITEMS.getKeys()){
                count++;
            }
            Random r = new Random();
            int random = r.nextInt(count);
            int count2=1;
            for (ResourceLocation location: ForgeRegistries.ITEMS.getKeys()){
            if(count2==random){
                tile.setFuel(ForgeRegistries.ITEMS.getValue(location).getDefaultInstance());
                tile.setFueled(false);
                break;
            }
                count2++;
            }
            e.getEntity().sendMessage(ITextComponent.nullToEmpty("Production Item set: "+ tile.getProduct().getItem().getRegistryName().getPath()+
                    "."+" Fuel: "+ tile.getFuel().getItem().getRegistryName().getPath()),e.getEntity().getUUID());
            e.getItemStack().setCount(e.getItemStack().getCount()-1);
            e.setCanceled(true);
            }else {
                if(!tile.getFuel().equals(e.getItemStack())) {
                    e.getEntity().sendMessage(
                            new StringTextComponent("This Block Already Has a Production Item: "
                                    + tile.getProduct().getItem().getRegistryName().getPath() + "."), e.getEntity().getUUID());
                    e.setCanceled(true);
                }else {
                    e.getEntity().sendMessage(ITextComponent.
                            nullToEmpty("You cannot produce the fuel item with this block!"),e.getEntity().getUUID());
                    e.setCanceled(true);
                }
            }
        }else {
            if(w.getBlockEntity(pos) instanceof ConveyorBeltTile){
                ConveyorBeltTile tile = (ConveyorBeltTile) w.getBlockEntity(pos);
                if(tile.getTileData().get("item_name") == null){
                    tile.setItem(e.getItemStack());
                    e.getItemStack().setCount(e.getItemStack().getCount()-1);
                    e.setCanceled(true);
                }else {
                    e.getEntity().sendMessage(new StringTextComponent("This Conveyor Belt already has an item "+ tile.getItem().getItem().getRegistryName()+ "."), e.getEntity().getUUID());
                    e.setCanceled(true);
                }

            }
        }
        }
    }
    }

}
