package com.BBsRs.bspuscheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
                
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	finish();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static int fragmentNumber;
        SharedPreferences sPref;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragmentNumber = sectionNumber;
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
        	  //set up preferences
            sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            
            TextView nope = (TextView)rootView.findViewById(R.id.textView1);
            ListView list = (ListView)rootView.findViewById(R.id.listView1);
            
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("aim_12-15_2.csv"), "Cp1251"));

                // do reading, usually loop until end of file reading  
                ArrayList<String> lines = new ArrayList<String>();
                String mLine;
                
                //define range of days
                int days[] = {0, 0, 0, 0, 0, 0, 0, 0};
                int currentLine = 0;
                
                while ((mLine = reader.readLine()) != null) {
                    //process line
                	lines.add(mLine); // process line 
                	
	                if(mLine != null && mLine.contains(getString(R.string.title_section1))) {
	                	days[1] = currentLine;
	                }
	                if(mLine != null && mLine.contains(getString(R.string.title_section2))) {
	                	days[2] = currentLine;
	                }
	                if(mLine != null && mLine.contains(getString(R.string.title_section3))) {
	                	days[3] = currentLine;
	                }
	                if(mLine != null && mLine.contains(getString(R.string.title_section4))) {
	                	days[4] = currentLine;
	                }
	                if(mLine != null && mLine.contains(getString(R.string.title_section5))) {
	                	days[5] = currentLine;
	                }
	                if(mLine != null && mLine.contains(getString(R.string.title_section6))) {
	                	days[6] = currentLine;
	                }
	                
	                currentLine++;
                 }
                days[7]=currentLine;
                reader.close();
                
                //create collection
                ArrayList<DiscCollection> discCollection = new ArrayList<DiscCollection>();
                
                int numberInUri=1;
                String TimeInUri="8:30-10:05";
                for (int r = days[fragmentNumber]; r < days[fragmentNumber+1]; r++) {
                	
                	if (lines.get(r).split(";").length==10){
                		
                		//calculate current number in uri
                		if (lines.get(r).split(";")[1].length()>0)
                			numberInUri=Integer.parseInt(lines.get(r).split(";")[1]);
                		
                		//calculate current number in uri
                		if (lines.get(r).split(";")[2].length()>0)
                			TimeInUri=(lines.get(r).split(";")[2]);
                		
		                String discName = lines.get(r).split(";")[6];
		                String weeks = lines.get(r).split(";")[3];
		                String numberInUni = numberInUri+"";
		                String Time = TimeInUri;
		                String group = lines.get(r).split(";")[5];
		                String place = lines.get(r).split(";")[7];
		                String curator = lines.get(r).split(";")[8];
		                String type = lines.get(r).split(";")[9];
		                String parity = lines.get(r).split(";")[4];
		                
		                //check for week equals
                        if (sPref.getInt("week", 0)==0){
                        	//check for group equals
                    		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.equals("0")){
                    			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type, parity));
                    		}
                        } else {
                        	//check for week parity equals
                        	if ((parity.equals("0")) || (parity.equals("1") && sPref.getInt("week", 0)%2!=0) || (parity.equals("2") && sPref.getInt("week", 0)%2==0)){
                        		//check for week equals
                        		String weeksInSplit[] = weeks.replaceAll(" ", "").split(",");
                        	
	                        	for (String oneWeekValue : weeksInSplit){
	                        		//if this is just number
	                        		if (oneWeekValue.replaceAll("\\.\\.", "#").split("#").length==1){
	                                	if (sPref.getInt("week", 0)==Integer.parseInt(oneWeekValue)){
	                                		//check for group equals
	                                		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.equals("0")){
	                                			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type, parity));
	                                			break;
	                                		}
	                                	}
	                        		}
	                        		//if this is range
	                        		if (oneWeekValue.replaceAll("\\.\\.", "#").split("#").length==2){
	                                	int weekStart = Integer.valueOf(oneWeekValue.replaceAll("\\.\\.", "#").split("#")[0]);
	                                	int weekEnd = Integer.valueOf(oneWeekValue.replaceAll("\\.\\.", "#").split("#")[1]);
	                                	if (sPref.getInt("week", 0)>=weekStart && sPref.getInt("week", 0)<=weekEnd){
	                                		//check for group equals
	                                		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.equals("0")){
	                                			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type, parity));
	                                			break;
	                                		}
	                                	}
	                        		}
	                        	}
                        	}
                        }
                		
                		Log.e("man", lines.get(r).split(";")[2]);
                	}
                }
                
                if (discCollection.size()==0){
                	nope.setVisibility(View.VISIBLE);
                	nope.setText(getActivity().getString(R.string.emptylist));
                	list.setVisibility(View.GONE);
                } else {
                	nope.setVisibility(View.GONE);
                	list.setVisibility(View.VISIBLE);
                	ListAdapter listAdapter = new ListAdapter(getActivity(), discCollection);
                    list.setAdapter(listAdapter);
                }
            } catch(Exception ioe) {
                ioe.printStackTrace();
                nope.setVisibility(View.VISIBLE);
                nope.setText(getActivity().getString(R.string.error));
            	list.setVisibility(View.GONE);
            }
            
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
