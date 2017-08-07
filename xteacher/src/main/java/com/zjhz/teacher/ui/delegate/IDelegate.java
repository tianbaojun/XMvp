package com.zjhz.teacher.ui.delegate;

/**
 * Created by zzd on 2017/4/18.
 */

public interface IDelegate<T extends IDrawerLayout> {
//    T getActivity();

    void openDrawer();

    void closeDrawer();

    void readStateOnOpen();

    void saveStateOnClose();
}
