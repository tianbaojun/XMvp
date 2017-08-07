package pro.ui.activity.xkgl.fragment.admin;


import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import pro.ui.activity.xkgl.fragment.BanJiBaseFramgent;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class BanJiSelectionFramgent extends BanJiBaseFramgent{
    protected void getBjxkList(){
        Map<String,String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.BJ_COURSE_ADMIN, BANJI);
    }

    public static void getBjxkList(String classId){
        Map<String,String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        map.put("inClassId", classId);
        NetworkRequest.request(map, CommonUrl.BJ_COURSE_ADMIN, BANJI);
    }
}
