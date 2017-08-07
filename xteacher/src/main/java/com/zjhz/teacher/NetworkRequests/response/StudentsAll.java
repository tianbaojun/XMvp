package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询   所有年级班级的响应数据
 */
public class StudentsAll implements Serializable {


    /**
     * code : 0
     * data : [{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"10","classNo":"0110","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级100班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-06-15 10:40:37","updateUser":0},{"classId":"11","classNo":"0111","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级11班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:25","updateUser":1},{"classId":"12","classNo":"0112","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级12班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:25","updateUser":1},{"classId":"267350111284105216","createTime":"2016-07-04 09:28:48","createUser":2,"gradeId":"267083691308617728","name":"sssss班","schoolId":"1","status":"1","teacherId":"12","updateTime":"2016-07-05 08:26:07","updateUser":2},{"classId":"267350697110933504","createTime":"2016-07-04 09:31:08","createUser":2,"gradeId":"267083691308617728","name":"一年级（2）班","schoolId":"1","status":"1","teacherId":"267349700544303104","updateTime":"2016-07-07 08:31:14","updateUser":0}],"gradeId":"267083691308617728","name":"一年级","schoolId":"1","status":"1"},{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"13","classNo":"0201","createTime":"2016-05-10 16:50:51","createUser":1,"gradeId":"267083691379920896","name":"二年级1班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:51","updateUser":1},{"classId":"14","classNo":"0202","createTime":"2016-05-10 16:50:51","createUser":1,"gradeId":"267083691379920896","name":"二年级2班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:51","updateUser":1},{"classId":"15","classNo":"0203","createTime":"2016-05-10 16:50:51","createUser":1,"gradeId":"267083691379920896","name":"二年级3班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:51","updateUser":1}],"gradeId":"267083691379920896","name":"二年级","schoolId":"1","status":"1"},{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"25","classNo":"0301","createTime":"2016-05-10 16:51:27","createUser":1,"gradeId":"267083691400892416","name":"三年级1班","rowSort":1,"schoolId":"1","status":"1","teacherId":"25","updateTime":"2016-05-10 16:51:27","updateUser":1},{"classId":"26","classNo":"0302","createTime":"2016-05-10 16:51:27","createUser":1,"gradeId":"267083691400892416","name":"三年级2班","rowSort":1,"schoolId":"1","status":"1","teacherId":"26","updateTime":"2016-05-10 16:51:27","updateUser":1}],"gradeId":"267083691400892416","name":"三年级","schoolId":"1","status":"1"},{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"37","classNo":"0401","createTime":"2016-05-10 16:55:47","createUser":1,"gradeId":"267083691434446848","name":"四年级1班","rowSort":1,"schoolId":"1","status":"1","teacherId":"37","updateTime":"2016-05-10 16:55:47","updateUser":1},{"classId":"38","classNo":"0402","createTime":"2016-05-10 16:55:47","createUser":1,"gradeId":"267083691434446848","name":"四年级2班","rowSort":1,"schoolId":"1","status":"1","teacherId":"38","updateTime":"2016-05-10 16:55:47","updateUser":1},{"classId":"39","classNo":"0403","createTime":"2016-05-10 16:55:47","createUser":1,"gradeId":"267083691434446848","name":"四年级3班","rowSort":1,"schoolId":"1","status":"1","teacherId":"39","updateTime":"2016-05-10 16:55:47","updateUser":1}],"gradeId":"267083691434446848","name":"四年级","schoolId":"1","status":"1"},{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"49","classNo":"0501","createTime":"2016-05-10 16:59:31","createUser":1,"gradeId":"267083691463806976","name":"五年级1班","rowSort":1,"schoolId":"1","status":"1","teacherId":"49","updateTime":"2016-05-10 16:59:31","updateUser":1},{"classId":"50","classNo":"0502","createTime":"2016-05-10 16:59:31","createUser":1,"gradeId":"267083691463806976","name":"五年级2班","rowSort":1,"schoolId":"1","status":"1","teacherId":"50","updateTime":"2016-06-21 08:53:51","updateUser":0},{"classId":"51","classNo":"0503","createTime":"2016-05-10 16:59:31","createUser":1,"gradeId":"267083691463806976","name":"五年级3班","rowSort":1,"schoolId":"1","status":"1","teacherId":"51","updateTime":"2016-05-10 16:59:31","updateUser":1}],"gradeId":"267083691463806976","name":"五年级","schoolId":"1","status":"1"},{"createTime":"2016-07-03 15:50:08","createUser":134,"detail":[{"classId":"61","classNo":"0601","createTime":"2016-05-10 17:02:56","createUser":1,"gradeId":"267083691484778496","name":"六年级1班","rowSort":1,"schoolId":"1","status":"1","teacherId":"61","updateTime":"2016-06-23 08:48:43","updateUser":2}],"gradeId":"267083691484778496","name":"六年级","schoolId":"1","status":"1"},{"createTime":"2016-07-04 09:52:24","createUser":2,"detail":[{"classId":"267356191443652608","createTime":"2016-07-04 09:52:58","createUser":2,"gradeId":"267356049999138816","name":"test1","schoolId":"1","status":"1","teacherId":"104"},{"classId":"267798841120526336","createTime":"2016-07-05 15:11:53","createUser":2,"gradeId":"267356049999138816","name":"test3","schoolId":"1","status":"1","teacherId":"132","updateTime":"2016-07-05 15:22:39","updateUser":2},{"classId":"267799153701031936","createTime":"2016-07-05 15:13:08","createUser":2,"gradeId":"267356049999138816","name":"年级手机班","schoolId":"1","status":"1","teacherId":"133","updateTime":"2016-07-05 15:19:53","updateUser":2},{"classId":"268176275238031360","createTime":"2016-07-06 16:11:41","createUser":0,"gradeId":"267356049999138816","name":"初一","schoolId":"1","status":"1","teacherId":"91"}],"gradeId":"267356049999138816","name":"初中","schoolId":"1","status":"1"},{"createTime":"2016-07-05 14:50:35","createUser":2,"detail":[{"classId":"268425841438887936","createTime":"2016-07-07 08:43:22","createUser":0,"gradeId":"267793478681890816","name":"（1）班","schoolId":"1","status":"1","teacherId":"55","updateTime":"2016-07-07 14:01:12","updateUser":2},{"classId":"268506136611655680","createTime":"2016-07-07 14:02:26","createUser":2,"gradeId":"267793478681890816","name":"（2）班","schoolId":"1","status":"1","teacherId":"92"}],"gradeId":"267793478681890816","name":"高中","schoolId":"1","status":"1"}]
     * msg : 返回结果
     * total : 8
     */

    public int code;
    public String msg;
    public int total;
    /**
     * createTime : 2016-07-03 15:50:08
     * createUser : 134
     * detail : [{"classId":"10","classNo":"0110","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级100班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-06-15 10:40:37","updateUser":0},{"classId":"11","classNo":"0111","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级11班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:25","updateUser":1},{"classId":"12","classNo":"0112","createTime":"2016-05-10 16:50:25","createUser":1,"gradeId":"267083691308617728","name":"一年级12班","rowSort":1,"schoolId":"1","status":"1","teacherId":"2","updateTime":"2016-05-10 16:50:25","updateUser":1},{"classId":"267350111284105216","createTime":"2016-07-04 09:28:48","createUser":2,"gradeId":"267083691308617728","name":"sssss班","schoolId":"1","status":"1","teacherId":"12","updateTime":"2016-07-05 08:26:07","updateUser":2},{"classId":"267350697110933504","createTime":"2016-07-04 09:31:08","createUser":2,"gradeId":"267083691308617728","name":"一年级（2）班","schoolId":"1","status":"1","teacherId":"267349700544303104","updateTime":"2016-07-07 08:31:14","updateUser":0}]
     * gradeId : 267083691308617728
     * name : 一年级
     * schoolId : 1
     * status : 1
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String createTime;
        public int createUser;
        public String gradeId;
        public String name;
        public String schoolId;
        public String status;
        /**
         * classId : 10
         * classNo : 0110
         * createTime : 2016-05-10 16:50:25
         * createUser : 1
         * gradeId : 267083691308617728
         * name : 一年级100班
         * rowSort : 1
         * schoolId : 1
         * status : 1
         * teacherId : 2
         * updateTime : 2016-06-15 10:40:37
         * updateUser : 0
         */

        public List<DetailBean> detail;

        public static class DetailBean implements Serializable{
            public String classId;
            public String classNo;
            public String createTime;
            public int createUser;
            public String gradeId;
            public String name;
            public int rowSort;
            public String schoolId;
            public String status;
            public String teacherId;
            public String updateTime;
            public int updateUser;
        }
    }
}
