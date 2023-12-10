package val_int1.bigger_craft.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.recipe.ShapelessRecipe;

@Mixin(ShapelessRecipe.Serializer.class)
public class ShapelessRecipe_SerializerMixin {

	@ModifyConstant(method="method_53760", constant=@Constant(intValue=9))
	private static int biggerCraft_modConstMethod53760(int original) {
		return Integer.MAX_VALUE;
	}
	
	
}
