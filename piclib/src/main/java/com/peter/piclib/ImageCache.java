package com.peter.piclib;


import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {

    private LruCache<String, Bitmap> mCache;

    public ImageCache() {
        int maxMemory = (int) (Runtime.getRuntime().freeMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    void addBitmap(String imgUrl, Bitmap bitmap) {
        mCache.put(imgUrl, bitmap);
    }

    Bitmap getCache(String imgUrl) {
        return mCache.get(imgUrl);
    }

    void clearCache() {
        mCache.evictAll();
    }
}