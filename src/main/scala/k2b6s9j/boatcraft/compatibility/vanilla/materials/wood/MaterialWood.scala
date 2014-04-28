package k2b6s9j.boatcraft.compatibility.vanilla.materials.wood

import k2b6s9j.boatcraft.api.traits.Material
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class MaterialWood(meta: Int, name: String) extends Material
{
	override def getTexture =
		new ResourceLocation("boatcraft",
			"textures/entity/boat/vanilla/wood/" +
			name.toLowerCase.replace(' ', '_') + ".png")
	
	override def getName = name
	
	override def getItem = new ItemStack(Blocks.planks, 1, meta)
	
	override def getStick = new ItemStack(Items.stick)
}