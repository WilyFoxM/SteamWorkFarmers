package com.wilyfox.steamworks.setup;

import com.wilyfox.steamworks.blocks.BeetrootFarmer.BeetrootFarmerBlock;
import com.wilyfox.steamworks.blocks.CactusFarmer.CactusFarmerBlock;
import com.wilyfox.steamworks.blocks.CarrotFarmer.CarrotFarmerBlock;
import com.wilyfox.steamworks.blocks.MelonFarmer.MelonFarmerBlock;
import com.wilyfox.steamworks.blocks.PotatoFarmer.PotatoFarmerBlock;
import com.wilyfox.steamworks.blocks.PumpkinFarmer.PumpkinFarmerBlock;
import com.wilyfox.steamworks.blocks.ShugarcaneFarmer.ShugarcaneFarmerBlock;
import com.wilyfox.steamworks.blocks.WheatFarmer.WheatFarmerBlock;
import com.wilyfox.steamworks.utils.tabs.FarmersTab;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeetrootBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

//    public static final RegistryObject<Block> COPPER_BLOCK = register("copper_block", () ->
//            new Block(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().harvestLevel(2).sound(SoundType.METAL)));
//
//    public static final RegistryObject<StorageBlock> STORAGE_BLOCK = registerWithTooltip("storage_block", () ->
//            new StorageBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD)), "Farms Wheat automatically");

    public static final RegistryObject<WheatFarmerBlock> WHEAT_FARMER_BLOCK = registerWithTooltip("wheat_farmer", () ->
            new WheatFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Wheat automatically");

    public static final RegistryObject<CarrotFarmerBlock> CARROT_FARMER_BLOCK = registerWithTooltip("carrot_farmer", () ->
            new CarrotFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Carrot automatically");

    public static final RegistryObject<PotatoFarmerBlock> POTATO_FARMER_BLOCK = registerWithTooltip("potato_farmer", () ->
            new PotatoFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Potato automatically");

    public static final RegistryObject<BeetrootFarmerBlock> BEETROOT_FARMER_BLOCK = registerWithTooltip("beetroot_farmer", () ->
            new BeetrootFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Beetroot automatically");

    public static final RegistryObject<ShugarcaneFarmerBlock> SHUGARCANE_FARMER_BLOCK = registerWithTooltip("shugarcane_farmer", () ->
            new ShugarcaneFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Sugarcane automatically");

    public static final RegistryObject<CactusFarmerBlock> CACTUS_FARMER_BLOCK = registerWithTooltip("cactus_farmer", () ->
            new CactusFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Cactus automatically");

    public static final RegistryObject<PumpkinFarmerBlock> PUMPKIN_FARMER_BLOCK = registerWithTooltip("pumpkin_farmer", () ->
            new PumpkinFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Pumpkins automatically");

    public static final RegistryObject<MelonFarmerBlock> MELON_FARMER_BLOCK = registerWithTooltip("melon_farmer", () ->
            new MelonFarmerBlock(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.STONE)), "Farms Melons automatically");

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, () ->
                new BlockItem(ret.get(), new Item.Properties().tab(FarmersTab.FARMERS_TAB)));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerWithTooltip(String name, Supplier<T> block, String tooltipKey) {
        RegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, () ->
                new BlockItem(ret.get(), new Item.Properties().tab(FarmersTab.FARMERS_TAB)) {
                    @Override
                    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
                        tooltip.add(new TranslationTextComponent(tooltipKey));
                        super.appendHoverText(stack, world, tooltip, flagIn);
                    }
                });
        return ret;
    }

    static void register() {};
}
