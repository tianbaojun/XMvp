package com.zjhz.teacher.hx.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.hx.widget.EaseConversationList;
import com.zjhz.teacher.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * conversation list fragment
 *
 */
public class EaseConversationListFragment extends EaseBaseFragment{
	private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<>();
    /**列表*/
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    List<String> lists = new ArrayList<>();
    private DefaultLoadingLayout loadingLayout;
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: // -------------------------------------------------------------------------------------------------------------------------------------------
                {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    if (conversationListView != null) {
                        LogUtil.e("历史列表要刷新啦EaseConversastionListFragment_three");
                        refreListView();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    protected EMConversationListener convListener = new EMConversationListener(){

		@Override
		public void onCoversationUpdate() {
			refresh();
		}
    };
    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);  // 列表
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(),conversationListView);
        query = (EditText) getView().findViewById(R.id.query); // 搜索 --------------------------------------------------
        // button to clear content in search bar
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }

    @Override
    public void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        if(listItemClickListener != null){
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    if (AppContext.getInstance().clickHx != null && AppContext.getInstance().clickHx.size() > 0) {
                        LogUtil.e("搜索字 = ",s.toString().trim());
                        lists.clear();
                        for (int i = 0; i < AppContext.getInstance().clickHx.size(); i++) {
                            if (s.toString().trim().equals(AppContext.getInstance().clickHx.get(i).name.trim().substring(0, 1))) {
                                LogUtil.e("截取首字 = ", AppContext.getInstance().clickHx.get(i).name.trim().substring(0, 1));
                                lists.add(AppContext.getInstance().clickHx.get(i).id);
                            }
                        }
                    }
                }else{
                    LogUtil.e("空字符串搜索");
                    conversationListView.filter(s);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(s.toString().trim())) {
                            LogUtil.e("截取的id集合 = ",lists.size() + "");
                            LogUtil.e("列表点击的id集合 = ", AppContext.getInstance().clickHx.size() + "");
                            if (lists != null && lists.size() > 0) {
                                for (int j = 0; j < lists.size(); j++) {
                                    for (int i = 0; i < AppContext.getInstance().clickHx.size(); i++) {
                                        if (AppContext.getInstance().clickHx.get(i).id.trim().equals(lists.get(j).trim())) {
                                            LogUtil.e("搜素环信姓名", AppContext.getInstance().clickHx.get(j).name);
                                            conversationListView.filter(lists.get(j));
                                        }
                                    }
                                }
                            }else{
                                LogUtil.e("空字符串搜索二");
                                conversationListView.filter(s);
                            }
                        }
                    }
                }, 200);

//                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lists.clear();
            if (conversationListView != null) {
                LogUtil.e("历史列表要刷新啦EaseConversastionListFragment_one");
                conversationList.clear();
                conversationList.addAll(loadConversationList());
                refreListView();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case "fly_fly":
                if (conversationListView != null) {
                    LogUtil.e("历史列表要刷新啦EaseConversastionListFragment_two");
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    refreListView();
                }
            break;
        }
    }

    /**刷新列表数据并且切换数据状态*/
    private void refreListView() {
        if(conversationList.size()>0) {
            conversationListView.refresh();
            loadingLayout.onDone();
        }else{
            loadingLayout.onEmpty();
        }
    }

    /**
     * connected to server
     */
    protected void onConnectionConnected(){
        errorItemContainer.setVisibility(View.GONE);
    }
    
    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected(){
        errorItemContainer.setVisibility(View.VISIBLE);
    }
    

    /**
     * refresh ui===========================================================================================================================================================
     */
    public void refresh() {
    	if(!handler.hasMessages(MSG_REFRESH)){
    		handler.sendEmptyMessage(MSG_REFRESH);
    	}
    }
    
    /**
     * load conversation list
     * 
     * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +    */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according bespeakTime stamp of last message
     * 
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
    
   protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            loadingLayout.onLoading();
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isConflict){
            outState.putBoolean("isConflict", true);
        }
    }
    
    /**
     * set conversation list item click listener
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

}
