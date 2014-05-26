package com.innovorder.innovorder;

import java.util.Date;

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
import android.widget.Toast;

public class WelcomeActivity extends Activity implements OnClickListener {

	private EditText nameEditText;

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
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		
		titleTextView.setTypeface(custom_font);
		descriptionTextView.setTypeface(custom_font);
		enterYourNameTextView.setTypeface(custom_font);
		startButton.setOnClickListener(this);
		startButton.setTypeface(custom_font);
		nameEditText.setTypeface(custom_font);
	}
	
	
	@Override
	public void onBackPressed() {
	    //moveTaskToBack(true);
		Toast.makeText(this, "T'en vas pas. Si tu l'aimes, t'en vas pas. Si tu l'aimes dis lui. Qu'elle est l'app de ta vie vie vie.", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		String name = nameEditText.getText().toString();
		if (name.isEmpty()) {
			return;
		}
		
		if (name.equals("exit123")) {
			finish();
			return;
		}
		
		Cart cart = Cart.getInstance();
		cart.empty();
		cart.setStartOrderDate(new Date());
		cart.setOrderName(name);
		
		Intent intent = new Intent(this, CarteActivity.class);
		intent.putExtra("refresh", true);
		startActivity(intent);
	}
}
