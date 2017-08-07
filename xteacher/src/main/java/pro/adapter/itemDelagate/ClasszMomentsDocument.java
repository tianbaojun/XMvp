package pro.adapter.itemDelagate;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.DownloadBean;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.utils.DownLoadAsyncTask;
import com.zjhz.teacher.utils.Md5Utils;
import com.zjhz.teacher.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tabjin on 2017/4/14.
 * Description:
 * What Changed:
 */

public class ClasszMomentsDocument implements ItemViewDelegate<ClasszMoment> {

    public static final String DOCUMENT = "1002";

    private WaitDialog dialog;
    private Map<String, String> map = new HashMap<>();

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_classz_moments_document;
    }

    @Override
    public boolean isForViewType(ClasszMoment item, int position) {
        if (DOCUMENT.equals(item.getDcCode()) || item.getDcCode() == null)
            return true;
        return false;
    }

    @Override
    public void convert(final ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        //打开文档
        documentOperation(holder, classzMoment);
        String str = classzMoment.getKeywordsValue();
        str = str.replace("|", " | ");
       /* if(str.contains("|")){
            Log.d("main", "found");
        }*/
        holder.setText(R.id.document_type, str);
    }

    private void documentOperation(final ViewHolder holder, ClasszMoment classzMoment) {
        if (classzMoment.getListAttachment() != null && classzMoment.getListAttachment().size() > 0) {
            holder.setText(R.id.document_name, classzMoment.getListAttachment().get(0).getAttName());
            final String url = classzMoment.getListAttachment().get(0).getAttPath();
            final String fileName = classzMoment.getListAttachment().get(0).getAttName();
            final String fileType = classzMoment.getListAttachment().get(0).getAttExtName();
            if(fileType != null) {
                switch (fileType) {
                    case "doc":
                    case "docx":
                        holder.setImageResource(R.id.document_icon, R.mipmap.word);
                        break;
                    case "ppt":
                    case "pptx":
                        holder.setImageResource(R.id.document_icon, R.mipmap.ppt);
                        break;
                    case "xls":
                    case "xlsx":
                        holder.setImageResource(R.id.document_icon, R.mipmap.excl);
                        break;
                    case "txt":
                        holder.setImageResource(R.id.document_icon, R.mipmap.txt);
                        break;
                    case "pdf":
                        holder.setImageResource(R.id.document_icon, R.mipmap.pdf);
                        break;
                    default:

                        break;
                }
            }
            final DownLoadAsyncTask downLoadAsyncTask = new DownLoadAsyncTask(holder.getConvertView().getContext(), new DownLoadAsyncTask.DownLoadFileListener() {
                @Override
                public void onDownLoad(int progress) {
                   /* dialog = new WaitDialog(holder.getConvertView().getContext());
                    dialog.setMessage("下载中");
                    dialog.show();*/
                }

                @Override
                public void onDownLoadErr() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    ToastUtils.showShort("下载失败");
                }

                @Override
                public void completeDownLoad(String savePathName, String fileName) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    String path = savePathName + File.separator + fileName;
                    map.put(Md5Utils.getMd5(url), path);
                    openFile(path, fileType, holder);
//                    holder.getConvertView().getContext().startActivity(getFileIntent(savePathName + File.separator + fileName, fileType));
                }
            });
            holder.setOnClickListener(R.id.document, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String md5 = Md5Utils.getMd5(url);
                    if (map.keySet().contains(md5)) {
                        if (new File(map.get(md5)).exists()) {
                            openFile(map.get(md5), fileType, holder);
                            return;
                        }
                    }
                    if (downLoadAsyncTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                        downLoadAsyncTask.execute(new DownloadBean(url, fileName));
                        if (dialog == null) {
                            dialog = new WaitDialog(holder.getConvertView().getContext());
                        }
                        dialog.setMessage("加载中...");
                        dialog.show();
                    }

                }
            });
        }
    }

    private void openFile(String path, String fileType, ViewHolder holder) {
        switch (fileType) {
            case "doc":
            case "docx":
                holder.getConvertView().getContext().startActivity(getWordFileIntent(path));
                break;
            case "ppt":
            case "pptx":
                holder.getConvertView().getContext().startActivity(getPptFileIntent(path));
                break;
            case "xls":
            case "xlsx":
                holder.getConvertView().getContext().startActivity(getExcelFileIntent(path));
                break;
            case "txt":
                holder.getConvertView().getContext().startActivity(getTextFileIntent(path, false));
                break;
            case "pdf":
                holder.getConvertView().getContext().startActivity(getPdfFileIntent(path));
                break;


        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }

    //android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
