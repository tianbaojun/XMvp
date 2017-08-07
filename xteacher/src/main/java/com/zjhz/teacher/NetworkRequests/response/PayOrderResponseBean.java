package com.zjhz.teacher.NetworkRequests.response;

import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.DateUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zzd on 2017/6/3.
 */

public class PayOrderResponseBean implements Serializable{

    private int payType;  //1 支付宝，2 微信
    private String securityCode;       //安全码，由接口1返回
    private String payeeUid;
    private String payUid;       //支付方：接口1返回的：payUid
    private String orderNo;       //订单号：接口1返回的：orderNo
    private String callUrl;       //回调地址：接口1返回
    private String amount;	//订单总金额：接口1返回，以分为单位
    private String payAmount;	//支付金额，接口1返回，以分为单位
    private String subject;       //订单名称，接口1返回
    private String body;       //订单描述，接口1返回


    private String receiveUid; //收款方：接口1返回的：payeeUid

    /**
     * type : 1
     * creatorUid : 288078455692595214
     * schoolId : 288069341826519040
     * id : 388483601173319681
     * status : 0
     */

    private String type;//1 支付宝，2 微信
    private String creatorUid;
    private String schoolId;
    private String id;
    private String status;
    private String createTime;
    private String week;
    private String monthDate;

    public String getReceiveUid() {
        return payeeUid;
    }

    public void setPayeeUid(String payeeUid){
        this.payeeUid = payeeUid;
        this.receiveUid = payeeUid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public String getPayUid() {
        return payUid;
    }

    public void setPayUid(String payUid) {
        this.payUid = payUid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCallUrl() {
        return callUrl;
    }

    public void setCallUrl(String callUrl) {
        this.callUrl = callUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setReceiveUid(String receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = null;
        try {
            date = sdf.parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        week = DateUtil.getWeekOfDate(date);
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonthDate(){
        if(createTime == null)
            return null;
        else
            return createTime.substring(5,11);
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public int getPic(){
        if("1".equals(type))
            return R.mipmap.zfb_pay;
        else  if("2".equals(type))
            return R.mipmap.wx_pay;
        return 0;
    }
}
