package com.iwhys.cnode.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iwhys.cnode.App;
import com.iwhys.cnode.R;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.constant.IntentAction;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.util.volley.VolleyErrorHelper;
import com.iwhys.cnode.util.volley.VolleyHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 发表新话题
 * Created by devil on 15/4/11.
 */
public class NewTopicFragment extends BaseFragment implements TextWatcher {

    //标题最少要输入的字符数
    private final static int MIN_TITLE_LENGTH = 10;
    private TextView confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_topic, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_topic);
        final RadioGroup tab_select = (RadioGroup) view.findViewById(R.id.tab_select);
        final EditText title = (EditText) view.findViewById(R.id.title);
        title.addTextChangedListener(this);
        final EditText content = (EditText) view.findViewById(R.id.content);
        confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onCommitting();
                Map<String, String> map = new HashMap<>();
                map.put(Params.ACCESS_TOKEN, App.getContext().access_token);
                map.put(Params.TITLE, title.getText().toString().trim());
                String c = content.getText().toString().trim();
                map.put(Params.CONTENT, c.length() > 0 ? c : "如题！");
                map.put(Params.TAB, (String) tab_select.findViewById(tab_select.getCheckedRadioButtonId()).getTag());
                JSONObject params = new JSONObject(map);
                JsonObjectRequest request = new JsonObjectRequest(UrlHelper.TOPICS, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgress();
                        sActivity.sendBroadcast(new Intent(IntentAction.NEW_TOPIC));
                        CommonUtils.hideKeyboard(sActivity);
                        CommonUtils.showToast(R.string.success);
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sActivity.finish();
                            }
                        }, 500);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgress();
                        VolleyErrorHelper.getMessage(error, sActivity);
                        CommonUtils.showToast(R.string.failure);
                    }
                });
                VolleyHelper.addToRequestQueue(request);
            }
        });
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showKeyboard(title);
            }
        }, 300);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = s.toString().trim().length();
        if (length < MIN_TITLE_LENGTH) {
            confirm.setText(length == 0 ? getString(R.string.confirm) : "差" + (MIN_TITLE_LENGTH - length) + "个字");
            confirm.setEnabled(false);
        } else {
            confirm.setText(R.string.confirm);
            confirm.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
