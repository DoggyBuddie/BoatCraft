package boatcraft.compatibility

import boatcraft.api.modifiers.Block
import boatcraft.api.modifiers.Material
import boatcraft.compatibility.industrialcraft2.IC2GuiHandler
import boatcraft.compatibility.industrialcraft2.modifiers.blocks.Generator
import boatcraft.compatibility.industrialcraft2.modifiers.blocks.Nuke
import boatcraft.compatibility.industrialcraft2.modifiers.materials.Carbon
import boatcraft.compatibility.industrialcraft2.modifiers.materials.Rubber
import boatcraft.core.GUIHandler
import boatcraft.core.utilities.Recipes
import cpw.mods.fml.common.Optional
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import ic2.api.item.IC2Items

object IC2 extends CompatModule {
	override protected def doPreInit(e: FMLPreInitializationEvent) {
		GUIHandler.handlerMap.put(code, IC2GuiHandler)
	}
	
	override protected def doPostInit(e: FMLPostInitializationEvent) {
		try {
			Recipes.removeRecipe(IC2Items getItem "boatRubber")
			Recipes.removeRecipe(IC2Items getItem "boatCarbon")
		}
		catch {
			case ex: NoClassDefFoundError => //That's OK
			case err: NoSuchMethodError => //Fine
			case ex: NoSuchMethodException => //No problem
			case ex: NullPointerException => //Sure
			case thr: Throwable => thr printStackTrace() //Weird...
		}
	}

	@Optional.Method(modid = "IC2")
	override protected def getMaterials: List[Material] = List[Material](
		Rubber,
		Carbon
	)

	@Optional.Method(modid = "IC2")
	override protected def getBlocks: List[Block] = List[Block](
		Generator,
		Nuke
	)
}