package zhengzhiren.android.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

/**
 * 与通话相关的操作
 * 
 * @author ZZR
 * 
 */
public class Telephony {

	/**
	 * 接听电话。需要android.permission.MODIFY_PHONE_STATE权限
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void answerCall(Context context) throws Exception {
		ITelephony iTelephony = getITelephony(context);
		// iTelephony.silenceRinger();
		iTelephony.answerRingingCall();
	}

	/**
	 * 拒接电话、结束通话。需要android.permission.CALL_PHONE权限
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void endCall(Context context) throws Exception {
		getITelephony(context).endCall();
	}

	/**
	 * 通过反射调用TelephonyManager私有方法，返回ITelephony接口
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static ITelephony getITelephony(Context context) throws Exception {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = c.getDeclaredMethod("getITelephony");
		getITelephonyMethod.setAccessible(true);
		return (ITelephony) getITelephonyMethod.invoke(tm);
	}

}
