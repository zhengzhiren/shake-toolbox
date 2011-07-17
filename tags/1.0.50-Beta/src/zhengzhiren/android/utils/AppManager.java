package zhengzhiren.android.utils;

import java.io.File;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * 对应用程序和包进行管理 （注意Intent.FLAG_ACTIVITY_NEW_TASK）
 * 
 * @author ZZR
 */
public class AppManager {

	// 调用PackageInstaller所需的相关常量

	private static final String SCHEME = "package";
	private static final String MIMETYPE = "application/vnd.android.package-archive";

	// 调用InstalledAppDetails所需的相关常量（当ApiLevel<9时，调用非公开接口）

	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
	 */
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";

	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
	 */
	private static final String APP_PKG_NAME_22 = "pkg";

	/**
	 * InstalledAppDetails所在包名
	 */
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";

	/**
	 * InstalledAppDetails类名
	 */
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

	/**
	 * 通过调用系统的PackageInstaller程序，实现间接安装应用的功能
	 * 
	 * @param context
	 * @param filePath
	 *            apk文件的路径
	 */
	public static void install(Context context, String filePath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)), MIMETYPE);
		context.startActivity(intent);
	}

	/**
	 * 通过调用系统的PackageInstaller程序，实现间接卸载应用的功能
	 * 
	 * @param context
	 * 
	 * @param packageName
	 *            应用程序的包名
	 */
	public static void uninstall(Context context, String packageName) {
		Uri uri = Uri.fromParts(SCHEME, packageName, null);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
	 * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
	 * 
	 * @param context
	 * 
	 * @param packageName
	 *            应用程序的包名
	 */
	public static void showInstalledAppDetails(Context context,
			String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 返回当前最顶层的Activity，即用户正在浏览的Activity。 需要android.permission.GET_TASKS权限
	 * 
	 * @param context
	 * @return
	 */
	public static ComponentName getTopActivity(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		return am.getRunningTasks(1).get(0).topActivity;
	}
}
