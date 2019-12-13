package com.peter.piclib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageDownloader {

    private ImageDownloader() {
    }

    public ImageDownloader(ImageCache imageCache) {
        this.imageCache = imageCache;
    }

    private final String TAG = getClass().getSimpleName();

    private final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    private final int connectTimeout = 5 * 1000;

    private final int readTimeout = 20* 1000;

    private ImageCache imageCache;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ThreadFactory threadFactory = new ThreadFactory() {
        @Override public Thread newThread(Runnable runnable) {
            Thread result = new Thread(runnable, "ImageLoader Dispatcher");
            result.setDaemon(false);
            return result;
        }
    };

    private ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), threadFactory);

    Bitmap decodeImage(String imgUrl) {

        InputStream imageStream;
        try {
            HttpURLConnection conn = createConnection(imgUrl);
            imageStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return BitmapFactory.decodeStream(imageStream);
    }

    void decodeResizeImage(final String imgUrl, final ProgressLoadListener listener) {
        decodeResizeImage(imgUrl, null, listener);
    }

    void decodeResizeImage(final String imgUrl, final ImageView imageView, final ProgressLoadListener listener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                InputStream imageStream = null;
                try {
                    String encodedUrl = Uri.encode(imgUrl, ALLOWED_URI_CHARS);
                    HttpURLConnection conn = createConnection(encodedUrl);
                    imageStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = resizeBitmap(imageStream, imageView);
                if (bitmap != null) {
                    imageCache.addBitmap(imgUrl, bitmap);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.update(bitmap);
                        }
                    });
                } else {
                    Log.d(TAG, "Bitmap is null after resizeBitmap");
                }
            }
        });
    }

    private Bitmap resizeBitmap(InputStream is, ImageView imageView) {
        if (is == null) {
            return null;
        }
        if (imageView == null) {
            return BitmapFactory.decodeStream(is);
        }

        Bitmap resizeBitmap = null;

        is = new BufferedInputStream(is);
        try {
            is.mark(is.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        if (imageView.getHeight() > options.outHeight || imageView.getWidth() > options.outWidth) {
            try {
                is.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            float insamplesize = calculateInSampleSize(options.outWidth, options.outHeight, imageView.getWidth(), imageView.getHeight());
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            // 容器尺寸过大
            if (bitmap != null) {
                Matrix matrix = new Matrix();
                matrix.postScale(insamplesize, insamplesize);
                Log.d(TAG, "before scale : width : " + bitmap.getWidth() + ", heoight : " + bitmap.getHeight());
                resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                Log.d(TAG, "after scale : width : " + bitmap.getWidth() + ", height : " + bitmap.getHeight());

                if (resizeBitmap != bitmap) {
                    bitmap.recycle();
                }
            }
        } else {
            try {
                is.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            options.inJustDecodeBounds = false;
            resizeBitmap = BitmapFactory.decodeStream(is, null, options);
        }

        return resizeBitmap;
    }

    private float calculateInSampleSize(int picWidth, int picHeight, int imageViewWidth, int imageViewHeight) {
        float insamplesize = 1;
        if (picHeight > imageViewHeight || picWidth > imageViewWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int halfHeight = imageViewHeight / 2;
            final int halfWidth = imageViewWidth / 2;
            while ((halfHeight / insamplesize) > imageViewHeight && (halfWidth / insamplesize) > imageViewWidth) {
                insamplesize *= 2;
            }
        } else {
            if (((float)imageViewHeight / (float)picHeight) < ((float)imageViewWidth / (float)picWidth)) {
                insamplesize = (float)imageViewHeight / (float) picHeight;
            }else {
                insamplesize = (float)imageViewWidth / (float)picWidth;
            }
        }
        return insamplesize;
    }

    private HttpURLConnection createConnection(String url) throws IOException {
        String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        return conn;
    }
}
