package com.iwhys.cnode.util;

import android.util.Log;

import com.iwhys.cnode.App;

/**
 * 作者：魔鬼 E-mail：iwhs@qq.com 创建时间：2014年5月21日 上午10:10:30 类说明：log输出类
 */

public class LogUtil {

    public enum LaunchMode {
        DEBUG, RELEASE
    }

    public static void e(Class<?> clazz, Object msg, Throwable tr) {
        if (App.LAUNCH_MODE == LaunchMode.DEBUG) {
            String tag = "";
            if (null != clazz) {
                tag = "类名：" + clazz.getSimpleName() + " ";
            }
            Log.e(App.TAG, tag + msg, tr);
        }
    }

    public static void e(Class<?> clazz, Object msg) {
        e(clazz, msg, null);
    }

    public static void d(Class<?> clazz, Object msg) {
        if (App.LAUNCH_MODE == LaunchMode.DEBUG) {
            String tag = "";
            if (null != clazz) {
                tag = "类名：" + clazz.getSimpleName() + " ";
            }
            Log.d(App.TAG, tag + msg);
        }
    }

    /**
     * System.out输出内容
     */
    public static void sysout(Class<?> clazz, Object msg){
        if (App.LAUNCH_MODE == LaunchMode.DEBUG){
            String tag = "";
            if (null != clazz) {
                tag = "类名：" + clazz.getSimpleName() + " ";
            }
            System.out.println(tag + msg);
        }
    }

    public static void sysout(Object msg){
        sysout(null, msg);
    }

    /**
     * 弹出提示
     */
    public final static void toast(Class<?> clazz, Object msg){
        if (App.LAUNCH_MODE == LaunchMode.DEBUG){
            String tag = "";
            if (null != clazz) {
                tag = "类名：" + clazz.getSimpleName() + " ";
            }
            CommonUtils.showToast(tag + msg);
        }
    }

    public static void toast(Object msg){
        toast(null, msg);
    }

}
