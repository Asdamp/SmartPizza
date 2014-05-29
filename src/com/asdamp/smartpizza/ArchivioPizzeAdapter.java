package com.asdamp.smartpizza;




import java.util.ArrayList;
import com.asdamp.database.DBAdapter;
import com.asdamp.utility.StringUtils;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArchivioPizzeAdapter extends ArrayAdapter<Integer> {




	private final Context context;
	private ArrayList<Integer> IDs;;
	public ArchivioPizzeAdapter(Context context, ArrayList<Integer> IDs) {
		super(context, R.layout.archivio_pizze_base, IDs);				
		this.context = context;
		this.IDs=IDs;
	}

	

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.archivio_pizze_base, parent, false);
		TextView titolo = (TextView) rowView.findViewById(R.id.pizza);
		TextView descrizione = (TextView) rowView.findViewById(R.id.ingredienti);	
		DBAdapter db=Costanti.getDB();
		titolo.setText(db.associaIdPizzaANome(IDs.get(position)));
		ArrayList<String> ingredienti=db.associaIdPizzaAIngredienti(IDs.get(position));
		String ingr="";
		if(ingredienti.size()>0){
		ingr=StringUtils.firstUp(ingredienti.get(0));
		for(int i=1;i<ingredienti.size();i++) ingr=ingr+", "+ingredienti.get(i);
		}
		descrizione.setText(ingr);
		return rowView;
	}



	
	}
