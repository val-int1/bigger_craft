package val_int1.bigger_craft.init.entrypoints;

import java.util.function.Consumer;

import val_int1.bigger_craft.data.CraftingTableData;

@FunctionalInterface
public interface BCTEntrypoint {
	
	public void onTableRegistry(Consumer<CraftingTableData> consumer);
	
}
