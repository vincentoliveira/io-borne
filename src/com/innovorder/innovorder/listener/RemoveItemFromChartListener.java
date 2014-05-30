package com.innovorder.innovorder.listener;

import com.innovorder.innovorder.AbstractCarteActivity;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public class RemoveItemFromChartListener implements OnClickListener {

	private Context context;
	private BaseAdapter adapter;

	public RemoveItemFromChartListener(Context context)
	{
		this.context = context;
	}
	
	public void setAdapter(BaseAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		removeItem((Long) v.getTag());
		
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		
		if (context instanceof AbstractCarteActivity) {
			((AbstractCarteActivity) context).reloadMenu();
		}
	}
	
	private void removeItem(long id) {
		CarteItemStorage carteItemStorage = new CarteItemStorage(context);
		CarteItem item = carteItemStorage.find(id);

		Cart cart = Cart.getInstance();
		cart.removeItem(item);
	}


}
