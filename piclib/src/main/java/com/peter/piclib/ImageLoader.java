package com.peter.piclib;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

    private final String TAG = getClass().getSimpleName();
    private ImageDownloader imageDownloader;
    private ImageCache imageCache;

    private ImageLoader(){
        imageCache = new ImageCache();
        imageDownloader = new ImageDownloader(imageCache);
    }

    private volatile static ImageLoader sInstance;

    public static ImageLoader getInstance(){
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                sInstance = new ImageLoader();
            }
        }
        return sInstance;
    }

    public void loadImage(String url, ProgressLoadListener listener) {
        loadImage(url, null, listener);
    }

    public void loadImage(Uri uri, final ImageView imageView, ProgressLoadListener listener) {
        loadImage(String.valueOf(uri), imageView, listener);
    }

    public void loadImage(String url, final ImageView imageView, ProgressLoadListener listener) {
        Log.d(TAG, "image url : " + url);

        Bitmap cacheBitmap = imageCache.getCache(url);
        if (cacheBitmap != null) {
            listener.update(cacheBitmap);
        } else {
            imageDownloader.decodeResizeImage(url, imageView, listener);
        }
    }

    public void loadOriginalImage(String url, ImageView imageView) {
        Log.d(TAG, "image url : " + url);

        imageView.setImageBitmap(imageDownloader.decodeImage(url));
    }

}
