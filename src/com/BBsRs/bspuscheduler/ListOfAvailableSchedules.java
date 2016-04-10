package com.BBsRs.bspuscheduler;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ListOfAvailableSchedules extends FragmentActivity {
	
	//preferences 
    SharedPreferences sPref;
	
	ProgressDialog prDialog = null;
	private final Handler handler = new Handler();
	
	ViewPager pager;
	MyFragmentPagerAdapter pagerAdapter;
	
	ArrayList<SchedulesCollection> schedulesCollection = new ArrayList<SchedulesCollection>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    //set up preferences
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        
	    super.onCreate(savedInstanceState);
	    
	    this.setContentView(R.layout.activity_list_of_available_schedules);
	    
	    pager = (ViewPager) findViewById(R.id.pager);
	    pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
	    
	    //prDialog
        prDialog = new ProgressDialog(this);
        
        loadAndParseListOfAvailableSchedules();
	}
	
	public void loadAndParseListOfAvailableSchedules(){
		//show an dialog intermediate 
        prDialog.setIndeterminate(true);
        prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prDialog.setMessage(getText(R.string.wait));
        prDialog.setCancelable(false);
        prDialog.setCanceledOnTouchOutside(false);
        try {
        	prDialog.show();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					
					Document doc = Jsoup.connect("http://brothers-rovers.ru/application_uni_schedule/get_list.php").timeout(30000).get();
					
					JSONObject obj = new JSONObject(doc.text());

					JSONArray arr = obj.getJSONArray("posts");
					for (int i = 0; i < arr.length(); i++)
					{
						schedulesCollection.add(new SchedulesCollection(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("university"), arr.getJSONObject(i).getString("faculty"), arr.getJSONObject(i).getString("group"), arr.getJSONObject(i).getString("comment"), arr.getJSONObject(i).getString("link")));
					}
					
					
					handler.post(new Runnable(){
						@Override
						public void run() {
							try{
								pager.setAdapter(pagerAdapter);
								prDialog.dismiss();
							} catch(Exception e){}
						}
					});
				} catch(Exception e){
					handler.post(new Runnable(){
						@Override
						public void run() {
							try{
								prDialog.dismiss();
							} catch(Exception e){}
						}
					});
					e.printStackTrace();
				}
			}
        }).start();
        
        
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//turn up download receiver
		registerReceiver(nextPage, new IntentFilter("next_page_srv"));
		registerReceiver(selectGroup, new IntentFilter("select_group_srv"));
	}
    
	public void onPause() {
		super.onPause();
		unregisterReceiver(nextPage);
		unregisterReceiver(selectGroup);
	}
	
	private BroadcastReceiver nextPage = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        try {
	        	pagerAdapter.setProgress(pager.getCurrentItem()+2);
	        	pager.setCurrentItem(pagerAdapter.mProgress);
	    	} catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }
	};
	
	private BroadcastReceiver selectGroup = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        try {
	    		//show an dialog intermediate 
	            prDialog.setIndeterminate(true);
	            prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            prDialog.setMessage(getText(R.string.wait));
	            prDialog.setCancelable(false);
	            prDialog.setCanceledOnTouchOutside(false);
	            try {
	            	prDialog.show();
	        	} catch (Exception e){
	        		e.printStackTrace();
	        	}
	            
	            new Thread(new Runnable() {
	    			@Override
	    			public void run() {
	    				try {
	    					Thread.sleep(500);
	    					
	    					File file = new File (getApplicationContext().getFilesDir(), "schedule.csv");       
	    					
	    					/* Open a connection to that URL. */
	    					URL url = new URL(sPref.getString("selectedGroupLink", ""));
	    			        
	    			        InputStream input = new BufferedInputStream(url.openStream());
	    			       	OutputStream output = new FileOutputStream(file);
	    			       	byte data[] = new byte[1024];
	    			       	int count;
	    		    		while ((count = input.read(data)) != -1) {
	    		    			output.write(data, 0, count);
	    		    		}
	    		    		output.flush();
	    		    		output.close();
	    		    		input.close();
	    					
	    					handler.post(new Runnable(){
	    						@Override
	    						public void run() {
	    							try{
	    					        	startActivity(new Intent(getApplicationContext(), LoaderActivity.class));
	    					        	finish();
	    								prDialog.dismiss();
	    							} catch(Exception e){}
	    						}
	    					});
	    				} catch(Exception e){
	    					handler.post(new Runnable(){
	    						@Override
	    						public void run() {
	    							try{
	    								prDialog.dismiss();
	    							} catch(Exception e){}
	    						}
	    					});
	    					e.printStackTrace();
	    				}
	    			}
	            }).start();
	    	} catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }
	};
	
	  private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		  
		  	public int mProgress = 1;
		  	
		    public MyFragmentPagerAdapter(FragmentManager fm) {
		      super(fm);
		    }
		    
		    @Override
		    public int getItemPosition(Object object) {
		        return POSITION_NONE;
		    }
		    
		    @Override
		    public Fragment getItem(int position) {
		    	return PageFragment.newInstance(position, schedulesCollection);
		    }
		    
		    @Override
		    public int getCount() {
		      return mProgress;
		    }
		    
		    public void setProgress(int progress){
		    	mProgress = progress;
		    	this.notifyDataSetChanged();
		    }
		  }
}
