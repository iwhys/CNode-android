package com.iwhys.cnode.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.umeng.analytics.MobclickAgent;


/**
 * Activity基类
 * Created by devil on 14-7-26.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //屏蔽menu键
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
