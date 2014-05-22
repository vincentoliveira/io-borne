package com.innovorder.innovorder.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RemoveItemFromChartListener implements OnItemClickListener {

	private Context context;

	public RemoveItemFromChartListener(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
	}

}
