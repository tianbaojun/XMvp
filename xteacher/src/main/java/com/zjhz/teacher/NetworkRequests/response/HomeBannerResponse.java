package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public class HomeBannerResponse implements Serializable{

    /**
     * code : 0
     * data : [{"content":"新闻内容这里是新新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容","createTime":"2016-07-19 11:13:55","createUser":"管理员","headImg":"http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeNwZ6ATKtQAAYdi6MSRQs668.png?attname=2.png","imageId":"272812381946515456","schoolId":"1","title":"新闻标题这里是新","updateTime":"2016-07-19 15:01:56","updateUser":"管理员"},{"content":"新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻新闻","createTime":"2016-07-19 11:13:42","createUser":"管理员","headImg":"http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeNwbqAc9wcAAQWtBX4phM822.png?attname=3.png","imageId":"272812330012643328","schoolId":"1","title":"新闻新闻新闻新闻新闻","updateTime":"2016-07-19 15:02:14","updateUser":"管理员"},{"content":"大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻大大大新闻新闻","createTime":"2016-07-19 11:13:07","createUser":"管理员","headImg":"http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeNweiAOjYaAANbzsJu3wk934.png?attname=4.png","imageId":"272812182020820992","schoolId":"1","title":"大大大新闻新闻","updateTime":"2016-07-19 15:02:56","updateUser":"管理员"},{"content":"这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容这里是新闻内容","createTime":"2016-07-18 13:14:35","createUser":"管理员","headImg":"http://msg.51jxh.com:226/group1/M00/00/0C/CmUBaVeMK5CAEs-KAANdQaSkp6A972.png?attname=%E6%9C%AA%E6%A0%87%E9%A2%98-1.png","imageId":"271755588197683200","schoolId":"1","title":"这里是新闻标题这里是新闻标题","updateTime":"2016-07-16 13:37:36","updateUser":"管理员"}]
     * msg : 查询到8条记录!
     * total : 8
     */

    public int code;
    public String msg;
    public int total;
    /**
     * content : 新闻内容这里是新新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容
     * createTime : 2016-07-19 11:13:55
     * createUser : 管理员
     * headImg : http://msg.51jxh.com:226/group1/M00/00/0D/CmUBaVeNwZ6ATKtQAAYdi6MSRQs668.png?attname=2.png
     * imageId : 272812381946515456
     * schoolId : 1
     * title : 新闻标题这里是新
     * updateTime : 2016-07-19 15:01:56
     * updateUser : 管理员
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String content;
        public String createTime;
        public String createUser;
        public String headImg;
        public String imageId;
        public String schoolId;
        public String title;
        public String updateTime;
        public String updateUser;
    }
}
