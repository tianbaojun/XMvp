package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.hx.EaseConstant;
import com.zjhz.teacher.hx.domain.EaseUser;
import com.zjhz.teacher.hx.ui.EaseBaseActivity;
import com.zjhz.teacher.hx.ui.EaseChatFragment;
import com.zjhz.teacher.hx.utils.EaseUserUtils;

public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private final static String TAG = ChatActivity.class.getSimpleName();
    private EaseChatFragment chatFragment;
    public String toChatUsername,name;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        AppContext.getInstance().addActivity(TAG,this);
        //user or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        name = getIntent().getExtras().getString("userName");
        EaseUserUtils.head = getIntent().getExtras().getString("userHead");
        EaseUserUtils.name = name;
        EaseUser mEaseUser = new EaseUser(toChatUsername);
        mEaseUser.setAvatar(getIntent().getExtras().getString("userHead"));
        mEaseUser.setNick(name);

        chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }
}
