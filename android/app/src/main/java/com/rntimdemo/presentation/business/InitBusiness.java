package com.rntimdemo.presentation.business;

import android.content.Context;
import android.util.Log;

import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusiness {

    private static final String TAG = InitBusiness.class.getSimpleName();

    private InitBusiness() {
    }

    public static void start(Context context, int appId, int accountType) {
        initImsdk(context, appId, accountType, 0);
    }

    public static void start(Context context, int appId, int accountType, int logLevel) {
        initImsdk(context, appId, accountType, logLevel);
    }


    /**
     * 初始化imsdk
     */
    private static void initImsdk(Context context, int appId, int accountType, int logLevel) {
        TIMSdkConfig config = new TIMSdkConfig(appId);
        config.enableLogPrint(true)
                .setLogLevel(TIMLogLevel.values()[logLevel]);

        //初始化imsdk
        TIMManager.getInstance().init(context, config);
        Log.i("IMDEMO", TIMManager.getInstance().getVersion());
        //禁止服务器自动代替上报已读
        Log.d(TAG, "initIMsdk");

    }


}
