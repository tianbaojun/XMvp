package com.zjhz.teacher.NetworkRequests;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:  网络接口请求链接
 */
public class CommonUrl {
    //生产环境
//    public final static String BASEURL ="http://www.1000xyun.com/api";
//    public final static String loadImageUrl = "http://www.1000xyun.com/mupload";//上传图片路径
//
    //测试环境
//    public final static String BASEURL ="http://test.1000xyun.com:81/api";
//    public final static String loadImageUrl = "http://test.1000xyun.com:81/mupload";//上传图片路径
    //开发环境

//    public final static String loadImageUrl = "http://eduyunxiao.com/mupload";//上传图片路径/

    /**
     * 演示
     */
//    public final static String BASEURL = "http://www.eduxiaoyuan.com/api";
//    public final static String loadImageUrl = "http://edu.51jxh.com:2222/mupload";//上传图片路径

    public final static int PAGE_SIZE = 20;

    /**
     * 登录
     */
    public final static String login = "OrgAuthService.login";
    /**
     * 退出
     */
    public final static String logOut = "OrgAuthService.logout";
    /**
     * 修改密码
     */
    public final static String updataPwd = "OrgUserService.changePwd";
    /**
     * 新闻列表
     */
    public final static String newsUrl = "OaNewsService.list";
    /**
     * 新闻点赞
     */
    public final static String newsPraise = "OaNewsService.addNewsPraise";
    /**
     * 新闻回复
     */
    public final static String newsReply = "OaNewsService.addNewReply";
    /**
     * 新闻详情
     */
    public final static String newsDetail = "OaNewsService.dtl";
    /**
     * 新闻详情
     */
    public final static String newsImageDetail = "OANewsImagesService.dtl";
    /**
     * 新闻回复列表
     */
    public final static String newsReplyList = "OaNewsService.listNewReply";
    /**
     * 删除新闻评论
     */
    public final static String removeNewReply = "OaNewsService.removeNewReply";
    /**
     * 食谱回复列表
     */
    public final static String foodReplyList = "OaCookBookService.listCookBookReply";
    /**
     * 食谱列表
     */
    public final static String foodList = "OaCookBookService.list";
    /**
     * 食谱详情
     */
    public final static String MENUDETAIL = "OaCookBookService.dtl";
    /**
     * 食谱点赞
     */
    public final static String foodPraise = "OaCookBookService.addCookBookPraise";
    /**
     * 食谱回复
     */
    public final static String foodReplay = "OaCookBookService.addCookBookReply";
    /**
     * 食谱添加
     */
    public final static String FOODADD = "OaCookBookService.add";
    /**
     * 删除食谱评论
     */
    public final static String removeCookBookReply = "OaCookBookService.removeCookBookReply";
    /**
     * 通知公告列表
     */
    public final static String announcementList = "OaNewsService.list";
    /**
     * 通知公告详情
     */
    public final static String announcementDetail = "OaNewsService.dtl";
    /**
     * 外出公告列表
     */
    public final static String outgoingList = "OaAnnouncementService.list";
    /**
     * 外出公告发布
     */
    public final static String outgoingAdd = "OaAnnouncementService.add";
    /**
     * 作业管理列表
     */
    public final static String homeworkList = "TeachHomeworkService.list";
    /**
     * 作业管理科目列表
     */
    public final static String homeworkSubjectList = "BaseSubjectService.list";
    /**
     * 作业管理班级列表
     */
    public final static String homeworkClassesList = "BaseClassService.listByTeacher";
     /**
     * 作业管理删除
     */
    public final static String homeworkRemove = "TeachHomeworkService.remove";
    /**
     * 作业管理发布
     */
    public final static String homeworkPublish = "TeachHomeworkService.publish";
    /**
     * 修改后作业管理发布
     */
    public final static String modifyPublish = "TeachHomeworkService.modifyPublish";
    /**
     * 作业管理保存
     */
    public final static String homeworkSave = "TeachHomeworkService.save";
    /**
     * 作业管理修改保存
     */
    public final static String homeworkmodifySave = "TeachHomeworkService.modifySave";
    /**
     * 值日值周配置
     */
    public final static String currentDuty = "SchoolOndutyService.getCurrentDuty";
    /**
     * 是否是值日领导
     */
    public final static String ISLEADER = "SchoolOndutyService.isDutyLeader";
    /**
     * 添加值日事项
     */
    public final static String ADDDUTYAFFAIRS = "SchoolOndutyService.addDutyDayAffairs";
    /**
     * 修改值日事项
     */
    public final static String MODIFYAFFAIRS = "SchoolOndutyService.modifyAffairs";
    /**
     * 删除值日事项
     */
    public final static String REMOVEAFFARIS = "SchoolOndutyService.removeAffairs";
    /**
     * 值日事项列表
     */
    public final static String AffairList = "SchoolOndutyService.dayAffairLists";
    /**
     * 值日值周具体每周的信息
     */
    public final static String currentDutyData = "SchoolOndutyWeekService.listDutyAllWeekByTime";
    /**
     * 当前有效校历的配置信息
     */
    public final static String SCHOOLCALENDARTIME = "SchoolCalendarService.currentCalendar";
    /**
     * 校历事项列表
     */
    public final static String SCHOOLEVENTITEM = "SchoolCalendarService.listSchedule";
    /**
     * 个人事项
     */
    public final static String PERSONEVENT = "OACalendarService.listAllByDay";
    /**
     * 添加个人事项
     */
    public final static String PERSONEVENTADD = "OACalendarService.add";
    /**
     * 修改个人事项
     */
    public final static String PERSONEVENTMODIFY = "OACalendarService.modify";
    /**
     * 删除对应的个人事项
     */
    public final static String PERSONEVENTDELETE = "OACalendarService.remove";
    /**
     * 我的课表
     */
    public final static String MYSCHEDULE = "TeachSchduleService.listDetailByUser";
    /**
     * 根据教师id，查询其他老师的课表
     */
    public final static String otherSchdule = "TeachSchduleService.listDetail";
    /**
     * 我的课表|详情分页查询
     */
    public final static String MYSCHEDULEPAGING = "TeachSchduleService.listDetail";
    /**
     * 我的课表|详情分页查询
     */
    public final static String MYSCHEDULEPAGING_NEW = "BaseTeacherService.list";
    /**
     * 我的课表教师名称分页查询
     */
    public final static String MYSCHEDULENAMEPAGING = "BaseTeacherService.list";
    /**
     * 获取年级班级集合信息
     */
    public final static String gradeClsList = "BaseGradeService.listGradeCls";
    /**
     * 获取年级集合信息
     */
    public final static String gradeList = "BaseGradeService.list";
    /**
     * 根据班级年级获取学生列表信息
     */
    public final static String RfidList = "BaseStudentService.list";
    /**
     * 德育根据学生学号获取学生列表信息
     */
    public final static String MORALEDUCATIONSTUDENTLIST = "BaseStudentService.listByCertificateId";
    /**
     * 考勤列表
     */
//    public final static String AttendanceList =  "SchoolRfidLocationService.checkWorkAttendance";
    public final static String AttendanceList = "SchoolRfidLocationService.classCheckWorkAttendance";
    /**
     * 校外定位查询当前学生的位置
     */
    public final static String SCHOOLROUTCURRENT = "SchoolRfidLocationService.outCurrentLocation";
    /**
     * 校外定位 学校初始位置
     */
    public final static String SCHOOLINITLOCATION = "SysSchoolService.dtl";
    /**
     * 校内当前位置
     */
//    public final static String schoolcurrentlocation  =  "SchoolRfidLocationService.inCurrentLocation";
    public final static String schoolcurrentlocation = "SchoolRfidLocationService.latelyLocation";
    /**
     * 校内设备
     */
    public final static String schoolrfidList = "SchoolRfidDeviceService.SchoolRfidDeviceList";
    /**
     * 校内定位上午、下午路径查询
     */
    public final static String inSideRouteList = "SchoolRfidLocationService.inSideRoute";
    /**
     * 根据学生姓名和学籍号码查询学生信息
     */
    public final static String SCHOOLSTUDENTNAME = "BaseStudentService.list";
    /**
     * 校外定位上学放学路径查询
     */
    public final static String SCHOOLPATH = "SchoolRfidLocationService.outSideRoute";
    /**
     * 检查用户是否有审批入口
     */
    public final static String approve = "SchoolRepairService.approveCheckEntry";
    //报修撤回
    public static final String REPAIR_REVOKE = "SchoolRepairService.recallTask";
    //报修撤回
    public static final String REPAIR_DELETE = "SchoolRepairService.deleteTask";
    /**
     * 当前用户报修列表信息
     */
    public final static String repairList = "SchoolRepairService.repairList";
    /**
     * 待审批报修列表
     */
    public final static String approveList = "SchoolRepairService.approveList";
    /**
     * 审批历史报修列表
     */
    public final static String HistoryList = "SchoolRepairService.approveHistoryList";
    /**
     * 部门列表
     */
    public final static String Deptlist = "OrgDeptService.queryForDept";
    /**
     * 新增请假接口
     */
    public final static String ADDLEAVE = "OAHolidayService.add";
    /**
     * 请假列表接口
     */
    public final static String LEAVELIST = "OAHolidayService.holidayList";
    /**
     * 请假详情接口
     */
    public final static String LEAVEDETAIL = "OAHolidayService.holidayDtl";
    /**
     * 检查用户是否有审批入口
     */
    public final static String LEAVECHECK = "OAHolidayService.approveCheckEntry";
    /**
     * 得到待审批请假列表
     */
    public final static String LEAVECHECKLIST = "OAHolidayService.approveList";
    /**
     * 审批请假详情
     */
    public final static String LEAVECHECKDETAIL = "OAHolidayService.approveDtlFlow";
//    OrgDeptService.queryForDept //OrgDeptService.list
    /**
     * 同意
     */
    public final static String AGREE = "OAHolidayService.executeTask";
    /**
     * 不同意
     */
    public final static String UNAGREE = "OAHolidayService.rejectTask";
    /**
     * 驳回之后重新提交请假申请
     */
    public final static String REJECTAPPLY = "OAHolidayService.alignTask";
//    public final static String LEAVEDETAIL =  "OAHolidayService.approveDtlFlow";
    /**
     * 驳回之后修改请假申请
     */
    public final static String MODIFYREJECTAPPLY = "OAHolidayService.modify";
    /**
     * 得到历史审批请假列表
     */
    public final static String LEAVEHISTORY = "OAHolidayService.approveHistoryList";
//    public final static String LEAVEDETAIL =  "OAHolidayService.approveDtlFlow";
    /**
     * 报修添加接口
     */
    public final static String add = "SchoolRepairService.add";
    /**
     * 提交报修申请接口
     */
    public final static String submit = "SchoolRepairService.submit";
    /**
     * 报修修改重新提交接口
     */
    public final static String modify = "SchoolRepairService.modify";
    /**
     * 驳回之后重新提交报修申请
     */
    public final static String alignTask = "SchoolRepairService.alignTask";
    /**
     * 报修人获取详情
     */
    public final static String repairDtl = "SchoolRepairService.repairDtl";
    /**
     * 审批人获取报修详情
     */
    public final static String approveDtlFlowDtl = "SchoolRepairService.approveDtlFlow";
    /**
     * 退回报修
     */
    public final static String return_repair = "SchoolRepairService.rejectTask";
    /**
     * 审批报修
     */
    public final static String executeTask_repair = "SchoolRepairService.executeTask";
    /**
     * 返回所有个人行事历的日期
     */
    public final static String PERSONCALENDEREVENT = "OACalendarService.listFlagByMonth";
    /**
     * 首页banner
     */
    public final static String HOMEBANNER = "OANewsImagesService.list";    // page":"1","pageSize":"4"
    /**
     * 首页banner新闻详情
     */
    public final static String HOMEBANNERDETAIL = "OANewsImagesService.dtl";    // "imageId":"4545"
    /**
     * 部门列表
     */
    public final static String teacherList = "OrgDeptService.queryAllDeptPerson";
    /**
     * 根据班级查询家长信息接口
     */
    public final static String listByClassId = "BaseParentService.listByClassId";
    /**
     * 根据学校查询应用
     */
//    public final static String SCHOOLAPP =  "SysAppSchoolService.listBySchoolId";    // "inSchoolId":"2"
    public final static String SCHOOLAPP = "SysLinkTerminalAppService.listAppByCode";    // "inSchoolId":"2"
    /**
     * 会议通知 推送，信息，站内通知
     */
//    public final static String sendMessageByAlias =  "OaMeetingService.sendByAlias";
    //合并群发借口  2016-10-14
    public final static String sendMessageByAlias = "PushService.sendByAlias";

    public final static String SETREMOVE = "PushLinkService.setRemove";
    /**
     * 发出群发消息列表查询接口
     */
    /*会议通知*/
    public final static String MessageList = "PushLinkService.setListForApp";
    /**
     * 查询收到的会议通知
     */
    public final static String schoollist = "PushLinkService.list";
    /**
     * 增加作业评论
     */
    public final static String HOMEWORKDISCUSSADD = "TeachHomeworkService.addHomeworkReply";
    /**
     * 删除作业评论
     */
    public final static String HOMEWORKDISCUSSREMOVE = "TeachHomeworkService.removeHomeworkReply";
    /**
     * 作业评论列表
     */
    public final static String HOMEWORKDISCUSSLIST = "TeachHomeworkService.listHomeworkReply";
//    /**
//     * 群发消息 推送，信息，站内通知f
//     */
//    public final static String sendByAlias = "PushService.sendByAlias";
    /**
     * 作业点赞
     */
    public final static String HOMEWORKLIKE = "TeachHomeworkService.addHomeworkPraise";
    /**
     * 作业取消点赞
     */
    public final static String HOMEWORKUNLIKE = "TeachHomeworkService.removeHomeworkPraise";
    /**
     * 作业点赞列表
     */
    public final static String HOMEWORKLIST = "TeachHomeworkService.listHomeworkPraise";
    /**
     * 查询用户未读的会议通知和消息数量
     */
    public final static String unReadNum = "PushLinkService.unReadMsgCount";

//    /** 查询收到的会议通知*/ 取消此接口
//    public final static String meetLinkList  =  "OaMeetingService.meetLinkList";

    /**查询收到群发消息接口*/
    /**
     * 请假管理获取请假类型的接口
     */
    public final static String LEAVETYPEFLY = "SysDictService.parentKeyTochilds";  // "paramKeys": "OA_HOLIDAY_TYPE"
    /**
     * 代课老师的接口
     */
    public final static String SUPPLYTEACHER = "OAHolidayService.listTeacherNotSchduleByWeekAndClazz";  // startTime， endTime
    /**
     * 判断是否有极光，短信推送
     */
    public final static String listPush = "SysSchoolPushService.listPushDistinct"; // "paramKeys": "OA_HOLIDAY_TYPE"
    /**
     * 个人信息
     */
    public final static String PersonInfo = "BaseTeacherService.dtl";
    /**
     * 根据时间id获取老师的课表 请假专用
     */
    public final static String LEAVE_ID_SCHEDULE = "TeachSchduleService.listByTime";
    /**
     * 添加请假排课信息 集合
     */
    public final static String LEAVE_TIPSAY_ADD = "OaHolidayLinkService.add";
    /**
     * 修改请假排课信息 集合
     */
    public final static String leave_modify = "OaHolidayLinkService.modify";
    /**
     * 添加请假排课  分页查询信息
     */
    public final static String LEAVE_TIPSAY_CHECK_PAGE = "OaHolidayLinkService.list";
    //添加请假申请是否可以排课，status=1，可以排课
    public final static String LEAVE_ADD_SCHEDULE_STATUS = "BaseSchoolConfigService.dtl";
    /**
     * 添加请假排课详情
     */
    public final static String LEAVE_TIPSAY_CHECK_DETAIL = "OaHolidayLinkService.dtl";

    //撤回请假申请
    public static final String LEAVE_REVOKE = "OAHolidayService.recallTask";
    //删除请假申请
    public static final String LEAVE_DELETE = "OAHolidayService.deleteTask";

//    /**查询用户未读的会议通知和消息数量*/
//    public final static String unReadNum =  "OaMeetingService.unReadMsgCount";
    /**
     * 学年 学期  成绩类型 SCHOOL_CALENDAR_YEAR，SCHOOL_CALENDAR_SEMESTER,SCHOOL_SCORE_TYPE
     */
    public final static String parentKeyTochilds = "SysDictService.parentKeyTochilds";
    public final static String getStuScoreAllType = "StuScoreDetailService.getStuScoreAllType";
    /**
     * 查询成绩列表
     */
    public final static String listForClass = "StuScoreSchemaClassService.listForClass";

//    /**代课老师的接口*/
//    public final static String SUPPLYTEACHER =  "OAHolidayService.listTeacherByNotSchdule";  // startTime， endTime
    /**
     * 查询班级的列表
     */
    public final static String listForScore = "StuScoreDetailService.listForScore";
    /**
     * 个人德育列表
     */
    public final static String PERSON_EDUCATION = "MoralManCheckService.list";
    /**
     * 个人德育专项列表
     */
    public final static String PERSON_EDUCATION_SPECIAL_LIST = "MoralItemService.list";
    /**
     * 个人德育录入提交按钮
     */
    public final static String PERSON_EDUCATION_INPUT_SUB = "MoralManCheckService.add";

//    /**添加请假排课信息 集合*/
    public final static String LEAVE_TIPSAY_ADD_LIST =  "OaHolidayLinkService.addList";
    /**
     * 班级德育方案列表
     */
    public final static String PERSON_EDUCATION_CLAZZ_PROJECT = "MoralClassSchemeService.list";
    /**
     * 班级德育主列表
     */
    public final static String PERSON_EDUCATION_CLAZZ_PROJECT_LIST = "MoralClassCheckService.listMain";
    /**
     * 班级德育根据班级年级获取督导员列表信息
     */
    public final static String RfidList_edu_class = "OrgTeamService.listInspector";
    /**
     * 班级德育根据督导员姓名和学籍号码查询督导员信息
     */
    public final static String SCHOOLSTUDENTNAME_EDUCLASS = "OrgTeamService.listInspector";
    /**
     * 班级德育进入打分提交页面的列表
     */
//    public final static String EDUCLASS_SCORE =  "MoralClassCheckService.listEmpty";
    public final static String EDUCLASS_SCORE = "MoralClassCheckService.list";
    public final static String EDUCLASS_SCOREEmpty = "MoralClassCheckService.listEmpty";
    /**
     * 班级德育进入打分提交按钮  添加
     */
    public final static String EDUCLASS_SCORE_SUB = "MoralClassCheckService.saveItems";
    /**
     * 班级德育进入打分提交按钮  添加
     */
    public final static String EDUCLASS_SCORE_SUB_ALL = "MoralClassCheckService.saveItemsList";
    /**
     * 班级德育进入打分提交按钮  修改
     */
    public final static String EDUCLASS_SCORE_MODIFY = "MoralClassCheckService.saveItems";
    /**
     * 班级德育普通老师筛选
     */
    public final static String EDUCLASS_SCORE_MODIFY_normal = "MoralClassCheckService.list";
    /**
     * 班级德育删除 批量
     */
    public final static String EDUCLASS_SCORE_MODIFY_DELETE_ALL = "MoralClassCheckService.listRemoveByGrade";
    /**
     * 班级德育删除
     */
    public final static String EDUCLASS_SCORE_MODIFY_DELETE = "MoralClassCheckService.removeByGrade";
    /**
     * 个人德育删除 批量
     */
    public final static String EDUPERSON_DELETE_ALL = "MoralManCheckService.listRemove";
    /**
     * 个人德育删除
     */
    public final static String EDUPERSON_DELETE = "MoralManCheckService.remove";
    /**
     * 个人德育修改
     */
    public final static String EDUPERSON_MODIFY = "MoralManCheckService.modify";
    /**
     * 查询未读数
     */
    public final static String CHECKUNREADNUM = "PushMgrService.count";
    /**
     * 查询混合消息列表
     */
    public final static String ALLMESSAGES = "PushMgrService.list";
    /**
     * 设置消息已读
     * PushMgrService.dtl&linkIds="1,2,3"   设置消息已读
     */
    public final static String SETHASREAD = "PushMgrService.dtl";
    /**
     * 删除消息
     * PushMgrService.del&linkIds="1,2,3"
     */
    public final static String DELETEMESSAGE = "PushMgrService.del";
    /**
     * 德育统计   获取学期接口
     * 参数：calType=1  返回学期内所有月份
     * calType 不传，或者传其它返回学期
     */
    public final static String EDUCOUNTGETALLTERM = "MoralCalculatetService.getQueryParams";
    /**
     * 统计接口：
     * 必填参数：startTime 统计的起始时间（都是从上一接口返回）
     * 统计的结束时间（都是从上一接口返回
     */
    public final static String EDUCOUNTGETCOUNT = "MoralCalculatetService.list";
    /**
     * 查询默认督导员
     */
    public final static String QUERYINITINSPECTOR = "OrgTeamService.queryInitInspector";
    /**
     * c查询督导员列表
     */
    public final static String LISTINSPECTOR = "OrgTeamService.listInspector";
    /**
     * 辑删除 群发消息
     */
    public final static String DELETETOALLMSG = "PushLinkService.setRemove";
    //群发消息，会议通知多删
    public final static String SETREMOVEALL = "PushLinkService.setRemoveAll";
    /**
     * 部门列表
     */
//    public final static String SECTIONLIST =  BASEURL + "?method=OrgDeptService.queryAllDeptPerson";
//    public final static String SECTIONLIST = BASEURL + "?method=OrgDeptService.queryFouTeacherIm";
//    /**
//     * 版本更新接口
//     */
//    public final static String CHECKVERSION = BASEURL + "?method=SysAppVersionService.checkVersion";

    //群发消息中获取家长列表的接口，有对权限进行判断
    public static final String LISTCLASS = "BaseGradeService.listClass";
    //群发消息和会议通知获取教师发送对象的接口，有权限判断
    public static final String GETPUSHDEPTPERSON = "OrgDeptService.getPushDeptPerson";
    //-------------------------班级圈接口----------------------------------------//
    //点赞和取消点赞接口   dpStatus  1代表点赞，2代表取消
    public static final String PRAISEORCANCEL = "SectionDynamicsPraiseService.praiseOrCancel";
    //点赞列表
    public static final String PRAISELIST = "SectionDynamicsPraiseService.praiseList";
    //评论列表&params={"dId":"374267149587320832"}
    public static final String COMMENTLIST = "SectionDynamicsCommentService.commentList";
    //评论
    public static final String COMMENT = "SectionDynamicsCommentService.comment";
    //回复
    public static final String REPLY = "SectionDynamicsCommentService.reply";
    //删除评论
    public static final String DELETECOMMENT = "SectionDynamicsCommentService.delete";
    //删除回复
    public static final String DELETEREPLY = "SectionDynamicsCommentService.delete";
    //添加动态
    public static final String ADDMOMENT = "SectionDynamicsClassifyService.add";
    //删除动态
    public static final String DELETEMOMENT = "SectionDynamicsClassifyService.delete";
    //修改动态
    public static final String UPDATEMOMENT = "SectionDynamicsClassifyService.update";
    //动态类型列表
    public static final String MOMENTTYPELIST = "SectionDynamicsClassifyService.list";
    //班级圈删除
    public static final String MOMENTDEL = "SectionDynamicsService.del";

    //班级动态添加
    public static final String MOMENTSAVE = "SectionDynamicsService.save";
    //资源共享添加
    public static final String DOCUMENTSAVE = "SectionDynamicsService.save";
    //班级红榜添加
    public static final String HONOURSAVE = "SectionDynamicsService.save";
    //班级动态列表全部显示
    public static final String MOMENTLIST = "SectionDynamicsService.list";
    //获取验证码接口
    public static final String GETCODE = "SectionUploadService.getCode";
    //班级圈搜索接口
    public static final String SEARCHLIST = "SectionDynamicsService.searchList";
    //资源共享数据字典
    public static final String SECTIONDIC = "SectionDynamicsService.sectionDic";
    /**
     * 最新版本接口
     */
    public final static String NEWEST_VERSION = "SysAppVersionService.checkVersion";
    /**
     * 最低强制更新版本接口
     */
    public final static String LOWEST_VERSION = "SysAppVersionService.queryForceVersion";
    public final static String VERSION_LIST = "SysAppVersionService.list";
    public final static String VERSION_INFO = "SysAppVersionService.dtl";
    /**
     * 访客接口
     */
    //访客列表
    public final static String VISITOR_LIST = "BaseVisitorService.list";
    //教师新增访客
    public final static String VISITOR_ADD = "BaseVisitorService.add";
    //查询访客记录详情
    public final static String VISITOR_INFO = "BaseVisitorService.dtl";
    //发送动态验证码
    public final static String VISITOR_SEND_CODE = "BaseVisitorService.sendSmsCode";
    //验证动态验证码
    public final static String VISITOR_VERIFY_CODE = "BaseVisitorService.validateVerificationCode";
    //发卡入校
    public final static String VISITOR_SCAN_IN = "BaseVisitorService.scanCodeEnter";
    //扫码离校
    public final static String VISITOR_SCAN_OUT = "BaseVisitorService.scanCodeLeave";
    //结束拜访
    public final static String VISITOR_END = "BaseVisitorService.endVisit";
    //角色类型（返回userType:1、教师，2、保安，99、未知）
    public final static String USER_ROLE = "BaseVisitorService.getCurrentUserType";
    public final static String VISITOR_SUBMIT = "BaseVisitorService.validateForm";
    //删除访客列表
    public final static String VISITOR_REMOVE = "BaseVisitorService.remove";

    /**临时接送**/
    //分页查询
    public final static String PICK_UP_SEARCHER = "SchoolShuttleService.searchShuttleApply";
    //接送查询  区分审批和未审批
    public final static String PICK_UP_SEARCHER_ISDONE = "SchoolShuttleService.searchApproveList";
    //门卫核实接送人员入校接口
    public final static String WM_APPROVAL_ENTER = "SchoolShuttleService.verifyEnter";
    //班主任审核
    public final static String BZR_APPROVAL = "SchoolShuttleService.classTeacherAffirm";
    //交接老师交接
    public final static String JJLS_APPROVAL = "SchoolShuttleService.handOver";
    //审核老师审核
    public final static String SHLS_APPROVAL = "SchoolShuttleService.check";
    //门卫审核
    public final static String MW_APPROVAL = "SchoolShuttleService.verify";
    //临时接送审核列表
    public static final String PICK_UP_APPROVAL_LIST = "SchoolShuttleService.searchApproveList";

    //支付接口
    //获取订单接口
    public static final String ORDER_DETAIL = "PayOrderTypeService.getOrderConfig";
    //下单
    public static final String CREATE_ORDER = "PayBusinessService.createOrder";
    //支付接口
    public static final String PAY = "PayMentService.pay";
    //支付流水
    public static final String PAY_LIST = "PayBusinessService.list";
    //新的今日食谱列表接口
    public static final String NEWLIST = "OaCookBookService.newList";
    //菜谱点赞列表
    public static final String COOKBOOKPARISELIST = "OaCookBookService.praiseList";
    //菜谱评论列表
    public static final String COOKBOOKCOMMENTLIST = "OaCookBookService.commentList";

    //自媒体新闻列表
    public static final String COMPANYNEWSLIST = "CompanyNewsService.list";
    //自媒体新闻查看
    public static final String COMPANYNEWSDTL = "CompanyNewsService.dtl";
    //自媒体新闻评论查看
    public static final String COMPANYNEWSREPLYLIST = "CompanyNewsService.replyList";
    //自媒体新闻评论添加
    public static final String COMPANYNEWSADDREPLY = "CompanyNewsReplyService.addNewReply";
    //自媒体新闻评论详情查看
    public static final String COMPANYNEWSREPLYDTL = "CompanyNewsReplyService.replyDtl";
    //自媒体新闻评论回复添加
    public static final String COMPANYNEWSADDNEWMSGREPLY = "CompanyNewsReplyService.addNewMsgReply";
    //自媒体新闻评论回复列表查看
    public static final String COMPANYNEWSREPLYSERVICELIST = "CompanyNewsReplyService.list";
    //自媒体新闻评论点赞或取消
    public static final String COMPANYNEWSPRAISEOCANCEL = "CompanyNewsPraiseService.praiseOrCancel";
    //自媒体新闻评论点赞或取消
    public static final String COMPANYNEWSREMOVEREPLY = "CompanyNewsReplyService.removeNewReply";

    //成长档案
    //查学生(1,学生名  或者2，班级名）
    public static final String GET_STUDENT_LIST = "BaseStudentService.getStudentList";
    //成长日记；列表
    public static final String GROWTH_DAILY_LIST = "StudentMemoryService.list";
    //成长日记新增
    public static final String GROWTH_DAILY_ADD = "StudentMemoryService.add";
    //成长日记删除
    public static final String GROWTH_DAILY_DELETE = "StudentMemoryService.remove";

    //德育表现列表
    public static final String GROWTH_MORAL_SCORE = "MoralScoreCountService.manGroupDtlByPage";

    //成长档案学期汇总数据查询
    public static final String TERMAPPRAISElISTSTUDENTID = "TermAppraiseContentService.listByStudentId";
    //成长档案学期汇总新增
    public static final String TERMAPPRAISEADD = "TermAppraiseContentService.add";
    //成长档案学期汇总寄语类型列表
    public static final String TERMAPPRAISETYPELIST = "TermAppraiseTyperService.listBySchoolId";
    //成长档案学习成绩数据查询
    public static final String STUDYRECORDCOUNT = "StuScoreDetailService.manGroupScoreDtl";
    //成长档案学期数据查询
    public static final String TERMLIST = "SysSchoolTermService.listBySchoolId";
    //成长档案学期汇总寄语默认查询
    public static final String APPRAISEDEFAULTLIST = "TermAppraiseDefaultService.listBySchoolId";

    public static final String HOMEWORK_SHARE_SELECT = "TeachHomeworkTaskService.dtlByShare";

    /**
     * 选课管理
     */
    //班级选课
    public static final String XK_BJXK = "StudentService.queryCourseGroup";
    //学生选课
    public static final String XK_XSXK = "StudentService.queryStudentGroup";

    //作业
    //作业列表
    public static final String HOMEWORK_LIST = "TeachHomeworkService.listByTeacherId";
    //根据科目查老师的班级
    public static final String HOMEWORK_ADD_CLASS ="TeachHomeworkService.getClassBySubject";
    //老师查看学生提交作业
    public static final String HOMEWORK_UPLOAD_DETAIL = "TeachHomeworkTaskService.dtl";
    //老师发布作业明细
    public static final String HOMEWORK_PUBLISH_DETAIL = "TeachHomeworkService.dtl";

    //班级学生作业提交情况(第一次)
    public static final String HOMEWORK_STUDENT_SUBMIT_INFO_FIRST = "TeachHomeworkTaskService.submitSituation";
    //班级学生作业提交情况
    public static final String HOMEWORK_STUDENT_SUBMIT_INFO = "TeachHomeworkTaskService.submitSituationByClassId";
    //老师批改作业
    public static final String HOMEWORK_CORRECT = "TeachHomeworkTaskService.modify";
    //改变作业状态  未发布，已发布，已完结
    public static final String HOMEWORK_MODIFY_STATUS = "TeachHomeworkService.modifyStatus";

    /**
     * 选课管理
     */
    //admin课堂成绩
    public static final String COURSE_HISTORY_ADMIN = "OptTeacherService.getCoursesHistoryByAdmin";
    //admin管理员配置选课
    public static final String QUERY_COURSE_ADMIN = "OptTeacherService.queryCoursesByAdmin";
    //admin管理员配置选课详情
    public static final String COURSE_DTL_ADMIN = "OptTeacherService.dtlByAdmin";
    //admin班级选课
    public static final String BJ_COURSE_ADMIN = "OptTeacherService.queryStudentCoursesByAdmin";
    //admin学生选课
    public static final String XK_XSXK_ADMIN = "OptTeacherService.queryStuCourseByAdmin";
    //admin课堂记录查看
    public static final String KTJL = "OptTeacherService.getCoursesHistoryByAdmin";
    //admin课堂记录查看筛选条件
    public static final String SELECTINFO = "OptTeacherService.selectInfo";
    //班主任课堂记录
    public static final String COURSE_HISTORY_BZR = "OptTeacherService.queryStuCoursesHistory";
    //班主任课堂归集
    public static final String COURSE_HISTORY_C_BZR = "OptTeacherService.queryCoursesByStudent";
    //班主任学生归集
    public static final String COURSE_HISTORY_S_BZR = "OptTeacherService.queryStudentCourses";
    //任课老师课程
    public static final String COURSE_RK = "OptTeacherService.getMyCourses";
    //任课老师课堂点名
    public static final String ROLLCALLlIST = "OptTeacherService.rollCalllist";
    //任课老师课堂点名提交
    public static final String ROLLCALLlISTSUB = "OptTeacherService.sumbitRollCallResult";
    //任课老师课堂成绩
    public static final String SCORElIST = "OptTeacherService.courseScorelist";
    //任课老师课堂成绩提交
    public static final String SCORElISTSUB = "OptTeacherService.sumbitCourseScore";


    public static final String COURSE_PT_HISTORY = "OptTeacherService.getMyCoursesHistory";

    //    public static String BASEURL = "http://192.168.3.102:80/api";

    //    public static String BASEURL = "http://192.168.3.102:80/api";
    /**
     * 部门列表
     */
//    public final static String SECTIONLIST =  BASEURL + "?method=OrgDeptService.queryAllDeptPerson";
//    public final static String SECTIONLIST = BASEURL + "?method=OrgDeptService.queryFouTeacherIm";
//    /**
//     * 版本更新接口
//     */
//    public final static String CHECKVERSION = BASEURL + "?method=SysAppVersionService.checkVersion";


    public final static String BASEURL_WWW = "http://www.1000xyun.com/api";
    public final static String BASEURL_DEV = "http://dev.1000xyun.com:81/api";
    public final static String BASEURL_DEV1 = "http://192.168.3.101:80/api";
    public final static String BASEURL_TEST = "http://test.1000xyun.com:82/api";
    public final static String BASEURL_TEST1 = "http://192.168.3.102:80/api";
    public final static String loadImageHead = "OrgUserService.changePhoto";//上传头像
    public static String BASEURL = BASEURL_WWW;
}