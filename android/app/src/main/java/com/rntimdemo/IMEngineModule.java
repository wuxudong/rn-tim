package com.rntimdemo;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntimdemo.presentation.business.InitBusiness;
import com.rntimdemo.presentation.business.LoginBusiness;
import com.rntimdemo.presentation.event.GroupEvent;
import com.rntimdemo.presentation.event.MessageEvent;
import com.rntimdemo.presentation.presenter.GroupManagerPresenter;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;

import java.util.Observable;
import java.util.Observer;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class IMEngineModule extends ReactContextBaseJavaModule {

    private static final String TYPE = "type";
    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String ROOMID = "roomId";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getName() {
        return "RCTIMEngine";
    }

    private IMEventHandler eventHandler = new IMEventHandler() {
        @Override
        public void onJoinGroup(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap map = Arguments.createMap();
                    map.putString(TYPE, "onJoinGroup");
                    map.putString(MSG, name);
                    commonEvent(map);
                }
            });
        }

        @Override
        public void onQuitGroup(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap map = Arguments.createMap();
                    map.putString(TYPE, "onQuitGroup");
                    map.putString(MSG, name);
                    commonEvent(map);
                }
            });
        }

        @Override
        public void onGroupAdd(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap map = Arguments.createMap();
                    map.putString(TYPE, "onGroupAdd");
                    map.putString(MSG, name);
                    commonEvent(map);
                }
            });
        }

        @Override
        public void onGroupDelete(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap map = Arguments.createMap();
                    map.putString(TYPE, "onGroupDelete");
                    map.putString(MSG, name);
                    commonEvent(map);
                }
            });
        }

        @Override
        public void onMessage(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap map = Arguments.createMap();
                    map.putString(TYPE, "onMessage");
                    map.putString(MSG, message);
                    commonEvent(map);
                }
            });
        }

    };


    public IMEngineModule(ReactApplicationContext reactContext) {
        super(reactContext);

    }

    @ReactMethod
    public void init(final ReadableMap options, final Promise promise) {
        InitBusiness.start(IMEngineModule.this.getReactApplicationContext(), options.getInt("appid"), options.getInt("accountType"));
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig = MessageEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);

        GroupEvent.getInstance().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
                if (cmd.type == GroupEvent.NotifyType.MEMBER_JOIN) {
                    eventHandler.onJoinGroup(cmd.toString());
                } else if (cmd.type == GroupEvent.NotifyType.MEMBER_QUIT) {
                    eventHandler.onJoinGroup(cmd.toString());
                } else if (cmd.type == GroupEvent.NotifyType.ADD) {
                    eventHandler.onGroupAdd(cmd.toString());
                } else if (cmd.type == GroupEvent.NotifyType.DEL) {
                    eventHandler.onGroupDelete(cmd.toString());
                }

            }
        });

        MessageEvent.getInstance().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                eventHandler.onMessage(data.toString());
            }
        });

        promise.resolve("success");
    }


    @ReactMethod
    public void login(
            String identify, String userSig,
            final Promise promise) {

        LoginBusiness.loginIm(identify, userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                promise.reject(String.valueOf(i), s);
            }

            @Override
            public void onSuccess() {
                promise.resolve("success");
            }
        });
    }

    @ReactMethod
    public void joinGroup(
            String groupId,
            final Promise promise) {

        GroupManagerPresenter.applyJoinGroup(groupId, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                promise.reject(String.valueOf(i), s);
            }

            @Override
            public void onSuccess() {
                promise.resolve(null);
            }
        });
    }

    @ReactMethod
    public void quitGroup(
            String groupId,
            final Promise promise) {

        GroupManagerPresenter.quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                promise.reject(String.valueOf(i), s);
            }

            @Override
            public void onSuccess() {
                promise.resolve(null);
            }
        });
    }


    private void commonEvent(WritableMap map) {
        sendEvent(getReactApplicationContext(), "im", map);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }


}