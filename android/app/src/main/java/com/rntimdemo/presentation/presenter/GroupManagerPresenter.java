package com.rntimdemo.presentation.presenter;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;

/**
 * 群管理逻辑
 */
public class GroupManagerPresenter {

    private static final String TAG = "GroupManagerPresenter";

    /**
     * 申请加入群
     *
     * @param groupId  群组ID
     * @param reason   申请理由
     * @param callBack 回调
     */
    public static void applyJoinGroup(String groupId, String reason, TIMCallBack callBack) {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, reason, callBack);
    }

    /**
     * 退出群
     *
     * @param groupId  群组ID
     * @param callBack 回调
     */
    public static void quitGroup(String groupId, TIMCallBack callBack) {
        TIMGroupManager.getInstance().quitGroup(groupId, callBack);
    }

}
