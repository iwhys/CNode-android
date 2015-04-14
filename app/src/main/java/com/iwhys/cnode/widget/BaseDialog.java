package com.iwhys.cnode.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iwhys.cnode.R;


/**
 * 作者：魔鬼
 * E-mail：iwhs@qq.com
 * 创建时间：2013年11月25日 上午9:25:20
 * 类说明：对话框基类，去除默认样式，必须自定义布局
 *
 * 修订时间：2014年08月14日 上午
 * 修订说明：显示之前对话框只重设宽高尺寸，位置、动画只在初始化设置一次，暴露是否可以取消、是否可以通过按返回键强制取消接口
 *
 */

public abstract class BaseDialog extends Dialog implements DialogInterface.OnDismissListener{

    protected Context mContext;
    protected View mView;

    private Window mWindow;
    //是否允许强制退出
    private boolean cancelForce = true;

    /**
     * @param context    上下文
     * @param resourceId 布局资源文件id
     */
    public BaseDialog(Context context, int resourceId) {
        super(context, R.style.MyDialog);
        mContext = context;
        mView = View.inflate(mContext, resourceId, null);
        mWindow = getWindow();
        init();
    }

    public void setCancelForce(boolean cancelForce){
        this.cancelForce = cancelForce;
    }

    /**
     * 设置外观并显示
     */
    @Override
    public void show() {
        setDialogSize();
        //显示之前刷新view，防止view变形
        mView.post(new Runnable() {

            @Override
            public void run() {
                mView.postInvalidate();
            }
        });
        super.show();
    }

    @Override
    public void hide() {
        if (isShowing()){
            this.dismiss();
        }
    }

    /**
     * 对话框消失之前执行
     */
    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    /**
     * 是否允许取消
     * @return
     */
    protected boolean dialogCancelable(){
        return true;
    }

    /**
     * 是否允许点击dialog范围之外取消
     * @return
     */
    protected boolean dialogCanceledOnTouchOutside(){
        return true;
    }

    /**
     * 子类需要修改动画，直接覆写该方法
     */
    protected int dialogAnimation() {
        return android.R.style.Animation_Dialog;
    }

    /**
     * 子类需要修改位置，直接覆写该方法
     * 默认屏幕底部
     */
    protected int dialogGravity(){
        return Gravity.CENTER;
    }

    /**
     * 设置窗口布局宽度，默认适应填充数据之后的View
     * @return
     */
    protected int dialogWidth(){
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return mView.getMeasuredWidth();
    }

    /**
     * 设置dialog高度
     */
    protected int dialogHeight() {
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return mView.getMeasuredHeight();
    }

    //初始化设置
    private void init(){
        setContentView(mView);
        setCanceledOnTouchOutside(dialogCanceledOnTouchOutside());
        setCancelable(dialogCancelable());
        setOnDismissListener(this);
        mWindow.setWindowAnimations(dialogAnimation());
        mWindow.setGravity(dialogGravity());
    }

    //设置对话框宽、高属性
    private void setDialogSize() {
        WindowManager.LayoutParams wlp = mWindow.getAttributes();
        wlp.width = dialogWidth();
        wlp.height = dialogHeight();
        mWindow.setAttributes(wlp);
    }

    /**
     * 获取字符串
     * @param resId
     * @return
     */
    protected String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    /*
 * 拦截返回键，触发时调用hide方法隐藏dialog
 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
            //event.getAction()!=KeyEvent.ACTION_UP 不响应抬起动作，可防止执行两次
            if (cancelForce){
                hide();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
