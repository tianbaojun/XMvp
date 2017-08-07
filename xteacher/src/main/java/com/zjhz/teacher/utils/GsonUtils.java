package com.zjhz.teacher.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.alibaba.fastjson.JSON;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class GsonUtils {
    private static Gson gson = null;

    public static Gson getInstance() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    /**
     * 返回一个对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return (T) GsonUtils.getInstance().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(JSONObject jsonObject, Class<T> clazz) {
        try {
            return (T) GsonUtils.getInstance().fromJson(jsonObject.getString("data"), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回一个对象
     *
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Type typeOfT) {
        try {
            return GsonUtils.getInstance().fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Map参数转成json格式传输
     *
     * @param map
     * @return
     */
    public static String toJson(Map map) {
        return GsonUtils.getInstance().toJson(map);
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType() List<LeaveSchedule> tmpBacklogList = gson.fromJson(backlogJsonStr, new TypeToken<List<LeaveSchedule>>() {}.getType());
     * @return
     */
    public static List<?> toList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        if (obj == null) {
            return null;
        } else {
            return gson.toJson(obj);
        }
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * 解析成list集合
     *
     * @param clazz
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> toArray(Class<T> clazz, JSONObject object) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(object.getJSONArray("data").toString(), type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;

            //            JSONArray array = object.getJSONArray("data");
            //            return JSON.parseArray(array.toString(),tClass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("fastjson解析错误", e.getMessage());
            return new ArrayList<T>();
        }
    }
/**
     * 解析成list集合
     *
     * @param clazz
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> toYourArray(Class<T> clazz, JSONObject object,String key) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(object.getJSONArray(key).toString(), type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;

            //            JSONArray array = object.getJSONArray("data");
            //            return JSON.parseArray(array.toString(),tClass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("fastjson解析错误", e.getMessage());
            return new ArrayList<T>();
        }
    }

    /**
     * 解析成对象
     *
     * @param tClass
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T toObjetJson(JSONObject object, Class<T> tClass) {
        try {
            JSONObject array = object.getJSONObject("data");
            return JSON.parseObject(array.toString(), tClass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("fastjson解析错误", e.getMessage());
            return null;
        }
    }

    /**
     * 解析成list集合
     *
     * @param clazz
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToArray(Class<T> clazz, JSONObject object) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(object.getJSONArray("data").toString(), type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
//            JSONArray array = object.getJSONArray("data");
//            return JSON.parseArray(array.toString(),tClass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("fastjson解析错误", e.getMessage());
            return new ArrayList<T>();
        }
    }
}
