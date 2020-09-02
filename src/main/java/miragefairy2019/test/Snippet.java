package miragefairy2019.test;

import mirrg.boron.util.UtilsString;

public class Snippet
{

	public static void main2(String[] args)
	{
		int countAll = 0;
		int[] count = new int[101];

		for (int j = 0; j < 1000000; j++) {
			double a = Math.random();
			double b = Math.random();
			for (int i = 0; i <= 100; i++) {
				double rate = i * 0.01;
				double x = a * (1 - rate) + b * rate;
				double y = k(rate, x);

				countAll++;
				count[(int) (y * 100)]++;

				/*
				System.out.println(String.format("%d %5.2f %5.2f %s",
					i,
					x,
					y,
					UtilsString.repeat('|', (int) (y * 100))));
					*/
			}
		}

		for (int i = 0; i <= 100; i++) {
			double a = count[i] / (double) countAll;
			System.out.println(String.format("%3d %7.5f %s",
				i,
				a * 100,
				UtilsString.repeat('|', (int) (100 * a * 100))));
		}

	}

	public static void main(String[] args)
	{
		int countAll = 0;
		int[] count = new int[101];

		for (int j = 0; j < 10000000; j++) {
			double a1 = Math.random();
			double a2 = Math.random();
			double a3 = Math.random();

			double y = Math.pow(a1, 1 / 3.0) * Math.pow(a2, 1 / 3.0) * Math.pow(a3, 1 / 3.0);

			countAll++;
			count[(int) (y * 100)]++;

		}

		for (int i = 0; i <= 100; i++) {
			double a = count[i] / (double) countAll;
			System.out.println(String.format("%3d %7.5f %s",
				i,
				a * 100,
				UtilsString.repeat('|', (int) (100 * a * 100))));
		}

	}

	public static void main_(String[] args)
	{
		int countAll = 0;
		int[] count = new int[101];

		for (int j = 0; j < 10; j++) {
			double a11 = Math.random();
			double a12 = Math.random();
			double a21 = Math.random();
			double a22 = Math.random();

			for (int i1 = 0; i1 <= 100; i1++) {
				for (int i2 = 0; i2 <= 100; i2++) {
					double rateX = i1 * 0.01;
					double rateY = i2 * 0.01;
					double x1 = k(rateX, a11 * (1 - rateX) + a12 * rateX);
					double x2 = k(rateX, a21 * (1 - rateX) + a22 * rateX);
					double y = k(rateY, x1 * (1 - rateY) + x2 * rateY);

					countAll++;
					count[(int) (y * 100)]++;

					/*
					System.out.println(String.format("%d %5.2f %5.2f %s",
						i,
						x,
						y,
						UtilsString.repeat('|', (int) (y * 100))));
						*/
				}
			}

		}

		for (int i = 0; i <= 100; i++) {
			double a = count[i] / (double) countAll;
			System.out.println(String.format("%3d %7.5f %s",
				i,
				a * 100,
				UtilsString.repeat('|', (int) (100 * a * 100))));
		}

	}

	private static double k(double rate, double x)
	{
		if (rate >= 0.5) rate = 1 - rate;
		return x < 0.5 ? k2(rate, x) : 1 - k2(rate, 1 - x);
	}

	private static double k2(double rate, double x)
	{
		return x < rate
			? (1 / (2 - 2 * rate)) * (x * x / rate)
			: (1 / (2 - 2 * rate)) * (2 * x - rate);
	}

}
