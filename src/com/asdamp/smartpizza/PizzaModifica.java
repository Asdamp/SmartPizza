package com.asdamp.smartpizza;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.asdamp.database.DBAdapter;
import com.asdamp.utility.TextEditDialog;
import com.asdamp.utility.TextEditDialog.TextEditDialogInterface;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

public class PizzaModifica extends SherlockFragmentActivity implements
		TextEditDialogInterface {
	public static final int GENERA_PIZZA = 0;
	public static final int MODIFICA = 1;
	public static final int CREA = 2;
	protected static final String ID_PIZZA_DA_VISUALIZZARE = "idPizza";
	protected static final String REQUEST_CODE = "RequestCode";
	private Button removeButton;
	private TextView noImage;
	private int req;
	private int id;
	private TextView n;
	private TextView d;
	private ImageView imPi;
	private IngredientiAdapterMod ia;
	private DragSortListView l;
	private ArrayList<SQLiteStatement> stack;
	private Bundle b;
	private String nome;
	private String descr;
	private Uri imm;
	private Uri outputUri; // this Uri is used if you pick an image with the
							// camera
	private ArrayList<String> ingredienti;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.pizza_mod);
		/*AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		/*prevent automatic keybord openin*/getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		b=this.getIntent().getExtras().getBundle("bundle");
		stack=new ArrayList<SQLiteStatement>();
		req=b.getInt(REQUEST_CODE);
		id=b.getInt(ID_PIZZA_DA_VISUALIZZARE, -1);
		l=(DragSortListView) this.findViewById(R.id.lista_ingredienti_lista);
		ingredienti=new ArrayList<String>();	
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		n=((TextView) this.findViewById(R.id.pizza_nome_pizza));
		d=((TextView) this.findViewById(R.id.pizza_descrizione));
		imPi=(ImageView) this.findViewById(R.id.pizza_immagine);
		noImage=(TextView) this.findViewById(R.id.no_image);
		n.requestFocus();
		ia=new IngredientiAdapterMod(this, R.layout.lista_ingredienti_mod_base,ingredienti);	
        View v =  ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pizza_mod_footer, null, false);
        //gestione del pulsante footer della listview. Consente di aggiungere un ingrediente
        Button but=(Button) v.findViewById(R.id.add);
		but.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				showTextEditDialog();
				
			}

			});
		// gestione del pulsante che permette l'apertura e la selezione di un'immagine
		Button but2= (Button) this.findViewById(R.id.pizza_mod_modifica_immagine);
		but2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				openImage();
				
			}});
		// gestione del pulsante di rimozione immagine. inizialmente � invisibile
		removeButton=(Button) this.findViewById(R.id.pizza_mod_remove_immagine);
		removeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				imm=null;
				imPi.setImageResource(R.drawable.no_image);
				removeButton.setVisibility(View.GONE);
				noImage.setVisibility(View.VISIBLE);

			}});
		l.addFooterView(v);
		l.setAdapter(ia);
		l.setRemoveListener(new RemoveListener(){

			@Override
			public void remove(int which) {
				String daEl=ia.getItem(which);
				if(id!=-1){
					stack.add(Costanti.getDB().compileRemoveIngredienteDaPizza(id,daEl));
				}
				ia.remove(daEl);;
			}});
		inizializza();

	}

	protected void showTextEditDialog() {
		Bundle bu = new Bundle();
		bu.putString(TextEditDialog.TITOLO, getString(R.string.aggiungi_ingrediente));
		bu.putString(TextEditDialog.SOTTOTITOLO, "");

		bu.putString(TextEditDialog.STRINGA_BASE, "");

		TextEditDialog te = new TextEditDialog();
		te.setArguments(bu);
		te.show(this.getSupportFragmentManager(), "testo");
	}

	private void inizializza() {

		switch (req) {
		case GENERA_PIZZA:
			generaPizza();
			if (ingredienti.size() == 0)
				Toast.makeText(
						this,
						R.string.Archivio_ingredienti_vuoto,
						Toast.LENGTH_LONG).show();
			nome = "Buon Appetito";
			descr = null;
			imm = null;
			visualizza();
			break;
		case MODIFICA:
			DBAdapter db = Costanti.getDB();
			ArrayList<String> s = db.associaIdPizzaAIngredienti(id);
			for (String i : s)
				ingredienti.add(i);
			nome = db.associaIdPizzaANome(id);
			descr = db.associaIdPizzaADescrizione(id);
			String imageLocation = db.associaIdPizzaAImmagine(id);
			if (imageLocation == null)
				imm = null;
			else
				imm = Uri.parse(imageLocation);
			visualizza();
			break;
		case CREA:
			ingredienti.add(getString(R.string.pomodoro));
			ingredienti.add(getString(R.string.mozzarella));
			nome = null;
			descr = null;
			imm = null;
			visualizza();
		}
	}

	private void generaPizza() {
		ArrayList<String> cate = b.getStringArrayList("categorie");
		ArrayList<ArrayList<Ingrediente>> ingr = new ArrayList<ArrayList<Ingrediente>>();

		for (int i = 0; i < cate.size(); i++) {
			/* shallow copy of the list in the bundle */
			ArrayList<Ingrediente> c = b.getParcelableArrayList(cate.get(i));
			ArrayList<Ingrediente> a = new ArrayList<Ingrediente>(c);
			ingr.add(a);
		}
		int ingrMin;
		int ingrMax;
		ArrayList<String> obbligatori = new ArrayList<String>();
		// gestione degli obbligatori. verr� modificata in seguito
		ArrayList<Ingrediente> tempObbl = ingr.get(cate.indexOf(R.string.base));
		ingrMin = tempObbl.size() + 1;
		ingrMax = ingrMin + 5;
		for (Ingrediente i : tempObbl)
			if (i.checked)
				obbligatori.add(i.nome);
		ArrayList<Ingrediente> ingre = new ArrayList<Ingrediente>();
		Random r = new Random();
		int numIng = r.nextInt(ingrMax - ingrMin) + ingrMin;
		ingredienti.clear();
		ingredienti.addAll(obbligatori);
		while (ingredienti.size() < numIng && ingr.size() > 0) {
			int catTemp = r.nextInt(ingr.size());
			ingre = ingr.get(catTemp);
			while (ingre.size() > 0) {
				int a = r.nextInt(ingre.size());
				Ingrediente t = ingre.get(a);
				if (!t.checked || obbligatori.contains(t.nome))
					ingre.remove(a);
				else {
					ingredienti.add(t.nome);
					ingre.remove(a);
					break;
				}
			}
			if (ingre.size() == 0)
				ingr.remove(catTemp);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.modifica_pizza, menu);
		switch (req) {
		case GENERA_PIZZA:
			menu.findItem(R.id.Genera).setVisible(true);
			break;
		}
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.Genera:
			generaPizza();
			if (ingredienti.size() == 0)
				Toast.makeText(
						this,
						R.string.Archivio_ingredienti_vuoto,
						Toast.LENGTH_SHORT).show();
			ia.notifyDataSetChanged();
			break;
		case R.id.salva:
			nome = n.getText().toString();
			descr = d.getText().toString();
			checkCostraint();
			if (id == -1) {
				id = Costanti.getDB().creaPizza(nome, descr, imm, ingredienti);
			} else {
				Costanti.getDB().execSQLiteStatements(stack);
				Costanti.getDB().updatePizza(nome, descr, imm, id);
			}
			returnToMain(id);
		}
		return false;
	}

	private void returnToMain(int idv) {
		Intent i = new Intent(this, Pizza.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle b = new Bundle();
		b.putInt(Pizza.ID_PIZZA_DA_VISUALIZZARE, idv);
		b.putInt(Pizza.REQUEST_CODE, Pizza.APRI_PIZZA);
		i.putExtra("bundle", b);
		startActivityForResult(i, Pizza.APRI_PIZZA);

	}

	private void checkCostraint() {
		ArrayList<String> temp = Costanti.getDB().fetchIngredienti();
		for (String i : ingredienti) {
			String te = i.toLowerCase(Locale.getDefault()).trim();

			if (!temp.contains(te))
				Costanti.getDB().aggiungiIngrediente(i);
		}
	}

	private void visualizza() {
		if (nome != null) {
			n.setText(nome);
		}
		if (nome != null) {
			d.setText(descr);
		}
		if (imm != null) {
			removeButton.setVisibility(View.VISIBLE);
			this.noImage.setVisibility(View.INVISIBLE);
			imPi.setImageURI(imm);
		}
		ia.notifyDataSetChanged();

	}

	@Override
	public void OnTextEditDialogPositiveClick(String t) {
		ingredienti.add(t);
		if (id != -1) {
			String lastIngrediente = ingredienti.get(ingredienti.size() - 1);
			stack.add(Costanti.getDB().compileAggiungiIngredienteAPizza(id,
					lastIngrediente));
		}
		ia.notifyDataSetChanged();
	}

	private void openImage() {

		// Determine Uri of camera image to save.
		final File root = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "SmartPizza" + File.separator);// smart pizza
																	// directory
		root.mkdirs();// create smartpizza directory if not exist
		final String fname = "img_" + System.currentTimeMillis() + ".jpg"; // genera
																			// un
																			// nome
																			// dell'immagine
																			// random
		final File sdImageMainDirectory = new File(root, fname); // create the
																	// file for
																	// the image
		outputUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				getString(R.string.seleziona_fonte));

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		if (resCode == RESULT_OK) {
			removeButton.setVisibility(View.VISIBLE);
			this.noImage.setVisibility(View.INVISIBLE);

			//check from where you have picked the image, camera? or gallery/file selector?
			if (requestCode == 100) {
				 final boolean isCamera;
		            if(data == null)
		            {
		                isCamera = true;
		            }
		            else
		            {
		                final String action = data.getAction();
		                if(action == null)
		                {
		                    isCamera = false;
		                }
		                else
		                {
		                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		                }
		            }

				if(isCamera) imm=this.outputUri; //if the selected intent is a camera, then you can found the image in outputUri
				else imm = data.getData(); //else, the data intent has the information about the selected image
		        imPi.setImageURI(imm);
			}
		}
	}

}
