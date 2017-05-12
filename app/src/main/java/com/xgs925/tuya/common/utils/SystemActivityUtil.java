package com.xgs925.tuya.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;

import com.xgs925.tuya.base.AppContext;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xgs925.tuya.common.utils.common.ToastUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;

import rx.functions.Action1;


/**
 * Created by Photostsrs on 2017/3/2.
 */

public class SystemActivityUtil {
    public static void startAlbumActivityForResult(final Activity activity, final int requestCode) {
        RxPermissions permissions = new RxPermissions(activity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                subscribe(new Action1<Boolean>() {
                              @Override
                              public void call(Boolean aBoolean) {
                                  if (aBoolean) {
                                      Intent intent = new Intent(Intent.ACTION_PICK);
                                      intent.setType("image/*");
                                      activity.startActivityForResult(intent, requestCode);
                                  } else {
                                      ToastUtil.show("no permission");
                                  }
                              }
                          }

                );
    }

    public interface IAlbumActivityCallback {
        void onAlbumActivityForResult(Bitmap bitmap);

        void onAlbumActivityForResult(Uri uri);
    }

    public static class AlbumActivityCallback implements IAlbumActivityCallback {

        @Override
        public void onAlbumActivityForResult(Bitmap bitmap) {

        }

        @Override
        public void onAlbumActivityForResult(Uri uri) {

        }
    }

    @Deprecated
    public static void endAlbumActivityForResult(Activity activity, Intent data, AlbumActivityCallback callback) {
        if (data == null) return;
        Bitmap bitmap = null;
        Uri uri = data.getData();
        bitmap = getBitmapByUri(uri);
        if (bitmap != null) callback.onAlbumActivityForResult(bitmap);
    }

    public static void endAlbumActivityForResult(Intent data, AlbumActivityCallback callback) {
        if (data == null) return;
        Bitmap bitmap = null;
        Uri uri = data.getData();
        bitmap = getBitmapByUri(uri);
        if (bitmap != null) callback.onAlbumActivityForResult(bitmap);
    }

    @Deprecated
    public static void endAlbumActivityForResult2Uri(Intent data, AlbumActivityCallback callback) {
        if (data == null) return;
        Uri uri = data.getData();
        if (uri != null) {
            callback.onAlbumActivityForResult(uri);
        }
    }


    public static Bitmap getBitmapByUri(Uri uri) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                InputStream input = AppContext.mApplicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                input.close();
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                Point size = BitmapUtil.ImageSampleFun(imageWidth, imageHeight, 2);
                InputStream input2 = AppContext.mApplicationContext.getContentResolver().openInputStream(uri);
                bitmap = decodeSampledBitmapByPath(input2, options, size.x, size.y);
                input2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private static Bitmap decodeSampledBitmapByPath(InputStream input,BitmapFactory.Options options, int reqWidth, int reqHeight) {
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(input, null, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
