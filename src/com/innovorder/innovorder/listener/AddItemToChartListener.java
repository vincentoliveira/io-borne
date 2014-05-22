package com.innovorder.innovorder.listener;

import java.util.ArrayList;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemToChartListener implements OnClickListener, OnItemClickListener {

	private Context context;
	private ArrayList<View> views = new ArrayList<View>();

	public AddItemToChartListener(Context context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		String text = "";
		if (v instanceof Button) {
			text = (String) ((Button) v).getText();
		} else if (v instanceof TextView) {
			text = (String) ((TextView) v).getText();
		} else {
			return;
		}

		String expected = context.getString(R.string.btn_commander);
		if (text.equals(expected)) {
			addItem((Long) v.getTag());
		} else {
			if (v instanceof Button) {
				((Button) v).setText(expected);
				((Button) v).setTextColor(context.getResources().getColor(android.R.color.white));
				((Button) v).setTextSize(20);
			} else if (v instanceof TextView) {
				((TextView) v).setText(expected);
				((TextView) v).setTextColor(context.getResources().getColor(android.R.color.white));
				((TextView) v).setTextSize(20);
			}
			v.setBackgroundResource(R.color.numa_pink);
			views.add(v);
		}
	}
	
	public void clearViews() {
		for (View v : views) {
			if (v instanceof Button) {
				((Button) v).setText(context.getString(R.string.btn_ajouter_panier));
				((Button) v).setTextColor(context.getResources().getColor(R.color.numa_pink));
				((Button) v).setTextSize(30);
			} else if (v instanceof TextView) {
				((TextView) v).setText(context.getString(R.string.btn_ajouter_panier));
				((TextView) v).setTextColor(context.getResources().getColor(R.color.numa_pink));
				((TextView) v).setTextSize(30);
				v.setBackgroundResource(android.R.color.white);
			}
		}
		
		views.clear();
	}

	private void addItem(long id) {
		CarteItemStorage carteItemStorage = new CarteItemStorage(context);
		CarteItem item = carteItemStorage.find(id);

		Cart cart = Cart.getInstance();
		cart.addItem(item);
		
		Toast.makeText(context, "Article ajouté", Toast.LENGTH_SHORT).show();
		this.clearViews();
	}

}
