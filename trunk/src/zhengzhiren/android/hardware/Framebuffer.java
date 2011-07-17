package zhengzhiren.android.hardware;

import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * 通过native代码读取framebuffer实现屏幕截图
 * 
 * @author ZZR
 * 
 */
public class Framebuffer {

	private static final String LIB_NAME = "fbread";

	/***
	 * 获取framebuffer缓冲数据
	 * 
	 * @param buffer
	 */
	private static native void getFramebuffer(ByteBuffer buffer);

	/**
	 * 返回屏幕宽度
	 * 
	 * @return
	 */
	private static native int getWidth();

	/**
	 * 返回屏幕高度
	 * 
	 * @return
	 */
	private static native int getHeight();

	/**
	 * 每个像素的字节数
	 * 
	 * @return
	 */
	public static native int getBytesPerPixel();

	public static Bitmap captureScreen(Context context) throws Exception {
		final int bpp = getBytesPerPixel();
		final int width = getWidth();
		final int height = getHeight();

		if (bpp <= 0 || width <= 0 || height <= 0) {
			throw new RuntimeException();
		}

		ByteBuffer buf = ByteBuffer.allocateDirect(width * height * bpp);
		getFramebuffer(buf);
		Config bmpConfig;
		switch (bpp) {
		case 2:
			bmpConfig = Config.RGB_565;
			break;
		case 4:
			bmpConfig = Config.ARGB_8888;
			break;
		default:
			bmpConfig = Config.ARGB_8888;
			break;
		}
		Bitmap bmp = Bitmap.createBitmap(width, height, bmpConfig);
		bmp.copyPixelsFromBuffer(buf);

		return bmp;
	}

	static {
		System.loadLibrary(LIB_NAME);
	}
}
