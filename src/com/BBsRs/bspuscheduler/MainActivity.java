package com.BBsRs.bspuscheduler;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

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
                POIFSFileSystem fs = new POIFSFileSystem(getActivity().getAssets().open("schedule1.xls"));
                HSSFWorkbook wb = new HSSFWorkbook(fs);
                HSSFSheet sheet = wb.getSheetAt(0);
                HSSFRow row;
                HSSFCell cell;

                int rows; // No of rows
                rows = sheet.getPhysicalNumberOfRows();

                int cols = 0; // No of columns
                int tmp = 0;

                // This trick ensures that we get the data properly even if it doesn't start from first few rows
                for(int i = 0; i < 10 || i < rows; i++) {
                    row = sheet.getRow(i);
                    if(row != null) {
                        tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                        if(tmp > cols) cols = tmp;
                    }
                }
                
                int days[] = {-1, 0, rows, rows, rows, rows, rows, rows};
                for(int r = 0; r < rows; r++) {
                    row = sheet.getRow(r);
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section1))) {
                    	days[1] = r;
                    }
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section2))) {
                    	days[2] = r;
                    }
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section3))) {
                    	days[3] = r;
                    }
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section4))) {
                    	days[4] = r;
                    }
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section5))) {
                    	days[5] = r;
                    }
                    if(row != null && row.getCell(1).toString().contains(getString(R.string.title_section6))) {
                    	days[6] = r;
                    }
                }
                
                ArrayList<DiscCollection> discCollection = new ArrayList<DiscCollection>();
                
                for(int r = days[fragmentNumber]; r < days[fragmentNumber+1]; r++) {
                    row = sheet.getRow(r);
                    if(row != null) {
                    	
                    	String rowInText = "";
                    	
                    	
                        for(int c = 2; c < cols; c++) {
                            cell = row.getCell((short)c);
                            if(cell != null) {
                            	if (cell.toString() == null || cell.toString().length()<1){
                        			if (sheet.getRow(r-1) != null)
                        				cell = sheet.getRow(r-1).getCell((short)c);
                            	}
                            	
                            	if (cell.toString() == null || cell.toString().length()<1){
                        			if (sheet.getRow(r-2) != null)
                        				cell = sheet.getRow(r-2).getCell((short)c);
                            	}
                            	
                            	if (cell.toString() == null || cell.toString().length()<1){
                        			if (sheet.getRow(r-3) != null)
                        				cell = sheet.getRow(r-3).getCell((short)c);
                            	}
                            	
                            	if (cell.toString() == null || cell.toString().length()<1){
                        			if (sheet.getRow(r-4) != null)
                        				cell = sheet.getRow(r-4).getCell((short)c);
                            	}
                            	
                            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            	rowInText+=cell.toString()+"#";
                            }
                        }
                        
                        String discName = rowInText.split("#")[4];
                    	String weeks = rowInText.split("#")[2];
                        String numberInUni = rowInText.split("#")[0];
                        String Time = rowInText.split("#")[1];
                        String group = rowInText.split("#")[3];
                        String place = rowInText.split("#")[5];
                        String curator = rowInText.split("#")[6];
                        String type = rowInText.split("#")[7];
                        
                        
                        if (sPref.getInt("week", 0)==0){
                    		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.length()>1){
                    			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type));
                    		}
                        } else {
                        	String weeksInSplit[] = weeks.replaceAll(" ", "").split(",");
                        	
                        	for (String oneWeekValue : weeksInSplit){
                        		//if this is just number
                        		if (oneWeekValue.replaceAll("\\.\\.", "#").split("#").length==1){
                                	if (sPref.getInt("week", 0)==Integer.parseInt(oneWeekValue)){
                                		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.length()>1){
                                			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type));
                                			break;
                                		}
                                	}
                        		}
                        		//if this is range
                        		if (oneWeekValue.replaceAll("\\.\\.", "#").split("#").length==2){
                                	int weekStart = Integer.valueOf(oneWeekValue.replaceAll("\\.\\.", "#").split("#")[0]);
                                	int weekEnd = Integer.valueOf(oneWeekValue.replaceAll("\\.\\.", "#").split("#")[1]);
                                	if (sPref.getInt("week", 0)>=weekStart && sPref.getInt("week", 0)<=weekEnd){
                                		if (sPref.getInt("group", 0)==0 || group.contains(""+sPref.getInt("group", 0)) || group.length()>1){
                                			discCollection.add(new DiscCollection(discName, weeks, numberInUni, Time, group, place, curator, type));
                                			break;
                                		}
                                	}
                        		}
                        	}
                        }
                        
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
