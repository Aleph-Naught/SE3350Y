package com.dataInput.samplescanner;

import se3350y.aleph.firealertscanner.*;
import android.view.Menu;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class ScanCodeDemo extends Activity {

	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private DataReceiver dataScanner = new DataReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_code_demo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan_code_demo, menu);
		return true;
	}

    @Override
	protected void onResume() {
    	registerScanner();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	
	public void registerScanner() {
		dataScanner = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		registerReceiver(dataScanner, intentFilter);
	}
	
	private void unregisterReceiver() {
		if (dataScanner != null) unregisterReceiver(dataScanner);
	} 
    
	private class DataReceiver extends BroadcastReceiver {
		String content = "";
		@Override
		
		//TODO redo the onrecieve so that it can be used in the scan activity. may need to remake the scan code demo class. hurr 
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				Intent intent1 = new Intent(getApplicationContext(), ScanActivity.class);
				intent1.putExtra("EXTRA_MESSAGE", content);
				startActivity(intent1);
			}
				
		}
	}
}
