package electrodynamics.prefab.utilities.math;

import org.joml.Vector3f;

/**
 * Wrapper class that standardizes all color value operations into fields
 * 
 * @author skip999
 *
 */
public class Color {

	public static final Color WHITE = new Color(0xFFFFFFFF);
	public static final Color BLACK = new Color(0, 0, 0, 0);
	public static final Color TEXT_GRAY = new Color(64, 64, 64, 255);
	public static final Color JEI_TEXT_GRAY = new Color(128, 128, 128, 255);

	private final int r;
	private final int g;
	private final int b;
	private final int a;

	private final float rFloat;
	private final float gFloat;
	private final float bFloat;
	private final float aFloat;

	private final int color;

	private final int[] colorArr;
	private final float[] colorFloatArr;
	private final Vector3f floatVector;

	public Color(int r, int g, int b, int a) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;

		rFloat = this.r / 256.0F;
		gFloat = this.g / 256.0F;
		bFloat = this.b / 256.0F;
		aFloat = this.a / 256.0F;

		color = (this.a << 24) + (this.r << 16) + (this.g << 8) + this.b;

		colorArr = new int[] { this.r, this.g, this.b, this.a };
		colorFloatArr = new float[] { this.rFloat, this.gFloat, this.bFloat, this.aFloat };
		floatVector = new Vector3f(this.rFloat, this.gFloat, this.bFloat);

	}

	public Color(int argb) {
		this((argb >> 16 & 0xFF), (argb >> 8 & 0xFF), (argb & 0xFF), (argb >> 24 & 0xFF));
	}

	public Color(float r, float g, float b, float a) {
		this((int) (r * 256.0F), (int) (g * 256.0F), (int) (b * 256.0F), (int) (a * 256.0F));
	}

	public Color(int[] colorArr) {
		this(colorArr[0], colorArr[1], colorArr[2], colorArr[3]);
	}

	public Color(float[] colorFloatArr) {
		this(colorFloatArr[0], colorFloatArr[1], colorFloatArr[2], colorFloatArr[3]);
	}

	public int r() {
		return r;
	}

	public int g() {
		return g;
	}

	public int b() {
		return b;
	}

	public int a() {
		return a;
	}

	public float rFloat() {
		return rFloat;
	}

	public float gFloat() {
		return gFloat;
	}

	public float bFloat() {
		return bFloat;
	}

	public float aFloat() {
		return aFloat;
	}

	public int color() {
		return color;
	}

	public int[] colorArr() {
		return colorArr;
	}

	public float[] colorFloatArr() {
		return colorFloatArr;
	}

	public Vector3f getFloatVector() {
		return floatVector;
	}

	public Color multiply(Color other) {
		return new Color((this.r * other.r) / 255, (this.g * other.g) / 255, (this.b * other.b) / 255, (this.a * other.a) / 255);
	}

	public Color blend(Color other, double amtOther) {
		double amtThis = 1 - amtOther;
		return new Color((int) ((r * amtThis + other.r * amtOther)), (int) ((g * amtThis + other.g * amtOther)), (int) ((b * amtThis + other.b * amtOther)), (int) ((a * amtThis + other.a * amtOther)));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Color other) {
			return this.r == other.r && this.g == other.g && this.b == other.b && this.a == other.a;
		}
		return false;
	}

	@Override
	public String toString() {
		return "r: " + r + ", g: " + g + ", b: " + b + ", a: " + a;
	}

	public static Color fromABGR(int abgr){
		return new Color((abgr & 0xFF), (abgr >> 8 & 0xFF), (abgr >> 16 & 0xFF), (abgr >> 24 & 0xFF));
	}

}
