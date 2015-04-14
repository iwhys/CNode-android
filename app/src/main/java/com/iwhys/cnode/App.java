package com.iwhys.cnode;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.LogUtil;
import com.iwhys.cnode.util.constant.Params;
import com.umeng.analytics.MobclickAgent;


/**
 * 全局程序类
 * Created by devil on 14-7-24.
 */
public class App extends Application {

    /**
     * 程序运行模式（调试、发布）
     */
    public final static LogUtil.LaunchMode LAUNCH_MODE = LogUtil.LaunchMode.DEBUG;

    /**
     * 程序标签
     */
    public final static String TAG = "CNode";

    /**
     * 访问令牌，用来判断是否登录
     * 用户名
     * 头像地址
     */
    public String access_token;

    // 应用程序上下文
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(instance);
        access_token = CommonUtils.getStringFromLocal(Params.ACCESS_TOKEN);
        //禁止默认统计
        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 静态方法返回程序上下文
     * @return
     */
    public static synchronized App getContext() {
        return instance;
    }

}
