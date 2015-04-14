package com.iwhys.cnode.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.cnode.R;
import com.iwhys.cnode.entity.Reply;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.ViewHolder;

/**
 * Created by devil on 14/11/12.
 * 主题列表适配器
 */
public class ReplyListAdapter extends BaseListAdapter<Reply> {

    private final static int ODD = 0, EVEN = 1;
    private Context context;

    public ReplyListAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_reply, viewGroup, false);
        }
        final Reply reply = (Reply) getItem(position);
        ViewHolder viewHolder = ViewHolder.get(view);

        TextView author = viewHolder.getView(R.id.author);
        TextView createAt = viewHolder.getView(R.id.create_at);
        TextView content = viewHolder.getView(R.id.content);
        SimpleDraweeView avatar = viewHolder.getView(R.id.avatar);

        author.setText(reply.getAuthor().getLoginname());
        createAt.setText(CommonUtils.commonTime(reply.getCreate_at()));
        String replyContent = reply.getContent();
        replyContent = replyContent.replace("src=\"//","src=\"https://");
        content.setText(Html.fromHtml(replyContent));
        view.setBackgroundResource(getItemViewType(position) == EVEN ? R.drawable.item_topic_even_bg : R.drawable.item_topic_odd_bg);
        String avatarUrl = reply.getAuthor().getAvatar_url();
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatarUrl)), context);
        view.setTag(R.id.first_tag, reply);
        return view;
    }
}
