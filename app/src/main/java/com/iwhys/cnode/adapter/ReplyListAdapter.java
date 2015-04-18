package com.iwhys.cnode.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.cnode.R;
import com.iwhys.cnode.entity.Reply;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.widget.ScrollListenerWebView;
import com.iwhys.mylistview.BaseListAdapter;
import com.iwhys.mylistview.ViewHolder;

/**
 * Created by devil on 14/11/12.
 * 回复列表适配器
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
        ScrollListenerWebView content = viewHolder.getView(R.id.web_view);
        content.setBackgroundColor(0);
        content.getBackground().setAlpha(0);
        SimpleDraweeView avatar = viewHolder.getView(R.id.avatar);

        author.setText(reply.getAuthor().getLoginname());
        createAt.setText(Html.fromHtml(CommonUtils.commonTime(reply.getCreate_at()) + " • " + "<font color='#82bb22'>" + (position + 1) + "楼</font>"));
        loadData(content, reply.getContent());
        view.setBackgroundResource(getItemViewType(position) == EVEN ? R.drawable.item_topic_even_bg : R.drawable.item_topic_odd_bg);
        String avatarUrl = reply.getAuthor().getAvatar_url();
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatarUrl)), context);
        view.setTag(R.id.first_tag, reply);
        return view;
    }

    //加载数据
    private void loadData(WebView webView, String content) {
        String linkCss = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/topic_detail.css\" type=\"text/css\"/>";
        content = linkCss + content;
        content = content.replace("//dn-cnode", "https://dn-cnode");
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }
}
