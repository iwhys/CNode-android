package com.iwhys.cnode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iwhys.cnode.R;
import com.iwhys.cnode.adapter.TopicListAdapter;
import com.iwhys.cnode.entity.Topic;
import com.iwhys.cnode.entity.Topics;
import com.iwhys.cnode.ui.activity.SingleInstanceActivity;
import com.iwhys.cnode.util.ActivitySwitcher;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.cnode.util.volley.DateTypeAdapter;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.util.volley.VolleyErrorHelper;
import com.iwhys.cnode.util.volley.VolleyHelper;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.CommonListView;
import com.iwhys.mylistview.CompatOnItemClickListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 栏目列表
 * fragment的生命周期中只初始化控件
 * 所有数据都通过refresh方法加载（通过宿主activity控制，确保fragment初始化完成后再加载数据）
 * Created by devil on 15/4/1.
 */
public class TopicListFragment extends BaseFragment {

    private String tab;
    private CommonListView<Topic> listView;

    /**
     * 刷新
     * @param rightNow 是否立即刷新
     */
    public void refresh(boolean rightNow) {
        if (listView == null) return;
        listView.refresh(rightNow);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = getArguments().getString(Params.TAB);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = new CommonListView<Topic>(sActivity) {

            @Override
            public BaseListAdapter<Topic> getAdapter(Context context) {
                return new TopicListAdapter(context);
            }

            @Override
            public void getDataList(final int page) {
                Map<String, Object> params = new HashMap<>();
                params.put(Params.TAB, tab);
                params.put(Params.LIMIT, 10);
                params.put(Params.PAGE, page);
                String url = UrlHelper.getTopicsUrl(params);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Topic> topicList = null;
                        if (response != null){
                            Gson gson = new GsonBuilder()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                                    .create();
                            Topics topics = gson.fromJson(response, Topics.class);
                            topicList = topics.getData();
                        }
                        listView.onGetDataSuccess(page, topicList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.getMessage(error, sActivity);
                        listView.onGetDataFailure(page);
                    }
                });
                VolleyHelper.addToRequestQueue(request, tab);
            }

        };
        listView.setOnItemClickListener(new CompatOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Topic topic = (Topic) view.getTag(R.id.first_tag);
                Bundle arguments = new Bundle();
                arguments.putString("id", topic.getId());
                arguments.putString("title", topic.getTitle());
                arguments.putString("author", topic.getAuthor().getLoginname());
                arguments.putString("content", topic.getContent());
                arguments.putString("create_at", CommonUtils.getTimeFormat("yyyy-MM-dd HH:mm", topic.getCreateAt()));
                arguments.putInt("reply_count", topic.getReply_count());
                Bundle bundle = new Bundle();
                bundle.putString(Params.FRAGMENT_NAME, TopicDetailFragment.class.getSimpleName());
                bundle.putBundle(Params.ARGUMENTS, arguments);
                ActivitySwitcher.pushDefault(sActivity, SingleInstanceActivity.class, bundle);
            }
        });
        return listView.getView();
    }
}
