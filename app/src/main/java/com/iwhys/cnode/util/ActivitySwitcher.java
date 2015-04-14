package com.iwhys.cnode.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iwhys.cnode.R;
import com.iwhys.cnode.ui.activity.SingleFragmentActivity;
import com.iwhys.cnode.util.constant.Params;


/**
 * 作者：魔鬼
 * E-mail：iwhs@qq.com
 * 创建时间：2013年10月15日 上午10:51:58
 * 类说明：Activity切换动画
 */

public class ActivitySwitcher {


    public static void pushForResultDefault(Context from, Class<?> to, int requestCode, Bundle bundle) {
        Intent intent = new Intent(from, to);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) from).startActivityForResult(intent, requestCode);
    }

    public static void pushForResultUpInAndStaticOut(Context from, Class<?> to, int requestCode, Bundle bundle){
        pushForResultDefault(from, to, requestCode, bundle);
        ((Activity) from).overridePendingTransition(R.anim.translate_up, R.anim.static_out);
    }

    public static void pushForResultDefault(Context from, Class<?> to, int requestCode) {
        pushForResultDefault(from, to, requestCode, null);
    }

    public static void pushDefault(Context from, Class<?> to) {
        pushDefault(from, to, null);
    }

    public static void pushFragment(Context from, Class<?> to){
        pushFragment(from, to, null);
    }

    public static void pushFragment(Context from, Class<?> to, Bundle argument){
        Bundle bundle = new Bundle();
        if (argument != null){
            bundle.putBundle(Params.ARGUMENTS, argument);
        }
        bundle.putString(Params.FRAGMENT_NAME, to.getSimpleName());
        pushDefault(from, SingleFragmentActivity.class, bundle);
    }

    public static void pushDefault(Context from, Class<?> to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) from).startActivity(intent);
    }

    public static void pushDefault(Context from, Class<?> to, Bundle bundle, int enterAnim, int exitAnim) {
        pushDefault(from, to, bundle);
        ((Activity) from).overridePendingTransition(enterAnim, exitAnim);
    }

    public static void pushAnimInAndStaticOut(Context from, Class<?> to) {
        pushAnimInAndStaticOut(from, to, null);
    }

    public static void pushAnimInAndStaticOut(Context from, Class<?> to, Bundle bundle) {
        pushDefault(from, to, bundle);
        ((Activity) from).overridePendingTransition(R.anim.anim_in, R.anim.static_out);
    }

    public static void pushSlideInAndSlideOut(Context from, Class<?> to, Bundle bundle) {
        pushDefault(from, to, bundle);
        ((Activity) from).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void pushUpInAndStaticOut(Context from, Class<?> to, Bundle bundle){
        pushDefault(from, to, bundle);
        ((Activity) from).overridePendingTransition(R.anim.translate_up, R.anim.static_out);
    }

    public static void pushCustomAnim(Context from, Class<?> to, int enterAnim, int exitAnim) {
        pushDefault(from, to, null, enterAnim, exitAnim);
    }

    public static void pushCustomAnim(Context from, Class<?> to, Bundle bundle, int enterAnim, int exitAnim) {
        pushDefault(from, to, bundle, enterAnim, exitAnim);
    }

    public static void popDefault(Context context) {
        ((Activity) context).finish();
    }

    public static void popStaticInAndAnimOut(Context context) {
        popCustomAnim(context, R.anim.static_in, R.anim.anim_out);
    }

    public static void popSlideInAndSlideOut(Context context) {
        popCustomAnim(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public static void popStaticInAndDownOut(Context context){
        popCustomAnim(context, R.anim.static_in, R.anim.translate_down);
    }

    public static void popCustomAnim(Context context, int enterAnim, int exitAnim) {
        popDefault(context);
        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
    }


}
