package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.adapter.SpinerAdapter;

import java.util.List;

public class SpinerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private SpinerAdapter mAdapter;
    private IOnItemSelectListener mItemSelectListener;

    public SpinerPopWindow(Context context){
        super(context);
        mContext = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
    }

    public void setItemListener(IOnItemSelectListener listener){
        mItemSelectListener = listener;
    }

    public void setAdatper(SpinerAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    public void refreshData(List<String> list, int selIndex){
        if (list != null && selIndex  != -1){
            if (mAdapter != null){
                mAdapter.refreshData(list, selIndex);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null){
            mItemSelectListener.onItemClick(pos);
        }
    }

    public interface IOnItemSelectListener{
        void onItemClick(int pos);
    }
}
