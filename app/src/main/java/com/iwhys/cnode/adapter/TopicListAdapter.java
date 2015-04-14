package com.iwhys.cnode.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.cnode.R;
import com.iwhys.cnode.entity.Topic;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.ViewHolder;

/**
 * Created by devil on 14/11/12.
 * 主题列表适配器
 */
public class TopicListAdapter extends BaseListAdapter<Topic> {

    private final static int ODD = 0, EVEN = 1;
    private Context context;

    public TopicListAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? EVEN : ODD;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //正常填充
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, viewGroup, false);
        }
        final Topic topic = (Topic) getItem(position);
        view.setBackgroundResource(getItemViewType(position) == EVEN ? R.drawable.item_topic_even_bg : R.drawable.item_topic_odd_bg);
        ViewHolder viewHolder = ViewHolder.get(view);

        TextView author = viewHolder.getView(R.id.author);
        TextView last_reply_at = viewHolder.getView(R.id.last_reply_at);
        TextView title = viewHolder.getView(R.id.title);
        SimpleDraweeView avatar = viewHolder.getView(R.id.avatar);
        TextView replyCount = viewHolder.getView(R.id.reply_count);
        TextView visitCount = viewHolder.getView(R.id.visit_count);
        author.setText(topic.getAuthor().getLoginname());
        if (topic.isTop()){
            appendIcon(author, R.drawable.icon_top);
        }
        if (topic.isGood()){
            appendIcon(author, R.drawable.icon_good);
        }
        last_reply_at.setText(CommonUtils.commonTime(topic.getLastReplyAt()));
        title.setText(topic.getTitle());
        String avatarUrl = topic.getAuthor().getAvatar_url();
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatarUrl)), context);
        replyCount.setText(topic.getReply_count() + "");
        visitCount.setText(topic.getVisit_count() + "");
        view.setTag(R.id.first_tag, topic);
        return view;
    }

    //插入图表到TextView
    private void appendIcon(TextView textView, int iconId){
        SpannableString string = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(context, iconId);
        string.setSpan(imageSpan, string.length() - 1, string.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(string);
    }

}
