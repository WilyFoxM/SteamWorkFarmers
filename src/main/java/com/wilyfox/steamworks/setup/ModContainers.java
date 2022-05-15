package com.wilyfox.steamworks.setup;

import com.wilyfox.steamworks.blocks.BeetrootFarmer.BeetrootFarmerContainer;
import com.wilyfox.steamworks.blocks.BeetrootFarmer.BeetrootFarmerScreen;
import com.wilyfox.steamworks.blocks.CactusFarmer.CactusFarmerContainer;
import com.wilyfox.steamworks.blocks.CactusFarmer.CactusFarmerScreen;
import com.wilyfox.steamworks.blocks.CarrotFarmer.CarrotFarmerContainer;
import com.wilyfox.steamworks.blocks.CarrotFarmer.CarrotFarmerScreen;
import com.wilyfox.steamworks.blocks.MelonFarmer.MelonFarmerBlock;
import com.wilyfox.steamworks.blocks.MelonFarmer.MelonFarmerContainer;
import com.wilyfox.steamworks.blocks.MelonFarmer.MelonFarmerScreen;
import com.wilyfox.steamworks.blocks.PotatoFarmer.PotatoFarmerContainer;
import com.wilyfox.steamworks.blocks.PotatoFarmer.PotatoFarmerScreen;
import com.wilyfox.steamworks.blocks.PumpkinFarmer.PumpkinFarmerContainer;
import com.wilyfox.steamworks.blocks.PumpkinFarmer.PumpkinFarmerScreen;
import com.wilyfox.steamworks.blocks.ShugarcaneFarmer.ShugarcaneFarmerContainer;
import com.wilyfox.steamworks.blocks.ShugarcaneFarmer.ShugarcaneFarmerScreen;
import com.wilyfox.steamworks.blocks.WheatFarmer.WheatFarmerContainer;
import com.wilyfox.steamworks.blocks.WheatFarmer.WheatFarmerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;

public class ModContainers {
//    public static final RegistryObject<ContainerType<StorageContainer>> STORAGE = register("storage", StorageContainer::new);
    public static final RegistryObject<ContainerType<WheatFarmerContainer>> WHEAT_FARMER = register("wheat_farmer", WheatFarmerContainer::new);
    public static final RegistryObject<ContainerType<CarrotFarmerContainer>> CARROT_FARMER = register("carrot_farmer", CarrotFarmerContainer::new);
    public static final RegistryObject<ContainerType<PotatoFarmerContainer>> POTATO_FARMER = register("potato_farmer", PotatoFarmerContainer::new);
    public static final RegistryObject<ContainerType<BeetrootFarmerContainer>> BEETROOT_FARMER = register("beetroot_farmer", BeetrootFarmerContainer::new);
    public static final RegistryObject<ContainerType<ShugarcaneFarmerContainer>> SHUGARCANE_FARMER = register("shugarcane_farmer", ShugarcaneFarmerContainer::new);
    public static final RegistryObject<ContainerType<CactusFarmerContainer>> CACTUS_FARMER = register("cactus_farmer", CactusFarmerContainer::new);
    public static final RegistryObject<ContainerType<PumpkinFarmerContainer>> PUMPKIN_FARMER = register("pumpkin_farmer", PumpkinFarmerContainer::new);
    public static final RegistryObject<ContainerType<MelonFarmerContainer>> MELON_FARMER = register("melon_farmer", MelonFarmerContainer::new);

    static void register() {
        
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreen(FMLClientSetupEvent event) {
//        ScreenManager.register(STORAGE.get(), StorageScreen::new);
        ScreenManager.register(WHEAT_FARMER.get(), WheatFarmerScreen::new);
        ScreenManager.register(CARROT_FARMER.get(), CarrotFarmerScreen::new);
        ScreenManager.register(POTATO_FARMER.get(), PotatoFarmerScreen::new);
        ScreenManager.register(BEETROOT_FARMER.get(), BeetrootFarmerScreen::new);
        ScreenManager.register(SHUGARCANE_FARMER.get(), ShugarcaneFarmerScreen::new);
        ScreenManager.register(CACTUS_FARMER.get(), CactusFarmerScreen::new);
        ScreenManager.register(PUMPKIN_FARMER.get(), PumpkinFarmerScreen::new);
        ScreenManager.register(MELON_FARMER.get(), MelonFarmerScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
