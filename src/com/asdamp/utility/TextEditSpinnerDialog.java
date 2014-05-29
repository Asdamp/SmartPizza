package com.asdamp.utility;




import java.util.ArrayList;
import java.util.List;

import com.asdamp.database.DBAdapter;
import com.asdamp.smartpizza.Costanti;
import com.asdamp.smartpizza.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class TextEditSpinnerDialog extends DialogFragment {
	
	

	
	public final static String DEFAULT_SPINNER="ds";
	private TextEditSpinnerDialogInterface c;
	private String titolo;
	private String messaggio;
	private String testoPrecedente;
	private List<String> list;
	public final static String TITOLO="titolo";
	public final static String SOTTOTITOLO="sottotitolo";
	public final static String STRINGA_BASE="strBase";
	public final static String DATI_SPINNER="sp";

	public interface TextEditSpinnerDialogInterface {
		public void OnTextEditSpinnerDialogPositiveClick(String t,String sp);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            c = (TextEditSpinnerDialogInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TextEditDialogInterface");
        }
	}
	public TextEditSpinnerDialog(){
	
	}
	
	public Dialog onCreateDialog(Bundle s){
	Bundle b=this.getArguments();
	titolo=b.getString(TITOLO);
	messaggio=b.getString(SOTTOTITOLO);
	testoPrecedente=b.getString(STRINGA_BASE);
	list=b.getStringArrayList(DATI_SPINNER);
	AlertDialog.Builder alert = new AlertDialog.Builder((Context) c);
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View v=inflater.inflate(R.layout.aggiungi_ingrediente, null);
    alert.setView(v);
	alert.setTitle(titolo);
	if(!(messaggio.equals(""))) alert.setMessage(messaggio);
	final EditText input = (EditText) v.findViewById(R.id.aggiungi_ingrediente_nome);
	final Spinner sp=(Spinner) v.findViewById(R.id.aggiungi_ingrediente_categorie);
	ArrayAdapter<String> adapter=new ArrayAdapter<String>((Context) c, android.R.layout.simple_spinner_dropdown_item, list);
	sp.setAdapter(adapter);
	sp.setSelection(b.getInt(DEFAULT_SPINNER));
	input.setText(testoPrecedente);
	input.setSelection(input.getText().length());
	alert.setPositiveButton(getString(R.string.conferma), new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int whichButton) {
	  String testoFinale = input.getText().toString();
	  String spinnerselect=list.get(sp.getSelectedItemPosition());
	  c.OnTextEditSpinnerDialogPositiveClick(testoFinale,spinnerselect);
	  
	  }
	
	});

	alert.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {
	    
	    
	  }
	});

	return alert.create();
	

}}
