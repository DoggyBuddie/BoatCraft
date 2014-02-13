package k2b6s9j.boatcraft.core.asm

import java.util.Map
import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import k2b6s9j.boatcraft.core.BoatCraft

class BoatCraftFMLLoadingPlugin extends IFMLLoadingPlugin
{
	override def getASMTransformerClass =
		Array(classOf[BoatCraftClassTransformer] getName)
	
	override def getModContainerClass = BoatCraft.getClass getName
	
	override def getSetupClass: String = null
	
	override def injectData(data: Map[String, AnyRef]) {}

    override def getAccessTransformerClass: String = null
}