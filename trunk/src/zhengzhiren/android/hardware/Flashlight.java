//package zhengzhiren.android.hardware;
//
//import android.hardware.Camera;
//import android.hardware.Camera.Parameters;
//
///**
// * 控制闪光灯
// * 使用时需添加android.permission.CAMERA等相关权限
// * 
// * @author ZZR
// * 
// */
//public class Flashlight {
//
//	Camera mCamera;
//
//	public void setOn(boolean on) {
//		if (on) {
//			if (mCamera == null) {
//				mCamera = Camera.open();
//			}
//			Parameters params = mCamera.getParameters();
//			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
//			mCamera.setParameters(params);
//		} else {
//			if (mCamera != null) {
//				Parameters params = mCamera.getParameters();
//				params.setFlashMode(Parameters.FLASH_MODE_OFF);
//				mCamera.setParameters(params);
//				mCamera.release();
//				mCamera = null;
//			}
//		}
//	}
// }
