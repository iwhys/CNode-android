package com.iwhys.cnode.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwhys.cnode.R;
import com.iwhys.cnode.util.OauthHelper;
import com.iwhys.cnode.util.constant.IntentAction;

/**
 * 用户信息页
 * Created by devil on 15/4/12.
 */
public class UserInfoFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.user_info);
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OauthHelper.logout();
                sActivity.sendBroadcast(new Intent(IntentAction.LOGIN));
                sActivity.finish();
            }
        });
        return view;
    }
}
