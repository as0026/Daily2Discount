package com.hjs.daily2discount.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @描述 SharedPreferences帮助类
 */
public class SharedPreferencesHelper {

	private static volatile SharedPreferencesHelper mInstance = null;
	private SharedPreferences sp = null;
	public static final String User_Location = "UserLocation";	//用户定位
	public static final String User_Bean = "user_bean";
	public static final String Status_Height = "StatusHeight";	//状态栏的高度
	public static final String App_firstOpen = "AppFirstOpen";	//是否为第一次打开应用
	public static final String Device_Identification_Code = "DeviceIdentificationCode";	//设备唯一标示

	private SharedPreferencesHelper(Context context){
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 获取全局的SharedPreferencesHelper单例实例
	 * @param context
	 * @return
	 */
	public static SharedPreferencesHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SharedPreferencesHelper.class) {
				if (mInstance == null) {
					mInstance = new SharedPreferencesHelper(context);
				}
			}
		}
		return mInstance;
	}

	public SharedPreferences getSp(){
		return sp;
	}

	/**
	 * @描述: 获取当前code对应的值
	 * @return String
	 */
	public String getValue(String code) {
		return sp.getString(code, "");
	}

	/**
	 * @描述: 保存当时使用的账号
	 * @return String
	 */
	public void putValue(String code, String value) {
		Editor editor = sp.edit();
		editor.putString(code, value);
		editor.commit();
	}
	
	/**
	 * @描述: 保存当前code对应的boolean值
	 * @return String
	 */
	public void putBooleanValue(String code, boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(code, value);
		editor.commit();
	}
	
	/**
	 * @描述: 获取当前code对应的boolean值
	 * @return String
	 */
	public boolean getBooleaValue(String code) {
		return sp.getBoolean(code, false);
	}
	
	/**
	 * @描述: 保存当前code对应的int值
	 * @return String
	 */
	public void putIntValue(String code, int value) {
		Editor editor = sp.edit();
		editor.putInt(code, value);
		editor.commit();
	}
	
	/**
	 * @描述: 获取当前code对应的int值
	 * @return String
	 */
	public int getIntValue(String code) {
		return sp.getInt(code, 0);
	}

	/**
	 * @描述: 用于多条数据添加
	 * @param code
	 * @param value
	 *            void
	 */
	public void putValues(String[] code, String[] value) {
		if (code == null || value == null || code.length != value.length) {
			throw new IllegalArgumentException("非法的参数!");
		}
		Editor editor = sp.edit();
		for (int i = 0; i < code.length; i++) {
			editor.putString(code[i], value[i]);
		}
		editor.commit();
	}

	/**
	 * @描述: 多条数据删除
	 * @param keys
	 *            void
	 */
	public void deleteByKeys(String[] keys) {
		if (keys == null || keys.length == 0) {
			throw new NullPointerException("keys不能为空!");
		}
		Editor editor = sp.edit();
		for (int i = 0; i < keys.length; i++) {
			editor.remove(keys[i]);
		}
		editor.commit();
	}

	/**
	 * 返回状态栏的高度
	 * @return
     */
	public int getStatusHeight(Activity mActivity){
		int statusHeight = getIntValue(SharedPreferencesHelper.Status_Height);
		if(statusHeight <= 0){
			statusHeight = StatusHeightHelper.getStatusHeight(mActivity);
			putIntValue(SharedPreferencesHelper.Status_Height,statusHeight);
		}
		return statusHeight;
	}

	/**
	 * 判断是否为第一次打开app
	 * false为是，true是不是
	 * @return
     */
	public boolean isFirstRun(){
		return getBooleaValue(SharedPreferencesHelper.App_firstOpen);
	}

	/**
	 * 获取设备的唯一标示
	 * @return
	 */
	public String getSoleNumber(Context context){
		String code = getValue(SharedPreferencesHelper.Device_Identification_Code);
		if(TextUtils.isEmpty(code)){
			code = PhoneStateHelper.getInstance(context).getPhoneIMEI();
			if(TextUtils.isEmpty(code)){
				code = UUID.randomUUID().toString().replaceAll("-","").substring(0,14);
			}
			putValue(SharedPreferencesHelper.Device_Identification_Code,code);
		}
		return code;
	}

}
