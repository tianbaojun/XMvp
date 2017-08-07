package com.zjhz.teacher.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.OrderPayResBean;
import com.zjhz.teacher.NetworkRequests.response.PayOrderResponseBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.PayResult;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {

    private static final int ZFB_SDK_PAY_FLAG = 1;

    private static final int ZFB_PAY = 1;
    private static final int WX_PAY = 2;

    private int payType = 0;

    private String orderNo;

    private boolean isPaySucSync = false;

    @BindView(R.id.title_back_img)
    TextView textView;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.pay_money_detail)
    TextView payMoneyDetail;
    @BindView(R.id.pay_money)
    EditText payMoneyTv;
    @BindView(R.id.zfb_pay)
    LinearLayout zfbPayLayout;
    @BindView(R.id.wx_pay)
    LinearLayout wxPayLayout;
    @BindView(R.id.more_pay_way)
    TextView morePayWayTv;
    @BindView(R.id.review_more_bill)
    TextView moreBillTv;
    @BindView(R.id.pay_history_list)
    ScrollViewWithListView payHistoryListView;
    @BindView(R.id.confirm_pay)
    TextView confirmPayTv;
    @BindView(R.id.zfb_pay_icon)
    ImageView zfbPayIcon;
    @BindView(R.id.wx_pay_icon)
    ImageView wxPayIcon;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ZFB_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        payMoneyTv.setText("");
                        getHistoryPay();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("result", payResult);
//                        startActivity(PayCompleteActivity.class, bundle);
//                        isPaySucSync = true;
//                        isPaySuc();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };


//    private InputFilter lengthFilter = new InputFilter() {
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end,
//                                   Spanned dest, int dstart, int dend) {
//            // source:当前输入的字符
//            // start:输入字符的开始位置
//            // end:输入字符的结束位置
//            // dest：当前已显示的内容
//            // dstart:当前光标开始位置
//            // dend:当前光标结束位置
//            Log.i("", "source=" + source + ",start=" + start + ",end=" + end
//                    + ",dest=" + dest.toString() + ",dstart=" + dstart
//                    + ",dend=" + dend);
//            if (dest.length() == 0 && source.equals(".")) {
//                return "0.";
//            }
//            String dValue = dest.toString();
//            if(dValue.contains(".") && ".".equals(source)){
//                return "";
//            }
//
//            String[] splitArray = dValue.split("\\.");
//            if (splitArray.length == 2) {
//                String numValue = splitArray[0];
//                String dotValue = splitArray[1];
//                if ((dotValue.length() >= 2 && dstart > numValue.length())) {
//                    return "";
//                }
//            }
//            return null;
//        }
//
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        titleTv.setText("个人支付");

//        payMoneyEd.setFilters(new InputFilter[] {lengthFilter});
        payType = ZFB_PAY;

        getHistoryPay();
    }

//    private void getOrderDetail(){
//        http://edu.51jxh.com:2223/api?method=PayOrderTypeService.add&params={"schoolId":"389571228425261056","useTime":"2016-08-30 13:39:37","startFlag":"1"}
//        Map<String, String> map = new HashMap<>();
//
//    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.title_back_img,R.id.pay_money_detail,R.id.zfb_pay, R.id.wx_pay, R.id.more_pay_way,
            R.id.review_more_bill,R.id.confirm_pay})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.pay_money_detail:
                break;
            case R.id.zfb_pay:
                payType = ZFB_PAY;
                zfbPayIcon.setImageResource(R.mipmap.select);
                wxPayIcon.setImageResource(R.mipmap.select_not);
                break;
            case R.id.wx_pay:
                payType = WX_PAY;
                zfbPayIcon.setImageResource(R.mipmap.select_not);
                wxPayIcon.setImageResource(R.mipmap.select);
                break;
            case R.id.more_pay_way:
                break;
            case R.id.review_more_bill:
                startActivity(PayHistoryActivity.class);
                break;
            case R.id.confirm_pay:
                if (payType == 0)
                    ToastUtils.showShort("请选择支付方式");
                else
                    createOrder();
                break;
        }
    }

    private void getHistoryPay(){
        Map<String, String> map = new HashMap<>();
        map.put("isPage","0");
        map.put("status","3");
        map.put("startRecordTime", getNMonthBefore(1));
        map.put("endRecordTime", getNMonthBefore(0));
        NetworkRequest.request(map, CommonUrl.PAY_LIST, Config.PAY_LIST);
    }

    private String getNMonthBefore(int n) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -n);
            return format.format(cal.getTime());
        } catch (Exception e) {

        }
        return "";
    }

    private void createOrder(){
//        if(TextUtils.isEmpty(payMoneyTv.getText().toString()) || Float.parseFloat(payMoneyTv.getText().toString()) == 0){
//            ToastUtils.showShort("支付金额不能为空");
//            return;
//        }
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("payAmount", Math.round(Float.parseFloat(payMoneyTv.getText().toString())*100)+"");//支付金额
        orderMap.put("amount", Math.round(Float.parseFloat(payMoneyTv.getText().toString())*100)+"");//订单总金额
        orderMap.put("subject", "软件服务费");//订单名称
        orderMap.put("body", "软件服务费");//订单描述
        orderMap.put("type", "1");//1表示教师端，2是家长端
        dialog.show();
        NetworkRequest.request(orderMap, CommonUrl.CREATE_ORDER, Config.CREATE_ORDER);
    }

    private void zfbPay(final String orderInfo){
//        final String orderInfo = "partner%3D%222088721011470498%22%26seller_id%3D%22zhip%401000xyun.com%22%26out_trade_no%3D%221022017053100000000420065%22%26subject%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26body%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26total_fee%3D%220.01%22%26notify_url%3D%22http%3A%2F%2Ftest.1000xyun.com%3A9001%2Fcallback%2FAlipayCallBackService%2Fcallback%22%26service%3D%22mobile.securitypay.payType%22%26payment_type%3D%221%22%26_input_charset%3D%22utf-8%22%26it_b_pay%3D%2230m%22%26sign%3D%22TwTdKvOyKE5paaTnPG1crP4svis9PC%252FLje%252BtP616m%252FYrKUdx1Vq3WVP31lIcHBluAkPhY8NEXBK5jKlkw%252F6bQpQV%252Fw%252BANSisUkCurAhS3toQot8Vgmu%252FzPR5074b5JBftlVYhUHtHALx%252F5mcmvsrah%252Bj01Kk6Jse9T4jiFeIwqE%253D%22%26sign_type%3D%22RSA%22";

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                try {
                    Map<String, String>  result = alipay.payV2(URLDecoder.decode(orderInfo,"UTF-8"), true);
                    Log.i("msp", result.toString());
                    Message msg = new Message();
                    msg.what = ZFB_SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void wxPay(){

    }

    private void isPaySuc(){
        Map<String, String> map = new HashMap<>();
        map.put("isPage","0");
        map.put("status","3");
        map.put("orderNo", orderNo);
        NetworkRequest.request(map, CommonUrl.PAY_LIST, Config.PAY_LIST_ORDER_NO);
    }

    @Subscribe
    public void onEventMainThread(EventCenter ev) {
        if(dialog.isShowing())
            dialog.dismiss();
        if(isPaySucSync){
            ToastUtils.showShort("支付成功");
        }
        switch (ev.getEventCode()){
            case Config.NOSUCCESS:
            case Config.ERROR:
            case Config.ERROR_JSON:
                ToastUtils.showShort("error");
                break;
            case Config.CREATE_ORDER:
                PayOrderResponseBean orderResponseBean = GsonUtils.toObjetJson((JSONObject) ev.getData(), PayOrderResponseBean.class);
                if (orderResponseBean != null) {
                    orderResponseBean.setPayType(payType);
                    orderNo = orderResponseBean.getOrderNo();
                }
                NetworkRequest.request(orderResponseBean, CommonUrl.PAY, Config.ORDER_PAY, 9001);
                break;
            case Config.ORDER_PAY:
                String orderInfo = null;
                OrderPayResBean payResBean = GsonUtils.toObjetJson((JSONObject) ev.getData(), OrderPayResBean.class);
                if (payResBean == null)
                    return;
                if(payType == ZFB_PAY){
                    orderInfo = payResBean.getPayInfo().getAliPayInfos().getRequestData();
                    zfbPay(orderInfo);
                }else if(payType == WX_PAY){
                }

                break;
            case Config.PAY_LIST:
                List<PayOrderResponseBean> orderBean = GsonUtils.toArray( PayOrderResponseBean.class, (JSONObject) ev.getData());
                ListViewDBAdapter adapter = new ListViewDBAdapter<PayOrderResponseBean>(this, orderBean, R.layout.pay_history_item, BR.payOrderBean);
                payHistoryListView.setAdapter(adapter);
                break;
            case Config.PAY_LIST_ORDER_NO:
                PayOrderResponseBean orderBean1 = GsonUtils.toObjetJson((JSONObject) ev.getData(), PayOrderResponseBean.class);
                if(orderBean1 != null){
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("result", orderBean1);
                        startActivity(PayCompleteActivity.class, bundle);
                }
                break;
        }
    }
}
