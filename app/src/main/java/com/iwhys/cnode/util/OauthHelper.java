package com.iwhys.cnode.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.iwhys.cnode.App;
import com.iwhys.cnode.R;
import com.iwhys.cnode.ui.activity.SingleFragmentActivity;
import com.iwhys.cnode.ui.fragment.CaptureFragment;
import com.iwhys.cnode.util.constant.Params;

/**
 * 授权辅助类
 * Created by devil on 15/4/11.
 */
public class OauthHelper {

    /**
     * 是否需要登录
     * @return 登录状态
     */
    public static boolean needLogin(){
        return TextUtils.isEmpty(App.getContext().access_token);
    }

    /**
     * 显示登录对话框
     * @param context 上下文
     */
    public static void showLogin(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("用户登录")
                .setMessage("请在PC端登录cnodejs.org，并在设置页面找到授权二维码，点击确定按钮扫码完成登录。")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
             .setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bd = new Bundle();
                        bd.putString(Params.FRAGMENT_NAME, CaptureFragment.class.getSimpleName());
                        ActivitySwitcher.pushDefault(context, SingleFragmentActivity.class, bd);
                    }
                });
        builder.show();
    }

    /**
     * 退出
     */
    public static void logout(){
        App.getContext().access_token = "";
        CommonUtils.saveStringToLocal(Params.ACCESS_TOKEN, "");
        CommonUtils.saveStringToLocal(Params.LOGIN_NAME, "");
        CommonUtils.saveStringToLocal(Params.AVATAR_URL, "");
    }
}
