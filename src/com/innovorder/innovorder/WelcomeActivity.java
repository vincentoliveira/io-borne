package com.innovorder.innovorder;

import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.model.Cart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements OnClickListener, OnEditorActionListener {

	private EditText nameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		getActionBar().setDisplayShowHomeEnabled(false);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");

		Button startButton = (Button) findViewById(R.id.startButton);
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		nameEditText = (EditText) findViewById(R.id.nameEditText);

		titleTextView.setTypeface(custom_font);
		descriptionTextView.setTypeface(custom_font);
		startButton.setOnClickListener(this);
		startButton.setTypeface(custom_font);
		nameEditText.setTypeface(custom_font);
		nameEditText.setOnEditorActionListener(this);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.getBoolean("thanks")) {
				Intent intent = new Intent(this, ThankYouActivity.class);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (nameEditText != null) {
			nameEditText.setText("");
		}
	}

	@Override
	public void onBackPressed() {
		// moveTaskToBack(true);
		CustomToast.makeText(this, R.string.not_allowed_to_quit, Toast.LENGTH_LONG, R.drawable.icon_warning).show();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
			start();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		start();
	}

	private void start() {
		String name = nameEditText.getText().toString();
		if (name.isEmpty()) {
			return;
		}

		if (name.equals("exit123")) {
			finish();
			return;
		}

		Cart cart = Cart.getInstance();
		cart.reinit();
		cart.setOrderName(name);
//
//		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//		startActivityForResult(intent, 0);

		 Intent intent = new Intent(this, CarteActivity.class);
		 intent.putExtra("refresh", true);
		 startActivity(intent);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			// Handle scan intent
			if (resultCode == Activity.RESULT_OK) {
				// Do nothing
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
			}
		} else {
			// Handle other intents
		}

	}
}
