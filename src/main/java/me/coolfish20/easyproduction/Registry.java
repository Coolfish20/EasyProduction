package me.coolfish20.easyproduction;

import me.coolfish20.easyproduction.blocks.ConveyorBelt;
import me.coolfish20.easyproduction.blocks.ResourceMaker;
import me.coolfish20.easyproduction.items.ConveyorBeltItem;
import me.coolfish20.easyproduction.items.ResourceMakerItem;
import me.coolfish20.easyproduction.tiles.ConveyorBeltTile;
import me.coolfish20.easyproduction.tiles.ResourceMakerTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class Registry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EasyProduction.MOD_ID);
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,EasyProduction.MOD_ID);
    public static DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,EasyProduction.MOD_ID);


    //Registries

    public static RegistryObject<ConveyorBelt> CONVEYOR_BLOCK = BLOCKS.register("conveyor_belt_block",
            ()-> new ConveyorBelt(AbstractBlock.Properties.of(Material.METAL).strength(7.0f,7.0f)));
    public static RegistryObject<ResourceMaker> RESOURCE_BLOCK = BLOCKS.register("resource_maker_block",
            ()-> new ResourceMaker(AbstractBlock.Properties.of(Material.METAL).strength(7,7)));

    public static RegistryObject<TileEntityType<ConveyorBeltTile>> CONVEYOR_TILE = TILES.register("conveyor_belt_tile",
            ()-> TileEntityType.Builder.of(ConveyorBeltTile::new, Registry.CONVEYOR_BLOCK.get()).build(null));

    public static RegistryObject<TileEntityType<ResourceMakerTile>> RESOURCE_TILE = TILES.register("resource_maker_tile",()->
           TileEntityType.Builder.of(ResourceMakerTile::new, Registry.RESOURCE_BLOCK.get()).build(null));


    public static RegistryObject<Item> CONVEYOR_ITEM = ITEMS.register("conveyor_belt", ()->
            new ConveyorBeltItem(CONVEYOR_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static RegistryObject<Item> RESOURCE_ITEM = ITEMS.register("resource_maker", ()->
            new ResourceMakerItem(RESOURCE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));//TODO: create a creativ tab
}
