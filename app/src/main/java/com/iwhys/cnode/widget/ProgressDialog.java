package com.iwhys.cnode.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.iwhys.cnode.R;


/**
 * Created by devil on 14-8-13.
 * 进度指示对话框
 */
public class ProgressDialog extends BaseDialog {

    private View mLoadingIcon;
    private TextView mloadingText;
    private Animation loadingAnim;

    public ProgressDialog(Context context){
        super(context, R.layout.dialog_progress);
        initWidget();
    }


    /**
     * 快捷设置loading状态为正在提交，并显示
     */
    public void onCommitting(){
        setText(R.string.on_committing);
        this.show();
    }

    /**
     * 快捷设置loading状态为正在载入，并显示
     */
    public void onLoading(){
        setText(R.string.on_loading);
        this.show();
    }

    /**
     * 设置载入过程显示的提示文本
     * @param text
     */
    public void setText(String text){
        if(!TextUtils.isEmpty(text)){
            mloadingText.setText(text);
        }
    }

    /**
     * 设置载入过程显示的提示文本
     * @param resid
     */
    public void setText(int resid){
        String text = mContext.getResources().getString(resid);
        setText(text);
    }

    @Override
    public void show() {
        //未设置文字，则隐藏文字控件，mView背景设置为透明
        if("".equals(mloadingText.getText())){
            mloadingText.setVisibility(View.GONE);
            mView.setBackgroundResource(Color.TRANSPARENT);
        }else{
            mloadingText.setVisibility(View.VISIBLE);
            mView.setBackgroundResource(R.drawable.dialog_loading_bg);
        }
        Animation a = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.50f, Animation.RELATIVE_TO_SELF, 0.50f);
        a.setDuration(1000);
        a.setFillAfter(true);
        a.setInterpolator(new LinearInterpolator());
        a.setRepeatCount(Animation.INFINITE);
        mLoadingIcon.startAnimation(loadingAnim);
        super.show();
    }

    private void initWidget(){
        mLoadingIcon = mView.findViewById(R.id.loading_icon);
        mloadingText = (TextView) mView.findViewById(R.id.loading_text);
        loadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_loading);
        setCancelForce(false);
    }

    @Override
    protected boolean dialogCancelable() {
        return false;
    }

    @Override
    protected boolean dialogCanceledOnTouchOutside() {
        return false;
    }
}
