package val_int1.bigger_craft.init;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import val_int1.bigger_craft.gui.BigCraftingScreen;

public class BCTInitClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HandledScreens.register(BCTInitCommon.TABLE_SCREEN_HANDLER, BigCraftingScreen::new);
	}

}
