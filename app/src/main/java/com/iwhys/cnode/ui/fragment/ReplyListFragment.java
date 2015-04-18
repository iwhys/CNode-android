package com.iwhys.cnode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iwhys.cnode.App;
import com.iwhys.cnode.R;
import com.iwhys.cnode.adapter.ReplyListAdapter;
import com.iwhys.cnode.entity.Content;
import com.iwhys.cnode.entity.Reply;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.OauthHelper;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.cnode.util.volley.DateTypeAdapter;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.util.volley.VolleyErrorHelper;
import com.iwhys.cnode.util.volley.VolleyHelper;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.CommonListView;
import com.iwhys.mylistview.CompatOnItemClickListener;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复列表
 * Created by devil on 15/4/9.
 */
public class ReplyListFragment extends BaseFragment implements TextWatcher, View.OnClickListener {

    private String id;
    private CommonListView<Reply> listView;
    private TextView reply_to, confirm;
    private EditText input;
    private View login_mask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        id = arguments.getString("id");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.reply_list);
        reply_to = (TextView) view.findViewById(R.id.reply_to);
        reply_to.setOnClickListener(this);
        confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        input = (EditText) view.findViewById(R.id.input);
        input.addTextChangedListener(this);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listView.scrollToBottom();
                }
            }
        });
        login_mask = view.findViewById(R.id.login_mask);
        login_mask.setOnClickListener(this);
        listView = new CommonListView<Reply>(sActivity) {
            @Override
            public BaseListAdapter<Reply> getAdapter(Context context) {
                return new ReplyListAdapter(context);
            }

            @Override
            public void getDataFromLocal() {
            }

            @Override
            public void getDataFromServer(final int page) {
                String url = UrlHelper.getTopicUrl(id);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Reply> replyList = null;
                        if (response != null) {
                            Gson gson = new GsonBuilder()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                                    .create();
                            Content content = gson.fromJson(response, Content.class);
                            replyList = content.getData().getReplies();
                        }
                        listView.onGetDataSuccess(page, replyList, System.currentTimeMillis() / 1000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.getMessage(error, sActivity);
                        listView.onGetDataFailure(page);
                    }
                });
                VolleyHelper.addToRequestQueue(request, id);
            }
        };
        listView.enableLoadMore(false);
        listView.setOnItemClickListener(new CompatOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                input.requestFocus();
                CommonUtils.showKeyboard(input);
                Reply reply = (Reply) view.getTag(R.id.first_tag);
                reply_to.setTag(reply.getId());
                reply_to.setText("@" + reply.getAuthor().getLoginname());
            }
        });
        listView.setJazzyEffect(null);
        ((ViewGroup) view.findViewById(R.id.list_container)).addView(listView.getView());
        view.post(new Runnable() {
            @Override
            public void run() {
                listView.refresh(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();
    }

    //检查登录状态
    private void checkLoginStatus() {
        if (OauthHelper.needLogin()) {
            login_mask.setVisibility(View.VISIBLE);
            input.setEnabled(false);
        } else {
            login_mask.setVisibility(View.GONE);
            input.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        confirm.setEnabled(s.toString().trim().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_mask:
                OauthHelper.showLogin(sActivity);
                break;
            case R.id.reply_to:
                clearReplyTo();
                break;
            case R.id.confirm:
                reply();
                break;
        }
    }

    //清空reply_to
    private void clearReplyTo() {
        reply_to.setText("");
        reply_to.setTag("");
    }

    //回复
    private void reply() {
        onCommitting();
        String url = UrlHelper.getReplyUrl(id);
        Map<String, String> map = new HashMap<>();
        map.put(Params.ACCESS_TOKEN, App.getContext().access_token);
        if (reply_to.getTag() != null) {
            map.put("reply_id", (String) reply_to.getTag());
        }
        map.put(Params.CONTENT, reply_to.getText().toString() + input.getText().toString().trim());
        JSONObject params = new JSONObject(map);
        JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                CommonUtils.showToast(R.string.success);
                input.setText("");
                clearReplyTo();
                listView.refresh(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                VolleyErrorHelper.getMessage(error, sActivity);
                CommonUtils.showToast(R.string.failure);
            }
        });
        VolleyHelper.addToRequestQueue(request, id);
    }
}
