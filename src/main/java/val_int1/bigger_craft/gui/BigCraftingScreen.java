package val_int1.bigger_craft.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import val_int1.bigger_craft.gui.handlers.CraftingScreenHandler;

@Environment(EnvType.CLIENT)
public class BigCraftingScreen extends HandledScreen<CraftingScreenHandler> {

	private final Identifier texture;
	
	public BigCraftingScreen(CraftingScreenHandler handler, PlayerInventory playerInv, Text title) {
		super(handler, playerInv, title);
		this.backgroundWidth = 176 + ((handler.gridWidth - 3) * 18);
		this.backgroundHeight = 166 + ((handler.gridHeight - 3) * 18);
		this.playerInventoryTitleY = this.backgroundHeight - 94;
		
		Identifier handlerId = handler.table.tableId;
		this.texture = new Identifier(handlerId.getNamespace(), "textures/gui/container/" + handlerId.getPath() + ".png");
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}
	
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, this.texture);
		context.drawTexture(this.texture, this.x, this.y, 0, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
	}

}
