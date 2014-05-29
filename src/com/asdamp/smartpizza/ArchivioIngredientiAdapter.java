package com.asdamp.smartpizza;




import java.util.ArrayList;
import com.asdamp.database.DBAdapter;
import com.asdamp.utility.StringUtils;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ArchivioIngredientiAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private ArrayList<ArrayList<Ingrediente>> childList;
	private ArrayList<String> groupList;
	private Context co;
	public ArchivioIngredientiAdapter(Context context, ArrayList<ArrayList<Ingrediente>> childList, ArrayList<String> groupList) {
        this.inflater = LayoutInflater.from(context);
        this.childList=childList;
        this.groupList=groupList;
        co=context;
}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		  ArrayList<Ingrediente> tempChild=childList.get(groupPosition);
		  if (convertView == null) {
		   convertView = inflater.inflate(R.layout.archivio_ingredienti_base, null);
		  }
		  TextView text = (TextView) convertView.findViewById(R.id.ingrediente);
		  Ingrediente c=tempChild.get(childPosition);
		  final String nomIngr=c.nome;
		  text.setText(StringUtils.firstUp(nomIngr));
		  ImageView bu=(ImageView) convertView.findViewById(R.id.remove);
		  bu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ArrayList<Ingrediente> temp=childList.get(groupPosition);
				try{
				Costanti.getDB().deleteIngrediente(nomIngr);
				temp.remove(childPosition);
				}
				catch(SQLiteConstraintException e){
					Toast.makeText(co, R.string.Ingrediente_presente_in_alcune_pizze_impossibile_eliminarlo,  Toast.LENGTH_LONG).show();
				}
				notifyDataSetChanged();
				
			}});
		  return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		 View v = convertView;

		    if (v == null) {
		        v = inflater.inflate(R.layout.genera_pizza_group_layout, parent, false);
		    }

		    TextView groupName = (CheckedTextView) v.findViewById(R.id.groupName);
		    groupName.setText(groupList.get(groupPosition));

		    return v;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}



	
	}
