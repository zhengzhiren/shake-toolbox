package zhengzhiren.android.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;

/**
 * 运行命令相关的操作
 * 
 * @author ZZR
 * 
 */
public class Bin {

	final static int FILE_BUF_SIZE = 2048;
	final static int OUTPUT_BUF_SIZE = 4096;

	/**
	 * 运行Linux命令
	 * 
	 * @param command
	 *            Linux命令
	 * @return 命令输出
	 */
	public static String exec(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			int read;
			char[] buffer = new char[OUTPUT_BUF_SIZE];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();
			process.waitFor();
			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 在root过的手机上执行root权限命令
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static String runRootCommand(String command) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream stdin = new DataOutputStream(
					process.getOutputStream());
			DataInputStream stdout = new DataInputStream(
					process.getInputStream());

			stdin.writeBytes(command + "\n");
			stdin.flush();

			stdin.writeBytes("exit\n");
			stdin.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stdout));
			int read;
			char[] buffer = new char[OUTPUT_BUF_SIZE];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();
			process.waitFor();
			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将assets中的可执行文件拷贝到destFile，并用chmod赋予可执行权限
	 * 
	 * @param context
	 * @param assetFile
	 *            可执行文件在assets中的名称
	 * @param destFile
	 *            要拷贝到的目标文件
	 * @param overwriteExisting
	 *            覆盖现有文件
	 */
	public static void loadBinFromAssets(Context context, String assetFile,
			String destFile, boolean overwriteExisting) {
		File file = new File(destFile);
		boolean existing = file.exists();
		if (!existing || overwriteExisting) {
			try {
				InputStream iStream = context.getAssets().open(assetFile);
				OutputStream oStream = new FileOutputStream(file);
				int read;
				byte[] buf = new byte[FILE_BUF_SIZE];
				while ((read = iStream.read(buf)) > 0) {
					oStream.write(buf, 0, read);
				}
				oStream.close();
				iStream.close();

				// 赋予可执行权限
				String cmd = String.format("chmod 755 %s", destFile);
				exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
