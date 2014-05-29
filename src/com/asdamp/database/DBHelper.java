package com.asdamp.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
Context c;
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		c=context;
	}

	public void onCreate(SQLiteDatabase sqlitedatabase) {
		sqlitedatabase.execSQL(PIZZA_DB);
		sqlitedatabase.execSQL(INGR_DB);
		sqlitedatabase.execSQL(INGR_PIZZA_DB);
		sqlitedatabase.execSQL(CATEGORIE_DB);
		
		try {
		    InputStream is = c.getResources().getAssets().open("pizzaDB.sql");
		    
			Scanner s=new Scanner(is);
			while(s.hasNext()){
				sqlitedatabase.execSQL(s.nextLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys = ON;");

	}

	public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldVersion, int newVersion) {
		switch(oldVersion){
			case 1:sqlitedatabase.execSQL("ALTER TABLE Pizze ADD COLUMN Foto CHAR DEFAULT (NULL)");
		}
	}

	private static final String PIZZA_DB ="CREATE TABLE Pizze ( "+
    "Nome        CHAR  collate nocase  DEFAULT ( NULL ),"+
    "Descrizione TEXT    DEFAULT ( NULL ),"+
   " ID          INTEGER PRIMARY KEY AUTOINCREMENT"+
   "                     NOT NULL, "
   + " Foto        CHAR    DEFAULT ( NULL ) );";
	private static final String CATEGORIE_DB="CREATE TABLE Categorie ( "+
		    "Categoria CHAR collate nocase PRIMARY KEY "+
			");";
	private static final String INGR_DB="CREATE TABLE Ingredienti ( "+
   " Ingrediente CHAR PRIMARY KEY collate nocase"+
                 "    NOT NULL,"+
   " Categoria   CHAR collate nocase NOT NULL"+
                 "    DEFAULT ( 'Vari' ) "+
                 "    REFERENCES Categorie ( Categoria ) ON DELETE RESTRICT"+
                 "                                       ON UPDATE CASCADE );";
	private static final String INGR_PIZZA_DB="CREATE TABLE IngredientiPizza ( "+
    "Pizza       INTEGER NOT NULL REFERENCES Pizze ( ID ) ON DELETE CASCADE "+
    "                                            ON UPDATE CASCADE,"+
   " Ingrediente CHAR collate nocase NOT NULL  REFERENCES Ingredienti ( Ingrediente ) ON DELETE RESTRICT "+
    "                                                           ON UPDATE CASCADE,"+
    "PRIMARY KEY ( Pizza, Ingrediente ));";
	public static final String DATABASE_NAME = "pizza.db";
	public static final int DATABASE_VERSION = 2;
}
