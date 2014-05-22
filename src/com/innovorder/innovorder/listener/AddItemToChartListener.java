package com.innovorder.innovorder.listener;

import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class AddItemToChartListener implements OnClickListener, OnItemClickListener {

	private Context context;

	public AddItemToChartListener(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
		addItem((Long) v.getTag());
	}
	
	private void addItem(long id) {
		CarteItemStorage carteItemStorage = new CarteItemStorage(context);
		CarteItem item = carteItemStorage.find(id);
		
		Cart cart = Cart.getInstance();
		cart.addItem(item);
	}

}
