package com.innovorder.innovorder.listener;


import com.innovorder.innovorder.CarteActivity;
import com.innovorder.innovorder.CategoryActivity;
import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.CarteItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class CategoryListListener implements OnItemClickListener {

	private Context context;
	private BaseAdapter adapter;

	public CategoryListListener(Context context, BaseAdapter adapter) {
		this.context = context;
		this.adapter = adapter;
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		CarteItem category = (CarteItem) adapter.getItem(position);
		if (category == null) {
			Intent intent = new Intent(context, CarteActivity.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);

			return;
		}

		Intent intent = new Intent(context, CategoryActivity.class);

		intent.putExtra("category_name", category.getName());
		intent.putExtra("category_id", category.getId());
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		
	}

}
