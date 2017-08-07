package com.zjhz.teacher.NetworkRequests;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description:  一些常量
 */
public class Config {

    public static final String TEACHER = "TEACHER";  // 代课老师
    public static final String TEACHERFLY = "TEACHERFLY";  // 代课老师
    public static final String ERROR = "error";  // 错误信息
    public static final String ERROR_JSON = "error_json";  // 错误信息
    public static final String NOSUCCESS = "noSuccess";  // 请求非成功
    public static final String LOGIN = "LOGIN";  // 登录
    public static final String LOGOUT = "LOGOUT";  // 登出
    /** 当前有效校历的配置信息*/
    public static final String SCHOOLCALENDARTIME = "SCHOOLCALENDARTIME";  // 当前有效校历的配置信息
    /** 校历事项列表*/
    public static final String SCHOOLEVENTITEM = "SCHOOLITEMEVENTLIST";  // 校历事项列表
    public static final String PERSONEVENT = "PERSONEVENT";  // 个人事项
    public static final String PERSONEVENT_SCROLL = "PERSONEVENT_SCROLL";  // 个人事项
    public static final String PERSONEVENTHOME = "PERSONEVENTHOME";  // 个人事项
    public static final String PERSONEVENTADD = "PERSONEVENTADD";  // 添加个人事项
    public static final String PERSONEVENTMODIFY = "PERSONEVENTMODIFY";  // 修改个人事项
    public static final String PERSONEVENTDELETE = "PERSONEVENTDELETE";  // 删除对应的个人事项
    public static final String MYSCHEDULE = "MYSCHEDULE";  // 我的课表
    public static final String MYSCHEDULEPAGING = "MYSCHEDULEPAGING";  // 我的课表分页
    public static final String MYSCHEDULEPAGING_NEW = "MYSCHEDULEPAGING_NEW";  // 我的课表分页(新)
    public static final String MYSCHEDULENAMEPAGING = "MYSCHEDULENAMEPAGING";  // 我的课表教师名称分页

    public static final String saveLocationFile = "saveLocationFile";

    /** 校外定位查询当前学生的位置*/
    public static final String SCHOOLROUTCURRENT = "SCHOOLROUTCURRENT";  // 校外定位查询当前学生的位置

    /** 校外定位学校初始位置*/
    public static final String SCHOOLINITLOCATION = "SCHOOLINITLOCATION";  // 校外定位学校初始位置

    /**根据年级班级获取学生列表 */
    public static final String RfidList = "RfidList";

    /**根据年级班级获取学生列表 */
    public static final String RFIDLIST_FLY = "RFIDLIST_FLY";

    /**根据年级班级获取学生列表 */
    public static final String RFIDLIST = "RFIDLIST";

    /**根据学生姓名和学籍号码查询学生信息 */
    public static final String SCHOOLSTUDENTNAME = "SCHOOLSTUDENTNAME";

    /**校外定位上学路径查询 */
    public static final String SCHOOLPATHUP = "SCHOOLPATHUP";

    /**校外定位放学路径查询 */
    public static final String SCHOOLPATHBACK = "SCHOOLPATHBACK";

    /**新增请假接口 */
    public static final String ADDLEAVE = "ADDLEAVE";

    /**驳回后修改请假内容接口 */
    public static final String MODIFYREJECTAPPLY = "MODIFYREJECTAPPLY";

    /**请假列表接口*/
    public static final String LEAVELIST = "LEAVELIST";

    /**请假详情接口*/
    public static final String LEAVEDETAIL = "LEAVEDETAIL";

    /**请假详情接口*/
    public static final String LEAVEDETAILSCHEDULE = "LEAVEDETAILSCHEDULE";

    /**检查用户是否有审批入口*/
    public static final String LEAVECHECK = "LEAVECHECK";
    /**检查用户是否有审批入口*/
    public static final String LEAVECHECKDETAILONE = "LEAVECHECKDETAILONE";

    /**得到待审批请假列表*/
    public static final String LEAVECHECKLIST = "LEAVECHECKLIST";

    /**审批请假详情*/
    public static final String LEAVECHECKDETAIL = "LEAVECHECKDETAIL";

    /**同意*/
    public static final String AGREE =  "AGREE";

    /**不同意*/
    public static final  String UNAGREE =  "UNAGREE";

    /**更多9宫*/
    public static final  String SCHOOLAPPFLY =  "SCHOOLAPPFLY";

    /**驳回之后重新提交请假申请*/
    public static final  String REJECTAPPLY =  "REJECTAPPLY";

    /**得到历史申请请假列表*/
    public static final  String LEAVEHISTORYAPPAY =  "LEAVEHISTORYAPPAY";

    /**得到历史审批请假列表*/
    public static final  String LEAVEHISTORY =  "LEAVEHISTORY";

    /**返回所有个人行事历的日期*/
    public static final  String PERSONCALENDEREVENT =  "PERSONCALENDEREVENT";

    /**返回所有个人行事历的日期*/
    public static final  String FLYPERSONCALENDEREVENT =  "FLYPERSONCALENDEREVENT";

    /**返回所有个人行事历的日期*/
    public static final  String PERSONCALENDEREVENTFLY =  "PERSONCALENDEREVENTFLY";

    /**部门名称和id文件名*/
    public static final  String DEPTNAMEANDID =  "DEPTNAMEANDID";
    /**部门名称和教师文件名*/
    public static final  String DEPTNAMTEACHER=  "DEPTNAMTEACHER";

    /**首页banner*/
    public static final  String HOMEBANNER =  "HOMEBANNER";

    /**首页banner*/
    public static final  String SCHOOLAPP =  "SCHOOLAPP";

    /**首页banner详情*/
    public static final  String HOMEBANNERDETAIL =  "HOMEBANNERDETAIL";

    /**增加作业评论*/
    public static final String HOMEWORKDISCUSSADD =  "HOMEWORKDISCUSSADD";

    /**作业评论列表*/
    public static final String HOMEWORKDISCUSSLIST =  "HOMEWORKDISCUSSLIST";
    /**作业评论列表*/
    public static final String HOMEWORKDISCUSSLISTS =  "HOMEWORKDISCUSSLISTS";

    /**删除评论*/
    public static final String HOMEWORKDISCUSSREMOVE =  "HOMEWORKDISCUSSREMOVE";

    /**删除评论*/
    public static final String HOMEWORKDISCUSSREMOVEDIS =  "HOMEWORKDISCUSSREMOVEDIS";

    /**作业点赞*/
    public static final String HOMEWORKLIKE =  "HOMEWORKLIKE";

    /**作业取消点赞*/
    public static final String HOMEWORKUNLIKE =  "HOMEWORKUNLIKE";

    /**作业点赞列表*/
    public static final String HOMEWORKLIST =  "HOMEWORKLIST";


    /**新闻列表*/
    public static final  String NEWSLIST =  "NEWSLIST";
    /**新闻列表点赞和回复数量改变*/
    public static final  String NEWSUPDATE =  "NEWSUPDATE";
    /**新闻详情*/
    public static final  String NEWSDETAIL =  "NEWSDETAIL";
    /**新闻回复列表*/
    public static final  String NEWSREPLAYLIST =  "NEWSREPLAYLIST";
    /**新闻回复*/
    public static final  String NEWSREPLAY =  "NEWSREPLAY";
    /**新闻点赞*/
    public static final  String NEWSPRAISE =  "NEWSPRAISE";
    /**新闻回复列表*/
    public static final  String NEWSREPLAYLISTs=  "NEWSREPLAYLISTs";
    /**食谱列表*/
    public static final  String FOODLIST =  "FOODLIST";
    /**食谱列表点赞和回复数量改变*/
    public static final  String FOODUPDATE =  "FOODUPDATE";
    /**食谱详情*/
    public static final  String FOODDETAIL =  "FOODDETAIL";
    /**食谱回复列表*/
    public static final  String FOODREPLAYLIST =  "FOODREPLAYLIST";
    /**食谱回复*/
    public static final  String FOODREPLAY =  "FOODREPLAY";
    /**食谱点赞*/
    public static final  String FOODPRAISE =  "FOODPRAISE";
    /**食谱发布*/
    public static final  String FOODRELEASE =  "FOODRELEASE";

    /**删除评论*/
    public static final  String DELETECOMMENT =  "DELETECOMMENT";
    /**新闻删除评论*/
    public static final  String NEWSDELETECOMMENT =  "NEWSDELETECOMMENT";
    /**食谱删除评论*/
    public static final  String FOODDELETECOMMENT =  "FOODDELETECOMMENT";

    /**外出公告列表*/
    public static final  String OUTGOINGLIST =  "OUTGOINGLIST";
    /**添加外出公告*/
    public static final  String ADDOUTGOING =  "ADDOUTGOING";

    /**我的报修申请列表*/
    public static final  String MYREPAIRLIST =  "MYREPAIRLIST";
    /**待审批报修列表*/
    public static final  String WAITREPAIRLIST =  "WAITREPAIRLIST";
    /**报修申请详情*/
    public static final  String REPAIRDETAIL =  "REPAIRDETAIL";
    /**报修申请审批*/
    public static final  String REPAIRapproval =  "REPAIRapproval";
    /**报修申请部门*/
    public static final  String REPAIRDEPT =  "REPAIRDEPT";
    /**报修申请重提详情*/
    public static final  String REREPAIRDETAIL =  "REREPAIRDETAIL";
    /**报修申请重提信息*/
    public static final  String REREPAIRresubmit =  "REREPAIRresubmit";
    /**报修申请重提确认*/
    public static final  String REREPAIRalignsubmit =  "REREPAIRalignsubmit";
    /**提交报修申请*/
    public static final  String REREPAIRRELEASE =  "REREPAIRRELEASE";
    /**提交报修申请*/
    public static final  String approveEntry =  "approveEntry";
    /**消息群发列表*/
    public static final  String MESSAGELIST =  "MESSAGELIST";
    /**会议通知列表*/
    public static final  String MEETINGLIST =  "MEETINGLIST";
    /**添加推送*/
    public static final  String ADDMESSAGE =  "ADDMESSAGE";
    /**发送老师对象列表*/
    public static final  String ADDMESSAGETEACHERLIST =  "ADDMESSAGETEACHERLIST";
    /**发送班级列表*/
    public static final  String ADDMESSAGEPARENTCLASSLIST =  "ADDMESSAGEPARENTCLASSLIST";
    /**发送通过班级获取家长列表*/
    public static final  String ADDMESSAGEPARENTLIST =  "ADDMESSAGEPARENTLIST";

    /**通知公告列表*/
    public static final  String announcementlist =  "announcementlist";
    /**通知公告详情*/
    public static final  String announcedetail =  "announcedetail";

    /**教师值日获取值日区间*/
    public static final  String currentDuty =  "currentDuty";
    /**教师值日*/
    public static final  String tearchduty_curr =  "tearchduty_curr";


    /**校内当前位置列表*/
    public static final  String currentlocation =  "currentlocation";

    /**校内区间位置列表*/
    public static final  String SideRoutelocation =  "SideRoutelocation";

    /**回望着纷乱的生活*/
    public static final  String LOOK =  "fly_fly";

    /**请假类型*/
    public static final  String LEAVETYPEFLY =  "LEAVETYPEFLY";

    /**请假类型驳回*/
    public static final  String LEAVETYPEFLYFLY =  "LEAVETYPEFLYFLY";

    /**代课老师*/
    public static final  String SUPPLYTEACHER =  "SUPPLYTEACHER";

    /**请假排课接口*/
    public static final  String LEAVE_ID_SCHEDULE =  "LEAVE_ID_SCHEDULE";

    /**个人信息*/
    public static final  String PersonInfo =  "PersonInfo";

    /**添加请假排课信息*/
    public static final  String LEAVE_TIPSAY_ADD =  "LEAVE_TIPSAY_ADD";

    /**添加请假排课信息*/
    public static final  String LEAVE_TIPSAY_CHECK_PAGE =  "LEAVE_TIPSAY_CHECK_PAGE";

    /**添加请假排课信息*/
    public static final  String LEAVE_TIPSAY_CHECK_PAGE_MODIFY =  "LEAVE_TIPSAY_CHECK_PAGE";

    /**学生*/
    public static final String STUDENT = "STUDENT";

    /**学生*/
    public static final String STUDENT_EDUCATION = "STUDENT_EDUCATION";

    /**个人德育*/
    public static final String PERSON_EDUCATION = "PERSON_EDUCATION";

    /**个人德育-筛选*/
    public static final String PERSON_EDUCATION_SCREEN = "PERSON_EDUCATION_SCREEN";

    /**个人德育-录入 根据学号查询对应的学生信息*/
    public static final String PERSON_EDUCATION_INPUTE = "PERSON_EDUCATION_INPUTE";

    /**个人德育-录入 根据班级查询对应的学生信息*/
    public static final String PERSON_EDUCATION_INPUTE_CLASS = "PERSON_EDUCATION_INPUTE_CLASS";

    /**个人德育-详情*/
    public static final String PERSON_EDUCATION_DETAIL = "PERSON_EDUCATION_DETAIL";

    /**获取年级班级的集合*/
    public static final String gradeClsList = "gradeClsList";

    /**获取年级班级的集合*/
    public static final String gradeClsList_clazz = "gradeClsList_clazz";

    /**获取年级班级的集合_德育*/
    public static final String gradeClsListedu = "gradeClsList_edu";

    /**个人德育专项列表*/
    public static final String PERSON_EDUCATION_SPECIAL_LIST = "PERSON_EDUCATION_SPECIAL_LIST";

    /**个人德育专项列表筛选*/
    public static final String PERSON_EDUCATION_SPECIAL_LIST_SCREEN = "PERSON_EDUCATION_SPECIAL_LIST_SCREEN";

    /**个人德育录入提交按钮*/
    public static final String PERSON_EDUCATION_INPUT_SUB = "PERSON_EDUCATION_INPUT_SUB";

    /**班级德育方案列表*/
    public static final String PERSON_EDUCATION_CLAZZ_PROJECT = "PERSON_EDUCATION_CLAZZ_PROJECT";

    /**班级德育主列表*/
    public static final String PERSON_EDUCATION_CLAZZ_PROJECT_LIST = "PERSON_EDUCATION_CLAZZ_PROJECT_LIST";

    /**班级德育主列表筛选*/
    public static final String PERSON_EDUCATION_CLAZZ_PROJECT_LIST_SCREEN = "PERSON_EDUCATION_CLAZZ_PROJECT_LIST_SCREEN";

    /**班级德育根据班级获取督导员*/
    public static final String RfidList_edu_class = "RfidList_edu_class";

    /**班级德育根据学号名称获取督导员*/
    public static final String SCHOOLSTUDENTNAME_EDUCLASS = "SCHOOLSTUDENTNAME_EDUCLASS";

    /**个人德育根据学号名称获取督导员*/
    public static final String SCHOOLSTUDENTNAME_PERCLASS = "SCHOOLSTUDENTNAME_PERCLASS";

    /**班级德育进入打分提交页面的列表*/
    public static final String EDUCLASS_SCORE =  "EDUCLASS_SCORE";

    /**班级德育进入打分提交页面的列表*/
    public static final String EDUCLASS_SCORE_MODIFY =  "EDUCLASS_SCORE_MODIFY";

    public static final String EDUCLASS_SCORE_DETAIL =  "EDUCLASS_SCORE_DETAIL";
    public static final String EDUCLASS_SCORE_DETAIL_UPDATE =  "EDUCLASS_SCORE_DETAIL_UPDATE";
    /**班级德育进入打分提交按钮  添加*/
    public static final String EDUCLASS_SCORE_SUB =  "EDUCLASS_SCORE_SUB";

    /**班级德育进入打分提交按钮  修改*/
    public static final String EDUCLASS_SCORE_SUB_MOD =  "EDUCLASS_SCORE_SUB_MOD";

    /**班级德育删除 批量*/
    public static final String EDUCLASS_SCORE_MODIFY_DELETE_ALL =  "EDUCLASS_SCORE_MODIFY_DELETE_ALL";

    /**班级德育删除 */
    public static final String EDUCLASS_SCORE_MODIFY_DELETE =  "EDUCLASS_SCORE_MODIFY_DELETE";

    /**个人德育删除 批量*/
    public static final String EDUPERSON_DELETE_ALL =  "EDUPERSON_DELETE_ALL";
    public static final String EDUCLASS_SCOREEmpty =  "EDUCLASS_SCOREEmpty";
    /**个人德育删除*/
    public static final String EDUPERSON_DELETE =  "EDUPERSON_DELETE";

    /**
     * 个人德育修改
     */
    public static final  String EDUPERSON_MODIFY = "EDUPERSON_MODIFY";

    /**
     * 德育信息统计，学期查询
     */
    public static final  String EDUCOUNTGETALLTERM = "EDUCOUNTGETALLTERM";
    /**
     * 德育信息
     */
    public static final  String EDUCOUNTGETCOUNT = "EDUCOUNTGETCOUNT";

    /**  获取年级信息 */
    public static final String gradeList = "gradeList";

    //删除群发消息
    public static final String DELETETOALLMSG = "DELETETOALLMSG";

    public static final String QUERYINITINSPECTOR = "QUERYINITINSPECTOR";

    public static final String BASE_URL = "base_url";


    public static final String SEPERATOR = "SEPERATOR";
    public static final String SHARE = "SHARE";
    public static final String DOCUMENT = "DOCUMENT";
    public static final String HONOUR = "HONOUR";

    //获取班级动态类型
    public static final String  MOMENTTYPELIST = "MOMENTTYPELIST";
    public static final String MOMENTLIST_ALL = "MOMENTLIST_ALL";
    public static final String MOMENTLIST_SHARE = "MOMENTLIST_SHARE";
    public static final String MOMENTLIST_DOC = "MOMENTLIST_DOC";
    public static final String MOMENTLIST_HONOUR = "MOMENTLIST_HONOUR";
    public static final String MOMENTSAVE = "MOMENTSAVE";
    public static final String DOCUMENTSAVE = "DOCUMENTSAVE";
    public static final String HONOURSAVE = "HONOURSAVE";
    public static final String GETCODE = "GETCODE";
    public static final String SHOWCOMMENTDIALOG = "SHOWCOMMENTDIALOG";
    public static final String COMMENT = "COMMENT";
    public static final String PRAISE = "PRAISE";
    public static final String CANCEL = "CANCEL";
    public static final String PRAISELIST = "PRAISELIST";
    public static final String REFRESHMOMENTLIST = "REFRESHMOMENTLIST";
    public static final String SHOWCOMMENTDIALOGINLIST = "SHOWCOMMENTDIALOGINLIST";
    public static final String SENDCOMMENT = "SENDCOMMENT";
    public static final String COMMENTLIST = "COMMENTLIST";
    public static final String SECTIONDIC = "SECTIONDIC";
    public static final String SEARCHLIST = "SEARCHLIST";
    public static final String GOT_UPLOAD_URL = "GOT_UPLOAD_URL";
    public static final String HIDDENINPUTWINDOW = "HIDDENINPUTWINDOW";

    public static final String LOWEST_VERSION = "lowest_version";

    public static final String UPLOAD_SUC = "upload_suc";
    public static final String REFRESHLASTEST = "REFRESHLASTEST";

    public static final String CREATE_ORDER = "create_order";
    public static final String ORDER_PAY = "order_pay";
    public static final String PAY_LIST = "pay_list";
    public static final String PAY_LIST_ORDER_NO = "pay_list_order_no";


    public static final String COMPANYNEWSREPLYDTL = "COMPANYNEWSREPLYDTL";
    public static final String COMPANYNEWSADDNEWMSGREPLY = "COMPANYNEWSADDNEWMSGREPLY";
    public static final String COMPANYNEWSREPLYSERVICELIST = "COMPANYNEWSREPLYSERVICELIST";
    public static final String COMPANYNEWSPRAISEOCANCEL = "COMPANYNEWSPRAISEOCANCEL";
    public static final String COMPANYNEWSREMOVEREPLY = "COMPANYNEWSREMOVEREPLY";


    public static final String TERMAPPRAISElISTSTUDENTID = "TERMAPPRAISElISTSTUDENTID";
    public static final String STUDYRECORDCOUNT = "STUDYRECORDCOUNT";
    public static final String TERMAPPRAISEADD = "TERMAPPRAISEADD";
    public static final String APPRAISEDEFAULTLIST = "APPRAISEDEFAULTLIST";
    public static final String TERMAPPRAISETYPELIST = "TERMAPPRAISETYPELIST";
    public static final String TERMLIST = "TERMLIST";
    public static final String PICK_UP_SEARCHER_ISDONE = "PICK_UP_SEARCHER_ISDONE";


    public static final String QUERY_STUDENT_COURSE_ADMIN = "QUERY_STUDENT_COURSE_ADMIN";
    public static final String COURSE_HISTORY_ADMIN = "COURSE_HISTORY_ADMIN";
    public static final String COURSE_HISTORY_BZR = "COURSE_HISTORY_BZR";
    public static final String QUERY_COURSE_ADMIN = "QUERY_COURSE_ADMIN";
    public static final String COURSE_DTL_ADMIN = "COURSE_DTL_ADMIN";
    public static final String KTJL = "KTJL";
    public static final String COURSE_HISTORY_S_BZR = "COURSE_HISTORY_S_BZR";
    public static final String XK_XSXK_ADMIN = "XK_XSXK_ADMIN";
    public static final String SELECTINFO = "SELECTINFO";
    public static final String COURSE_RK = "COURSE_RK";
    public static final String ROLLCALLlIST = "ROLLCALLlIST";
    public static final String SCORElIST = "SCORElIST";
    public static final String ROLLCALLlISTSUB = "ROLLCALLlISTSUB";
    public static final String SCORElISTSUB = "SCORElISTSUB";
    public static final String KTJLWEEK = "KTJLWEEK";
    public static final String COURSE_HISTORY_BZR_WEEK = "COURSE_HISTORY_BZR_WEEK";
    public static final String ISLEADER = "ISLEADER";
    public static final String ADDDUTYAFFAIRS = "ADDDUTYAFFAIRS";
}
