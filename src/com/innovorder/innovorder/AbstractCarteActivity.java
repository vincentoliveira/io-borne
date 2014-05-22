package com.innovorder.innovorder;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.innovorder.innovorder.adapter.NavDrawerListAdapter;
import com.innovorder.innovorder.model.CarteItem;

public abstract class AbstractCarteActivity extends FragmentActivity implements OnItemClickListener {
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;
	protected NavDrawerListAdapter adapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		
		Drawable d = getResources().getDrawable(R.drawable.header_logo);  
		getActionBar().setBackgroundDrawable(d);
		
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
			ChartDialogFragment chartFragment = new ChartDialogFragment();
			chartFragment.show(getSupportFragmentManager(), "chart");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_commande).setVisible(!drawerOpen);
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		CarteItem category = (CarteItem) adapter.getItem(position);
		if (category == null) {
			Intent intent = new Intent(this, CarteActivity.class);
			startActivity(intent);
			finish();

			return;
		}
		
		 Intent intent = new Intent(this, CategoryActivity.class);
		
		 intent.putExtra("category_name", category.getName());
		 intent.putExtra("category_id", category.getId());
		 startActivity(intent);
		
		 finish();
	}
}
