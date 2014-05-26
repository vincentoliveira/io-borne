package com.innovorder.innovorder.storage;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.webservice.DownloadImageTask;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class BitmapStorage {

	private LruCache<String, Bitmap> mMemoryCache;
	private static BitmapStorage instance = null;
	private boolean init;
	
	private BitmapStorage() {
		init = false;
	}
	
	public static BitmapStorage getInstance()
	{
		if (instance == null) {
			instance = new BitmapStorage();
		}
		
		return instance;
	}
	
	public boolean isInit() {
		return this.init;
	}
	
	public void init(int cacheSize) {
	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };
	    this.init = true;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		Log.i("BitmapStorage", "Add: " + key);
	    if (mMemoryCache != null && getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
		Log.i("BitmapStorage", "Get: " + key);
		if (mMemoryCache == null) {
			return null;
		}
		
	    return mMemoryCache.get(key);
	}
	
	public void loadBitmap(String imageKey, ImageView imageView) {
		Log.i("BitmapStorage", "Load: " + imageKey);
	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	imageView.setImageBitmap(bitmap);
	    } else {
	    	imageView.setImageResource(R.drawable.ic_launcher);
	    	DownloadImageTask task = new DownloadImageTask(imageView);
	        task.execute(imageKey);
	    }
	}
}
