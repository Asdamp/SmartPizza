package com.asdamp.smartpizza;

import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.asdamp.database.DBAdapter;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

public class ArchivioIngredienti extends SherlockFragmentActivity {
	private final ArrayList<ArrayList<Ingrediente>> ingr=new ArrayList<ArrayList<Ingrediente>>();;
	private ArrayList<String> cate;
	
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.genera_pizza);
		/*AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		final ExpandableListView l=(ExpandableListView) this.findViewById(R.id.genera_pizza_list);
		
		cate=retriveCategory();
		for(int i=0;i<=cate.size()-1;i++) ingr.add(retriveIngr(cate.get(i)));
		ArchivioIngredientiAdapter gpa=new ArchivioIngredientiAdapter(this, ingr, cate);
		l.setAdapter(gpa);
		for(int i=0; i < gpa.getGroupCount(); i++)
		    l.expandGroup(i);
		/*TODO quando viene cliccato ingrediente, permetterne la modifica 
		 * l.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent i=new Intent(ArchivioIngredienti.this,Pizza.class);
				Bundle b=new Bundle();
				b.putInt(Pizza.ID_PIZZA_DA_VISUALIZZARE, IDs[position]);
				b.putInt(Pizza.REQUEST_CODE, Pizza.APRI_PIZZA);
				i.putExtra("bundle", b);
				startActivityForResult(i, Pizza.APRI_PIZZA);
			}});*/
	}
private ArrayList<Ingrediente> retriveIngr(String cat) {
	Cursor c=Costanti.getDB().associaCategoriaAIngredienti(cat);
	ArrayList<Ingrediente> a =new ArrayList<Ingrediente>();
	if(c.getCount()!=0){
	c.moveToFirst();
	do{
		a.add(new Ingrediente(c.getString(c.getColumnIndex(DBAdapter.DB_INGREDIENTI_NOME))));
	}
	while(c.moveToNext());
	}
	return a;
}

private ArrayList<String> retriveCategory() {
	Cursor c=Costanti.getDB().fetchAll(DBAdapter.DB_CATEGORIE);
	ArrayList<String> a=new ArrayList<String>();
	c.moveToFirst();
	do{
		a.add(c.getString(c.getColumnIndex(DBAdapter.DB_CATEGORIE_CATEGORIA)));
	}
	while(c.moveToNext());
	return a;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()){
	case android.R.id.home:
		finish();
		break;
}
	return true;
}}