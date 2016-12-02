package com.hjs.daily2discount.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast输出类
 */
public class ToastUtil {

	public static void showShortToast(Context context, int res){
		Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();
	}
	
	public static void showShortToast(Context context, String str){
		Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context, int res){
		Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(Context context, String str){
		Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
	
}
