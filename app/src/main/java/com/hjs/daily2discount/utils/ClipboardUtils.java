package com.hjs.daily2discount.utils;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;

/**
 * android实现文本复制到剪切板功能
 * Created by Administrator on 2016/12/2.
 */
public class ClipboardUtils {

    /**
     * 将android中的文本复制到剪切板
     * API 11之前： android.text.ClipboardManager,API 11之后： android.content.ClipboardManager
     * @param context
     * @param copyContent
     */
    public static void copy(Context context, String copyContent){
        if(CommonUtils.isEmpty(copyContent)){
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            android.text.ClipboardManager cm = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(copyContent);
        }else{
            android.content.ClipboardManager cm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, copyContent));
        }
        ToastUtil.showShortToast(context,"复制成功");
    }

    /**
     * 实现粘贴功能
     * @param context
     * @return
     */
    public static String paste(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            android.text.ClipboardManager cm = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if(cm.hasText()){
                return cm.getText().toString().trim();
            }
        }else{
            android.content.ClipboardManager cm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.hasPrimaryClip()){
                cm.getPrimaryClip().getItemAt(0).getText();
            }
        }
        return null;
    }
}
