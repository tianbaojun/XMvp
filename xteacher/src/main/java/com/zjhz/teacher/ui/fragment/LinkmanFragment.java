package com.zjhz.teacher.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.request.LinkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.SocketBean;
import com.zjhz.teacher.ui.activity.ChatActivity;
import com.zjhz.teacher.ui.activity.DetailDataActivity;
import com.zjhz.teacher.ui.view.AnimatedExpandableListView;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-15
 * Time: 15:57
 * Description: 联系人
 */
public class LinkmanFragment extends BaseFragment {
    private final static String TAG = LinkmanFragment.class.getSimpleName();
    @BindView(R.id.fragment_linkman_listView)
    AnimatedExpandableListView listView;
    @BindView(R.id.fragment_linkman_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    List<GroupItem>  groupItems = new ArrayList<>();
    DefaultLoadingLayout loadingLayout ;
    private ExampleAdapter adapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_linkman, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(),swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(Constance.colors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadingLayout.onLoading();
                initStringRequestPost();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initStringRequestPost();
        adapter = new ExampleAdapter(context);
        adapter.setData(groupItems);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (!listView.isGroupExpanded(groupPosition)) {
                    listView.expandGroupWithAnimation(groupPosition);
                } else {
                    listView.collapseGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {return false;}

    @Override
    public void onEventMainThread(EventCenter eventCenter) {}

    /**部门列表*/
    private void initStringRequestPost() {
        loadingLayout.onLoading();
//        LogUtil.e("baseUrl = ",SharedPreferencesUtils.getSharePrefString("base_url"));
        StringRequest stringRequestPost = new StringRequest(Request.Method.POST, CommonUrl.BASEURL + "?method=OrgDeptService.queryFouTeacherIm",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingLayout.onDone();
                        LogUtil.e("LinkmanFragment", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            groupItems.clear();
                            if (object != null) {
                                if (object.optString("code").equals("0")) {
                                    JSONArray data = object.optJSONArray("data");
                                    if (data != null && data.length() > 0) {
                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject o = (JSONObject) data.get(i);
                                            GroupItem mGroupItem = new GroupItem();
                                            mGroupItem.title = o.optString("deptName");  // 每一组的名称
                                            JSONArray users = o.optJSONArray("users");
                                            if (users != null && users.length() > 0) {
                                                for (int j = 0; j < users.length(); j++) {
                                                    JSONObject u = (JSONObject) users.get(j);
                                                    ChildItem child = new ChildItem();
                                                    child.title = u.optString("nickName");    // 组内每一个名称
                                                    child.userName = u.optString("userId");    // 环信的userName
                                                    child.imageUrl = u.optString("photoUrl");    // 环信的userName头像
                                                    child.phoneNumber = u.optString("phoneNo");    // 电话号码
                                                    child.teacherNumber = u.optString("jobNumber");    // 教工号
                                                    child.studentNumber = u.optString("jobNumber");    // 学号
                                                    child.studentName = u.optString("studentName");    // 学生姓名
                                                    child.clan = u.optString("parentRelation");    // 亲属关系
                                                    mGroupItem.items.add(child);

                                                    SocketBean mSocketBean = new SocketBean();
                                                    mSocketBean.headImage = u.optString("photoUrl");
                                                    mSocketBean.name = u.optString("nickName");
                                                    mSocketBean.id = u.optString("userId");
                                                    AppContext.getInstance().hxId.add(mSocketBean);

//                                                    EaseUser mEaseUser = new EaseUser(u.optString("userId"));
//                                                    mEaseUser.setAvatar(u.optString("photoUrl"));
//                                                    mEaseUser.setNick(u.optString("nickName"));
//                                                    AppContext.getInstance().fly.put(u.optString("userId"),mEaseUser);
                                                }
                                                groupItems.add(mGroupItem);
                                            }
                                        }
                                    }
                                    LogUtil.e("交流联系人", "groupItems.size() = " + groupItems.size());
                                    if(groupItems.size()>0){
                                        loadingLayout.onDone();
                                        adapter.notifyDataSetChanged();
                                    }else{
                                        loadingLayout.onEmpty();
                                    }
                                    LogUtil.e("交流联系人", "AppContext.getInstance().hxId.size() = " + AppContext.getInstance().hxId.size());
                                } else {
                                    loadingLayout.onEmpty();
                                    ToastUtils.toast("获取数据失败，请稍候再试");
                                }
                            }
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
               loadingLayout.onEmpty();
                ToastUtils.toast("获取数据失败,请稍候再试");
                Log.e("LinkmanFragment", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                LogUtil.e("老师id = ",SharedPreferencesUtils.getSharePrefString("app_login_teacherId"));
                LinkRequest link = new LinkRequest(SharedPreferencesUtils.getSharePrefString("app_login_teacherId"));
                map.put("params", GsonUtils.toJson(link));
                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String,String> map = new HashMap<>();
                map.put("token",SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey,""));
                return map;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(context);
        mQueue.add(stringRequestPost);
    }

    private static class GroupItem implements Serializable {
        String title;
        List<ChildItem> items = new ArrayList<>();
    }

    private static class ChildItem implements Serializable {
        String title;
        String imageUrl;
        String userName;
        String phoneNumber;
        String studentNumber;
        String studentName;
        String teacherNumber;
        String clan;
    }

    private static class ChildHolder {
        TextView title;
        RelativeLayout link_main_child_ofly;
        ImageView headImage;
        ImageView phone, notify, message;
    }

    private static class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        private List<GroupItem> items;
        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }
        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            final ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.fragment_linkman_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.fragment_linkman_list_item_textTitle);
                holder.headImage = (ImageView) convertView.findViewById(R.id.fragment_linkman_list_item_imageview);
                holder.phone = (ImageView) convertView.findViewById(R.id.link_main_child_phone);
                holder.notify = (ImageView) convertView.findViewById(R.id.link_main_child_notify);
                holder.message = (ImageView) convertView.findViewById(R.id.link_main_child_message);
                holder.link_main_child_ofly = (RelativeLayout) convertView.findViewById(R.id.link_main_child_ofly);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            GlideUtil.loadImageHead(item.imageUrl, holder.headImage); // 显示头像
            holder.headImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,DetailDataActivity.class);
                    intent.putExtra("userId",item.userName);
                    intent.putExtra("userName",item.title);
                    intent.putExtra("userHead",item.imageUrl);
                    intent.putExtra("phone_number",item.phoneNumber);
                    intent.putExtra("student_number",item.studentNumber);
                    intent.putExtra("student_name",item.studentName);
                    intent.putExtra("teacher_number",item.teacherNumber);
                    intent.putExtra("clan",item.clan);
                    startActivity(intent);
                }
            });

            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+item.phoneNumber));
                    startActivity(intent);
                }
            });

            holder.notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri smsToUri = Uri.parse("smsto:"+item.phoneNumber);
                    Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                    startActivity(mIntent);
                }
            });

            holder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SocketBean bean = new SocketBean();
                    bean.id = item.userName;
                    bean.name = item.title;
                    if (AppContext.getInstance().clickHx != null && AppContext.getInstance().clickHx.size() > 0) {
                        for (int i = 0; i < AppContext.getInstance().clickHx.size(); i++) {
                            if (!item.userName.trim().equals(AppContext.getInstance().clickHx.get(i).id.trim())) {
                                AppContext.getInstance().clickHx.add(bean);
                            }
                        }
                    }else{
                        AppContext.getInstance().clickHx.add(bean);
                    }

//                    SharedPreferencesUtils.putStrListValue("fly_list",AppContext.getInstance().clickHx);
                    Intent  intent = new Intent(context,ChatActivity.class);
                    LogUtil.e("环信userId = " + item.userName);
                    intent.putExtra("userId",item.userName);
                    intent.putExtra("userName",item.title);
                    intent.putExtra("userHead",item.imageUrl);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            if (items != null && items.size() > 0 && items.get(groupPosition).items != null && items.get(groupPosition).items.size() > 0) {
//                LogUtil.e("交流联系人","items.size() = " + items.size() + "-----" + "items.get(groupPosition).items.size() = " + items.get(groupPosition).items.size());
                return items.get(groupPosition).items.size();
            }else{
//                LogUtil.e("交流联系人","items.get(groupPosition).items = " + items.get(groupPosition).items );
                return 0;
            }
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            if (items != null && items.size() > 0) {
//                LogUtil.e("交流联系人","items.size() = " + items.size() );
                return items.size();
            }else {
//                LogUtil.e("交流联系人","items = " + items );
                return 0;
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.fragment_linkman_group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.linkman_group_item_textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.title.setText(item.title);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }
}
