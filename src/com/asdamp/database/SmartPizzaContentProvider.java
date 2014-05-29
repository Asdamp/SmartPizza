package com.asdamp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SmartPizzaContentProvider extends ContentProvider {
	  private DBHelper database;

	 private static final int TODOS = 10;
	  private static final int TODO_ID = 20;

	  private static final String AUTHORITY = "com.asdamp.smartpizza.contentprovider";

	  private static final String BASE_PATH = "pizza";
	  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
		      + "/" + BASE_PATH);
	  static {
		    sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
		    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/ingredienti", TODO_ID);
		  }
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		database=new DBHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 int uriType = sURIMatcher.match(uri);
		 String table;
		    switch (uriType) {
		    case TODOS:
		    	table=DBAdapter.DB_PIZZE;
		      break;
		    case TODO_ID:
		    	table=DBAdapter.DB_INGREDIENTI;

		      break;
		    default:
		      throw new IllegalArgumentException("Unknown URI: " + uri);
		    }

		    SQLiteDatabase db = database.getWritableDatabase();
		    Cursor cursor = db.query(table, projection, selection, selectionArgs, null,null,sortOrder);
		    // make sure that potential listeners are getting notified
		    cursor.setNotificationUri(getContext().getContentResolver(), uri);

		    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
