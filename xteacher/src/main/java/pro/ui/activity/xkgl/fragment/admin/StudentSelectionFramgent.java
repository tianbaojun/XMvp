package pro.ui.activity.xkgl.fragment.admin;


import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import pro.ui.activity.xkgl.fragment.StudentBaseFramgent;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class StudentSelectionFramgent extends StudentBaseFramgent{
    protected void getDate(){
        Map<String,String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.XK_XSXK_ADMIN, STUDENT);
    }

    public static void getData(String classId){
        Map<String,String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        map.put("inClassId", classId);
        NetworkRequest.request(map, CommonUrl.XK_XSXK_ADMIN, STUDENT);
    }
}
