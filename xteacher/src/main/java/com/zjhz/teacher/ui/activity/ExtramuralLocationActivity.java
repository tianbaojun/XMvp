package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolInitialPositionRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolPathRequest;
import com.zjhz.teacher.NetworkRequests.request.StudentCurrentPositionRequest;
import com.zjhz.teacher.NetworkRequests.response.SchoolInitialPositionResponse;
import com.zjhz.teacher.NetworkRequests.response.StudentCurrentPositionResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.SchoolPath;
import com.zjhz.teacher.bean.StudentEvent;
import com.zjhz.teacher.ui.baidumap.overlayutil.DrivingRouteOverlay;
import com.zjhz.teacher.ui.baidumap.overlayutil.OverlayManager;
import com.zjhz.teacher.ui.view.SectorMenu;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.WheelUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 校外定位
 * 13757580879
 * 057588167329
 */
public class ExtramuralLocationActivity extends BaseActivity implements WheelUtil.OnWheelClickSubmit, View.OnClickListener ,BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener, BaiduMap.OnMapLoadedCallback{

    @BindView(R.id.extramural_title_tv)
    public TextView titleTv;
    @BindView(R.id.extramural_right_text)
    TextView right_text;
    @BindView(R.id.fly_menu)
    public SectorMenu menu;
    @BindView(R.id.activity_extramural_location_bmapView)
    public MapView mMapView;

    boolean isExist = false;  // 是否选择学生
    RoutePlanSearch mSearch = null;    // 搜索模块
    public BaiduMap mBaiduMap;
    RouteLine route = null;
    int nodeIndex = -1;//节点索引,供浏览节点时使用
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;

    private final static String TAG = ExtramuralLocationActivity.class.getSimpleName();
    public double latitude = 39.915291;
    public double longitude = 116.403857;
    private String cardNum;
    WheelUtil mWheelUtil;
    public int fly;  // 判断路径还是当前位置
    private ArrayList<LatLng> points;
    private ArrayList<SchoolPath> schoolPaths;

    private static ArrayList<LatLng>   toPaths;
    private static ArrayList<LatLng>   backPaths;

    DrivingRouteOverlay overlay = null;

    /**判断是哪个按钮*/
    int but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_extramural_location);
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuNameKey,"请选择学生");
        SharedPreferencesUtils.removeKey(ConstantKey.lastStuRicardKey);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        titleTv.setText("校外定位");

        mWheelUtil = new WheelUtil();
        mWheelUtil.setOnClickSubmit(this);
        mWheelUtil.initDatas();

        // 地图初始化
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        dialog.show();
        mark(latitude,longitude);
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.setOnMarkerClickListener(overlay);

        menu.setonMenuItemClickListener(new SectorMenu.onMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 1:
                        but = 1;
                        executeLocationTask(cardNum, position);
                        break;
                    case 2:
                        but =2;
                        executeLocationTask(cardNum, position);
                        break;
                    case 3:
                        but = 3;
                        executeLocationTask(cardNum, position);
                        break;
                    case 4:
                        but = 4;
                        if (isExist) {
                            mWheelUtil.selectData(ExtramuralLocationActivity.this);
                        }else {
                            Intent intenr = new Intent(ExtramuralLocationActivity.this,StudentsCurrentPositionActivity.class);
                            intenr.putExtra("from",100);
                            startActivity(intenr);
                        }
                        break;
                }
//                Toast.makeText(ExtramuralLocationActivity.this, position + ": " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
      *   百度地图轨迹定位
    */
    public void executeLocationTask(String cardNum, int viewID) {
        if (isExist) {
            if( viewID == 1 ) {
                dialog.show();
                requestCurrentLocation(cardNum);  // 当前位置

            }else if(viewID == 2) {
                dialog.show();
                requestRoutePath( "0", cardNum, TimeUtil.refFormatNowDate());  // 上学

            }else  if(viewID ==  3) {
                dialog.show();
                requestRoutePath("1", cardNum, TimeUtil.refFormatNowDate());   // 放学

            }
        } else {
//            ToastUtils.toast("没有选择学生，请先选择学生");
            startActivity(StudentsCurrentPositionActivity.class);
        }
    }


    /**历史路径*/
    public  void requestHistoryRoutePath(String time, String flag) {
        dialog.show();
        if (isExist) {
            requestRoutePath(flag, cardNum, time);
        } else {
            startActivity(StudentsCurrentPositionActivity.class);
        }

    }
    //******************************远程获取轨迹数据*************************************//
    //获取学生当前位置
    private void  requestCurrentLocation(String cardNum ) {
        //  LogUtil.e("历史校外定位的学生卡号。从学生定位返回过来 = ", cardNum);
        StudentCurrentPositionRequest studentCurrentPositionRequest = new StudentCurrentPositionRequest(cardNum);
        NetworkRequest.request(studentCurrentPositionRequest, CommonUrl.SCHOOLROUTCURRENT, Config.SCHOOLROUTCURRENT);
    }

    //获取学生上学、放学轨迹数据
    private void  requestRoutePath(String flag, String cardNum, String time ) {
        dialog.show();
        SchoolPathRequest schoolPathRequest = new SchoolPathRequest(cardNum, time, flag);
        if(flag.equals("0")){
            NetworkRequest.request(schoolPathRequest, CommonUrl.SCHOOLPATH, Config.SCHOOLPATHUP);
        }else if(flag.equals("1")){
            NetworkRequest.request(schoolPathRequest, CommonUrl.SCHOOLPATH, Config.SCHOOLPATHBACK);
        }
    }

    private void DrawLocationTask(  StudentCurrentPositionResponse studentCurrentPositionResponse ) {

        List<StudentCurrentPositionResponse.DataBean> studentCurrentPosition = studentCurrentPositionResponse.data;
        if (!BaseUtil.isEmpty(studentCurrentPosition)) {

            LatLng curPoint= new LatLng(Double.parseDouble(studentCurrentPosition.get(0).pointyBaidu), Double.parseDouble(studentCurrentPosition.get(0).pointxBaidu));
            String datetime = studentCurrentPosition.get(0).recordTime;

            mBaiduMap.clear();
            MapStatus newStatus = new MapStatus.Builder().target(curPoint).zoom(17).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(newStatus);  //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            mBaiduMap.setMapStatus(mMapStatusUpdate);

            BitmapDescriptor bitmap = BitmapDescriptorFactory                                    //构建Marker图标
                    .fromResource(R.drawable.currentmark);
            OverlayOptions option = new MarkerOptions()                                         //构建MarkerOption，用于在地图上添加Marker
                    .position(curPoint)
                    .icon(bitmap);

            // TODO 定位时间
//            OverlayOptions  textOption   =  new TextOptions()
//                    .bgColor(this.getResources().getColor(R.color.highlighted_text_material_light))
//                    .fontSize(24)
//                    .fontColor(this.getResources().getColor(R.color.red))
//                    .text(datetime)
//                    .position(curPoint);

            mBaiduMap.clear();
            mBaiduMap.addOverlay(option);                                                       //在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(textOption);

            if(1>2) {
                Toast.makeText(this, "路径暂无", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //绘制上学、放学轨迹路线
    private    void   DrawSchoolRouteTask( String  type,  ArrayList<LatLng>  paths ) {
        mBaiduMap.clear();
        overlay = new MyDrivingRouteOverlay(mBaiduMap);
        ArrayList<LatLng>  path  =  null;
        String pathType  = "";
        ArrayList<LatLng>  routesPoints  =  null;
        Boolean isToOrFromSchool =   type.equals("to");

        if(  isToOrFromSchool){
            pathType  =  "gotoschool";
        }else {
            pathType  =  "backfromschool";
        }
        path =  paths;
        if( path != null) {
            PlanNode startPoint  = null ;
            PlanNode endPoint    = null ;
            List<PlanNode>   wayPoints  = null;

            int  wayLength  = path.size();
            if(  wayLength < 2 ){
                if( isToOrFromSchool ) {
                    Toast.makeText(this, "上学路径暂无", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    pathType  =  "backfromschool";
                    Toast.makeText(this, "放学路径暂无", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            /*WalkingRoutePlanOption walkingRoutePlanOption =
                    new WalkingRoutePlanOption();
            if(wayLength>=2) {
                walkingRoutePlanOption.from(PlanNode.withLocation(path.get(0)))
                        .to(PlanNode.withLocation(path.get(1)));
                mSearch.walkingSearch(walkingRoutePlanOption);
            }*/

            wayPoints = new ArrayList<PlanNode>();

            for( int i = 0 ; i< wayLength ; i++ ) {

                PlanNode tempPoint  =  PlanNode.withLocation(path.get(i)) ;

                addPositionToMap(path, i);
                if( i == 0 ){
                    startPoint =  tempPoint;
                }
                if( i == ( wayLength - 1)) {
                    endPoint  =  tempPoint;
                }

               if( i!= 0  && i != (wayLength - 1)) {
                    wayPoints.add(tempPoint);
                }
            }

            DrivingRoutePlanOption drivingRoutePlanOption  =
                     new DrivingRoutePlanOption();
            drivingRoutePlanOption.from(startPoint);
            drivingRoutePlanOption.to(endPoint);

            if (  wayLength == 1 || wayLength  ==  2) {
                mSearch.drivingSearch(drivingRoutePlanOption);
            }else {
                drivingRoutePlanOption.passBy(wayPoints);
                mSearch.drivingSearch(drivingRoutePlanOption);
            }

        }
    }

    private void addPositionToMap(ArrayList<LatLng> path, int i) {
        OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
                .fontSize(24).fontColor(0xFFFF00FF).text(i+","+ path.get(i).latitude+","+ path.get(i).longitude).rotate(-30)
                .position(path.get(i));

        mBaiduMap.addOverlay(ooText);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case "STUDENT":  // 从学生列表返回
                isExist = true;
//                cardNum = (String) event.getData();
                StudentEvent studentEvent = (StudentEvent) event.getData();
                cardNum = studentEvent.getCardNum();
                String hestoryDate = studentEvent.getDate();
                String hestoryFlag = studentEvent.getFlag();
                switch (but){
                    case 1:
                        dialog.show();
                        StudentCurrentPositionRequest studentCurrentPositionRequest = new StudentCurrentPositionRequest(cardNum);
                        NetworkRequest.request(studentCurrentPositionRequest, CommonUrl.SCHOOLROUTCURRENT, Config.SCHOOLROUTCURRENT);
                        break;
                    case 2:
                        requestRoutePath( "0", cardNum, TimeUtil.refFormatNowDate());
                        break;
                    case 3:
                        requestRoutePath( "1", cardNum, TimeUtil.refFormatNowDate());
                        break;
                    case 4:
                        requestHistoryRoutePath(hestoryDate, hestoryFlag);
                        break;
                }
                break;

            case Config.SCHOOLROUTCURRENT:  //学生当前位置
                dialog.dismiss();
                fly = 1;
                JSONObject currentLocation = (JSONObject) event.getData();
                StudentCurrentPositionResponse studentCurrentPositionResponse = GsonUtils.toObject(currentLocation.toString(), StudentCurrentPositionResponse.class);
                if (studentCurrentPositionResponse.data != null && studentCurrentPositionResponse.data.size() > 0) {
                    if (!TextUtils.isEmpty(studentCurrentPositionResponse.data.get(0).pointyBaidu) && !TextUtils.isEmpty(studentCurrentPositionResponse.data.get(0).pointxBaidu)) {
                        DrawLocationTask(studentCurrentPositionResponse);
                    }else{
                        ToastUtils.toast("数据为空");
                    }
                }

                break;

            case Config.SCHOOLINITLOCATION: //学校默认定位坐标
                dialog.dismiss();
                fly = 1;
               // mRoutePlanDelegate.useDefaultIcon = true;
                ArrayList<LatLng> pointsZero = new ArrayList<>();
                JSONObject initLocation = (JSONObject) event.getData();
                LogUtil.e("初始化学校位置", initLocation.toString());
                SchoolInitialPositionResponse mSchoolInitialPositionResponse = GsonUtils.toObject(initLocation.toString(), SchoolInitialPositionResponse.class);
                if (mSchoolInitialPositionResponse.getData() != null) {
                    String latitude = mSchoolInitialPositionResponse.getData().getLatitude();
                    String longitude = mSchoolInitialPositionResponse.getData().getLongitude();
                    if (!SharePreCache.isEmpty(latitude) && !SharePreCache.isEmpty(longitude)){
                        initMapState(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        SharedPreferencesUtils.putSharePrefString("CURRENT_SCHOOL_LAT", latitude);
                        SharedPreferencesUtils.putSharePrefString("CURRENT_SCHOOL_LAT", longitude);
                    }else{
                        ToastUtils.showShort("无学校位置信息.......");
                    }
                }else{
                    ToastUtils.showShort("无学校位置信息.......");
                }
                break;

            case Config.SCHOOLPATHUP:  //上学路径
                fly = 1;
                dialog.dismiss();
                points = new ArrayList<>();
                points.clear();
                JSONObject path = (JSONObject) event.getData();
                toPaths  = new ArrayList<LatLng>();
                LatLng p = null ;
                if (path != null) {
                    JSONArray data = path.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                p = new LatLng(Double.parseDouble(o.optString("pointyBaidu")), Double.parseDouble(o.optString("pointxBaidu")));  //当前位置坐标
                                toPaths.add(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        DrawSchoolRouteTask("to", toPaths);
                    }else{
                        ToastUtils.showShort("无路径信息.......");
                    }
                }else{
                    ToastUtils.showShort("无路径信息.......");
                }
                break;

            case Config.SCHOOLPATHBACK:  //放学路径
                dialog.dismiss();
                fly = 1;
                points = new ArrayList<>();
                points.clear();
                path = (JSONObject) event.getData();
                toPaths  = new ArrayList<LatLng>();
                 p = null ;
                if (path != null) {
                    JSONArray data = path.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                p = new LatLng(Double.parseDouble(o.optString("pointyBaidu")), Double.parseDouble(o.optString("pointxBaidu")));  //当前位置坐标
                                toPaths.add(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        DrawSchoolRouteTask("from", toPaths);
                    }else{
                        ToastUtils.showShort("无路径信息.......");
                    }
                }else{
                    ToastUtils.showShort("无路径信息.......");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        right_text.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.lastStuNameKey));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      // unregisterReceiver(mReceiver);
        if (mMapView != null) {
            mMapView = null;
        }
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuNameKey, "选择学生");
    }

    @OnClick({R.id.extramuraltitle_back_img, R.id.extramural_right_text, R.id.extramural_right_text_one})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.extramuraltitle_back_img:
                finish();
                break;
            case R.id.extramural_right_text:
                startActivity(StudentsCurrentPositionActivity.class);
                break;
            case R.id.extramural_right_text_one:
                startActivity(StudentsCurrentPositionActivity.class);
                break;
        }
    }

    @Override
    public void onClickSubmit() {
        requestHistoryRoutePath(mWheelUtil.historyDate, mWheelUtil.flag);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showShort("抱歉，未找到结果");
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = drivingRouteResult.getRouteLines().get(0);
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapLoaded() {
        DefaultSchoolLocation();
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    // 绘制当前位置
    private  void  DefaultSchoolLocation() {
        String lat =  SharedPreferencesUtils.getSharePrefString("CURRENT_SCHOOL_LAT");
        String lng =  SharedPreferencesUtils.getSharePrefString("CURRENT_SCHOOL_LNG");
        if (!lat.equals("") && !lng.equals("")) {
            initMapState(Double.parseDouble(lat), Double.parseDouble(lng));
        }else {
            SchoolInitialPositionRequest schoolInitialPositionRequest = new SchoolInitialPositionRequest();
            NetworkRequest.request(null, CommonUrl.SCHOOLINITLOCATION, Config.SCHOOLINITLOCATION);
        }
    }

    //初始化地图原始显示状态模式
    private  void  initMapState(Double defaultTargetLat, Double defaultTargetLng) {
        mBaiduMap.clear();
        LatLng p = new LatLng(defaultTargetLat, defaultTargetLng );
        MapStatus newStatus = new MapStatus.Builder().target(p).zoom(16).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(newStatus);  //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mark(defaultTargetLat,defaultTargetLng);
    }

    public void mark(double latitude,double longitude){
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.currentmark);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location_out);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

}
