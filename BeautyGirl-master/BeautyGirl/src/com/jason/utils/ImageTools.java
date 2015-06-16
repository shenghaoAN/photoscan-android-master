package com.jason.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Tools for handler picture
 */
public final class ImageTools {

    /**
     * Transfer drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Input stream to bitmap
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Bitmap inputStreamToBitmap(InputStream inputStream)
            throws Exception {
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Byte transfer to bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Byte transfer to drawable
     *
     * @param byteArray
     * @return
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    /**
     * Bitmap transfer to bytes
     *
     * @param byteArray
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Drawable transfer to bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);
        ;
        return bytes;
    }

    /**
     * Base64 to byte[]
     //	 */
//	public static byte[] base64ToBytes(String base64) throws IOException {
//		byte[] bytes = Base64.decode(base64);
//		return bytes;
//	}
//
//	/**
//	 * Byte[] to base64
//	 */
//	public static String bytesTobase64(byte[] bytes) {
//		String base64 = Base64.encode(bytes);
//		return base64;
//	}

    /**
     * Create reflection images
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * Get rounded corner images
     *
     * @param bitmap
     * @param roundPx 5 10
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * Resize the drawable
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Context context, Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(context.getResources(), newbmp);
    }

    /**
     * Get images from SD card by path and the name of image
     *
     * @param photoName
     * @return
     */
    public static Bitmap getPhotoFromSDCard(String path, String photoName) {
        Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
        if (photoBitmap == null) {
            return null;
        } else {
            return photoBitmap;
        }
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Get image from SD card by path and the name of image
     *
     * @param fileName
     * @return
     */
    public static boolean findPhotoFromSDCard(String path, String photoName) {
        boolean flag = false;

        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (dir.exists()) {
                File folders = new File(path);
                File photoFile[] = folders.listFiles();
                for (int i = 0; i < photoFile.length; i++) {
                    String fileName = photoFile[i].getName().split("\\.")[0];
                    if (fileName.equals(photoName)) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * Save image to the SD card
     *
     * @param context     上下文
     * @param photoBitmap 要保存的图片
     * @param photoName   文件名
     * @param path        文件路径
     * @param isRefresh   是否刷新图库
     * @throws java.io.IOException
     */
    public static String savePhotoToSDCard(Context context, Bitmap photoBitmap, String path, String photoName, boolean isRefresh) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName);
            if (!photoFile.exists()) {
                try {
                    photoFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                        if (isRefresh) {
                            // 刷新圖庫
                            Uri localUri = Uri.fromFile(photoFile);
                            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    localUri);
                            context.sendBroadcast(localIntent);
                        }
                        return photoFile.getAbsolutePath();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     *
     * createFile
     *
     * @param path 文件路径
     * @param name 文件名
     * @return file
     */
    public static File createFile(String path, String name) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path, name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return file;
        }
        return null;
    }

    /**
     * Delete the image from SD card
     *
     * @param context
     * @param path    file:///sdcard/temp.jpg
     */

    public static void deleteAllPhoto(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            deleteAllFile(folder);
        }
    }


    /**
     * delete file
     * 删除所有文件包括文件夹
     *
     * @param file
     */
    public static void deleteAllFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteAllFile(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }


    /**
     * 保存文件,刷新图库
     *
     * @param bm
     * @param fileName
     * @throws java.io.IOException
     */
    public void saveFile(Context context, Bitmap bm, String fileName, String ALBUM_PATH) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(ALBUM_PATH, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        // 刷新圖庫
        Uri localUri = Uri.fromFile(myCaptureFile);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                localUri);
        context.sendBroadcast(localIntent);
        bos.flush();
        bos.close();
    }

    /**
     * @param path   文件路径
     * @param width  要压缩的宽度
     * @param height 要压缩的高度
     *               <p/>
     *               读取路径中的图片，然后将其转化为缩放后的bitmap
     *               图片大小压缩
     */
    public static Bitmap saveBefore(String path, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        System.out.println("original width=" + options.outWidth + "  original height=" + options.outHeight);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(path, options);
        int degree = RotateBitmap.readPictureDegree(path);
        bitmap = RotateBitmap.rotateBitmap(bitmap, degree);
        System.out.println("------degree----" + degree);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h);
        saveJPGE_After(bitmap, path);
        return bitmap;
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return 取得合适的bitmap大小
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int width = options.outWidth;
//		final int height = options.outHeight;
//        float scaleWidth = ((float) reqWidth) / width;
//        float scaleHeight = ((float) reqHeight) / height;
//        if(reqWidth == 0){
//            reqWidth = (int)scaleWidth;
//        }
//        if(reqHeight == 0){
//            reqHeight = (int)scaleHeight;
//        }
        int inSampleSize = 1;

//		if (height > reqHeight || width > reqWidth) {
//			if (width > height) {
//				inSampleSize = Math.round((float) height / (float) reqHeight);
//			} else {
//				inSampleSize = Math.round((float) width / (float) reqWidth);
//			}
//		}
        //width > reqWidth壓縮寬度，高度自適應
        if (width > reqWidth) {
            inSampleSize = (int) Math.ceil((float) width / (float) reqWidth);
            // inSampleSize只能是2的次幂
            if (inSampleSize >= 3 && inSampleSize < 8)
                inSampleSize = 4;
            else if (inSampleSize == 8)
                inSampleSize = 8;
        }
        System.out.println("inSampleSize=" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 获取合适的Bitmap平时获取Bitmap就用这个方法.
     *
     * @param path    路径.
     * @param data    byte[]数组.
     * @param context 上下文
     * @param uri     uri
     * @param target  模板宽或者高的大小.
     * @param width   是否是宽度
     * @return
     */
    public static Bitmap getResizedBitmap(String path, byte[] data,
                                          Context context, Uri uri, int target, boolean width) {
        Options options = null;

        if (target > 0) {

            Options info = new Options();
            //这里设置true的时候，decode时候Bitmap返回的为空，
            //将图片宽高读取放在Options里.
            info.inJustDecodeBounds = true;
            decode(path, data, context, uri, info);
            int dim = info.outWidth;
            System.out.println("---original width--" + dim);
            System.out.println("---original height--" + info.outWidth);
            if (!width)
                dim = Math.max(dim, info.outHeight);
            int ssize = sampleSize(dim, target);

            options = new Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = ssize;

        }
        Bitmap bm = null;
        try {
            bm = decode(path, data, context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;

    }

    /**
     * 解析Bitmap的公用方法.
     *
     * @param path
     * @param data
     * @param context
     * @param uri
     * @param options
     * @return
     */
    public static Bitmap decode(String path, byte[] data, Context context,
                                Uri uri, BitmapFactory.Options options) {

        Bitmap result = null;

        if (path != null) {

            result = BitmapFactory.decodeFile(path, options);

        } else if (data != null) {

            result = BitmapFactory.decodeByteArray(data, 0, data.length,
                    options);

        } else if (uri != null) {
            //uri不为空的时候context也不要为空.
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;

            try {
                inputStream = cr.openInputStream(uri);
                result = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    /**
     * 获取合适的sampleSize.
     * 这里就简单实现都是2的倍数啦.
     *
     * @param width
     * @param target
     * @return
     */
    private static int sampleSize(int width, int target) {
        int result = 1;
        for (int i = 0; i < 10; i++) {
            if (width < target * 2) {
                break;
            }
            width = width / 2;
            result = result * 2;
        }
        return result;
    }

    /**
     * 回收内存
     *
     * @param imageView
     */
    public static void recycleImage(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable != null
                        && bitmapDrawable.getBitmap() != null
                        && !bitmapDrawable.getBitmap().isRecycled()) {
                    imageView.setImageDrawable(null);
                    bitmapDrawable.setCallback(null);
                    if (!bitmapDrawable.getBitmap().isRecycled()) {
                        bitmapDrawable.getBitmap().recycle();
                    }
                }
            }
        }
    }

    /**
     * 获取图片文件夹
     *
     * @return path
     */
    public static String getUploadPathFolder() {
        return Environment
                .getExternalStorageDirectory().getPath() + "/XingbaoHuiUpload/";
    }

    /**
     * 随机获取图片文件名
     *
     * @return fileName
     */
    public static String getFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }
}
