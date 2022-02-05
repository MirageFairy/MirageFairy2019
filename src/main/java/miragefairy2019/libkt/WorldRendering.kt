package miragefairy2019.libkt

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.util.math.Vec3d
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** ブロックの上側に情報をポップアップ表示します。 */
fun drawBlockNameplateMultiLine(fontRenderer: FontRenderer, string: String, pos: Vec3d, yaw: Float, pitch: Float) = drawNameplateMultiLine(
    fontRenderer,
    string,
    pos.x + 0.5 + 0.6 * sin(yaw.toDouble() * PI / 180.0),
    pos.y + 1.25,
    pos.z + 0.5 + 0.6 * -cos(yaw.toDouble() * PI / 180.0),
    yaw,
    pitch
) - 1.25

/** @return 次にレンダリングを開始するY座標 */
fun drawNameplateMultiLine(fontRenderer: FontRenderer, string: String, x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Double {
    val strings = string.chunked(40)
    strings.forEachIndexed { i, subString ->
        EntityRenderer.drawNameplate(
            fontRenderer,
            subString,
            x.toFloat(),
            y.toFloat() + (strings.size - i - 1).toFloat() * 0.25f,
            z.toFloat(),
            0, // 追加Yポジション
            yaw,
            pitch,
            false, // 2人称視点の場合はフリップする
            false // 対象はスニーク中ではない
        )
    }
    return y + strings.size.toFloat() * 0.25f
}
