package com.zjhz.teacher.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zjhz.teacher.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class GlideUtil {
    private static Context mContext;

    public static void getContext(Context context) {
        mContext = context;
    }

    /**
     * @param url  加载的图片路径
     * @param view 把加载好的图片展示出来的ImageView
     */
    public static void loadImageBg(String url, final View view) {
        SimpleTarget target = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, GlideAnimation glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(resource);
                } else {
                    view.setBackgroundDrawable(resource);
                }
            }
        };
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(R.mipmap.header)  // 正在加载时显示的图片
                .error(R.mipmap.header)          // 加载失败时显示的图片
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().transform(new GlideRoundTransform(mContext, 4f)).into(target);
    }

    /**
     * 将图像转换为四个角有弧度的图像
     */
    public static class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform(Context context, float dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap ALL = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (ALL == null) {
                ALL = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(ALL);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
//            Log.e("11aa", radius + "");
            return ALL;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    /**
     * @param url       加载的图片路径
     * @param imageView 把加载好的图片展示出来的ImageView
     */
    public static void loadImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(R.mipmap.default_product_new)  // 正在加载时显示的图片
                .error(R.mipmap.default_product_new)          // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().into(imageView);
    }

    public static void loadImageWithDefault(String url, ImageView imageView, int defaultResource) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(defaultResource)  // 正在加载时显示的图片
                .error(defaultResource)
                // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().into(imageView);
    }

    /**
     * 加载头 像
     *
     * @param url
     * @param imageView
     */
    public static void loadImageHead(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
//                .placeholder(R.mipmap.defult_man)  // 正在加载时显示的图片
//                .error(R.mipmap.defult_man)  // 正在加载时显示的图片
                .dontAnimate()
                .placeholder(R.mipmap.header)   // 正在加载时显示的图片
                .error(R.mipmap.header)          // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * @param url       加载的图片路径
     * @param imageView 把加载好的图片展示出来的ImageView
     */
    public static void loadImageNews(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(R.mipmap.adapter_news_none)  // 正在加载时显示的图片
                .error(R.mipmap.adapter_news_none)          // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().into(imageView);
    }

    /**
     * 防止只有一张图片覆盖
     *
     * @param
     */
    public static void loadImageNull(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(R.mipmap.pic_add_icon)  // 正在加载时显示的图片
                .error(R.mipmap.pic_add_icon)          // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 防止只有一张图片覆盖
     *
     * @param
     */
    public static void loadImageNone(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void homeImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)  // 加载的图片路径
                .dontAnimate()
                .placeholder(R.mipmap.home_twelve_one)  // 正在加载时显示的图片
                .error(R.mipmap.home_twelve_one)          // 加载失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 获取应用下的图像
     *
     * @param context
     * @param avatar_url
     * @return
     */
    public static Bitmap getUserIcon(Context context, String avatar_url) {
        Bitmap bitmap = null;
        try {
            File file = new File(context.getFilesDir(), URLEncoder.encode(avatar_url, "utf-8"));
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 把用户的图像信息保存到应用文件夹里
     * 注意：用户登录后保存该文件
     *
     * @param context 上下文
     * @param image   bitmap类型图片
     * @param url     服务器上图片的url
     */
    public static void saveUserPicToFile(Context context, Bitmap image, String url) {
        FileOutputStream fos = null;
        try {
            File file = context.getFilesDir();        // 获取应用的目录
            File bitmapFile = new File(file, URLEncoder.encode(url, "utf-8"));
            fos = new FileOutputStream(bitmapFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存到本地文件
     *
     * @param inSream
     * @param file
     * @throws Exception readAsFile(inSream, new File(Environment.getExternalStorageDirectory()+"/"+"test.jpg"));
     *                   iv_pic.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/test.jpg")); 获取
     */
    public static void readAsFile(InputStream inSream, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();
    }

    public static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }
}
