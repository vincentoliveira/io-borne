package com.innovorder.innovorder.storage;

import java.util.ArrayList;
import java.util.List;

import com.innovorder.innovorder.model.CarteItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CarteItemStorage {
	// Database fields
	private SQLiteDatabase database;
	private IOHelper dbHelper;
	private String[] allColumns = { IOHelper.COLUMN_ID, IOHelper.COLUMN_IO_ID, IOHelper.COLUMN_PARENT_ID, IOHelper.COLUMN_TYPE, IOHelper.COLUMN_NAME, IOHelper.COLUMN_DESCRIPTION, IOHelper.COLUMN_PRICE, IOHelper.COLUMN_VAT, IOHelper.COLUMN_MEDIA };

	public CarteItemStorage(Context context) {
		dbHelper = new IOHelper(context);
	}

	protected void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	protected void close() {
		dbHelper.close();
	}

	public void clearAll() {
		open();
		database.delete(IOHelper.TABLE_CARTE_ITEM, null, null);
		close();
	}

	public boolean addItem(CarteItem item) {
		ContentValues values = new ContentValues();
		values.put(IOHelper.COLUMN_IO_ID, item.getId());
		values.put(IOHelper.COLUMN_PARENT_ID, item.getParentId());
		values.put(IOHelper.COLUMN_TYPE, item.getType());
		values.put(IOHelper.COLUMN_NAME, item.getName());
		values.put(IOHelper.COLUMN_DESCRIPTION, item.getDescription());
		values.put(IOHelper.COLUMN_PRICE, item.getPrice());
		values.put(IOHelper.COLUMN_VAT, item.getVat());
		values.put(IOHelper.COLUMN_MEDIA, item.getMediaUrl());

		try {
			open();
			long insertId = database.insert(IOHelper.TABLE_CARTE_ITEM, null, values);
			close();
			
			for (CarteItem child : item.getChildren()) {
				addItem(child);
			}
			
			return insertId != -1;
		} catch (SQLException e) {
			Log.d("DishStorage", e.getMessage());
		}

		return false;
	}

	public List<CarteItem> getAllItems() {
		try {
			open();
			List<CarteItem> items = new ArrayList<CarteItem>();

			Cursor cursor = database.query(IOHelper.TABLE_CARTE_ITEM, allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				CarteItem item = cursorToItem(cursor);

				if (item != null) {
					items.add(item);
				}
				
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
			close();
			return items;
		} catch (SQLException e) {
			Log.d("DishStorage", e.getMessage());
		}

		return null;
	}


	public List<CarteItem> getItemsByParentId(long parentId) {
		try {
			open();
			List<CarteItem> items = new ArrayList<CarteItem>();

			Cursor cursor = database.query(IOHelper.TABLE_CARTE_ITEM, allColumns, IOHelper.COLUMN_PARENT_ID + " = " + parentId, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				CarteItem item = cursorToItem(cursor);

				if (item != null) {
					items.add(item);
				}
				
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
			close();
			return items;
		} catch (SQLException e) {
			Log.d("DishStorage", e.getMessage());
		}

		return null;
	}

	private CarteItem cursorToItem(Cursor cursor) {
		CarteItem item = new CarteItem();
		item.setId(cursor.getLong(1));
		item.setParentId(cursor.getLong(2));
		item.setType(cursor.getString(3));
		item.setName(cursor.getString(4));
		item.setDescription(cursor.getString(5));
		item.setPrice(cursor.getFloat(6));
		item.setVat(cursor.getFloat(7));
		item.setMediaUrl(cursor.getString(8));

		return item;
	}

	public List<CarteItem> getMainCategories() {
		return getItemsByParentId(0);
	}

	public CarteItem find(long id) {
		try {
			open();
			Cursor cursor = database.query(IOHelper.TABLE_CARTE_ITEM, allColumns, IOHelper.COLUMN_IO_ID + " = " + id, null, null, null, null);

			cursor.moveToFirst();
			CarteItem item = cursorToItem(cursor);

			// make sure to close the cursor
			cursor.close();
			close();
			
			return item;
		} catch (SQLException e) {
			Log.d("DishStorage", e.getMessage());
		}

		return null;
	}
}
