package com.innovorder.innovorder.listener;

import java.util.ArrayList;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemToChartListener implements OnClickListener {

	private Context context;
	private ArrayList<View> views = new ArrayList<View>();
	private BaseAdapter adapter;

	public AddItemToChartListener(Context context) {
		this.context = context;
	}
	
	public void setAdapter(BaseAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		addItem((Long) v.getTag());

		if (adapter != null) {
			adapter.notifyDataSetChanged();
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
