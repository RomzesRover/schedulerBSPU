package com.BBsRs.bspuscheduler;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PageFragment extends Fragment {
	
	//preferences 
    SharedPreferences sPref;
	  
	  static final String ARGUMENT_PAGE_NUMBER = "Current_Page_Deep";
	  static final String ARGUMENT_SCHEDULE_COLLECTION = "Schedule_Collection";
	  
	  int page;
	  ArrayList<SchedulesCollection> schedulesCollection;
	  
	  static PageFragment newInstance(int page, ArrayList<SchedulesCollection> schedulesCollection) {
	    PageFragment pageFragment = new PageFragment();
	    Bundle arguments = new Bundle();
	    arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
	    arguments.putParcelableArrayList(ARGUMENT_SCHEDULE_COLLECTION, schedulesCollection);
	    pageFragment.setArguments(arguments);
	    return pageFragment;
	  }
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    page = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
	    schedulesCollection = getArguments().getParcelableArrayList(ARGUMENT_SCHEDULE_COLLECTION);
	  }
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  
		    //set up preferences
	        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        
		    View view = inflater.inflate(R.layout.fragment_pager_list_of_available_schedules, null);
		    
		    // находим список
		    ListView lvMain = (ListView) view.findViewById(R.id.listView1);
		    ArrayAdapter<String> adapter;
		    //загаловок
		    TextView title = (TextView)view.findViewById(R.id.textView1);
		    
		    final ArrayList<String> currList = new ArrayList<String>();
		    final ArrayList<String> idsList = new ArrayList<String>();
		    final ArrayList<String> linksList = new ArrayList<String>();
		    
		    switch(page){
		    case 0:
		    	title.setText(getActivity().getResources().getString(R.string.choose_one));
		    	
		    	for (SchedulesCollection one : schedulesCollection){
		    		if (!currList.contains(one.university))
		    			currList.add(one.university);
		    	}
		    	
			    // создаем адаптер
			    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, (currList));
			    // присваиваем адаптер списку
			    lvMain.setAdapter(adapter);
			    
			    lvMain.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						getActivity().sendBroadcast(new Intent("next_page_srv"));
						sPref.edit().putString("selectedUniversity", currList.get(position)).commit();
					}
			    });
		    	break;
		    case 1:
		    	title.setText(getActivity().getResources().getString(R.string.choose_two));
		    	
		    	for (SchedulesCollection one : schedulesCollection){
		    		if (one.university.equals(sPref.getString("selectedUniversity", "")) && !currList.contains(one.faculty))
		    			currList.add(one.faculty);
		    	}
		    	
			    // создаем адаптер
			    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, (currList));
			    // присваиваем адаптер списку
			    lvMain.setAdapter(adapter);
			    
			    lvMain.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						getActivity().sendBroadcast(new Intent("next_page_srv"));
						sPref.edit().putString("selectedFaculty", currList.get(position)).commit();
					}
			    });
		    	break;
		    case 2:
		    	title.setText(getActivity().getResources().getString(R.string.choose_three));
		    	
		    	for (SchedulesCollection one : schedulesCollection){
		    		if (one.university.equals(sPref.getString("selectedUniversity", "")) && one.faculty.equals(sPref.getString("selectedFaculty", "")) && !currList.contains(one.group)){
		    			currList.add(one.group);
		    			idsList.add(one.id);
		    			linksList.add(one.link);
		    		}
		    	}
		    	
			    // создаем адаптер
			    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, (currList));
			    // присваиваем адаптер списку
			    lvMain.setAdapter(adapter);
			    
			    lvMain.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						sPref.edit().putString("selectedGroup", currList.get(position)).commit();
						sPref.edit().putString("selectedGroupId", idsList.get(position)).commit();
						sPref.edit().putString("selectedGroupLink", linksList.get(position)).commit();
						getActivity().sendBroadcast(new Intent("select_group_srv"));
					}
			    });
		    	break;
		    }
		    return view;
	  }
	}