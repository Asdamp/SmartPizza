package com.asdamp.smartpizza;

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.asdamp.database.DBAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.asdamp.utility.*;
public class MainActivity extends SherlockFragmentActivity implements TextEditSpinnerDialog.TextEditSpinnerDialogInterface{

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*AdRegistration.setAppKey("3c665e8fe2ef44dcbaee4dfa933a42cb");
		AdRegistration.enableTesting(false);
		AdRegistration.enableLogging(false);
		AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		ImageView randoPizza=(ImageView) this.findViewById(R.id.RandoPizza);
		ImageView archivioPizze=(ImageView) this.findViewById(R.id.ArchivioPizze);
		ImageView aggPizza=(ImageView) this.findViewById(R.id.AggPizza);
		ImageView CreaNuovaPizza=(ImageView) this.findViewById(R.id.CreaNuovaPizza);
		ImageView archivioIngredienti=(ImageView) this.findViewById(R.id.Archivioingredienti);
		ImageView AggiungiIngredienti=(ImageView) this.findViewById(R.id.AggiungiIngredienti);
		DBAdapter db=new DBAdapter(this);
		db.apri();
		randoPizza.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(MainActivity.this, Pizza.class);
				Bundle b=new Bundle();
				b.putInt(Pizza.REQUEST_CODE, Pizza.RANDOPIZZA);
				i.putExtra("bundle", b);
				startActivityForResult(i, Pizza.RANDOPIZZA);
			}
		});
		archivioPizze.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(MainActivity.this, ArchivioPizze.class);
				startActivity(i);
			}
		});
		CreaNuovaPizza.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(MainActivity.this, GeneraPizza.class);
				startActivity(i);
			}
		});
		aggPizza.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= new Intent(MainActivity.this, PizzaModifica.class);
				Bundle b=new Bundle();
				b.putInt(PizzaModifica.REQUEST_CODE, PizzaModifica.CREA);
				i.putExtra("bundle", b);
				startActivityForResult(i, PizzaModifica.CREA);
				
			}
		});
		AggiungiIngredienti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTextEditDialog(v);
			}
		});
		archivioIngredienti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this,ArchivioIngredienti.class);
				startActivity(i);
			}
		});
		
	}
	public void showTextEditDialog(View v) {
		Bundle p = new Bundle();
		ArrayList<String> list=new ArrayList<String>();
		Cursor c=Costanti.getDB().fetchAll(DBAdapter.DB_CATEGORIE);
		c.moveToFirst();
		do{
			list.add(c.getString(c.getColumnIndex(DBAdapter.DB_CATEGORIE_CATEGORIA)));
		}while(c.moveToNext());
		p.putString(TextEditSpinnerDialog.TITOLO, getString(R.string.aggiungi_ingrediente));
		p.putString(TextEditSpinnerDialog.SOTTOTITOLO, getString(R.string.scrivi_il_nome_dell_ingrediente_e_seleziona_la_categoria_attinente));
		p.putStringArrayList(TextEditSpinnerDialog.DATI_SPINNER, list);
		p.putInt(TextEditSpinnerDialog.DEFAULT_SPINNER, list.indexOf(R.string.vari));
		DialogFragment textDialog = new TextEditSpinnerDialog();
		textDialog.setArguments(p);
		textDialog.show(this.getSupportFragmentManager(), "aggiungiIngr");
	}
	@Override
	public void OnTextEditSpinnerDialogPositiveClick(String t, String sp) {
		try{
			Costanti.getDB().aggiungiIngrediente(t,sp);
		}
		catch(SQLException e){
			Toast.makeText(this, this.getString(R.string.ingrediente_gia_presente), Toast.LENGTH_SHORT).show();

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		return super.onCreateOptionsMenu(menu);
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.about:
				Intent i=new Intent(this, About.class);
				this.startActivity(i);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
