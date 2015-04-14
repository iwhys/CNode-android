package com.iwhys.cnode.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.zbar.lib.CaptureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码捕获类
 * Created by devil on 15/4/11.
 */
public class CaptureFragment extends BaseFragment implements CaptureView.OnCaptureListener {

    private CaptureView captureView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureView = new CaptureView(sActivity);
        int w = CommonUtils.getScreenWidth(sActivity) * 4 / 5;
        captureView.setCropLayoutSize(w, w);
        captureView.setOnCaptureListener(this);
        return captureView.getView();
    }

    @Override
    public void onCapture(String result) {
        if (!TextUtils.isEmpty(result)) {
            oauth(result);
        } else {
            captureError();
        }
    }

    //扫码失败
    private void captureError() {
        CommonUtils.showToast(R.string.failure);
        captureView.restartPreview();
    }

    /**
     * 验证访问令牌
     *
     * @param access_token 令牌
     */
    private void oauth(final String access_token) {
        showProgress(R.string.on_login);
        Map<String, String> map = new HashMap<>();
        map.put(Params.ACCESS_TOKEN, access_token);
        JSONObject params = new JSONObject(map);
        final JsonObjectRequest request = new JsonObjectRequest(UrlHelper.ACCESS_TOKEN, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                if (response != null) {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            CommonUtils.showToast(R.string.success);
                            App.getContext().access_token = access_token;
                            String login_name = response.getString(Params.LOGIN_NAME);
                            String avatar_url = response.getString(Params.AVATAR_URL);
                            CommonUtils.saveStringToLocal(Params.ACCESS_TOKEN, access_token);
                            CommonUtils.saveStringToLocal(Params.LOGIN_NAME, login_name);
                            CommonUtils.saveStringToLocal(Params.AVATAR_URL, avatar_url);
                            sActivity.sendBroadcast(new Intent(IntentAction.LOGIN));
                            sActivity.finish();
                        } else {
                            captureError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        captureError();
                    }
                } else {
                    captureError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                VolleyErrorHelper.getMessage(error, sActivity);
                captureError();
            }
        });
        VolleyHelper.addToRequestQueue(request, access_token);
    }

    @Override
    public void onResume() {
        super.onResume();
        captureView.onResume();
    }

    @Override
    public void onPause() {
        super.onStop();
        captureView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        captureView.onDestroy();
    }

}
