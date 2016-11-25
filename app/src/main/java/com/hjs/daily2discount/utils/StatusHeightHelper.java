package com.hjs.daily2discount.utils;

import android.app.Activity;
import android.graphics.Rect;

/**
 * 获取状态栏的高度
 * Created by Administrator on 2016/7/4.
 */
public class StatusHeightHelper {
    /**
     * 获取手机状态栏的高度
     * @param mActivity
     * @return
     */
    public static int getStatusHeight(Activity mActivity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = mActivity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
