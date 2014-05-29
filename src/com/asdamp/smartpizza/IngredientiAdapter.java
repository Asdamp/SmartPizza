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

public class IngredientiAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> ingr;

	public IngredientiAdapter(Context context, ArrayList<String> ingr) {
		super(context, R.layout.lista_ingredienti, ingr);
		this.context = context;
		this.ingr=ingr;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.lista_ingredienti, parent, false);
		TextView ingrediente = (TextView) rowView.findViewById(R.id.lista_ingredienti_ingrediente);
		ingrediente.setText(StringUtils.firstUp(ingr.get(position)));
		return rowView;
	}

}
