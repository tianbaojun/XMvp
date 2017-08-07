package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-06
 * Time: 16:57
 * Description: 校外定位  上学放学历史路经查询  响应数据
 */
public class SchoolPathResponse implements Serializable{


    /**
     * code : 0
     * data : [{"deviceType":"0","pointxBaidu":"120.06","pointyBaidu":"29.001","recordTime":1451606400,"stationAddress":"df 绍兴胜利西路110号","stationCode":"FM001"},{"deviceType":"0","pointxBaidu":"121.191078","pointyBaidu":"28.893609","recordTime":1451608200,"stationAddress":"河阳路 东方大道路口","stationCode":"013454678941"}]
     * msg : 查询到2条记录!
     * total : 2
     */

    public int code;
    public String msg;
    public int total;
    /**
     * deviceType : 0
     * pointxBaidu : 120.06
     * pointyBaidu : 29.001
     * recordTime : 1451606400
     * stationAddress : df 绍兴胜利西路110号
     * stationCode : FM001
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String deviceType;
        public String pointxBaidu;
        public String pointyBaidu;  // 纬度
        public int recordTime;
        public String stationAddress;
        public String stationCode;
    }
}
