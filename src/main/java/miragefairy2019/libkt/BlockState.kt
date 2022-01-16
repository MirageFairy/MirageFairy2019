package miragefairy2019.libkt

import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState

operator fun <T : Comparable<T>> IBlockState.get(property: IProperty<T>): T = getValue(property)
fun <T : Comparable<T>, V : T> IBlockState.with(property: IProperty<T>, value: V): IBlockState = withProperty(property, value)
