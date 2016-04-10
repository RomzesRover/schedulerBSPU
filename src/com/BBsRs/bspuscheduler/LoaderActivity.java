package com.BBsRs.bspuscheduler;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class LoaderActivity extends Activity {
	
    //animate shape
    private ImageView mBackgroundShape;
    private ImageView mBackgroundShapeWhite;
    TextView show;
    SharedPreferences sPref;
    int check=0;
    
    Spinner locale;
    Spinner locale2;
    
    private final Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
		  //set up preferences
        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        File file = new File (this.getFilesDir(), "schedule.csv");
        Log.i("dfds", file.getAbsolutePath());
        if (!file.exists()){
        	startActivity(new Intent(this, ListOfAvailableSchedules.class));
        	finish();
        }
	    
	    this.setContentView(R.layout.loader);
	    
	    //init views
	    mBackgroundShape = (ImageView) findViewById(R.id.bg);
	    mBackgroundShapeWhite = (ImageView) findViewById(R.id.bg_white);
	    show = (TextView)findViewById(R.id.textView1);
	    locale = (Spinner)findViewById(R.id.spinnerLocale);
	    locale2 = (Spinner)findViewById(R.id.spinnerLocale2);
	    
	    locale.setSelection(sPref.getInt("week", 0));
	    locale2.setSelection(sPref.getInt("group", 0));
	    
	    locale.setOnItemSelectedListener(new OnItemSelectedListener(){    
	    	@Override
	    	public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
	    		check++;
	    		if(check>1){
		    		Editor ed = sPref.edit();   
		    		ed.putInt("week", i); 	
		    		ed.commit();
	    		}
	    	} 
	    	@Override     
	    	public void onNothingSelected(AdapterView<?> parentView) {}
	    }); 
	    
	    locale2.setOnItemSelectedListener(new OnItemSelectedListener(){    
	    	@Override
	    	public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
	    		check++;
	    		if(check>1){
		    		Editor ed = sPref.edit();   
		    		ed.putInt("group", i); 	
		    		ed.commit();
	    		}
	    	} 
	    	@Override     
	    	public void onNothingSelected(AdapterView<?> parentView) {}
	    }); 
	    
	    show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		});
	    
	    
	    //start animation
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
		    	onServiceOn();
			}
		}, 600);
	}
	
	private static final int ANIMATION_DURATION = 800;
	private float mFullScreenScale;
	
	private void onServiceOn() {
        if (mBackgroundShape == null) {
            return;
        }
        if (mFullScreenScale <= 0.0f) {
            mFullScreenScale = getMeasureScale();
        }
        
        mBackgroundShapeWhite.setVisibility(View.GONE);
        
        mBackgroundShape.animate()
        .scaleX(mFullScreenScale)
        .scaleY(mFullScreenScale)
        .setInterpolator(new AccelerateDecelerateInterpolator())
        .setDuration(ANIMATION_DURATION);
        
        AlphaAnimation anim = new AlphaAnimation(0, 1);
		anim.setDuration((long)(ANIMATION_DURATION*1.5));
		anim.setInterpolator(new DecelerateInterpolator());
		
		locale.setVisibility(View.VISIBLE);
		locale2.setVisibility(View.VISIBLE);
		locale.startAnimation(anim);
		locale2.startAnimation(anim);
    }
	
    private float getMeasureScale() {
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float displayHeight = outMetrics.heightPixels;
        float displayWidth  = outMetrics.widthPixels;
        return (Math.max(displayHeight, displayWidth) /
                getApplicationContext().getResources().getDimensionPixelSize(R.dimen.button_size)) * 2;
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFullScreenScale = getMeasureScale();
    }

}
