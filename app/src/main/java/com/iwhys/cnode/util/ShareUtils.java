package com.iwhys.cnode.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * 分享工具类
 * Created by devil on 15/1/7.
 */
public class ShareUtils {

    private final static String ID_WEIXIN = "com.tencent.mm";//微信
    private final static String ID_WEIXIN_FRIENDS = "com.tencent.mm.ui.tools.ShareImgUI";//微信好友
//    private final static String ID_WEIXIN_FRIENDS_ZOOM = "com.tencent.mm.ui.tools.ShareToTimeLineUI";//微信朋友圈
    private final static String ID_QQ = "tencent.mobileqq";//QQ
//    private final static String ID_QQ_WEIBO = "com.tencent.wblog";//腾讯微博
//    private final static String ID_SINA_WEIBO = "com.sina.weibo";//新浪微博
//    private final static String ID_RENREN = "renren";//人人
//    private final static String ID_EVERNOTE = "evernote";//印象笔记
    private final static String[] INTENT_IDS = new String[]{ID_WEIXIN, ID_QQ};

    /**
     * 分享
     *
     * @param context
     * @param shareContent
     */
    public static void commonShare(Context context, String shareContent) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //要分享的文字内容
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        context.startActivity(shareIntent);
    }

    /**
     * 按照顺序直接启动分享程序
     * 依次为：微信、QQ，都没有则启动通用分享
     *
     * @param context
     * @param shareContent
     */
    public static void directShare(Context context, String shareContent) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (String INTENT_ID : INTENT_IDS) {
                final ResolveInfo resolveInfo = getTargetInfo(resInfo, INTENT_ID);
                if (resolveInfo != null) {
                    share.putExtra(Intent.EXTRA_TEXT, shareContent);
                    if (INTENT_ID.equals(ID_WEIXIN)) {
                        share.setComponent(new ComponentName(ID_WEIXIN, ID_WEIXIN_FRIENDS));
                        context.startActivity(share);
                    } else {
                        share.setPackage(resolveInfo.activityInfo.packageName);
                        context.startActivity(Intent.createChooser(share, "请选择"));
                    }
                    return;
                }
            }
        }
        //以上intentType均不存在，则启动通用分享
        commonShare(context, shareContent);
    }

    //获取包含目标intent的ResolveInfo
    private static ResolveInfo getTargetInfo(List<ResolveInfo> resInfo, String intentType) {
        for (ResolveInfo info : resInfo) {
            if (info.activityInfo.packageName.toLowerCase().contains(intentType) ||
                    info.activityInfo.name.toLowerCase().contains(intentType)) {
                return info;
            }
        }
        return null;
    }

}
