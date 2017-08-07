package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.hx.domain.EaseEmojicon;
import com.zjhz.teacher.hx.domain.EaseEmojiconGroupEntity;
import com.zjhz.teacher.hx.model.EaseDefaultEmojiconDatas;
import com.zjhz.teacher.hx.utils.EaseSmileUtils;
import com.zjhz.teacher.hx.widget.emojicon.EaseEmojiconMenu;
import com.zjhz.teacher.hx.widget.emojicon.EaseEmojiconMenuBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zzd on 2017/4/25.
 * input menu
 * <p>
 * including below component:
 * EaseChatPrimaryMenu: main menu bar, text input, send button
 * EaseChatExtendMenu: grid menu with image, file, location, etc
 * EaseEmojiconMenu: emoji icons
 */

public class ChatInputMenu extends LinearLayout {
    private EaseEmojiconMenu emojiconMenu;
    private EditText msgTextEd;
    private ImageView emojiConImg;
    private TextView sendTv;

    private LayoutInflater layoutInflater;

    private Handler handler = new Handler();
    private ChatInputMenu.ChatInputMenuListener listener;
    private Context context;

    private List<EaseEmojiconGroupEntity> emojiconGroupList;

    public ChatInputMenu(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.chat_input_menu, this);
        emojiconMenu = (EaseEmojiconMenu) findViewById(R.id.emojicon_menu);
        msgTextEd = (EditText) findViewById(R.id.msg_text_ed);
        emojiConImg = (ImageView) findViewById(R.id.emoji_img);
        sendTv = (TextView) findViewById(R.id.send_tv);

        emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
        emojiconGroupList.add(new EaseEmojiconGroupEntity(R.drawable.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));

        initEvent();
    }

    private void initEvent() {
        // emojicon menu
        emojiconMenu.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

            @Override
            public void onExpressionClicked(EaseEmojicon emojicon) {
                if (emojicon.getEmojiText() != null) {
                    sendTv.append(EaseSmileUtils.getSmiledText(context, emojicon.getEmojiText()));
                }
            }

            @Override
            public void onDeleteImageClicked() {
                if (!TextUtils.isEmpty(sendTv.getText())) {
                    KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    sendTv.dispatchKeyEvent(event);
                }
            }
        });

        emojiConImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconMenu.getVisibility() == GONE) {
                    emojiConImg.setImageResource(R.drawable.ease_chatting_biaoqing_btn_enable);
                    emojiconMenu.setVisibility(VISIBLE);
                    hideKeyboard();
                } else {
                    showKeyboard();
                    emojiConImg.setImageResource(R.drawable.ease_chatting_biaoqing_btn_normal);
                    emojiconMenu.setVisibility(GONE);
                }
            }
        });

        msgTextEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() == 0) {
                    sendTv.setOnClickListener(null);
                    sendTv.setBackgroundResource(R.drawable.btn_send_msg);
                } else {
                    sendTv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onSendMessage(s.toString());
                        }
                    });
                    sendTv.setBackgroundResource(R.drawable.common_btn_bg);
                }
            }
        });
    }

//    //如果输入法在窗口上已经显示，则隐藏，反之则显示
//    private void hideOrShowKeyboard() {
////        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
////        imm.hideSoftInputFromWindow(msgTextEd.getWindowToken(), 0) ;
//        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }


    public void setChatInputMenuListener(ChatInputMenu.ChatInputMenuListener listener) {
        this.listener = listener;
    }

    public interface ChatInputMenuListener {
        /**
         * when send message button pressed
         *
         * @param content message content
         */
        void onSendMessage(String content);
    }
}
