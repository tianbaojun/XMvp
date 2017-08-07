package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class RollCallbean implements Serializable {


    private String status;
    private List<SutList> stuList;

    public RollCallbean() {
        stuList = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SutList> getStuList() {
        return stuList;
    }

    public void setStuList(List<SutList> stuList) {
        this.stuList = stuList;
    }

    /**
     * studentId : 288114326579974151
     * name : 覃琳
     * optId : 401955842611482624
     * status : 0
     */
    public class SutList implements Serializable{

        private long studentId;
        private String name;
        private long optId;
        private String status;
        private String photoUrl;

        public long getStudentId() {
            return studentId;
        }

        public void setStudentId(long studentId) {
            this.studentId = studentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOptId() {
            return optId;
        }

        public void setOptId(long optId) {
            this.optId = optId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }

}

