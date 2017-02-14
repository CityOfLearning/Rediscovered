package com.stormister.rediscovered;

import java.util.Random;

public class OldNoiseGenerator2 {

	private static int field_4296_d[][] = { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 },
			{ -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };

	private static final double field_4294_f = 0.5D * (Math.sqrt(3D) - 1.0D);

	private static final double field_4293_g = (3D - Math.sqrt(3D)) / 6D;

	private static double func_4156_a(int ai[], double d, double d1) {
		return (ai[0] * d) + (ai[1] * d1);
	}

	private static int wrap(double d) {
		return d <= 0.0D ? (int) d - 1 : (int) d;
	}

	private int field_4295_e[];
	public double field_4292_a;
	public double field_4291_b;
	public double field_4297_c;

	public OldNoiseGenerator2() {
		this(new Random());
	}

	public OldNoiseGenerator2(Random random) {
		field_4295_e = new int[512];
		field_4292_a = random.nextDouble() * 256D;
		field_4291_b = random.nextDouble() * 256D;
		field_4297_c = random.nextDouble() * 256D;
		for (int i = 0; i < 256; i++) {
			field_4295_e[i] = i;
		}

		for (int j = 0; j < 256; j++) {
			int k = random.nextInt(256 - j) + j;
			int l = field_4295_e[j];
			field_4295_e[j] = field_4295_e[k];
			field_4295_e[k] = l;
			field_4295_e[j + 256] = field_4295_e[j];
		}

	}

	public void func_4157_a(double ad[], double d, double d1, int i, int j, double d2, double d3, double d4) {
		int k = 0;
		for (int l = 0; l < i; l++) {
			double d5 = ((d + l) * d2) + field_4292_a;
			for (int i1 = 0; i1 < j; i1++) {
				double d6 = ((d1 + i1) * d3) + field_4291_b;
				double d10 = (d5 + d6) * field_4294_f;
				int j1 = wrap(d5 + d10);
				int k1 = wrap(d6 + d10);
				double d11 = (j1 + k1) * field_4293_g;
				double d12 = j1 - d11;
				double d13 = k1 - d11;
				double d14 = d5 - d12;
				double d15 = d6 - d13;
				int l1;
				int i2;
				if (d14 > d15) {
					l1 = 1;
					i2 = 0;
				} else {
					l1 = 0;
					i2 = 1;
				}
				double d16 = (d14 - l1) + field_4293_g;
				double d17 = (d15 - i2) + field_4293_g;
				double d18 = (d14 - 1.0D) + (2D * field_4293_g);
				double d19 = (d15 - 1.0D) + (2D * field_4293_g);
				int j2 = j1 & 0xff;
				int k2 = k1 & 0xff;
				int l2 = field_4295_e[j2 + field_4295_e[k2]] % 12;
				int i3 = field_4295_e[j2 + l1 + field_4295_e[k2 + i2]] % 12;
				int j3 = field_4295_e[j2 + 1 + field_4295_e[k2 + 1]] % 12;
				double d20 = 0.5D - (d14 * d14) - (d15 * d15);
				double d7;
				if (d20 < 0.0D) {
					d7 = 0.0D;
				} else {
					d20 *= d20;
					d7 = d20 * d20 * func_4156_a(field_4296_d[l2], d14, d15);
				}
				double d21 = 0.5D - (d16 * d16) - (d17 * d17);
				double d8;
				if (d21 < 0.0D) {
					d8 = 0.0D;
				} else {
					d21 *= d21;
					d8 = d21 * d21 * func_4156_a(field_4296_d[i3], d16, d17);
				}
				double d22 = 0.5D - (d18 * d18) - (d19 * d19);
				double d9;
				if (d22 < 0.0D) {
					d9 = 0.0D;
				} else {
					d22 *= d22;
					d9 = d22 * d22 * func_4156_a(field_4296_d[j3], d18, d19);
				}
				ad[k++] += 70D * (d7 + d8 + d9) * d4;
			}

		}

	}

}