package com.zjhz.teacher.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@SuppressLint("NewApi")
public class CMCPhotoUtil {

	// ---------------------------------------------------------------------------------------------------------------------------------------
	public static File getImageFile2(Context mContext) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(mContext.getExternalCacheDir(),
					UUID.randomUUID() + "temp.jpg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			return file;
		} else {
			File file = new File(mContext.getCacheDir(), UUID.randomUUID()
					+ "temp.jpg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}

	/** 在磁盘上创建临时空文件[用于保存照相的图片或者选取的图片进行处理] */
	public static File getImageFile(Context mContext) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(mContext.getExternalCacheDir(), "temp.jpg"); // storage/emulated/0/Android/data/com.qts.customer/cache
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			return file;
		} else {
			File file = new File(mContext.getCacheDir(), "temp.jpg"); // data/data/com.qts.customer/cache
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}

	/** 在磁盘上创建临时空文件[用于保存照相的图片或者选取的图片进行处理] */
	public static File getImageFile(Context mContext, String picname) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(mContext.getExternalCacheDir(), picname); // storage/emulated/0/Android/data/com.qts.customer/cache
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			return file;
		} else {
			File file = new File(mContext.getCacheDir(),picname); // data/data/com.qts.customer/cache
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}

	// 参考:http://blog.csdn.net/tempersitu/article/details/20557383
	// 参考2:http://blog.csdn.net/zbjdsbj/article/details/42387551
	/** 4.4以上选取相册 */
	public static String GetPhotoPath(Context context, Intent data) {
		try {

			Uri uri = data.getData();
			final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT; // 是否是4.4以上
			// DocumentProvider
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
				// ExternalStorageProvider
				if (isExternalStorageDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];
					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/"
								+ split[1];
					}
					// TODO handle non-primary volumes
				}
				// DownloadsProvider
				else if (isDownloadsDocument(uri)) {
					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"),
							Long.valueOf(id));
					return getDataColumn(context, contentUri, null, null);
				}
				// MediaProvider
				else if (isMediaDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}
					final String selection = "_id=?";
					final String[] selectionArgs = new String[] { split[1] };
					return getDataColumn(context, contentUri, selection,
							selectionArgs);
				}

			}

			// MediaStore (and general)
			else if ("content".equalsIgnoreCase(uri.getScheme())) {
				// Return the remote address
				if (isGooglePhotosUri(uri))
					return uri.getLastPathSegment();
				return getDataColumn(context, uri, null, null);
			}
			// File
			else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private static String getDataColumn(Context context, Uri uri,
										String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	private static String selectImage(Context context, Intent data) {
		String filePath = data.getData().getEncodedPath();
		if (filePath != null) {
			filePath = Uri.decode(filePath);
			ContentResolver cr = context.getContentResolver();
			StringBuffer buff = new StringBuffer();
			buff.append("(").append(Images.ImageColumns.DATA).append("=")
					.append("'" + filePath + "'").append(")");
			Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { Images.ImageColumns._ID,
							Images.ImageColumns.DATA }, buff.toString(), null,
					null);
			int index = 0;
			int dataIdx = 0;
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				index = cur.getColumnIndex(Images.ImageColumns._ID);
				index = cur.getInt(index);
				dataIdx = cur.getColumnIndex(Images.ImageColumns.DATA);
				filePath = cur.getString(dataIdx);
			}
			cur.close();
		}
		return filePath;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** 压缩图片 */
	public static Bitmap CompresPhoto(String filePath, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);// 压缩图片
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);// 处理图片旋转
		bm = rotateBitmap(bm, degree);

		double maxSize = 1500.00;// 限制的文件大小
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bm = zoomImage(bm, bm.getWidth() / Math.abs(i), bm.getHeight()
					/ Math.abs(i));
		}
		return bm;

	}

	/** 压缩图片 */
	public static Bitmap CompresPhoto2(String filePath, int width, int height) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);// 压缩图片
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);// 处理图片旋转
		bm = rotateBitmap(bm, degree);
		int bytecount = bm.getRowBytes() * bm.getHeight(); // 获取字节数

		ByteArrayOutputStream baos = null;
		try {
			// 大于2m 进行再压缩
			if (bytecount > 2 * 1024 * 1024) {
				baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); // 70
																	// 是压缩率，表示压缩30%;
																	// 如果不压缩是100，表示压缩率为0

			}

		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	public static Bitmap CompresPhoto_bitmap(Bitmap bmp, int width, int height) {
		double maxSize = 1500.00;// 限制的文件大小
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bmp = zoomImage(bmp, bmp.getWidth() / Math.abs(i), bmp.getHeight()
					/ Math.abs(i));
		}
		return bmp;

	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
								   double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	// -------------------------------------------------------------------
	/** 查看图片旋转角度 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/** 获取压缩比 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/** 把旋转的图片恢复正常角度 */
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	/**
	 * 把Bitmap保存为File
	 * 
	 * @param bitmap
	 * @param file
	 */
	public static void SaveBitmapFile(Bitmap bitmap, File file) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将bitMap换一个文件名保存
	 * 
	 * @param filePath
	 * @param fileName
	 * @param bitmap
	 */
	public static void saveBitMapFile(String filePath, String fileName, Bitmap bitmap) {
		if (null == bitmap)
			return;
		File f = new File(filePath, fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitMap(String filePath) {
		Bitmap bitmap=null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			bitmap= BitmapFactory.decodeFile(filePath, options);
		}
		catch (Exception e) {
			return null;
		}
		return bitmap;
	}

	/**图片转换为byte[]*/
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
