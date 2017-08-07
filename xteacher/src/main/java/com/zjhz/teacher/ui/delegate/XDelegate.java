package com.zjhz.teacher.ui.delegate;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.io.Serializable;

/**
 * Created by zzd on 2017/4/18.
 */

public abstract class XDelegate<T extends IDrawerLayout, D extends Serializable> implements IDelegate<T>, View.OnClickListener{

//    protected T activity;
    protected IDrawerLayout iDrawerLayout;
    protected DrawerLayout drawerLayout;
    protected D dataBean;
    protected ViewGroup delegateLayout;

    protected Context context;

    protected TextView navigationHeaderLeft, navigationHeaderRight,drawerClearTv;

    public XDelegate(Context context, IDrawerLayout drawerLayout) {
//        this.activity = (T) drawerLayout;
        this.context = context;
        this.iDrawerLayout = drawerLayout;
        this.drawerLayout = drawerLayout.getDrawerLayout();
        this.dataBean = (D) drawerLayout.getDataBean();
        this.delegateLayout = drawerLayout.getDeleteLayout();

        navigationHeaderLeft = (TextView) delegateLayout.findViewById(R.id.navigation_header_left);
        navigationHeaderRight = (TextView) delegateLayout.findViewById(R.id.navigation_header_right);
        drawerClearTv = (TextView) delegateLayout.findViewById(R.id.drawer_layout_clear);

        navigationHeaderLeft.setOnClickListener(this);
        navigationHeaderRight.setOnClickListener(this);
        drawerClearTv.setOnClickListener(this);
    }

//    @Override
//    public T getActivity() {
//        return activity;
//    }

    @Override
    public void openDrawer() {
        if (drawerLayout == null || delegateLayout == null)
            return;
        drawerLayout.openDrawer(delegateLayout);
        readStateOnOpen();
    }

    @Override
    public void closeDrawer() {
        if (drawerLayout == null || delegateLayout == null)
            return;
        drawerLayout.closeDrawer(delegateLayout);
        saveStateOnClose();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_header_left:
                closeDrawer();
                break;
            case R.id.navigation_header_right:
                closeDrawer();
                break;
            case R.id.drawer_layout_clear:
                resetData();
                break;
        }
    }

    public abstract void resetData();

    public abstract void initialize();
}
