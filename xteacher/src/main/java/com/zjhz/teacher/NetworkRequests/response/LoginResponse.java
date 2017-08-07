package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/21.
 */
public class LoginResponse implements Serializable {

    /**
     * code : 0
     * data : {"TimeOut":14400,"loginTime":1469078985376,"nickName":"周跃宗","os":"app","phoneNo":"13819671044","photoUrl":"http://ww1.sinaimg.cn/crop.3.45.1919.1919.1024/6b805731jw1em0hze051hj21hk1isn5k.jpg","roles":[{"roleId":"259182261381894144","roleName":"教师","roleType":1}],"schoolId":"1","schoolName":"哲商","sourceId":"24","sourceType":"1","token":"5e59dcd46527b81ba9f7a9ae062b4381","userId":"24","userName":"zyz"}
     * msg : 登录成功！
     * total : 1
     */

    public int code;
    /**
     * TimeOut : 14400
     * loginTime : 1469078985376
     * nickName : 周跃宗
     * os : app
     * phoneNo : 13819671044
     * photoUrl : http://ww1.sinaimg.cn/crop.3.45.1919.1919.1024/6b805731jw1em0hze051hj21hk1isn5k.jpg
     * roles : [{"roleId":"259182261381894144","roleName":"教师","roleType":1}]
     * schoolId : 1
     * schoolName : 哲商
     * sourceId : 24
     * sourceType : 1
     * token : 5e59dcd46527b81ba9f7a9ae062b4381
     * userId : 24
     * userName : zyz
     */

    public DataBean data;
    public String msg;
    public int total;

    public static class DataBean implements Serializable {
        public int TimeOut;
        public long loginTime;
        public String nickName;
        public String os;
        public String phoneNo;
        public String photoUrl;
        public String schoolId;
        public String schoolName;
        public String sourceId;
        public String sourceType;
        public String token;
        public String userId;
        public String userName;
        /**
         * roleId : 259182261381894144
         * roleName : 教师
         * roleType : 1
         */

        public List<RolesBean> roles;

        public static class RolesBean implements Serializable {
            public String roleId;
            public String roleName;
            public int roleType;
        }
    }
}
