package com.zjhz.teacher.ui.delegate;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.ExtramuralLocationActivity;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 16:57
 * Description: 校外定位
 */
public class ExtramuralLocationDelegate {

    public ExtramuralLocationActivity activity;
    public LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
//    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true;// 是否首次定位

    /***/
    public ExtramuralLocationDelegate(ExtramuralLocationActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        initView();
        initData();
    }

    private void initData() {
        activity.titleTv.setText("校外定位");
    }

    private void initView() {
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        activity.mMapView.showZoomControls(false); // 隐藏放大缩小的加减号按钮
        startLocation();
    }

    /**
     * 开启定位
     */
    public void startLocation() {
        // 开启定位图层
        activity.mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(activity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(false);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        mLocClient.stop();
        activity.mBaiduMap.setMyLocationEnabled(false);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || activity.mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            activity.mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                mark(activity.latitude, activity.longitude);
                LatLng ll = new LatLng(activity.latitude, activity.longitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                activity.mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public void destroy() {
        // 退出时销毁定位
//        mLocClient.stop();
    }

    /**给百度地图给对应的坐标设置标注图*/
    public void mark(double latitude,double longitude){
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location_out);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        activity.mBaiduMap.addOverlay(option);
    }
}
