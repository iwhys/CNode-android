package com.iwhys.cnode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwhys.cnode.adapter.TopicListAdapter;
import com.iwhys.cnode.entity.Topic;
import com.iwhys.cnode.entity.User;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.CommonListView;

import java.util.List;

/**
 * 用户栏目列表
 * fragment的生命周期中只初始化控件
 * 所有数据都通过refresh方法加载（通过宿主activity控制，确保fragment初始化完成后再加载数据）
 * Created by devil on 15/4/1.
 */
public class UserTopicListFragment extends BaseFragment {

    private String tab;
    private CommonListView<Topic> listView;

    /**
     * 刷新
     */
    public void refresh(User user) {
        List<Topic> topics;
        switch (tab) {
            case "recent_topics":
                topics = user.getRecent_topics();
                break;
            case "recent_replies":
                topics = user.getRecent_replies();
                break;
            default:
                topics = user.getCollect_topics();
                break;
        }
        listView.onGetDataSuccess(1, topics, 0);
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
            public void getDataFromLocal() {

            }

            @Override
            public void getDataFromServer(final int page) {

            }

        };
//        listView.setOnItemClickListener(new CompatOnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Topic topic = (Topic) view.getTag(R.id.first_tag);
//                Bundle arguments = new Bundle();
//                arguments.putString("id", topic.getId());
//                arguments.putString("title", topic.getTitle());
//                arguments.putString("author", topic.getAuthor().getLoginname());
//                arguments.putString("content", topic.getContent());
//                arguments.putString("create_at", CommonUtils.getTimeFormat("yyyy-MM-dd HH:mm", topic.getCreateAt()));
//                arguments.putInt("reply_count", topic.getReply_count());
//                Bundle bundle = new Bundle();
//                bundle.putString(Params.FRAGMENT_NAME, TopicDetailFragment.class.getSimpleName());
//                bundle.putBundle(Params.ARGUMENTS, arguments);
//                ActivitySwitcher.pushDefault(sActivity, SingleInstanceActivity.class, bundle);
//            }
//        });
        listView.enableLoadMore(false);
        return listView.getView();
    }
}
