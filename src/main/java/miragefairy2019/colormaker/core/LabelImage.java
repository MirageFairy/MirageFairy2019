package miragefairy2019.colormaker.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mirrg.boron.util.struct.ImmutableArray;

public class LabelImage extends JLabel
{

	public ColorConstants colorConstants = new ColorConstants();
	public Color backgroundColor = Color.black;

	public void setImage(ImmutableArray<ImageLayer> arrayList)
	{
		setIcon(new ImageIcon(createImage(arrayList)));
	}

	private BufferedImage createImage(ImmutableArray<ImageLayer> imageLayers)
	{
		BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {

				// ラベルの背景色で初期化
				int r1 = backgroundColor.getRed();
				int g1 = backgroundColor.getGreen();
				int b1 = backgroundColor.getBlue();

				for (ImageLayer imageLayer : imageLayers) {

					// 乗算する色
					Color colorMul = colorConstants.getColor(imageLayer.colorIdentifier);

					// 画像の色
					int argbOver = imageLayer.image.getRGB(x / 4, y / 4);
					int a2 = (argbOver >> 24) & 0xff;
					int r2 = (argbOver >> 16) & 0xff;
					int g2 = (argbOver >> 8) & 0xff;
					int b2 = (argbOver >> 0) & 0xff;

					// 画像の色を乗算する色で更新
					r2 = r2 * colorMul.getRed() / 255;
					g2 = g2 * colorMul.getGreen() / 255;
					b2 = b2 * colorMul.getBlue() / 255;

					// 現在の色を更新
					r1 = (r1 * (255 - a2) + r2 * a2) / 255;
					g1 = (g1 * (255 - a2) + g2 * a2) / 255;
					b1 = (b1 * (255 - a2) + b2 * a2) / 255;

				}

				// 色セット
				image.setRGB(x, y, ((r1 & 0xff) << 16) | ((g1 & 0xff) << 8) | ((b1 & 0xff) << 0));

			}
		}

		return image;
	}

}
