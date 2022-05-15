package com.wilyfox.steamworks.utils.tabs;

import com.wilyfox.steamworks.setup.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class FarmersTab {
    public static final ItemGroup FARMERS_TAB = new ItemGroup("farmers_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.WHEAT_FARMER_BLOCK.get());
        }
    };
}
