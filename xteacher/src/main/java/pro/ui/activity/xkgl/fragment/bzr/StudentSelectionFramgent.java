package pro.ui.activity.xkgl.fragment.bzr;


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
        NetworkRequest.request(map, CommonUrl.COURSE_HISTORY_S_BZR, STUDENT);
    }
}
