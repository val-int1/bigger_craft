package val_int1.bigger_craft.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.recipe.ShapelessRecipe;

@Mixin(ShapelessRecipe.Serializer.class)
public class ShapelessRecipe_SerializerMixin {

	@ModifyConstant(method="read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/ShapelessRecipe;", constant=@Constant(intValue=9))
	private int biggerCraft_modConstRead(int original) {
		return Integer.MAX_VALUE;
	}
	
	
}
