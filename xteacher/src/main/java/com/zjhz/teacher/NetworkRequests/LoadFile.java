package com.zjhz.teacher.NetworkRequests;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.CMCPhotoUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.PictureUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 *
 */
public class LoadFile {

    private static FileCallBack fileCallBack;
    private FileCallBackNew fileCallBackNew;
    private Activity activity;
    private String pathUrls = "";
    public LoadFile(FileCallBack fileCallBack ){
        LoadFile.fileCallBack = fileCallBack;
    }

    public LoadFile(FileCallBackNew fileCallBackNew, Activity activity){
        this.fileCallBackNew = fileCallBackNew;
        this.activity = activity;
    }

    public void clearPathUrls() {
        this.pathUrls = "";
    }

    public void uploadFile(List<File> files){
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        int j = 0;
        for (int i = 0 ; i < files.size() ; i++){
            if (!files.get(i).getPath().startsWith("http")) {
                j++;
                builder.addFormDataPart("file" + i, files.get(i).getName(), RequestBody.create(null, files.get(i)));
            }
            else
                pathUrls += files.get(i).getPath() + ",";
        }
        if(j == 0){
            if (fileCallBack != null)
                fileCallBack.onfileComplete(null);
            if(fileCallBackNew != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pathUrls.length() > 0)
                            pathUrls = pathUrls.substring(0, pathUrls.length() - 1);
                        fileCallBackNew.onFileComplete(pathUrls);
                        pathUrls = "";
                    }
                });
            }
            return;
        }
        String url = CommonUrl.BASEURL.replace("api", "") + "mupload";
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder().url(url)
                .addHeader("token",SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey))
                .post(requestBody)
                .build();
        OkHttpClient mOkHttpClient = InitOkHttpClient.getInstance();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (fileCallBack != null) {
                    fileCallBack.onfileErr(request);
                }
                if (fileCallBackNew != null) {
                    fileCallBackNew.onFileErr(request);
                }
                System.out.println("onFailure:"+request.toString());
                System.out.println("IOException:"+e.getMessage());
            }
            @Override
            public void onResponse(final Response response) throws IOException {
                System.out.println("onResponse:"+response.toString());
                if (fileCallBack != null) {
                    fileCallBack.onfileComplete(response);
                }
                if (fileCallBackNew != null) {
                    final String urls = upLoadResponse(response);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(urls)) {
                                if(pathUrls.length() > 0)
                                    pathUrls = pathUrls.substring(0, pathUrls.length() - 1);
                            } else {
                                pathUrls = pathUrls + urls;
                            }
                            fileCallBackNew.onFileComplete(pathUrls);
                        }
                    });
                }
            }
        });
    }


//    public void uploadFile(List<File> files){
//        MultipartBuilder builder = new MultipartBuilder();
//        builder.type(MultipartBuilder.FORM);
//        for (int i = 0 ; i < files.size() ; i++){
//            builder.addFormDataPart("file" + i,files.get(i).getName(),RequestBody.create(null,files.get(i)));
//            LogUtil.e("|图片参数 = ",files.get(i).getName() + "  ==== " + files.get(i));
//        }
//        String url = CommonUrl.BASEURL.replace("api", "") + "mupload";
//        RequestBody requestBody = builder.build();//http://msg.51jxh.com:2223/
//        final Request request = new Request.Builder().url(url)
//                .addHeader("token",SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey))
//                .post(requestBody)
//                .build();
//        OkHttpClient mOkHttpClient = InitOkHttpClient.getInstance();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                if(fileCallBack != null)
//                    fileCallBack.onfileErr(request);
//                if(fileCallBackNew != null){
//                    fileCallBackNew.onFileErr(request);
//                }
//
//                System.out.println("onFailure:"+request.toString());
//                System.out.println("IOException:"+e.getMessage());
//            }
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                System.out.println("onResponse:"+response.toString());
//                if(fileCallBack != null)
//                    fileCallBack.onfileComplete(response);
//                if(fileCallBackNew != null){
//                    final String urls = upLoadResponse(response);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            fileCallBackNew.onFileComplete(urls);
//                        }
//                    });
//                }
//            }
//        });
//    }


    private String upLoadResponse(final Response response){
        if (response.code() == 200){
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
                String newUrl = "";
                if (imageUrls != null){
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)){newUrl += f0+",";}
                    String f1 = imageUrls.getFile1();
                    if (!SharePreCache.isEmpty(f1)){newUrl += f1+",";}
                    String f2 = imageUrls.getFile2();
                    if (!SharePreCache.isEmpty(f2)){newUrl += f2+",";}
                    String f3 = imageUrls.getFile3();
                    if (!SharePreCache.isEmpty(f3)){newUrl += f3+",";}
                    String f4 = imageUrls.getFile4();
                    if (!SharePreCache.isEmpty(f4)){newUrl += f4+",";}
                    String f5 = imageUrls.getFile5();
                    if (!SharePreCache.isEmpty(f5)){newUrl += f5+",";}
                    String f6 = imageUrls.getFile6();
                    if (!SharePreCache.isEmpty(f6)){newUrl += f6+",";}
                    String f7 = imageUrls.getFile7();
                    if (!SharePreCache.isEmpty(f7)){newUrl += f7+",";}
                    String f8 = imageUrls.getFile8();
                    if (!SharePreCache.isEmpty(f8)){newUrl += f8+",";}
                }
                if (!SharePreCache.isEmpty(newUrl)){
                    return newUrl.substring(0, newUrl.length() - 1);
                }else {
                    return "";
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("发布失败code=" + response.code());
                }
            });
        }
        return "";
    }

    /**
     * uri转换file
     * @param activity
     * @param files
     * @return
     */
    public List<File> uriToFile(Activity activity, List<LocalImageHelper.LocalFile> files){
        List<File> imgUrl = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            //图片uri
            Uri uri = Uri.parse(files.get(i).getThumbnailUri());

            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor =activity.managedQuery(uri,proj,null,null,null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
//            文件路径
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
//            压缩图片
//            File file = new File(uri.toString());
//            String img_path = file.getPath();
//            Bitmap bitmap = CMCPhotoUtil.CompresPhoto(img_path,300,300);
            Bitmap bitmap = PictureUtil.getSmallBitmap(img_path,480,800);
            if (bitmap != null){
//                将bitmap保存为file
                CMCPhotoUtil.SaveBitmapFile(bitmap,file);
            }
            imgUrl.add(file);
        }
        return imgUrl;
    }

    /**
     * uri转换file
     * @paths   图片路径集合
     * @return
     */
    public List<File> uriToFile( String[] paths){
        List<File> imgUrl = new ArrayList<>();
        if(null != paths)
        for (int i = 0; i < paths.length; i++) {
            if(paths[i] == null)
                continue;
            String img_path = paths[i];
            File file = new File(paths[i]);
            Bitmap bitmap = PictureUtil.getSmallBitmap(img_path,480,800);
            if (bitmap != null){
                CMCPhotoUtil.SaveBitmapFile(bitmap,file);
            }
            imgUrl.add(file);
        }
        return imgUrl;
    }

    /**
     * uri转换file
     *
     * @return
     * @paths 图片路径集合
     */
    public List<File> uriToFile(List<String> paths) {
        List<File> imgUrl = new ArrayList<>();
        if (null != paths)
            for (int i = 0; i < paths.size(); i++) {
                if(paths.get(i) == null)
                    continue;
                String img_path = paths.get(i);
                File file = new File(paths.get(i));
                Bitmap bitmap = PictureUtil.getSmallBitmap(img_path, 480, 800);
                if (bitmap != null) {
                    CMCPhotoUtil.SaveBitmapFile(bitmap, file);
                }
                imgUrl.add(file);
            }
        return imgUrl;
    }

    /**
     * uri转换file
     *
     * @return
     * @paths 路径集合
     */
    public List<File> uriToAttrFile(List<String> paths) {
        List<File> url = new ArrayList<>();
        if (null != paths)
            for (int i = 0; i < paths.size(); i++) {
                if(paths.get(i) == null)
                    continue;
                File file = new File(paths.get(i));
                url.add(file);
            }
        return url;
    }


    public interface FileCallBack {
        void onfileComplete(Response response);//第一个参数是URL，第二个参数是上传成功后服务器返回的结果

        void onfileErr(Request request);//失败后返回URL,可以自己改
    }

    public interface FileCallBackNew{
        void onFileComplete(String urls);//第一个参数是URL，第二个参数是上传成功后服务器返回的结果

        void onFileErr(Request request);//失败后返回URL,可以自己改
    }
}
