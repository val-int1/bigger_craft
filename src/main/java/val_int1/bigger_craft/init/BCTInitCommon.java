package val_int1.bigger_craft.init;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import val_int1.bigger_craft.blocks.CraftingTableBlock;
import val_int1.bigger_craft.blocks.entities.CraftingTableBEntity;
import val_int1.bigger_craft.gui.handlers.CraftingScreenHandler;
import val_int1.bigger_craft.init.entrypoints.BCTEntrypoint;

public class BCTInitCommon implements ModInitializer {

	public static final String MODID = "bigger_craft";
	public static final String MOD_NAME = "Bigger Crafter Tabler";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	
	public static ScreenHandlerType<CraftingScreenHandler> TABLE_SCREEN_HANDLER;
	public static BlockEntityType<CraftingTableBEntity> CHESTED_BE_TYPE;
	
	private static final ArrayList<Identifier> REGISTERED = new ArrayList<Identifier>();
	private static final ArrayList<Block> CHESTED = new ArrayList<Block>();
	
	private static final ArrayList<ItemStack> STACKS = new ArrayList<ItemStack>();
	
	@Override
	public void onInitialize() {		
		for(BCTEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("bct_register", BCTEntrypoint.class)) {
			entrypoint.onTableRegistry((tableData) -> {
				Identifier id = tableData.id;
				int gridWidth = tableData.gridWidth, gridHeight = tableData.gridHeight;
				boolean holdsItems = tableData.holdsItems;
				
				createTable(id, id, gridWidth, gridHeight, holdsItems);
				for(Identifier id2 : tableData.variants) {
					createTable(id2, id, gridWidth, gridHeight, holdsItems);
				}
			});
		}
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(BCTInitCommon::addTablesToCreative);
		Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, "items"),
			FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.bigger_craft.items"))
				.icon(() -> STACKS.get(0))
				.entries(BCTInitCommon::addTablesToCreative)
				.build()
		);
		
		TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "crafting_screen_handler"),
			new ExtendedScreenHandlerType<CraftingScreenHandler>(CraftingScreenHandler::new));
		
		Block[] blocks = CHESTED.toArray(Block[]::new);
		CHESTED_BE_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "chested_big_crafting_table"),
			FabricBlockEntityTypeBuilder.<CraftingTableBEntity>create(CraftingTableBEntity::new, blocks).build());
	}
	
	private static void createTable(Identifier id, Identifier tableId, int gridWidth, int gridHeight, boolean holdsItems) {
		if(REGISTERED.contains(id)) {
			throw new IllegalArgumentException("Crafting Table with id `" + id + "` already exists");
		}
		REGISTERED.add(id);
		
		Block block = CraftingTableBlock.create(tableId, gridWidth, gridHeight, holdsItems);
		if(holdsItems) {
			CHESTED.add(block);
		}
		
		Registry.register(Registries.BLOCK, id, block);
		Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
		STACKS.add(new ItemStack(block));
	}
	
	private static void addTablesToCreative(FabricItemGroupEntries entries) {
		entries.addAfter(Blocks.CRAFTING_TABLE, STACKS);
	}
	
	private static void addTablesToCreative(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
		entries.addAll(STACKS);
	}
	
}
