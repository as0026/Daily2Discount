package com.hjs.daily2discount.ui.activity;

import android.app.Application;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.utils.CrashHandler;
import com.hjs.daily2discount.utils.FileUtils;

/**
 * Created by Administrator on 2016/12/1.
 */
public class DiscountApplication extends Application {
    public static DiscountApplication ins = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if(this == null){
            onTerminate();
            return;
        }
        ins = this;
        initApplication();
    }

    private void initApplication() {
        initScreen();
        // 未捕获的异常处理
        if (!AppGlobal.ISDEBUG) {
            CrashHandler.getInstance().init(getApplicationContext());
        }
    }

    private void initScreen() {
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        AppGlobal.screenWidth = display.widthPixels;
        AppGlobal.screenHeight = display.heightPixels;
        AppGlobal.screenDensityDpi = display.densityDpi;
        AppGlobal.screenDensityDpiRadio = display.density;
        AppGlobal.scaledDensity = display.scaledDensity;
        AppGlobal.externalFileDir = FileUtils.getFilesPath(getApplicationContext(), Environment.DIRECTORY_PICTURES);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static DiscountApplication getIns(){
        return ins;
    }
}
