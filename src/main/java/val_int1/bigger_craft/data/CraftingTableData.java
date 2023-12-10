package val_int1.bigger_craft.data;

import java.util.ArrayList;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;

public class CraftingTableData {
	
	public final Identifier id;
	public final int gridWidth, gridHeight;
	public final boolean holdsItems;
	public final ImmutableList<Identifier> variants;
	
	private CraftingTableData(Identifier id, int gridWidth, int gridHeight, boolean holdsItems, ImmutableList<Identifier> variants) {
		this.id = id;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.holdsItems = holdsItems;
		this.variants = variants;
	}
	
	public static Builder builder(String id, int size) {
		return builder(new Identifier(id), size, size);
	}
	
	public static Builder builder(String id, int gridWidth, int gridHeight) {
		return builder(new Identifier(id), gridWidth, gridHeight);
	}
	
	public static Builder builder(String namespace, String path, int size) {
		return builder(new Identifier(namespace, path), size, size);
	}
	
	public static Builder builder(String namespace, String path, int gridWidth, int gridHeight) {
		return builder(new Identifier(namespace, path), gridWidth, gridHeight);
	}
	
	public static Builder builder(Identifier id, int size) {
		return builder(id, size, size);
	}
	
	public static Builder builder(Identifier id, int gridWidth, int gridHeight) {
		Objects.requireNonNull(id);
		
		Builder builder = Builder.INSTANCE;
		if(!builder.built) {
			throw new IllegalStateException("Builder already in use");
		}
		builder.built = false;
		
		builder.id = id;
		builder.gridWidth = gridWidth;
		builder.gridHeight = gridHeight;
		builder.variants.clear();
		builder.holdsItems = false;
		
		return builder;
	}
	
	public static class Builder {
		
		private static final Builder INSTANCE = new Builder();
		
		private boolean built = true; // To avoid the first builder(...) call to throw
		
		private Identifier id;
		private int gridWidth, gridHeight;
		
		private final ArrayList<Identifier> variants = new ArrayList<Identifier>();
		private boolean holdsItems = false;
		
		private Builder() {
			if(INSTANCE != null) {
				throw new IllegalStateException("Do not instanciate the builder manually");
			}
		}
		
		/**
		 * Registers a variant for this table.
		 * @param id - {@link Identifier} of the variant (as a string)
		 * @return Itself
		 */
		public Builder addVariant(String id) {
			return this.addVariant(new Identifier(id));
		}
		
		/**
		 * Registers a variant for this table.
		 * @param namespace - namespace for the {@link Identifier} of the variant
		 * @param path - path for the {@link Identifier} of the variant
		 * @return Itself
		 */
		public Builder addVariant(String namespace, String path) {
			return this.addVariant(new Identifier(namespace, path));
		}
		
		/**
		 * Registers a variant for this table.
		 * @param id - {@link Identifier} of the variant
		 * @return Itself
		 */
		public Builder addVariant(Identifier id) {
			this.variants.add(id);
			return this;
		}
		
		/**
		 * Enables the table to hold items.
		 * @return Itself
		 */
		public Builder holdsItems() {
			this.holdsItems = true;
			return this;
		}
		
		/**
		 * Creates the CraftingTableData object. You must refresh the builder before calling it again.	
		 * @return The built {@link CraftingTableData} object	
		 */
		public CraftingTableData build() {
			if(this.built) {
				throw new IllegalStateException("Builder has already been built, please call `CraftingTableData.builder(...)` again");
			}
			this.built = true;
			return new CraftingTableData(this.id, this.gridWidth, this.gridHeight, this.holdsItems, ImmutableList.copyOf(this.variants));
		}
	}

}
