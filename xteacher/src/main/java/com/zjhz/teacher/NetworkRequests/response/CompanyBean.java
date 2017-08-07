package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class CompanyBean implements Serializable{
    private  String company_zh;
    private  String company_mail;
    private  String company_address;
    private  String home_title;
    private String tel;
    private String contact;
    private String lock_times;
    private String restrict_login_mode;
    private String copyright;
    private String legal_representative;
    private String default_password;
    private String company_en;
    private String post;
    private String fax;

    public String getCompany_zh() {
        return company_zh;
    }

    public void setCompany_zh(String company_zh) {
        this.company_zh = company_zh;
    }

    public String getCompany_mail() {
        return company_mail;
    }

    public void setCompany_mail(String company_mail) {
        this.company_mail = company_mail;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getHome_title() {
        return home_title;
    }

    public void setHome_title(String home_title) {
        this.home_title = home_title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLock_times() {
        return lock_times;
    }

    public void setLock_times(String lock_times) {
        this.lock_times = lock_times;
    }

    public String getRestrict_login_mode() {
        return restrict_login_mode;
    }

    public void setRestrict_login_mode(String restrict_login_mode) {
        this.restrict_login_mode = restrict_login_mode;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLegal_representative() {
        return legal_representative;
    }

    public void setLegal_representative(String legal_representative) {
        this.legal_representative = legal_representative;
    }

    public String getDefault_password() {
        return default_password;
    }

    public void setDefault_password(String default_password) {
        this.default_password = default_password;
    }

    public String getCompany_en() {
        return company_en;
    }

    public void setCompany_en(String company_en) {
        this.company_en = company_en;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
