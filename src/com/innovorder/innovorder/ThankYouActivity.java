package com.innovorder.innovorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ThankYouActivity extends Activity implements OnClickListener {

	private Timer timer;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);
		
		getActionBar().setDisplayShowHomeEnabled(false);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");

		Button startButton = (Button) findViewById(R.id.finishButton);
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		TextView description1TextView = (TextView) findViewById(R.id.description1TextView);
		TextView description2TextView = (TextView) findViewById(R.id.description2TextView);
		TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
		
		titleTextView.setTypeface(custom_font);
		description1TextView.setTypeface(custom_font);
		description2TextView.setTypeface(custom_font);
		startButton.setOnClickListener(this);
		startButton.setTypeface(custom_font);
		timeTextView.setTypeface(custom_font);
		
		Date date = new Date();
		date.setTime(date.getTime() + 5 * 60 * 1000);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("H'h'mm");
		timeTextView.setText(dateFormatter.format(date));
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  ThankYouActivity.this.timer = null;
			  ThankYouActivity.this.finish();
		  }
		}, 10*1000);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	public void finish() {
		if (timer != null) {
			timer.cancel();
		}
		
		super.finish();
	}
}


