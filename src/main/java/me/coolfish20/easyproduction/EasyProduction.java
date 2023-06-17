package me.coolfish20.easyproduction;


import me.coolfish20.easyproduction.events.RightClickBlockEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("easyproduction")
public class EasyProduction {

    public static String MOD_ID = "easyproduction";

    public EasyProduction() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.BLOCKS.register(bus);
        Registry.TILES.register(bus);
        Registry.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(new RightClickBlockEvent());
    }
}
