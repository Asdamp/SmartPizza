package com.asdamp.smartpizza;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import com.asdamp.database.DBAdapter;
//the class Costanti is supposed to be a singleton. this class is initializated by MainApplication class
public class Costanti {
	private static Costanti costanti = null;
	private static DBAdapter database;
	public static void inizializza(Context c) {
		if(costanti==null) costanti= new Costanti(c);
	}

	private Costanti(Context c) {
		database = (new DBAdapter(c));
	}

	public static DBAdapter getDB() {
		return database;
	}
	public static void chiudiDB() {
		database.chiudi();
	}
	

}
