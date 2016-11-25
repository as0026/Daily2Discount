package com.hjs.daily2discount.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @描述 TelephonyManager管理类
 * @作者 郝炯淞
 * @创建时间 2013 上午10:37:21
 */
public class PhoneStateHelper {

	private static volatile PhoneStateHelper mInstance = null;
	private TelephonyManager pManager = null;

	private PhoneStateHelper(Context context){
		pManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static PhoneStateHelper getInstance(Context context) {
		// 创建电话管理
		if (mInstance == null) {
			synchronized(PhoneStateHelper.class) {
				if (mInstance == null) {
					mInstance = new PhoneStateHelper(context);
				}
			}
		}
		return mInstance;
	}
	/**
	 * 获取手机号码
	 * @return
	 */
	public String getPhoneNumber(){
		return pManager.getLine1Number();
	}
	/**
	 * 获取手机的IMEI
	 * 需要android.permission.READ_PHONE_STATE权限
	 * @return
	 */
	public String getPhoneIMEI(){
		return pManager.getDeviceId();
	}
	/**
	 * 判断是否为中国移动
	 * @return
	 */
	public boolean isChinaMobile(){
		if(pManager.getNetworkOperator().trim().equals("46000")){
			return true;
		}else{
			return false;
		}
	}
}
