package val_int1.bigger_craft.init;

import java.util.function.Consumer;

import val_int1.bigger_craft.data.CraftingTableData;
import val_int1.bigger_craft.init.entrypoints.BCTEntrypoint;

public class BCTBuiltin implements BCTEntrypoint {

	@Override
	public void onTableRegistry(Consumer<CraftingTableData> consumer) {
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "4x4_crafting_table", 4)
				.addVariant(BCTInitCommon.MODID, "andesite_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "diorite_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "granite_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "deepslate_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "blackstone_4x4_crafting_table")
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "5x5_crafting_table", 5)
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "6x6_crafting_table", 6)
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "7x7_crafting_table", 7)
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "8x8_crafting_table", 8)
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "9x9_crafting_table", 9)
				.build()
		);

		// Chested
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_crafting_table", 3)
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_4x4_crafting_table", 4)
				.addVariant(BCTInitCommon.MODID, "andesite_chested_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "diorite_chested_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "granite_chested_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "deepslate_chested_4x4_crafting_table")
				.addVariant(BCTInitCommon.MODID, "blackstone_chested_4x4_crafting_table")
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_5x5_crafting_table", 5)
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_6x6_crafting_table", 6)
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_7x7_crafting_table", 7)
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_8x8_crafting_table", 8)
				.holdsItems()
				.build()
		);
		consumer.accept(
			CraftingTableData.builder(BCTInitCommon.MODID, "chested_9x9_crafting_table", 9)
				.holdsItems()
				.build()
		);
		
	}

}
