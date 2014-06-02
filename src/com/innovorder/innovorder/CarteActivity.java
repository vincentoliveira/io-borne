package com.innovorder.innovorder;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.parser.CarteItemParser;
import com.innovorder.innovorder.storage.CarteItemStorage;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.webservice.GetCarteItemsCall;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

public class CarteActivity extends AbstractCarteActivity implements WebserviceCallListener, OnItemClickListener {	
	private CarteItemStorage carteItemStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carte);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		carteItemStorage = new CarteItemStorage(this);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.getBoolean("refresh")) {
				getCarteItems();
			}
		}
		
		List<CarteItem> categories = carteItemStorage.getMainCategories();
		setLeftMenu(categories);
		
		//mDrawerLayout.openDrawer(mDrawerList);
		
		View seeMyChartButtonLayout = findViewById(R.id.seeMyChartButtonLayout);
		seeMyChartButtonLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CarteActivity.this.openCartDialog();
			}
		});
		
		ImageView mainImageView = (ImageView) findViewById(R.id.mainImageView);
		mainImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CarteActivity.this.mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		TextView seeMyCartTextView = (TextView) findViewById(R.id.seeMyCartTextView);
		String text = getString(R.string.see_my_cart) + " (" + Cart.getInstance().getItems().size() + ")";
		seeMyCartTextView.setText(text);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");
		seeMyCartTextView.setTypeface(custom_font);
		
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// moveTaskToBack(true);
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(R.string.cancel)
		.setMessage(R.string.really_cancel)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Stop the activity
				Cart.getInstance().empty();
				CarteActivity.this.finish();
			}

		}).setNegativeButton(R.string.no, null).show();
	}
	
	private void getCarteItems() {
		// get username/password
		LoginManager loginManager = new LoginManager(this);
		if (!loginManager.isAthentificated()) {
			finish();
			return;
		}
		String restaurant = loginManager.getRestaurant();
		String password = loginManager.getPassword();

		GetCarteItemsCall getCarteItems = new GetCarteItemsCall();
		getCarteItems.setParentActivity(this);
		getCarteItems.execute(restaurant, password);
	}
	

	@Override
	public void onWebserviceCallFinished(String response, String tag) {

		if (tag.equals("getCarteItems")) {
			ArrayList<CarteItem> items = null;
			if (response != null) {
				items = CarteItemParser.parse(response);
			}
			
			if (items == null) {
				CustomToast.makeText(this, R.string.error_unknown, Toast.LENGTH_LONG).show();
				return;
			} else {
				carteItemStorage.clearAll();
				for (CarteItem item : items) {
					carteItemStorage.addItem(item);
				}
			}
			
			List<CarteItem> categories = carteItemStorage.getItemsByParentId(0);
			setLeftMenu(categories);
		} else {
			super.onWebserviceCallFinished(response, tag);
		}
	}
}
