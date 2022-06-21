package miragefairy2019.libkt

import miragefairy2019.common.ResourceName
import net.minecraft.util.ResourceLocation

val ResourceName.resourceLocation get() = ResourceLocation(domain, path)
val ResourceLocation.resourceName get() = ResourceName(resourceDomain, resourcePath)
