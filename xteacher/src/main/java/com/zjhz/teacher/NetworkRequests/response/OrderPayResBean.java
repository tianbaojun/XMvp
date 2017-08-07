package com.zjhz.teacher.NetworkRequests.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zzd on 2017/6/3.
 */

public class OrderPayResBean implements Serializable{

    /**
     * payInfo : {"aliPayInfos":{"request-Da-ta":"partner%3D%222088721011470498%22%26seller_id%3D%22zhip%401000xyun.com%22%26out_trade_no%3D%221022017053100000000458640%22%26subject%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26body%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26total_fee%3D%220.01%22%26notify_url%3D%22http%3A%2F%2Ftest.1000xyun.com%3A9001%2Fcallback%2FAlipayCallBackService%2Fcallback%22%26service%3D%22mobile.securitypay.pay%22%26payment_type%3D%221%22%26_input_charset%3D%22utf-8%22%26it_b_pay%3D%2230m%22%26sign%3D%22QJzhquBXxfJSHwYlydAw-fkoSWB4GpLFEbQGiB%252Fjfm5s0KcInNxHp8K1SQ9FSW5Z23P1%252FWUrNSmN7G62wfD8Fu%252F%252B%252F5w%252Bnr99DU6riIMWbzmpWUROsPQGg%252FZ5PVpYPFSO3O1SNSNjbfId0J8ps3Lr3Pz5eDgB%252FFrdnWNFAr4cyMqM%253D%22%26sign_type%3D%22RSA%22"},"success":true,"tenPayInfos":{},"type":1,"unionPayInfos":{}}
     */

    private PayInfoBean payInfo;

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }

    public static class PayInfoBean {
        /**
         * aliPayInfos : {"request-Da-ta":"partner%3D%222088721011470498%22%26seller_id%3D%22zhip%401000xyun.com%22%26out_trade_no%3D%221022017053100000000458640%22%26subject%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26body%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26total_fee%3D%220.01%22%26notify_url%3D%22http%3A%2F%2Ftest.1000xyun.com%3A9001%2Fcallback%2FAlipayCallBackService%2Fcallback%22%26service%3D%22mobile.securitypay.pay%22%26payment_type%3D%221%22%26_input_charset%3D%22utf-8%22%26it_b_pay%3D%2230m%22%26sign%3D%22QJzhquBXxfJSHwYlydAw-fkoSWB4GpLFEbQGiB%252Fjfm5s0KcInNxHp8K1SQ9FSW5Z23P1%252FWUrNSmN7G62wfD8Fu%252F%252B%252F5w%252Bnr99DU6riIMWbzmpWUROsPQGg%252FZ5PVpYPFSO3O1SNSNjbfId0J8ps3Lr3Pz5eDgB%252FFrdnWNFAr4cyMqM%253D%22%26sign_type%3D%22RSA%22"}
         * success : true
         * tenPayInfos : {}
         * type : 1
         * unionPayInfos : {}
         */

        private AliPayInfosBean aliPayInfos;
        private boolean success;
        private TenPayInfosBean tenPayInfos;
        private int type;
        private UnionPayInfosBean unionPayInfos;

        public AliPayInfosBean getAliPayInfos() {
            return aliPayInfos;
        }

        public void setAliPayInfos(AliPayInfosBean aliPayInfos) {
            this.aliPayInfos = aliPayInfos;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public TenPayInfosBean getTenPayInfos() {
            return tenPayInfos;
        }

        public void setTenPayInfos(TenPayInfosBean tenPayInfos) {
            this.tenPayInfos = tenPayInfos;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public UnionPayInfosBean getUnionPayInfos() {
            return unionPayInfos;
        }

        public void setUnionPayInfos(UnionPayInfosBean unionPayInfos) {
            this.unionPayInfos = unionPayInfos;
        }

        public static class AliPayInfosBean {
            /**
             * request-Da-ta : partner%3D%222088721011470498%22%26seller_id%3D%22zhip%401000xyun.com%22%26out_trade_no%3D%221022017053100000000458640%22%26subject%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26body%3D%22iOS%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95Chen%22%26total_fee%3D%220.01%22%26notify_url%3D%22http%3A%2F%2Ftest.1000xyun.com%3A9001%2Fcallback%2FAlipayCallBackService%2Fcallback%22%26service%3D%22mobile.securitypay.pay%22%26payment_type%3D%221%22%26_input_charset%3D%22utf-8%22%26it_b_pay%3D%2230m%22%26sign%3D%22QJzhquBXxfJSHwYlydAw-fkoSWB4GpLFEbQGiB%252Fjfm5s0KcInNxHp8K1SQ9FSW5Z23P1%252FWUrNSmN7G62wfD8Fu%252F%252B%252F5w%252Bnr99DU6riIMWbzmpWUROsPQGg%252FZ5PVpYPFSO3O1SNSNjbfId0J8ps3Lr3Pz5eDgB%252FFrdnWNFAr4cyMqM%253D%22%26sign_type%3D%22RSA%22
             */

            @SerializedName("request-Da-ta")
            private String requestData;

            public String getRequestData() {
                return requestData;
            }

            public void setRequestData(String requestData) {
                this.requestData = requestData;
            }
        }

        public static class TenPayInfosBean {
        }

        public static class UnionPayInfosBean {
        }
    }
}
