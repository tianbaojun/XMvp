package com.zjhz.teacher.NetworkRequests.response;

import android.text.TextUtils;

import com.zjhz.teacher.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzd on 2017/5/16.
 */

public class PickUpStudentResBean implements Serializable {
    /**
     * applyId : 387794854798692352
     * applyPersonName : 汤楚琪
     * applyPersonRelation : BASE_PARENT_RELATION_2
     * applyShuttleTime : 2017-05-31 16:00:00
     * checkMsgs : [{"checkDesc":"班主任","checkMsgId":"387794858569371648","checkRole":"0","checkState":1,"checkTime":"2017-06-01 18:15:26","name":"章银平","schoolId":"288069341826519040"},{"checkDesc":"交接教师","checkMsgId":"387794858695200768","checkRole":"1","checkState":1,"checkTime":"2017-06-02 09:09:51","name":"陈国栋","schoolId":"288069341826519040"},{"checkDesc":"审核教师","checkMsgId":"387794858959441920","checkRole":"2","checkState":1,"checkTime":"2017-06-02 09:19:52","name":"杨妙","schoolId":"288069341826519040"},{"checkDesc":"审核教师","checkMsgId":"387794859055910912","checkRole":"2","checkState":0,"name":"梁棋","schoolId":"288069341826519040"},{"checkDesc":"审核教师","checkMsgId":"387794859118825472","checkRole":"2","checkState":1,"checkTime":"2017-06-02 09:31:06","name":"王岚","schoolId":"288069341826519040"},{"checkDesc":"审核教师","checkMsgId":"387794859177545728","checkRole":"2","checkState":1,"checkTime":"2017-06-02 09:31:28","name":"安全教师","schoolId":"288069341826519040"},{"checkDesc":"门卫放行","checkMsgId":"387794859236265984","checkRole":"3","checkState":0,"name":"门卫","schoolId":"288069341826519040"}]
     * classTeacherUser : 288078455667429377
     * createTime : 2017-06-01 18:13:14
     * createUser : 288114334880501778
     * flowId : 387336781990727680
     * handoverTeacherUser : 288078455709372436
     * haveCheckTimes : 3
     * haveCheckUsers : 288078455692595202,288078455692595210,288078455738732561
     * needCheckGroup :
     * needCheckTeacher : 288078455692595205
     * schoolId : 288069341826519040
     * shuttleCause : 培训奥数
     * shuttlePersonId : 377906999343452164
     * shuttlePersonName : 张三
     * shuttlePersonPhotoUrl : http://www.baidu.com
     * shuttlePersonRelation : 叔侄
     * state : 3
     * studentClassId : 288113366604451840
     * studentClassName : 一（1）班
     * studentId : 288114322939318285
     * studentName : 向榆汇
     * studentPhotoUrl : http://www.student.photo
     * term : 2016-2017-上
     * totalCheckTimes : 4
     */

    private String applyId;
    private String applyPersonName;
    private String applyPersonRelation;
    private String applyShuttleTime;
    private String classTeacherUser;
    private String createTime;
    private String createUser;
    private String flowId;
    private String handoverTeacherUser;
    private int haveCheckTimes;
    private String haveCheckUsers;
    private String needCheckGroup;
    private String needCheckTeacher;
    private String schoolId;
    private String shuttleCause;
    private String shuttlePersonId;
    private String shuttlePersonName;
    private String shuttlePersonPhotoUrl;
    private String shuttlePersonRelation;
    private int state;//申请信息状态：0,新增,1, 核实家长入校,2, 待交接,3, 审核中,4, 待离校,5, 已完结,6, 放弃执行,//未进入学校时放弃7, 未执行,8, 异常离校,9, 放弃执行;//进入学校后放弃
    private String studentClassId;
    private String studentClassName;
    private String studentId;
    private String studentName;
    private String studentPhotoUrl;
    private String term;
    private int totalCheckTimes;
    private String btnText;
    private List<CheckMsgsBean> checkMsgs;

    private String shuttlePersonPhone;

    private boolean isMeApproval;//自己是否审核
    private boolean isBZR = false;//是否班主任，班主任要选择交接老师

    public String getBtnText() {
        switch (state){
            case 0:
                btnText = "核准申请";
                break;
            case 1:
                btnText = "交接审核";
                break;
            case 2:
                btnText = "核实入校";
                break;
            case 3:
                btnText = "核准申请";
                break;
            case 4:
                btnText = "核实离校";
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
        }
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyPersonRelation() {
        return applyPersonRelation;
    }

    public void setApplyPersonRelation(String applyPersonRelation) {
        this.applyPersonRelation = applyPersonRelation;
    }

    public String getApplyShuttleTime() {
        return applyShuttleTime;
    }

    public void setApplyShuttleTime(String applyShuttleTime) {
        this.applyShuttleTime = applyShuttleTime;
    }

    public String getClassTeacherUser() {
        return classTeacherUser;
    }

    public void setClassTeacherUser(String classTeacherUser) {
        this.classTeacherUser = classTeacherUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getHandoverTeacherUser() {
        return handoverTeacherUser;
    }

    public void setHandoverTeacherUser(String handoverTeacherUser) {
        this.handoverTeacherUser = handoverTeacherUser;
    }

    public int getHaveCheckTimes() {
        return haveCheckTimes;
    }

    public void setHaveCheckTimes(int haveCheckTimes) {
        this.haveCheckTimes = haveCheckTimes;
    }

    public String getHaveCheckUsers() {
        return haveCheckUsers;
    }

    public void setHaveCheckUsers(String haveCheckUsers) {
        this.haveCheckUsers = haveCheckUsers;
    }

    public String getNeedCheckGroup() {
        return needCheckGroup;
    }

    public void setNeedCheckGroup(String needCheckGroup) {
        this.needCheckGroup = needCheckGroup;
    }

    public String getNeedCheckTeacher() {
        return needCheckTeacher;
    }

    public void setNeedCheckTeacher(String needCheckTeacher) {
        this.needCheckTeacher = needCheckTeacher;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getShuttleCause() {
        return shuttleCause;
    }

    public void setShuttleCause(String shuttleCause) {
        this.shuttleCause = shuttleCause;
    }

    public String getShuttlePersonId() {
        return shuttlePersonId;
    }

    public void setShuttlePersonId(String shuttlePersonId) {
        this.shuttlePersonId = shuttlePersonId;
    }

    public String getShuttlePersonName() {
        return shuttlePersonName;
    }

    public void setShuttlePersonName(String shuttlePersonName) {
        this.shuttlePersonName = shuttlePersonName;
    }

    public String getShuttlePersonPhotoUrl() {
        return shuttlePersonPhotoUrl;
    }

    public void setShuttlePersonPhotoUrl(String shuttlePersonPhotoUrl) {
        this.shuttlePersonPhotoUrl = shuttlePersonPhotoUrl;
    }

    public String getShuttlePersonRelation() {
        return shuttlePersonRelation;
    }

    public void setShuttlePersonRelation(String shuttlePersonRelation) {
        this.shuttlePersonRelation = shuttlePersonRelation;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(String studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getStudentClassName() {
        return studentClassName;
    }

    public void setStudentClassName(String studentClassName) {
        this.studentClassName = studentClassName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhotoUrl() {
        return studentPhotoUrl;
    }

    public void setStudentPhotoUrl(String studentPhotoUrl) {
        this.studentPhotoUrl = studentPhotoUrl;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getTotalCheckTimes() {
        return totalCheckTimes;
    }

    public void setTotalCheckTimes(int totalCheckTimes) {
        this.totalCheckTimes = totalCheckTimes;
    }

    public List<CheckMsgsBean> getCheckMsgs() {
        return checkMsgs;
    }

    public void setCheckMsgs(List<CheckMsgsBean> checkMsgs) {
        this.checkMsgs = checkMsgs;
    }

    public String getShuttlePersonPhone() {
        return shuttlePersonPhone;
    }

    public void setShuttlePersonPhone(String shuttlePersonPhone) {
        this.shuttlePersonPhone = shuttlePersonPhone;
    }

    public String getApplyString(){
        return studentName+"的"+shuttlePersonRelation+"申请"+
                applyShuttleTime+"接送"+studentName;
    }

    public String getShuttlePersonNameRelation(){
        return shuttlePersonName+"("+shuttlePersonRelation+")";
    }

    public String getStudentNameAndClass(){
        return studentName+"  "+studentClassName;
    }

    public boolean isMeApproval() {
        return isMeApproval;
    }

    public void setMeApproval(boolean meApproval) {
        isMeApproval = meApproval;
    }

    public boolean isBZR() {
        return isBZR;
    }

    public void setBZR(boolean BZR) {
        isBZR = BZR;
    }

    public static class CheckMsgsBean implements Serializable {
        /**
         * checkDesc : 班主任
         * checkMsgId : 387794858569371648
         * checkRole : 0
         * checkState : 1
         * checkTime : 2017-06-01 18:15:26
         * name : 章银平
         * schoolId : 288069341826519040
         */

        private String checkDesc;
        private String checkMsgId;
        private int checkRole;//0，班主任 1，交接教师 2，审核教师 3，门卫
        private int checkState;//0，待审核 1，已审核
        private String checkTime;
        private String name;
        private String schoolId;
        private int state;

        private String nameDesc;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCheckDesc() {
            return checkDesc;
        }

        public void setCheckDesc(String checkDesc) {
            this.checkDesc = checkDesc;
        }

        public String getCheckMsgId() {
            return checkMsgId;
        }

        public void setCheckMsgId(String checkMsgId) {
            this.checkMsgId = checkMsgId;
        }

        public int getCheckRole() {
            return checkRole;
        }

        public void setCheckRole(int checkRole) {
            this.checkRole = checkRole;
        }

        public int getCheckState() {
            return checkState;
        }

        public void setCheckState(int checkState) {
            this.checkState = checkState;
        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getNameDesc() {
            if(TextUtils.isEmpty(name)){
                return "暂无交接老师";
            }
            return name + "(" + checkDesc + ")";
        }

        public void setNameDesc(String nameDesc) {
            this.nameDesc = nameDesc;
        }

        public int getAuditPic() {
            if (checkState == 1) {//已审核
                switch (checkRole) {//0，班主任 1，交接教师 2，审核教师 3，门卫
                    case 0:
                        return R.mipmap.approval_bzr;
                    case 1:
                        return R.mipmap.approval_jjls;
                    case 2:
                        return R.mipmap.approval_shls;
                    case 3:
                        if(state == 2)
                            return R.mipmap.approval_yrx;
                        else if(state > 2)
                            return R.mipmap.approval_ylx;
                        return R.mipmap.approval_mw;
                }
            }
            if (checkState == 0) {//未审核
                switch (checkRole) {//0，班主任 1，交接教师 2，审核教师 3，门卫
                    case 0:
                        return R.mipmap.approval_ds;
                    case 1:
                        return R.mipmap.approval_djj;
                    case 2:
                        return R.mipmap.approval_ds;
                    case 3:
                        return R.mipmap.approval_dhs;
                }
            }
            return 0;
        }
    }


//    public TeacherType teacherType;
//    public boolean isApproval;//是否审核
//
//    public String term; //学期
//    public String realityShuttleTime; //实际接送时间
//    public String applyShuttleTime; //申请接送时间
//    public String studentName; //学生姓名
//    public String studentClassName; //所在班级
//    public String shuttlePersonName; //接送人姓名
//    public String shuttlePersonRelation; //接送人与学生的关系
//    public int state; //状态
//    public String applyPersonRelation; //申请人与学生的关系
//    public String createUser; //创建/申请人用户ID
//    public String applyPersonName; //申请人姓名
//    public String shuttleCause; //接送原因
//    public String studentPhotoUrl; //学生照片url
//    public String shuttlePersonPhoto_url; //接送人照片URL
//    public String checkMsgs; //教师、门卫们交接/审核/核对信息
//
//    public String phoneNum;//电话号码
//
//    public int getImgType(){
//        switch (teacherType){
//            case bzr:
//                return R.mipmap.bzr_complete;
//            case jjls:
//                return R.mipmap.jjls_complete;
//            case shls:
//                return R.mipmap.sh_complete;
//            case aqls:
//                return R.mipmap.sh_complete;
//            case mw:
//                return R.mipmap.mw_complete;
//        }
//        return R.mipmap.sh_complete;
//    }
//
//    public enum TeacherType{
//        bzr,//班主任
//        jjls,//交接老师
//        shls,//生活老师
//        aqls,//安全老师
//        mw//门卫
//    }
//
//    public String getApplyString(){
//        return studentName+shuttlePersonRelation+"申请"+
//                applyShuttleTime+"接送"+studentName;
//    }
//
//    public String getShuttlePersonNameRelation(){
//        return shuttlePersonName+"("+shuttlePersonRelation+")";
//    }
//
//    public String getStudentClassName(){
//        return studentName+"  "+studentClassName;
//    }
}
