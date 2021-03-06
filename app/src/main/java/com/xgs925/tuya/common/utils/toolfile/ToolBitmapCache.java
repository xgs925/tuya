package com.xgs925.tuya.common.utils.toolfile;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Photostsrs on 2017/3/20.
 */

public class ToolBitmapCache {
    private static ToolBitmapCache instance;
    private LruCache<String, Bitmap> bitmapLruCache;

    private ToolBitmapCache() {
        initLruCache();
    }

    public static ToolBitmapCache getInstance() {
        if (instance == null) {
            instance = new ToolBitmapCache();
        }
        return instance;
    }

    private void initLruCache() {
        int maxMem = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMem / 8;
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String path, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addBitmap(String path, Bitmap bitmap) {
        if(path==null) return;
        bitmapLruCache.remove(path);
        if(bitmap==null) return;
        bitmapLruCache.put(path, bitmap);
    }

    public void deleteBitmap(String path) {
        bitmapLruCache.remove(path);
    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap = bitmapLruCache.get(path);
        if(bitmap!=null) {
            Bitmap.Config config;
           if(bitmap.getConfig()== Bitmap.Config.RGB_565){
               config= Bitmap.Config.RGB_565;
           }else {
               config= Bitmap.Config.ARGB_8888;
           }
            return bitmap.copy(config,true);
        }else {
            return null;
        }
    }

    public void renameBitmap(String oldPath, String newPath) {
        Bitmap bitmap = getBitmap(oldPath);
        if (bitmap == null) return;
        deleteBitmap(oldPath);
        addBitmap(newPath, bitmap);
    }

    public void copy(String oldPath, String newPath) {
        Bitmap bitmap = getBitmap(oldPath);
        if (bitmap == null) return;
        addBitmap(newPath, Bitmap.createBitmap(bitmap));
    }
}
