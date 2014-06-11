package com.innovorder.innovorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.Payment;
import com.innovorder.innovorder.parser.LydiaPaymentParser;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.utils.PriceFormatter;
import com.innovorder.innovorder.webservice.LydiaPaymentCall;
import com.innovorder.innovorder.webservice.PaymentCall;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ThankYouActivity extends Activity implements OnClickListener, WebserviceCallListener {

	private Timer timer;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);

		getActionBar().setDisplayShowHomeEnabled(false);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");

		Button finishButton = (Button) findViewById(R.id.finishButton);
		ImageButton lydiaButton = (ImageButton) findViewById(R.id.lydiaButton);
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		TextView description1TextView = (TextView) findViewById(R.id.description1TextView);
		TextView description2TextView = (TextView) findViewById(R.id.description2TextView);
		TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
		TextView choosePaymentTextView = (TextView) findViewById(R.id.choosePaymentTextView);
		TextView amountTextView = (TextView) findViewById(R.id.amountTextView);
		
		titleTextView.setTypeface(custom_font);
		description1TextView.setTypeface(custom_font);
		description2TextView.setTypeface(custom_font);
		finishButton.setOnClickListener(this);
		finishButton.setTypeface(custom_font);
		timeTextView.setTypeface(custom_font);
		choosePaymentTextView.setTypeface(custom_font);
		amountTextView.setTypeface(custom_font);

		lydiaButton.setOnClickListener(this);

		Date date = new Date();
		date.setTime(date.getTime() + 5 * 60 * 1000);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("H'h'mm");
		timeTextView.setText(dateFormatter.format(date));


		Cart cart = Cart.getInstance();
		description1TextView.setText(getString(R.string.thank_you_description1, cart.getOrderName()));
		amountTextView.setText(PriceFormatter.format(cart.getServerPrice()));

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ThankYouActivity.this.timer = null;
				ThankYouActivity.this.finish();
			}
		}, 10 * 2000);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			
			Log.d("scanContent", scanContent);

			Cart cart = Cart.getInstance();
			Payment payment = new Payment();
			payment.setAmount(cart.getServerPrice());
			cart.setPayment(payment);
			
			LydiaPaymentCall lydiaCall = new LydiaPaymentCall();
			lydiaCall.setParentActivity(this);
			lydiaCall.execute(scanContent);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.lydiaButton) {
			if (timer != null) {
				timer.cancel();
			}
			
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		} else if (v.getId() == R.id.finishButton) {
			finish();
		}
	}

	@Override
	public void finish() {
		if (timer != null) {
			timer.cancel();
		}

		super.finish();
	}

	@Override
	public void onWebserviceCallFinished(String response, String tag) {
		Cart cart = Cart.getInstance();
		Payment payment = cart.getPayment();
		
		if (tag.equals("lydiaPayment")) {
			LydiaPaymentParser parser = new LydiaPaymentParser();
			parser.parse(response);
			payment.setType(getString(R.string.payment_type_lydia));
			if (parser.getError() == 0) {
				payment.setStatus(getString(R.string.payment_status_success));
				payment.setTransactionId(parser.getTransactionId());
			} else {
				payment.setStatus(getString(R.string.payment_status_error));
				payment.addComment("ERROR:" + parser.getError());
				payment.addComment(parser.getMessage());
			}
			
			LoginManager loginManager = new LoginManager(this);
			String restaurant = loginManager.getRestaurant();
			String password = loginManager.getPassword();
			PaymentCall call = new PaymentCall();
			call.setParentActivity(this);
			call.execute(restaurant, password);
			
			setPaymentResut(parser.getError() == 0);
		}
	}
	
	public void setPaymentResut(boolean success) {
		TextView choosePaymentTextView = (TextView) findViewById(R.id.choosePaymentTextView);
		ImageButton lydiaButton = (ImageButton) findViewById(R.id.lydiaButton);
		TextView amountTextView = (TextView) findViewById(R.id.amountTextView);
		Button finishButton = (Button) findViewById(R.id.finishButton);

		choosePaymentTextView.setVisibility(View.INVISIBLE);
		lydiaButton.setVisibility(View.GONE);
		if (success) {
			amountTextView.setText(R.string.payment_success);
		} else {
			amountTextView.setText(R.string.payment_error);
		}
		finishButton.setText(R.string.finish);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ThankYouActivity.this.timer = null;
				ThankYouActivity.this.finish();
			}
		}, 10 * 2000);
	}
}
