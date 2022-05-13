package com.wilyfox.steamworks;

import com.wilyfox.steamworks.setup.ModContainers;
import com.wilyfox.steamworks.setup.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("steamworks")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SteamWorks
{
    public static final String MODID = "steamworks";

    private static final Logger LOGGER = LogManager.getLogger();

    public SteamWorks() {
        MinecraftForge.EVENT_BUS.register(this);

        Registration.register();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModContainers.registerScreen(event);
    }

}
