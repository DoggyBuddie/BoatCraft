package boatcraft.api.boat

import java.util

import scala.collection.JavaConversions.mapAsScalaMap

import boatcraft.api.Registry
import boatcraft.core.modifiers.blocks.Empty
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.resources.I18n
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemBoat
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.world.World

/**
 * The Item Class used for all items that can be deployed like a Boat.
 * Extends ItemBoat from Vanilla Minecraft.
 */
class ItemCustomBoat extends ItemBoat {
	hasSubtypes = true

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) {
		val stack = new ItemStack(item)
		stack.stackTagCompound = new NBTTagCompound
		for ((nameMat, material) <- Registry.materials) {
			stack.stackTagCompound setString("material", nameMat)
			for ((nameBlock, block) <- Registry.blocks) {
				stack.stackTagCompound setString("block", nameBlock)
				list.asInstanceOf[util.List[ItemStack]] add stack.copy
			}
		}
	}

	override def getUnlocalizedName(stack: ItemStack) =
		"boat." +
			Registry.getMaterial(stack) +
			"." +
			Registry.getBlock(stack)

	override def getItemStackDisplayName(stack: ItemStack): String = stack match {
		case x if Registry.getBlock(stack).==(Empty) =>
			I18n.format(Registry.getMaterial(stack).getLocalizedName, Array()) + " " + I18n.format("core.forms.dinghy.name", Array())
		case x if Registry.getBlock(stack).!=(null) =>
		I18n.format(Registry.getMaterial(stack).getLocalizedName, Array()) + " " + I18n.format("core.forms.dinghy.name", Array()) +	" " + I18n.format("core.module.linkingword", Array()) + " " + I18n.format(Registry.getBlock(stack).getLocalizedName, Array())
		case _ =>
		I18n.format("core.forms.dinghy.name", Array())
	}

	override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
		val f: Float = 1.0F
		val f1: Float = player.prevRotationPitch +
			(player.rotationPitch - player.prevRotationPitch) * f
		val f2: Float = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f
		val d0: Double = player.prevPosX + (player.posX - player.prevPosX) * f.asInstanceOf[Double]
		val d1: Double = player.prevPosY + (player.posY - player.prevPosY) * f + 1.62D - player.yOffset
		val d2: Double = player.prevPosZ + (player.posZ - player.prevPosZ) * f.asInstanceOf[Double]
		val vec3: Vec3 = world.getWorldVec3Pool.getVecFromPool(d0, d1, d2)
		val f3: Float = MathHelper.cos(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f4: Float = MathHelper.sin(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f5: Float = -MathHelper.cos(-f1 * 0.017453292F)
		val f6: Float = MathHelper.sin(-f1 * 0.017453292F)
		val f7: Float = f4 * f5
		val f8: Float = f3 * f5
		val d3: Double = 5.0D
		val vec31: Vec3 = vec3.addVector(f7.toDouble * d3,
			f6.toDouble * d3,
			f8.toDouble * d3)
		val movingobjectposition: MovingObjectPosition =
			world rayTraceBlocks(vec3, vec31, true)
		if (movingobjectposition == null)
			stack
		else {
			val vec32: Vec3 = player getLook f
			var flag: Boolean = false
			val f9: Float = 1.0F
			val list: util.List[_] = world.getEntitiesWithinAABBExcludingEntity(player,
				player.boundingBox addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3)
					expand(f9.asInstanceOf[Double], f9.asInstanceOf[Double], f9.asInstanceOf[Double]))
			var i: Int = 0
			for (i <- 0 until list.size()) {
				val entity: Entity = list.get(i).asInstanceOf[Entity]

				if (entity.canBeCollidedWith) {
					val f10: Float = entity.getCollisionBorderSize
					val axisalignedbb: AxisAlignedBB = entity.boundingBox.
						expand(f10 toDouble, f10 toDouble, f10 toDouble)

					if (axisalignedbb isVecInside vec3)
						flag = true
				}
			}

			if (flag) stack
			else {
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					i = movingobjectposition.blockX
					var j = movingobjectposition.blockY
					val k = movingobjectposition.blockZ

					if (world.getBlock(i, j, k) == Blocks.snow_layer)
						j = j - 1

					var boat: EntityCustomBoat = null
					val material = Registry getMaterial stack
					val block = Registry getBlock stack

					boat = EntityCustomBoat(world, i + 0.5, j + 1.0, k + 0.5)
					boat setMaterial (material toString)

					boat setBlock (block toString)
					boat.rotationYaw =
						((MathHelper.floor_double(player.rotationYaw * 4.0 / 360.0 + 0.5) & 3) - 1) * 90

					if (!world.getCollidingBoundingBoxes(boat,
						boat.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty)
						return stack

					if (!world.isRemote)
						world spawnEntityInWorld boat

					if (!player.capabilities.isCreativeMode)
						stack.stackSize = stack.stackSize - 1
				}
				stack
			}
		}
	}
}

object ItemCustomBoat extends ItemCustomBoat {}
