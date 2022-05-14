package com.wilyfox.steamworks.blocks.CactusFarmer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wilyfox.steamworks.SteamWorks;
import com.wilyfox.steamworks.blocks.ShugarcaneFarmer.ShugarcaneFarmerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CactusFarmerScreen extends ContainerScreen<CactusFarmerContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(SteamWorks.MODID, "textures/gui/shugarcane_farmer.png");

    public CactusFarmerScreen(CactusFarmerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.inventoryLabelY = this.inventoryLabelY - 18;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y , partialTicks);
        this.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(TEXTURE);

        int posX = (this.width - this.imageWidth) / 2;
        int posY = (this.height - this.imageHeight) / 2;

        blit(matrixStack, posX, posY, 0, 0, 176, 149);
    }
}
