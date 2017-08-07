package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/28.
 */
public class HomeListResponse implements Serializable{


    /**
     * msg : 查询到16条记录!
     * code : 0
     * total : 16
     * data : [{"appIcon":"http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTESSAfjmbAAAQK3jHgAE803.png?attname=14.png","linkId":"274251411372380160","createTime":"2016-07-18 11:29:27","appName":"个人行事历","appId":"1","schoolId":"271688204724211712","updateUser":272519939070365696,"createUser":1,"updateTime":"2016-07-23 15:42:29","webIcon":"http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeQGlOAGuc-AAAR2rMLam4844.png?attname=%E4%B8%AA%E4%BA%BA%E8%A1%8C%E4%BA%8B%E5%8E%86.png","status":"1"}]
     */

    public String msg;
    public int code;
    public int total;
    /**
     * appIcon : http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTESSAfjmbAAAQK3jHgAE803.png?attname=14.png
     * linkId : 274251411372380160
     * createTime : 2016-07-18 11:29:27
     * appName : 个人行事历
     * appId : 1
     * schoolId : 271688204724211712
     * updateUser : 272519939070365696
     * createUser : 1
     * updateTime : 2016-07-23 15:42:29
     * webIcon : http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeQGlOAGuc-AAAR2rMLam4844.png?attname=%E4%B8%AA%E4%BA%BA%E8%A1%8C%E4%BA%8B%E5%8E%86.png
     * status : 1
     * Icon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfaylmAHZuIAAAQBiGbZ0A497.png?attname=CmUBaVeQGomAH4QMAAAQBiGbZ0A181.png","code":"hytz","linkId":"288357754072993792","appName":"会议通知","appId":"285157159728058368","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1faynuARmw3AAASy-Feu8g036.png?attname=CmUBaVeTEVmAUDf4AAASy-Feu8g748.png","rowSort":2,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfay7SASu4ZAAANXcr2eM0765.png?attname=CmUBaVeQHkmAIZ6pAAANXcr2eM0059.png","code":"xskq","linkId":"288357719809724416","appName":"学生考勤","appId":"285157159904219136","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1fay9aASpBtAAAPfyVBgDA941.png?attname=CmUBaVeTEj-ANR6PAAAPfyVBgDA003.png","rowSort":3,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfayuyAZLr-AAANQpe2vN4107.png?attname=CmUBaVeQHYWAOB1GAAANQpe2vN4414.png","code":"xlgl","linkId":"288357620048203776","appName":"校历管理","appId":"285157159811944448","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1fayw2AbDOwAAASmkQ_eUc038.png?attname=CmUBaVeTEf-AF38RAAASmkQ_eUc371.png","rowSort":4,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfayj-APU4zAAAR2rMLam4849.png?attname=CmUBaVeQGlOAGuc-AAAR2rMLam4844.png","code":"grxs","linkId":"288357642546450432","appName":"个人行事历","appId":"285157159719669760","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1fayXuANKPhAAAQK3jHgAE875.png?attname=CmUBaVeTESSAfjmbAAAQK3jHgAE803.png","rowSort":5,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfay9OAE1tuAAAPIzDRqu8743.png?attname=CmUBaVeQHnuAcVg-AAAPIzDRqu8984.png","code":"zyck","linkId":"288357779809243136","appName":"作业查看","appId":"285163511330377728","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1fay_OAXSoGAAAQ-wShAAA839.png?attname=CmUBaVeTEmCAIQqQAAAQ-wShAAA874.png","rowSort":6,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfayqWAEPg5AAAPZKArMBM166.png?attname=CmUBaVeQHM2AbOnvAAAPZKArMBM061.png","code":"qjgl","linkId":"288357683369611264","appName":"请假管理","appId":"285157159753224192","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1faysSAWT2tAAATRph0stk622.png?attname=CmUBaVeTEYGAG8gGAAATRph0stk838.png","rowSort":7,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfayoqAKkLQAAAPHpeD4Hk394.png?attname=CmUBaVeQHO6AHb7JAAAPHpeD4Hk498.png","code":"qfxx","linkId":"288357621218414592","appName":"群发消息","appId":"285157159765807104","schoolId":"288069341826519040","terminalId":"273249080069591040","webIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBe1fayqeAG3_WAAAV0iivaKc692.png?attname=CmUBaVeTEY2AbZHAAAAV0iivaKc602.png","rowSort":8,"status":"1"},{"appIcon":"http:\/\/www.1000xyun.com:80\/group1\/M00\/00\/1F\/CmUBelfay8KAOjqyAAAOOrCKpko021.png?attname=CmUBaVeQHl6AXQ1EAAAOOrCKpko998.png","code":"zrgl","linkId":"288357742597378048","ap
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String appIcon;
        public String linkId;
        public String createTime;
        public String appName;
        public String appId;
        public String code;
        public String schoolId;
        public long updateUser;
        public int createUser;
        public String updateTime;
        public String webIcon;
        public String status;
    }
}
