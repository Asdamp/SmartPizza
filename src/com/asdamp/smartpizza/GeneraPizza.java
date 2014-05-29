package com.asdamp.smartpizza;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.asdamp.database.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class GeneraPizza extends SherlockFragmentActivity {
	DBAdapter db=Costanti.getDB();
	private final ArrayList<ArrayList<Ingrediente>> ingr=new ArrayList<ArrayList<Ingrediente>>();;
	private ArrayList<String> cate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.genera_pizza);
		/*AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		final ExpandableListView l=(ExpandableListView) this.findViewById(R.id.genera_pizza_list);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		cate=retriveCategory();
		for(int i=0;i<=cate.size()-1;i++) ingr.add(retriveIngr(cate.get(i)));
		GeneraPizzaAdapter gpa=new GeneraPizzaAdapter(this, ingr, cate);
		l.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		l.setClickable(true);
		l.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				CheckedTextView ct=(CheckedTextView) v.findViewById(R.id.itemName);
				ct.toggle();
				ingr.get(groupPosition).get(childPosition).checked=ct.isChecked();
				return true;
			}});
		l.setAdapter(gpa);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.genera_pizza, menu);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
			case R.id.conferma:
				Intent i=new Intent(this, PizzaModifica.class);
				Bundle b=new Bundle();
				for(int j=0;j<cate.size();j++){
					b.putParcelableArrayList(cate.get(j), ingr.get(j));
				}
				b.putStringArrayList("categorie", cate);
				b.putInt(Pizza.REQUEST_CODE, PizzaModifica.GENERA_PIZZA);
				i.putExtra("bundle",b);
				
				this.startActivityForResult(i, PizzaModifica.GENERA_PIZZA);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private ArrayList<Ingrediente> retriveIngr(String cat) {
		Cursor c=db.associaCategoriaAIngredienti(cat);
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
		Cursor c=db.fetchAll(DBAdapter.DB_CATEGORIE);
		ArrayList<String> a=new ArrayList<String>();
		c.moveToFirst();
		do{
			a.add(c.getString(c.getColumnIndex(DBAdapter.DB_CATEGORIE_CATEGORIA)));
		}
		while(c.moveToNext());
		return a;
	}

	
}
