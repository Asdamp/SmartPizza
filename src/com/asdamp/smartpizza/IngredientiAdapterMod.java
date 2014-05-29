package com.asdamp.smartpizza;

import java.util.ArrayList;
import java.util.List;
import com.asdamp.database.DBAdapter;
import com.asdamp.utility.StringUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngredientiAdapterMod extends ArrayAdapter<String> {
	private Context c;
	private ArrayList<String> ing;
	private int res;
	public IngredientiAdapterMod(Context context, int resource,
			ArrayList<String> ingredienti) {
		super(context, resource, ingredienti);
		c=context;
		ing=ingredienti;
		res=resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(res, parent, false);
		TextView ingrediente = (TextView) rowView.findViewById(R.id.lista_ingredienti_mod_nome);
		ingrediente.setText(StringUtils.firstUp(ing.get(position)));
		return rowView;
	}
	

}
