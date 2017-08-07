package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-15
 * Time: 15:57
 * Description: IM联系人部门列表返回数据
 */
public class SectionListResponse implements Serializable{


    /**
     * code : 0
     * data : [{"createTime":"2016-07-19 09:56:19","createUser":0,"deptCode":"1","deptId":"272792856349904896","deptName":"教导处","isLeaf":1,"level":1,"parentId":"0","schoolId":"271688204724211712","status":"1","updateTime":"2016-07-19 10:19:03","updateUser":0,"users":[{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601009","nickName":"冯利","phoneNo":"14511111119","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0E/CmUBaVeTAbWAOPA0AAAeeFl7t8s176.png?attname=touxiang1.png","schoolId":"271688204724211712","sourceId":"272484438401617920","sourceType":"1","status":"1","updateTime":"2016-07-23 14:36:36","updateUser":271688204749377536,"userId":"272484438405812224","userName":"14511111119"},{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601003","nickName":"何礼忠","phoneNo":"14511111113","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTDiOARXVvAAAcHtuXQfs666.png?attname=touxiang2.png","schoolId":"271688204724211712","sourceId":"272484438284177408","sourceType":"1","status":"1","updateTime":"2016-07-23 15:29:38","updateUser":272484438246428672,"userId":"272484438288371712","userName":"14511111113"},{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601013","nickName":"包小红","phoneNo":"14511111123","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTAd2ADxOlAAAdXE2s4MA439.png?attname=touxiang4.png","schoolId":"271688204724211712","sourceId":"272484438481309696","sourceType":"1","status":"1","updateTime":"2016-07-23 14:37:20","updateUser":271688204749377536,"userId":"272484438485504000","userName":"14511111123"}]}]
     * msg : 查询结果
     * total : 14
     */

    public int code;
    public String msg;
    public int total;
    /**
     * createTime : 2016-07-19 09:56:19
     * createUser : 0
     * deptCode : 1
     * deptId : 272792856349904896
     * deptName : 教导处
     * isLeaf : 1
     * level : 1
     * parentId : 0
     * schoolId : 271688204724211712
     * status : 1
     * updateTime : 2016-07-19 10:19:03
     * updateUser : 0
     * users : [{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601009","nickName":"冯利","phoneNo":"14511111119","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0E/CmUBaVeTAbWAOPA0AAAeeFl7t8s176.png?attname=touxiang1.png","schoolId":"271688204724211712","sourceId":"272484438401617920","sourceType":"1","status":"1","updateTime":"2016-07-23 14:36:36","updateUser":271688204749377536,"userId":"272484438405812224","userName":"14511111119"},{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601003","nickName":"何礼忠","phoneNo":"14511111113","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTDiOARXVvAAAcHtuXQfs666.png?attname=touxiang2.png","schoolId":"271688204724211712","sourceId":"272484438284177408","sourceType":"1","status":"1","updateTime":"2016-07-23 15:29:38","updateUser":272484438246428672,"userId":"272484438288371712","userName":"14511111113"},{"createTime":"2016-07-18 13:30:47","createUser":0,"deptId":"272792856349904896","jobNumber":"201601013","nickName":"包小红","phoneNo":"14511111123","photoUrl":"http://msg.51jxh.com:226/group1/M00/00/0F/CmUBaVeTAd2ADxOlAAAdXE2s4MA439.png?attname=touxiang4.png","schoolId":"271688204724211712","sourceId":"272484438481309696","sourceType":"1","status":"1","updateTime":"2016-07-23 14:37:20","updateUser":271688204749377536,"userId":"272484438485504000","userName":"14511111123"}]
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String createTime;
        public int createUser;
        public String deptCode;
        public String deptId;
        public String deptName;
        public int isLeaf;
        public int level;
        public String parentId;
        public String schoolId;
        public String status;
        public String updateTime;
        public int updateUser;
        /**
         * createTime : 2016-07-18 13:30:47
         * createUser : 0
         * deptId : 272792856349904896
         * jobNumber : 201601009
         * nickName : 冯利
         * phoneNo : 14511111119
         * photoUrl : http://msg.51jxh.com:226/group1/M00/00/0E/CmUBaVeTAbWAOPA0AAAeeFl7t8s176.png?attname=touxiang1.png
         * schoolId : 271688204724211712
         * sourceId : 272484438401617920
         * sourceType : 1
         * status : 1
         * updateTime : 2016-07-23 14:36:36
         * updateUser : 271688204749377536
         * userId : 272484438405812224
         * userName : 14511111119
         */

        public List<UsersBean> users;

        public static class UsersBean implements Serializable{
            public String createTime;
            public int createUser;
            public String deptId;
            public String jobNumber;
            public String nickName;
            public String phoneNo;
            public String photoUrl;
            public String schoolId;
            public String sourceId;
            public String sourceType;
            public String status;
            public String updateTime;
            public long updateUser;
            public String userId;
            public String userName;
        }
    }
}
