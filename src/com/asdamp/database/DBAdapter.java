package com.asdamp.database;

import java.util.ArrayList;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.asdamp.smartpizza.R;

public class DBAdapter {

	
	public DBAdapter(Context context1) {
		context = context1;
	}

	public DBAdapter apri() throws SQLException {
		try {
			dbHelper = new DBHelper(context);
			database = dbHelper.getWritableDatabase();
		} catch (Exception exception) {
			Log.e("DB", "il database era già aperto");
		}
		return this;
	}

	public void chiudi() {
		dbHelper.close();
		database.close();
	}
	public Cursor fetchPizze() {
		return database.query(DB_PIZZE, null, null, null, null, null, DB_PIZZE_NOME);
	}
	public ArrayList<String> fetchIngredienti() {
		String select[]=new String[1];
		select[0]=DB_INGREDIENTI_NOME;
		Cursor c=database.query(DB_INGREDIENTI, select, null, null, null, null,DB_INGREDIENTI_NOME);
		c.moveToFirst();
		ArrayList<String> ing=new ArrayList<String>();
		for(int i=0; i<c.getCount();i++){
			ing.add(c.getString(c.getColumnIndex(DB_INGREDIENTI_NOME)));
			c.moveToNext();
		}
		return ing;
	}
	public ArrayList<Integer> fetchIDPizze() {
		String select[]=new String[1];
		select[0]=DB_PIZZE_ID;
		Cursor c=database.query(DB_PIZZE, select, null, null, null, null, DB_PIZZE_NOME/*nonm èdetto che funioni. metere null se non va*/);
		c.moveToFirst();
		ArrayList<Integer> IDs=new ArrayList<Integer>();
		for(int i=0; i<c.getCount();i++){
			IDs.add(c.getInt(c.getColumnIndex(DB_PIZZE_ID)));
			c.moveToNext();
		}
		return IDs;
	}
	private void reorderIngredienti(ArrayList<String> ingredienti) {
		int m=ingredienti.indexOf(R.string.mozzarella);
		if(m!=-1) {
			ingredienti.remove(m);
			ingredienti.add(0, context.getString(R.string.mozzarella));

		}
		int p=ingredienti.indexOf(R.string.pomodoro);

		if(p!=-1) {
			ingredienti.remove(p);
			ingredienti.add(0, context.getString(R.string.pomodoro));
		}}
	public ArrayList<String> associaIdPizzaAIngredienti(int ID){
		String select[]=new String[1];
		select[0]=DB_PIZZE_ING_INGREDIENTE;
		Cursor c=database.query(DB_PIZZE_ING, select, DB_PIZZE_ING_PIZZA+" = "+ID, null, null, null, null);
		c.moveToFirst();
		ArrayList<String> ingredienti=new ArrayList<String>();
		for(int i=0;i<c.getCount();i++){
			ingredienti.add(c.getString(c.getColumnIndex(DB_PIZZE_ING_INGREDIENTE)));
			c.moveToNext();
		}
		this.reorderIngredienti(ingredienti);
		return ingredienti;
	}
	public String associaIdPizzaANome(Integer ID) {
		String select[]=new String[1];
		select[0]=DB_PIZZE_NOME;
		Cursor c=database.query(DB_PIZZE, select, DB_PIZZE_ID+" = "+ID, null, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex(DB_PIZZE_NOME));
	}
	public String associaIdPizzaADescrizione(int id) {
		String select[]=new String[1];
		select[0]=DB_PIZZE_DESCRIZIONE;
		Cursor c=database.query(DB_PIZZE, select, DB_PIZZE_ID+" = "+id, null, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex(DB_PIZZE_DESCRIZIONE));
	}
	public String associaIdPizzaAImmagine(int id) {
		String select[]=new String[1];
		select[0]=DB_PIZZE_IMMAGINE;
		Cursor c=database.query(DB_PIZZE, select, DB_PIZZE_ID+" = "+id, null, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex(DB_PIZZE_IMMAGINE));
	}

	public Cursor associaCategoriaAIngredienti(String cat) {
		String select[]=new String[1];
		select[0]=DB_INGREDIENTI_NOME;
		Cursor c=database.query(DB_INGREDIENTI, select, DB_INGREDIENTI_CATEGORIA+" = '"+cat+"'", null, null, null, null);
		return c;
	}
	public Cursor fetchAll(String db) {
		return database.query(db, null, null, null, null, null, null);
	}
	public void aggiungiIngrediente(String t, String sp) throws SQLException{
		ContentValues cv=new ContentValues();
		String te=t.toLowerCase(Locale.getDefault()).trim();
		cv.put(DB_INGREDIENTI_NOME, te);
		cv.put(DB_INGREDIENTI_CATEGORIA, sp);
		database.insertOrThrow(DB_INGREDIENTI, null, cv);
	}
	public void aggiungiIngrediente(String t) throws SQLException{
		ContentValues cv=new ContentValues();
		String te=t.toLowerCase(Locale.getDefault()).trim();
		cv.put(DB_INGREDIENTI_NOME, te);
		database.insertOrThrow(DB_INGREDIENTI, null, cv);
	}
	public SQLiteStatement compileRemoveIngredienteDaPizza(int id, String daEl) {
		String sql= "delete from "+DB_PIZZE_ING+" where "+DB_PIZZE_ING_PIZZA+" =" +id +" AND "+ DB_PIZZE_ING_INGREDIENTE+"='"+daEl+"'";
		return database.compileStatement(sql);
				}
	public void execSQLiteStatements(ArrayList<SQLiteStatement> stack) {
		for(SQLiteStatement s:stack) s.execute();
		
	}
	public SQLiteStatement compileAggiungiIngredienteAPizza(int id, String ingrediente) {
		String te=ingrediente.toLowerCase(Locale.getDefault()).trim();

		String sql= "insert into "+DB_PIZZE_ING+" (" +DB_PIZZE_ING_PIZZA+","+ DB_PIZZE_ING_INGREDIENTE+") values ("+id+",'"+te+"')";
		return database.compileStatement(sql);

	}
	/*public void removeIngredienteDaPizza(int id, String daEl) {
		database.delete(DB_PIZZE_ING, DB_PIZZE_ING_PIZZA+" =" +id +" AND "+ DB_PIZZE_ING_INGREDIENTE+"='"+daEl+"'", null);
		
	}*/
	private ContentValues createPizzaContentValues(String nome, String descr, Uri imm){
		
		ContentValues values=new ContentValues();
		values.put(DB_PIZZE_DESCRIZIONE, descr);
		values.put(DB_PIZZE_NOME, nome);
		if(imm==null) values.putNull(DB_PIZZE_IMMAGINE);
		else values.put(DB_PIZZE_IMMAGINE, imm.toString());
		return values;
		
	}
	public int creaPizza(String nome, String descr,
			Uri imm, ArrayList<String> ingredienti) {
		ContentValues cv=this.createPizzaContentValues(nome, descr, imm);
		long id=database.insertOrThrow(DB_PIZZE, null, cv);
		for(String in:ingredienti){
			cv=new ContentValues();
			cv.put(DB_PIZZE_ING_PIZZA, id);
			String te=in.toLowerCase(Locale.getDefault()).trim();

			cv.put(DB_PIZZE_ING_INGREDIENTE, te);
			database.insertOrThrow(DB_PIZZE_ING, null, cv);
		}
		return (int) id;
	}
	public void updatePizza(String nome, String descr,Uri imm, int id) {
		ContentValues values=this.createPizzaContentValues(nome, descr, imm);
	
		database.update(DB_PIZZE, values, DB_PIZZE_ID+" = "+id, null);
		
	}

	public void deletePizza(Integer id) {
		database.delete(DB_PIZZE, DB_PIZZE_ID+" = "+id, null);
	}
	public void deleteIngrediente(String nomeIngrediente) throws SQLiteConstraintException{
		
		database.delete(DB_INGREDIENTI, DB_INGREDIENTI_NOME+" = '"+nomeIngrediente+"'", null);
		
		
		
	}
	private Context context;
	public SQLiteDatabase database;
	private DBHelper dbHelper;
	public static final String DB_INGREDIENTI="Ingredienti";
	public static final String DB_INGREDIENTI_NOME="Ingrediente";
	public static final String DB_PIZZE="Pizze";
	public static final String DB_PIZZE_NOME="Nome";
	public static final String DB_PIZZE_DESCRIZIONE="Descrizione";

	public static final String DB_PIZZE_ID="ID";
	public static final String DB_PIZZE_ING="IngredientiPizza";
	public static final String DB_PIZZE_ING_PIZZA="Pizza";
	public static final String DB_PIZZE_ING_INGREDIENTE="Ingrediente";
	public static final String DB_CATEGORIE_CATEGORIA = "Categoria";
	public static final String DB_CATEGORIE="Categorie";
	public static final String DB_INGREDIENTI_CATEGORIA = "Categoria";
	public static final String DB_PIZZE_IMMAGINE = "Foto";

	
	
	
	

	
	
	

	


}
