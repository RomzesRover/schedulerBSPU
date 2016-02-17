package com.BBsRs.bspuscheduler;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{

	ArrayList<DiscCollection> discCollection = new ArrayList<DiscCollection>();
	Context context;
	LayoutInflater inflater;
	
	//preferences 
    SharedPreferences sPref;
	
	public ListAdapter (Context _context, ArrayList<DiscCollection> _discCollection){
		discCollection = _discCollection;
		context = _context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	// êîë-âî ýëåìåíòîâ
	  @Override
	  public int getCount() {
	    return discCollection.size();
	  }

	  // ýëåìåíò ïî ïîçèöèè
	  @Override
	  public DiscCollection getItem(int position) {
	    return discCollection.get(position);
	  }

	  // id ïî ïîçèöèè
	  @Override
	  public long getItemId(int position) {
	    return position;
	  }
	  
    static class ViewHolder {
    	public TextView discName;
    	public TextView weeks;
        public TextView numberInUni;
        public TextView Time;
        public TextView group;
        public TextView place;
        public TextView curator;
        public TextView type;
        public TextView parity;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.ic_simple_schedule, parent, false);
            holder = new ViewHolder();
            holder.discName = (TextView) rowView.findViewById(R.id.dicsName);
            holder.weeks = (TextView) rowView.findViewById(R.id.weeks);
            holder.numberInUni = (TextView) rowView.findViewById(R.id.numberInUni);
            holder.Time = (TextView) rowView.findViewById(R.id.time);
            holder.group = (TextView) rowView.findViewById(R.id.group);
            holder.place = (TextView) rowView.findViewById(R.id.place);
            holder.curator = (TextView) rowView.findViewById(R.id.curator);
            holder.type = (TextView) rowView.findViewById(R.id.type);
            holder.parity = (TextView) rowView.findViewById(R.id.parity);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        
        holder.discName.setText(String.valueOf(discCollection.get(position).discName));
        holder.weeks.setText(String.format(context.getString(R.string.weeks), String.valueOf(discCollection.get(position).weeks)));
        holder.numberInUni.setText(String.format(context.getString(R.string.numberInUni), String.valueOf(discCollection.get(position).numberInUni)));
        holder.Time.setText(String.format(context.getString(R.string.time), String.valueOf(discCollection.get(position).Time)));
        holder.group.setText(String.format(context.getString(R.string.group), String.valueOf((discCollection.get(position).group.equals("0") ? context.getString(R.string.group_0) : discCollection.get(position).group.equals("1") ? context.getString(R.string.group_1) : discCollection.get(position).group.equals("2") ? context.getString(R.string.group_2) : "undefined"))));
        holder.place.setText(String.format(context.getString(R.string.place), String.valueOf(discCollection.get(position).place)));
        holder.curator.setText(String.format(context.getString(R.string.curator), String.valueOf(discCollection.get(position).curator)));
        holder.type.setText(String.format(context.getString(R.string.type), String.valueOf(discCollection.get(position).type)));
        holder.parity.setText(String.format(context.getString(R.string.parity), String.valueOf((discCollection.get(position).parity.equals("0") ? context.getString(R.string.parity_0) : discCollection.get(position).parity.equals("1") ? context.getString(R.string.parity_1) : discCollection.get(position).parity.equals("2") ? context.getString(R.string.parity_2) : "undefined"))));
        
        return rowView;
    }
}