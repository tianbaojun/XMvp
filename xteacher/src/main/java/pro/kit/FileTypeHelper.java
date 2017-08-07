package pro.kit;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tabjin on 2017/5/3.
 * Description:
 * What Changed:
 */

public class FileTypeHelper {

    public static FileFormat getFileFormat(String fileName) {
        if (fileName == null)
            return FileFormat.NONE;
        int length = fileName.length();
        if (fileName.substring(length - 3, length).equals("doc") || fileName.substring(length - 4, length).equals("docx"))
            return FileFormat.WORD;
        else if (fileName.substring(length - 3, length).equals("xls") || fileName.substring(length - 4, length).equals("xlsx"))
            return FileFormat.EXCEL;
        else if (fileName.substring(length - 3, length).equals("ppt") || fileName.substring(length - 4, length).equals("pptx"))
            return FileFormat.PPT;
        else if (fileName.substring(length - 3, length).equals("pdf"))
            return FileFormat.EXCEL;
        else if (fileName.substring(length - 3, length).equals("txt"))
            return FileFormat.TXT;
        return FileFormat.NONE;
    }

    public enum FileFormat {
        WORD,
        PPT,
        EXCEL,
        PDF,
        TXT,
        NONE
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        Log.e("tabjin", filepath);
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        return degree;
    }

    public static String saveBitmap(Context context, String picName, Bitmap bm) {
        File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
