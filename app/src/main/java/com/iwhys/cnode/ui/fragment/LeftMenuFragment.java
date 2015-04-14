package com.iwhys.cnode.ui.fragment;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.cnode.R;
import com.iwhys.cnode.util.ActivitySwitcher;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.OauthHelper;
import com.iwhys.cnode.util.ShareUtils;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.mylistview.CompatOnItemClickListener;


public class LeftMenuFragment extends BaseFragment implements View.OnClickListener {

    private SimpleDraweeView avatar;
    private TextView loginname;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_menu, container, false);
        view.findViewById(R.id.user_info).setOnClickListener(this);
        avatar = (SimpleDraweeView) view.findViewById(R.id.avatar);
        loginname = (TextView) view.findViewById(R.id.loginname);
        setUserInfo();
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(sActivity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.left_menu_title)) {

            //菜单图标
            private final TypedArray icons = getResources().obtainTypedArray(R.array.left_menu_icon);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.white_btn_color));
                textView.setBackgroundResource(R.drawable.item_left_menu_bg);
                final Drawable drawable = icons.getDrawable(position);
                assert drawable != null;
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);
                textView.setCompoundDrawablePadding(CommonUtils.dip2px(8));
                textView.setHeight(CommonUtils.dip2px(50));
                return textView;
            }
        });
        listView.setOnItemClickListener(new CompatOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        ActivitySwitcher.pushFragment(sActivity, SettingFragment.class);
                        break;
                    case 1:
                        ShareUtils.commonShare(sActivity, getString(R.string.share_content));
                        break;
                    case 2:
                        ActivitySwitcher.pushFragment(sActivity, AboutFragment.class);
                        break;
                }
            }
        });
        TextView version = (TextView) view.findViewById(R.id.version);
        version.setText(getString(R.string.version) + CommonUtils.getVersionInfo().versionName);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_info) {
            if (!OauthHelper.needLogin()){
                ActivitySwitcher.pushFragment(sActivity, UserInfoFragment.class);
            } else {
                OauthHelper.showLogin(sActivity);
            }
        }
    }

    /**
     * 设置用户信息
     */
    public void setUserInfo(){
        String name = CommonUtils.getStringFromLocal(Params.LOGIN_NAME);
        String avatar_url = CommonUtils.getStringFromLocal(Params.AVATAR_URL);
        avatar.setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatar_url)), sActivity);
        loginname.setText(TextUtils.isEmpty(name) ? getString(R.string.login) : name);
    }
}
