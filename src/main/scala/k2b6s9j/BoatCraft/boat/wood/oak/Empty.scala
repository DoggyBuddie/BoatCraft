package k2b6s9j.BoatCraft.boat.wood.oak

import k2b6s9j.BoatCraft.boat.Boat.{ItemCustomBoat, RenderBoat, EntityCustomBoat}
import k2b6s9j.BoatCraft.boat.Materials
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraft.item.ItemStack
import net.minecraft.world.World

object Empty {

  class Entity(world: World) extends EntityCustomBoat(world) with Materials.Entity.Wood.Oak {

    override def useItemID(): Boolean = {
      true
    }

    override def customBoatItemID(): Int = {
      Item.shiftedID
    }

  }

  object Item {

    var ID: Int = 0
    var shiftedID: Int = 0

  }

  class Item(id: Int) extends ItemCustomBoat(id) {

    setUnlocalizedName("boat.wood.oak.empty")
    //func_111206_d("boatcraft:boat.wood.oak.empty")
    GameRegistry.registerItem(this, "Oak Wood Boat")
    Item.shiftedID = this.itemID
    OreDictionary.registerOre("boatOakWoodEmpty", new ItemStack(this))

    override def getEntity(world: World, x: Int, y: Int, z: Int): EntityCustomBoat = {
      new Entity(world)
    }

  }

  class Render extends RenderBoat with Materials.Render.Wood.Oak {

    override def getEntity(): EntityCustomBoat = {
      val entity: Entity =  null
      return entity
    }

  }

}