package com.innovorder.innovorder.adapter;

import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.BitmapStorage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private List<CarteItem> navDrawerItems;
	private String mediaBaseUrl;
	
	public NavDrawerListAdapter(Context context, List<CarteItem> categories){
		this.context = context;
		this.navDrawerItems = categories;

		this.mediaBaseUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_media);
	}

	@Override
	public int getCount() {
		return navDrawerItems.size() + 1;
	}

	@Override
	public CarteItem getItem(int position) {
		if (position == 0) {
			return null;
		}
		return navDrawerItems.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        
       ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
       TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		
		if (position == 0) {
	        txtTitle.setText(context.getString(R.string.menu_retour_accueil));
	        imgIcon.setImageResource(R.drawable.icon_home);
		} else {
			CarteItem item = this.getItem(position);
	        txtTitle.setText(item.getName());
			BitmapStorage bitmapStorage = BitmapStorage.getInstance();
			bitmapStorage.loadBitmap(mediaBaseUrl + item.getMediaUrl(), imgIcon);
		}
        
        return convertView;
	}

}
