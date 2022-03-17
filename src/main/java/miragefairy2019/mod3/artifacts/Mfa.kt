package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ja
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreIngredient

class MfaPage(val mainIngredient: List<ItemStack>, val subIngredients: List<List<ItemStack>>, val unlocalizedContent: String)

object Mfa {
    val mfaPages = mutableListOf<MfaPage>()
    val module = module {
        fun register(key: String, getMainIngredient: () -> List<ItemStack>, vararg subIngredientsGetters: () -> List<ItemStack>, getJapaneseContent: () -> String) {
            onMakeLang {
                ja("miragefairy2019.mfa.$key", getJapaneseContent().trimIndent().replace("\n", "\\n"))
            }
            onAddRecipe {
                mfaPages += MfaPage(getMainIngredient(), subIngredientsGetters.map { it() }, "miragefairy2019.mfa.$key")
            }
        }

        fun ore(oreName: String): () -> List<ItemStack> = { OreIngredient(oreName).matchingStacks.toList() }
        fun item(registerName: String): () -> List<ItemStack> = { listOf(Item.getByNameOrId("${ModMirageFairy2019.MODID}:$registerName")!!.createItemStack()) }
        fun fairy(type: String) = ore("mirageFairy2019Fairy${type.toUpperCamelCase()}Rank1")

        register("00000461", fairy("zombie")) {
            """
            【MFA-00000461：おかず】
            ｳ˝ｪｱｰｰ「来たぞ……」
            ｹﾞｪｪｪｪｪ「任せろ、支度はできている」
            
            ﾑﾞｩｩｩｩｩ「――待て、様子が違う」
            ｳ˝ｪｱｰｰ「馬鹿な」
            ｳ˝ｪｱｰｰ「なぜ人間が刀を持っている？」
            ﾑﾞｩｩｩｩｩ「ｹﾞｪｪｪｪｪ、戻ってこい！」
            
            ｹﾞｪｪｪｪｪ「ｧﾞｴｴ！」
            """
        }
        register("00169455", fairy("enderman")) {
            """
            【MFA-00169455：つかまえた】
            人間「キミってどこから来たの？」
            「えっと……」
            
            人間「なんてお名前？」
            「あの、私、名前持ってなくて…」
            人間「ｼｪ…ﾕﾌﾄ…ｿﾙ…言いづらいね…」
            人間「黒いし、ｵﾌﾞｼﾃﾞｨｱﾝって呼ぼう！」
            人間「それでもいいかな？」
            （^_^）
            """
        }
        register("16487544", item("miragium_axe"), fairy("air")) {
            """
            【MFA-16487544：落とし物】
            （……）
            
            ﾙﾒﾘ「待って！」
            
            ﾙﾒﾘ「妖精さん！！」
            ﾙﾒﾘ「待って！」
            （……）
            
            ﾙﾒﾘ「あっ！」
            """
        }
        register("30948551", fairy("pufferfish"), fairy("salmon")) {
            """
            【MFA-30948551：栄養満点】
            「甘いものが恋しくなりました！」
            「ケーキ屋さんが食べたいです！」
            ｻﾙﾓｰﾆｬ「帰りに寄ろっか」
            「ありがとうございます！」
            ―――――――
            ｻﾙﾓｰﾆｬ「着いたよ、何にしよっか？」
            
            「すぅぅぅぅぅぅっっっっ！！！」
            ｻﾙﾓｰﾆｬ「本当に店ごと食べるんだ」
            """
        }
        register("34681526", fairy("magenta_glazed_terracotta"), fairy("carrot"), fairy("dispenser"), fairy("lilac")) {
            """
            【MFA-34681526：美術展にて】
            ﾂｧｯﾛｰﾁｬ「大変！ﾃﾞｨｽﾍﾟﾝｾｰﾘｬが！」
            
            ﾃﾞｨｽﾍﾟﾝｾｰﾘｬ（……）
            
            ﾘﾗｰﾂｧ「止まって！こっちじゃないの！」
            
            ﾃﾞｨｽﾍﾟﾝｾｰﾘｬ（……）
            
            （？）
            """
        }
        register("34749950", fairy("melon"), fairy("cinnabar")) {
            """
            【MFA-34749950：走れ】
            ﾂｨﾅﾊﾞｰﾘｬ「今度劇をするんだけど――」
            ﾂｨﾅﾊﾞｰﾘｬ「主役のﾒﾛｽやってみない！？」
            「ﾒﾛｰﾆｬでいいの…？できるかな…」
            ﾂｨﾅﾊﾞｰﾘｬ「私、王様だから安心して！」
            「ありがとう……やってみる…！」
            ―― 本番当日 ――
            ﾂｨﾅﾊﾞｰﾘｬ「ﾒﾛｰﾆｬ、真っ裸じゃないか」
            ﾂｨﾅﾊﾞｰﾘｬ「早くそのﾏﾝﾄを着るがいい」
            「あうぅ…」
            """
        }
        register("34996052", fairy("chicken")) {
            """
            【MFA-34996052：妖精が先か？】
            N-183207「お夕飯何にしようかしら」
            （ﾄﾞｷﾄﾞｷ）
            N-183211「私、ステーキが食べたい！」
            （ﾎｯ…）
            N-183213「ふわふわしたのがいー！」
            （……）
            N-183211「私もあれまた食べたい！」
            N-183207「オムレツにしよっか☆」
            （Σ）
            """
        }
        register("43685165", fairy("bread"), fairy("axe"), fairy("poisonous_potato"), fairy("fire")) {
            """
            【MFA-43685165：闇鍋ﾊﾟｰﾃｨｰ】
            「今日は揚げ物だよ！召し上がれ！」
            『いただきまーす！』ｸﾞﾂｸﾞﾂ…
            
            ｱｰｼｬ「なんて旨いﾋﾞｽｹｯﾄなんだ」ｻｸｻｸ
            ﾎﾟｲｿﾉｳｾﾎﾟﾀｰﾁｬ「それﾌﾞﾚｱｰｼﾞｬの髪！」
            ﾌｨｰﾘｬ「へー！！！！！！！！……」
            
            「ﾌｨｰﾘｬ？お鍋持ってどうするの？」
            「こ、こっち見ないで…？」
            """
        }
        register("46805554", item("dish"), fairy("cake")) {
            """
            【MFA-46805554：クリームまみれ】
            N-111615「そこに乗っちゃだめだよ！」
            （…？）
            
            （…………）
            N-111615「もう！」
            
            N-111615「……」
            
            N-111615「……そこが好きなの？」
            """
        }
        register("64897357", ore("mirageFairy2019TwinkleStone")) {
            """
            【MFA-64897357：蛍】
            ｿﾃﾞｨｱ「ほら」
            
            「綺麗だな…」
            「虫みたいだ」
            
            ｿﾃﾞｨｱ「……ふふ、意外」
            「…えっ？」
            
            ｿﾃﾞｨｱ「星みたいって言うかなって」
            """
        }
    }
}
