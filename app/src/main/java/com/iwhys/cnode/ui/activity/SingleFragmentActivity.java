package com.iwhys.cnode.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.iwhys.cnode.R;
import com.iwhys.cnode.util.SimpleFactory;
import com.iwhys.cnode.util.constant.Params;

/**
 * 单个fragment的Activity
 * Created by devil on 15/3/5.
 */
public class SingleFragmentActivity extends BaseBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getIntent().getExtras();
            String fragmentName = bundle.getString(Params.FRAGMENT_NAME);
            Bundle arguments = bundle.getBundle(Params.ARGUMENTS);
            FrameLayout container = new FrameLayout(this);
            container.setId(R.id.wrap_content);
            setContentView(container);
            getSupportFragmentManager().beginTransaction().add(R.id.wrap_content, SimpleFactory.createFragment(fragmentName, arguments)).commit();
        } catch (Exception e){
            e.printStackTrace();
            finish();
        }
    }
}
