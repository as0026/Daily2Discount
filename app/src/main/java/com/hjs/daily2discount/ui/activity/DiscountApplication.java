package com.hjs.daily2discount.ui.activity;

import android.app.Application;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.utils.CrashHandler;
import com.hjs.daily2discount.utils.FileUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

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
        initBmob();
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

    private void initBmob(){
        //自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId("02fe5fbc7c638a137c2dc757bd37198b")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(40)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
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
