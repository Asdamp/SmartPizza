package com.asdamp.smartpizza;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class GeneraPizzaAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private ArrayList<ArrayList<Ingrediente>> childList;
	private ArrayList<String> groupList;
	public GeneraPizzaAdapter(Context context, ArrayList<ArrayList<Ingrediente>> childList, ArrayList<String> groupList) {
        this.inflater = LayoutInflater.from(context);
        this.childList=childList;
        this.groupList=groupList;
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
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		  ArrayList<Ingrediente> tempChild=childList.get(groupPosition);
		  if (convertView == null) {
		   convertView = inflater.inflate(R.layout.genera_pizza_child_layout, null);
		  }
		  CheckedTextView text = (CheckedTextView) convertView.findViewById(R.id.itemName);
		  Ingrediente c=tempChild.get(childPosition);
		  text.setText(c.nome);
		  text.setChecked(c.checked);
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
