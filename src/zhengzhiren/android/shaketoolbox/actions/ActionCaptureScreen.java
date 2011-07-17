package zhengzhiren.android.shaketoolbox.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import zhengzhiren.android.hardware.Framebuffer;
import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.Bin;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * 操作——屏幕截图
 * 
 * @author ZZR
 * 
 */
public class ActionCaptureScreen extends Action {

	private static final String BIN_NAME = "fbchmod";
	private static final String PREF_CAPTURE_SCREEN = "capture_screen";
	private static final String PREF_PIC_COMPRESS_FORMAT = "pic_compress_format";

	public ActionCaptureScreen(Context context) {
		super(context);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_CAPTURE_SCREEN;
	}

	@Override
	public int getIcon() {
		return R.drawable.capture_screen;
	}

	@Override
	public int getName() {
		return R.string.action_capture_screen_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_capture_screen_desc;
	}

	@Override
	public void invokeAction() {
		String bin = getBinPath(mContext.getPackageName());
		// 执行命令，修改framebuffer权限使其可读
		String cmd = String.format("%s read", bin);
		Bin.runRootCommand(cmd);

		// 截图、保存
		try {
			Bitmap bmp = Framebuffer.captureScreen(mContext);
			try {
				String path = savePic(bmp);
				// Toast提示图片保存路径
				String toast = String.format(
						mContext.getResources().getString(
								R.string.toast_pic_saved_fmt), path);
				Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(mContext, R.string.toast_cant_write_sdcard,
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(mContext, R.string.toast_root_required,
					Toast.LENGTH_LONG).show();
		}

		// 执行命令，恢复framebuffer权限
		cmd = String.format("%s noread", bin);
		Bin.runRootCommand(cmd);
	}

	/**
	 * 从Assets中释放所需的可执行文件
	 */
	public static void releaseBin(Context context) {
		String dest = getBinPath(context.getPackageName());
		Bin.loadBinFromAssets(context, BIN_NAME, dest, true);
	}

	/**
	 * 可执行文件的存放路径
	 * 
	 * @param packageName
	 * @return
	 */
	private static String getBinPath(String packageName) {
		final String dest = String.format("/data/data/%s/%s", packageName,
				BIN_NAME);
		return dest;
	}

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @return保存图片的路径
	 * @throws Exception
	 */
	private String savePic(Bitmap bitmap) throws Exception {
		// 创建目录
		File dir = new File("/sdcard/ShakeToolbox");
		boolean dirExisting = (dir.exists() ? true : dir.mkdir());
		if (!dirExisting) {
			throw new RuntimeException();
		}

		// 图片保存格式
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		String fmt = sp.getString(PREF_PIC_COMPRESS_FORMAT, "PNG");
		CompressFormat format;
		int quality = 100;
		String picFileName;
		if (fmt == "PNG") {
			format = CompressFormat.PNG;
			picFileName = String.format("%s.png", System.currentTimeMillis());
		} else {
			format = CompressFormat.JPEG;
			picFileName = String.format("%s.jpg", System.currentTimeMillis());
		}

		File pic = new File(dir, picFileName);
		try {
			OutputStream oStream = new FileOutputStream(pic);
			bitmap.compress(format, quality, oStream);
			oStream.close();
			return pic.getAbsolutePath();
		} catch (Exception e) {
			throw e;
		}
	}

}
