package com.iwhys.cnode.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.iwhys.cnode.App;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by devil on 14-7-22.
 * 通用方法
 */
public class CommonUtils {

    private final static Context APP_CONTEXT = App.getContext();

    private CommonUtils() {
        throw new AssertionError();
    }

    /**
     * 获取包信息
     *
     * @return 包信息
     */
    public static PackageInfo getVersionInfo() {
        try {
            PackageManager pm = APP_CONTEXT.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(APP_CONTEXT.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当前系统版本大于等于输入的版本号
     *
     * @param versionCode
     * @return
     */
    public static boolean gtVersion(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }

    /**
     * 检查网络是否可用
     *
     * @return true可用， false不可用
     */
    public static boolean isNetWorkAvailable() {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) APP_CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 手机振动
     *
     * @param duration 时间ms
     */
    public static void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) APP_CONTEXT.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 反射获取类
     *
     * @param className
     * @return class
     */
    public static Class<?> getClassByString(String className) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 根据手机分辨率从dp转成px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = APP_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 从sp到px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = APP_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 计算文字宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 隐藏输入法键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            final View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示输入法键盘
     *
     * @param view
     */
    public static void showKeyboard(EditText view) {
        InputMethodManager imm = (InputMethodManager) APP_CONTEXT.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 根据item高度重新设置ListView高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    /**
     * 设置CheckBox消除sdk版本造成的差异(小于16padding未计算button的宽度)
     * @param checkBox 控件
     * @param pLeft 左边距
     * @param pRight 右编剧
     * @param resid 图片资源id
     */
    public static void setCheckBox(CheckBox checkBox, int pLeft, int pRight, int resid) {
        int width = BitmapFactory.decodeResource(APP_CONTEXT.getResources(), resid).getWidth();
        checkBox.setButtonDrawable(resid);
        int paddingLeft = checkBox.getPaddingLeft() + Build.VERSION.SDK_INT <= 16 ? width : 0;
        checkBox.setPadding(paddingLeft + dip2px(pLeft), checkBox.getPaddingTop(), checkBox.getPaddingRight() +
                dip2px(pRight), checkBox.getPaddingBottom());
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveStringToLocal(String key, String value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveBooleanToLocal(String key, boolean value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveIntToLocal(String key, int value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveFloatToLocal(String key, float value) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putFloat(key, value);
        return editor.commit();//提交修改
    }

    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static String getStringFromLocal(String key) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static boolean getBooleanFromLocal(String key, boolean defValue) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean getBooleanFromLocal(String key) {
        return getBooleanFromLocal(key, false);
    }

    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static int getIntFromLocal(String key) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 读取信息
     *
     * @param key
     * @return
     */
    public static float getFloatFromLocal(String key) {
        SharedPreferences sharedPreferences = APP_CONTEXT.getSharedPreferences(key, Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    /**
     * 显示提示
     *
     * @param message
     */
    public static void showToast(String message) {
        Toast.makeText(APP_CONTEXT, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int resId) {
        Toast.makeText(APP_CONTEXT, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 不足两位的数字前加0补齐两位
     *
     * @param number
     * @return
     */
    public static String getDoubleDigit(int number) {
        StringBuilder builder = new StringBuilder();
        if (number >= 0 && number < 10) {
            builder.append("0");
        }
        builder.append(number).toString();
        return builder.toString();
    }

    /**
     * 格式化日期
     *
     * @param millis 以毫秒为单位
     * @param flags  日期格式标志
     * @return
     */
    public static String formatDateTime(long millis, int flags) {
        return DateUtils.formatDateTime(APP_CONTEXT, millis, flags);
    }

    /**
     * 格式化当前时间
     *
     * @param flags
     * @return
     */
    public static String formatCurrentTime(int flags) {
        return formatDateTime(System.currentTimeMillis(), flags);
    }

    /**
     * 格式化日期为月日时分
     *
     * @param date
     * @return
     */
    public static String formatMDHM(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        if (date == null) return "";
        return dateFormat.format(date);
    }

    /**
     * 获取时间格式
     *
     * @param format
     * @param date
     * @return
     */
    public static String getTimeFormat(String format, Date date) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    public static String getTimeFormat(String format, long timestamp) {
        return getTimeFormat(format, new Date(timestamp * 1000));
    }

    /**
     * 计算两个日期之间相差天数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     * @throws java.text.ParseException
     */
    public static int daysBetween(Date beginDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            beginDate = sdf.parse(sdf.format(beginDate));
            endDate = sdf.parse(sdf.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 从字符串获取日期
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static Date getDateFromString(String timeStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据当前时间返回通俗时间值
     *
     * @param date
     * @return
     */
    public static String commonTime(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int y1 = c.get(Calendar.YEAR);
        int d1 = c.get(Calendar.DAY_OF_YEAR);
        long t1 = c.getTimeInMillis();
        c.setTime(new Date());
        int y2 = c.get(Calendar.YEAR);
        int d2 = c.get(Calendar.DAY_OF_YEAR);
        long t2 = c.getTimeInMillis();
        int yearGap = y2 - y1;
        int dayGap = d2 - d1; // 与现在时间相差天数
        long timeGap = (t2 - t1) / 1000;//与现在时间相差秒数
        String timeStr = "";
        if (yearGap == 0) {//当年
            if (dayGap == 0) {// 当天，直接显示时间
                if (timeGap > 60 * 60 * 4){// 4小时-24小时
                    timeStr = getTimeFormat("HH:mm", date);
                } else if (timeGap > 60 * 60) {// 1小时-24小时
                    timeStr = timeGap / (60 * 60) + "小时前";
                } else if (timeGap > 60) {// 1分钟-59分钟
                    timeStr = timeGap / 60 + "分钟前";
                } else {// 1秒钟-59秒钟
                    timeStr = "刚刚";
                }
            } else if (dayGap == 1) {// 昨天+时间
                timeStr = "昨天 " + getTimeFormat("HH:mm", date);
            } else if (dayGap == 2) {// 前天+时间
                timeStr = "前天 " + getTimeFormat("HH:mm", date);
            } else {// 大于3天，显示具体月日及时间
                timeStr = getTimeFormat("MM-dd HH:mm", date);
            }
        } else {//非当年现实完整的年月日及时间
            timeStr = getTimeFormat("yyyy-MM-dd", date);
        }
        return timeStr;

    }

    /**
     * 打开市场中的本软件
     *
     * @param context 上下文
     */
    public static void gotoAppMarket(Context context) {
//        String str = "market://search?q=pname:" + context.getPackageName();
        String str = "market://details?id=" + context.getPackageName();
        Intent marketIntent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(marketIntent, 0);
        switch (resInfo.size()){
            case 0:
                CommonUtils.showToast("您未安装应用市场，无法评分！");
                break;
            case 1:
                //直接启动唯一的市场
                context.startActivity(marketIntent);
                break;
            default:
                //启动市场选择界面
                context.startActivity(Intent.createChooser(marketIntent, "请选择"));
                break;
        }
    }

    /**
     * 播放系统声音
     */
    public static void playSystemSound() {
        // Uri alert =
        // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // alert =
        // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // alert =
        // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(App.getContext(), notification);
        r.play();
    }

}
