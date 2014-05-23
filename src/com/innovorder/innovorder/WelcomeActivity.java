package com.innovorder.innovorder;

import com.innovorder.innovorder.model.Cart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		Drawable d = getResources().getDrawable(R.drawable.header_logo);  
		getActionBar().setBackgroundDrawable(d);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");

		Button startButton = (Button) findViewById(R.id.startButton);
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		TextView enterYourNameTextView = (TextView) findViewById(R.id.enterYourNameTextView);
		EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
		
		titleTextView.setTypeface(custom_font);
		descriptionTextView.setTypeface(custom_font);
		enterYourNameTextView.setTypeface(custom_font);
		startButton.setOnClickListener(this);
		startButton.setTypeface(custom_font);
		nameEditText.setTypeface(custom_font);
		
	}

	@Override
	public void onClick(View v) {
		Cart.getInstance().empty();
		
		Intent intent = new Intent(this, CarteActivity.class);
		intent.putExtra("refresh", true);
		startActivity(intent);
	}
}