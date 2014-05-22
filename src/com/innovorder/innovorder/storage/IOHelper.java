package com.innovorder.innovorder.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IOHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "innovorder.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_CARTE_ITEM = "items";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_IO_ID = "io_id";
	public static final String COLUMN_PARENT_ID = "parent_id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_VAT = "vat";
	public static final String COLUMN_MEDIA = "media";

	// Database creation sql statement
	private static final String CREATE_CARTE_ITEM = "create table " + TABLE_CARTE_ITEM + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_IO_ID + " integer, " + 
			COLUMN_PARENT_ID + " integer, " +
			COLUMN_TYPE + " text not null, " + 
			COLUMN_NAME + " text not null, " + 
			COLUMN_DESCRIPTION + " text, " +
			COLUMN_PRICE + " real, " + 
			COLUMN_VAT + " real, " + 
			COLUMN_MEDIA + " text);";

	public IOHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_CARTE_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(IOHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTE_ITEM);
		onCreate(database);
	}

}
