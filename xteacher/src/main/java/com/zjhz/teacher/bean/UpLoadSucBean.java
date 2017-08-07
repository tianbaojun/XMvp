package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.net.URLDecoder;

/**
 * Created by zzd on 2017/4/26.
 */

public class UpLoadSucBean implements Serializable {

    /**
     * status : 0
            * code : 754845
            * uid : 288078455713566737
            * inSchoolId : 288069341826519040
            * msgId : 374660264408125440
            * fileType : 2
            * attPath : http://file.1000xyun.com:81/group1/M00/00/03/wKgDZ1kAIC6AVJFpAAA3S7R--WI27.docx
            * webId : 374660231352815616
            * __curentUser : 288078455713566737
            * attName : Android%E4%BC%98%E5%8C%96%E6%96%B9%E6%A1%88.docx
    * schoolId :
            * msgNature : 1
            * __curentSchool : 288069341826519040
            * type : 6
            */

    private String status;
    private String code;
    private String uid;
    private String inSchoolId;
    private String msgId;
    private String fileType;
    private String attPath;
    private String webId;
    private String __curentUser;
    private String attName;
    private String schoolId;
    private String msgNature;
    private String __curentSchool;
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInSchoolId() {
        return inSchoolId;
    }

    public void setInSchoolId(String inSchoolId) {
        this.inSchoolId = inSchoolId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getAttPath() {
        return attPath;
    }

    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }

    public String get__curentUser() {
        return __curentUser;
    }

    public void set__curentUser(String __curentUser) {
        this.__curentUser = __curentUser;
    }

    public String getAttName() {
        if (attName == null)
            return null;
//        try {
        attName = URLDecoder.decode(attName);
//            attName = new String(attName.getBytes("UTF-8"),"GB2312");
//            attName = new String(attName.getBytes("GB2312"));
//            attName = new String(utf8.getBytes(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getMsgNature() {
        return msgNature;
    }

    public void setMsgNature(String msgNature) {
        this.msgNature = msgNature;
    }

    public String get__curentSchool() {
        return __curentSchool;
    }

    public void set__curentSchool(String __curentSchool) {
        this.__curentSchool = __curentSchool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileFormat getFileFormat() {
        if (getAttName() == null)
            return FileFormat.NONE;
        int length = attName.length();
        if (attName.substring(length - 3, length).equals("doc") || attName.substring(length - 4, length).equals("docx"))
            return FileFormat.WORD;
        else if (attName.substring(length - 3, length).equals("xls") || attName.substring(length - 4, length).equals("xlsx"))
            return FileFormat.EXCEL;
        else if (attName.substring(length - 3, length).equals("ppt") || attName.substring(length - 4, length).equals("pptx"))
            return FileFormat.PPT;
        else if (attName.substring(length - 3, length).equals("pdf"))
            return FileFormat.EXCEL;
        else if (attName.substring(length - 3, length).equals("txt"))
            return FileFormat.TXT;
        return FileFormat.NONE;
    }

    public enum FileFormat {
        WORD,
        PPT,
        EXCEL,
        PDF,
        TXT,
        NONE
    }
}
