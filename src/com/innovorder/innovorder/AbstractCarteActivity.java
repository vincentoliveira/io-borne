package com.innovorder.innovorder;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.innovorder.innovorder.adapter.NavDrawerListAdapter;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.BitmapStorage;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

public abstract class AbstractCarteActivity extends FragmentActivity implements WebserviceCallListener, OnItemClickListener {
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;
	protected NavDrawerListAdapter adapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);

		BitmapStorage bitmapStorage = BitmapStorage.getInstance();
		if (!bitmapStorage.isInit()) {
			int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			int cacheSize = maxMemory / 4;
			bitmapStorage.init(cacheSize);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_commande:
			openCartDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void openCartDialog() {
		ReceiptDialogFragment chartFragment = new ReceiptDialogFragment();
		chartFragment.show(getSupportFragmentManager(), "chart");
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_commande).setVisible(!drawerOpen);
		
		String text = "(" + Cart.getInstance().getItems().size() + ")";
		menu.getItem(0).setTitle(text);
		
		return super.onPrepareOptionsMenu(menu);
	}

	protected void setLeftMenu(List<CarteItem> items) {
		// Set menu
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), items);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(this);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		CarteItem category = (CarteItem) adapter.getItem(position);
		if (category == null) {
			Intent intent = new Intent(this, CarteActivity.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();

			return;
		}

		Intent intent = new Intent(this, CategoryActivity.class);

		intent.putExtra("category_name", category.getName());
		intent.putExtra("category_id", category.getId());
		startActivity(intent);
	}

	@Override
	public void onWebserviceCallFinished(String response, String tag) {
		if (tag.equals("order")) {
			//TODO: check order result
			
			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.putExtra("thanks", true);
			startActivity(intent);
		}
	}
	
	public void reloadView() {
		invalidateOptionsMenu();
	}

	public void onItemAdd() {
		Intent intent = new Intent(this, ReceiptActivity.class);
		startActivity(intent);
	}

	protected void cancelOrder() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.cancel).setMessage(R.string.really_cancel).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Stop the activity
				Intent intent = new Intent(AbstractCarteActivity.this, WelcomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}

		}).setNegativeButton(R.string.no, null).show();
	}
}
