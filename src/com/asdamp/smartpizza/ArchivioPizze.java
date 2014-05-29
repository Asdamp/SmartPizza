package com.asdamp.smartpizza;

import java.nio.IntBuffer;
import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.asdamp.database.DBAdapter;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArchivioPizze extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.archivio);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		/*AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		this.getSupportActionBar().setHomeButtonEnabled(true);
		DragSortListView l=(DragSortListView) this.findViewById(R.id.listaArchivio);
		final ArrayList<Integer> IDs=Costanti.getDB().fetchIDPizze();

		
		final ArchivioPizzeAdapter ada=new ArchivioPizzeAdapter (this,IDs);
		
		l.setAdapter(ada);
		l.setRemoveListener(new RemoveListener(){

			@Override
			public void remove(int which) {
				int idTemp=IDs.get(which);
				IDs.remove(which);
				Costanti.getDB().deletePizza(idTemp);
				ada.notifyDataSetChanged();
			}
			
		});
		l.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent i=new Intent(ArchivioPizze.this,Pizza.class);
				Bundle b=new Bundle();
				b.putInt(Pizza.ID_PIZZA_DA_VISUALIZZARE, IDs.get(position));
				b.putInt(Pizza.REQUEST_CODE, Pizza.APRI_PIZZA);
				i.putExtra("bundle", b);
				startActivityForResult(i, Pizza.APRI_PIZZA);
			}});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			finish();
			break;
	}
		return true;
	}
}
