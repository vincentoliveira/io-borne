package com.innovorder.innovorder.webservice;

import java.io.InputStream;

import com.innovorder.innovorder.storage.BitmapStorage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	String url;
	Boolean isThumb;

	public DownloadImageTask(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		url = urls[0];

		Log.d("HttpConnection", "get image: " + url);

		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(url).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("DownloadImageTask", e.getMessage());
			e.printStackTrace();
			return null;
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		if (result == null)
			return;
		
		BitmapStorage.getInstance().addBitmapToMemoryCache(url, result);
		bmImage.setImageBitmap(result);
	}
}
