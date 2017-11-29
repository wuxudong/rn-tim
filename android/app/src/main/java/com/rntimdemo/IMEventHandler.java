package com.rntimdemo;

/**
 * Created by wuxudong on 28/11/2017.
 */

interface IMEventHandler {
    void onJoinGroup(String name);
    void onQuitGroup(String name);
    void onGroupAdd(String name);
    void onGroupDelete(String name);
    void onMessage(String message);
}
