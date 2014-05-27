package com.innovorder.innovorder.Toast;

import com.innovorder.innovorder.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
	public static Toast makeText(Context context, String text, int duration, int iconResId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);

		TextView textView = (TextView) layout.findViewById(R.id.text);
		textView.setText(text);
		
		ImageView imageView = (ImageView) layout.findViewById(R.id.icon);
		if (iconResId != 0) {
			imageView.setImageResource(iconResId);
		} else {
			imageView.setVisibility(View.GONE);
		}
		
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Aachen_bt.ttf");
		textView.setTypeface(custom_font);
		
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		
		return toast;
	}
	
	public static Toast makeText(Context context, String text, int duration) {
		return makeText(context, text, duration, 0);
	}
	
	public static Toast makeText(Context context, int resId, int duration, int iconResId) {
		return makeText(context, context.getString(resId), duration, iconResId);
	}
	
	public static Toast makeText(Context context, int resId, int duration) {
		return makeText(context, context.getString(resId), duration, 0);
	}
}
