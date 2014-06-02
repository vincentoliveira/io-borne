package com.innovorder.innovorder;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.adapter.ItemListAdapter;
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

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");
		TextView promotedTextView = (TextView) findViewById(R.id.promotedTextView);
		promotedTextView.setTypeface(custom_font);
		
		getPromotedProduct();
	}
	
	public void getPromotedProduct() {
		CarteItem item1 = carteItemStorage.find(32);
		CarteItem item2 = carteItemStorage.find(31);
		CarteItem item3 = carteItemStorage.find(39);
		ArrayList<CarteItem> list = new ArrayList<CarteItem>();
		list.add(item1);
		list.add(item2);
		list.add(item3);
		
		GridView gridView = (GridView) findViewById(R.id.promotedGridView);
		
		ItemListAdapter gridViewAdapter = new ItemListAdapter(this, list, R.layout.dish_item);
		gridView.setAdapter(gridViewAdapter);
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
				Intent intent = new Intent(CarteActivity.this, WelcomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
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
