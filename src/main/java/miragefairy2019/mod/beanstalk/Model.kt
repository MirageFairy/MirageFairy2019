package miragefairy2019.mod.beanstalk

import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataUv


fun element(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        down = DataFace(texture = texture),
        up = DataFace(texture = texture),
        north = DataFace(texture = texture),
        south = DataFace(texture = texture),
        west = DataFace(texture = texture),
        east = DataFace(texture = texture)
    )
)

fun ud(from: DataPoint, to: DataPoint, downTexture: String, upTexture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        down = DataFace(texture = downTexture),
        up = DataFace(texture = upTexture)
    )
)


fun downDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        down = DataFace(uv = DataUv(from.x, 16.0 - to.z, to.x, 16.0 - from.z), texture = texture),
        up = DataFace(uv = DataUv(to.x, 16.0 - to.z, from.x, 16.0 - from.z), texture = texture)
    )
)

fun upDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        down = DataFace(uv = DataUv(to.x, 16.0 - to.z, from.x, 16.0 - from.z), texture = texture),
        up = DataFace(uv = DataUv(from.x, 16.0 - to.z, to.x, 16.0 - from.z), texture = texture)
    )
)

fun northDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        north = DataFace(uv = DataUv(from.x, 16.0 - to.y, to.x, 16.0 - from.y), texture = texture),
        south = DataFace(uv = DataUv(to.x, 16.0 - to.y, from.x, 16.0 - from.y), texture = texture)
    )
)

fun southDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        north = DataFace(uv = DataUv(to.x, 16.0 - to.y, from.x, 16.0 - from.y), texture = texture),
        south = DataFace(uv = DataUv(from.x, 16.0 - to.y, to.x, 16.0 - from.y), texture = texture)
    )
)

fun westDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        west = DataFace(uv = DataUv(from.z, 16.0 - to.y, to.z, 16.0 - from.y), texture = texture),
        east = DataFace(uv = DataUv(to.z, 16.0 - to.y, from.z, 16.0 - from.y), texture = texture)
    )
)

fun eastDuplex(from: DataPoint, to: DataPoint, texture: String) = DataElement(
    from = from,
    to = to,
    faces = DataFaces(
        west = DataFace(uv = DataUv(to.z, 16.0 - to.y, from.z, 16.0 - from.y), texture = texture),
        east = DataFace(uv = DataUv(from.z, 16.0 - to.y, to.z, 16.0 - from.y), texture = texture)
    )
)
