package com.dataInput.samplescanner;

import se3350y.aleph.firealertscanner.*;
import android.view.Menu;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class ScanCodeDemo extends Activity {

	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private DataReceiver dataScanner = new DataReceiver();
	private TextView tv_getdata_from_scanner;
	private TextView tv_getdata_from_edittext;
	private EditText editText;
	
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
    	initialComponent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	
	private void initialComponent() {
		tv_getdata_from_scanner = (TextView)findViewById(R.id.tv_getdata_from_scanner);
		tv_getdata_from_edittext  = (TextView)findViewById(R.id.tv_getdata_from_edittext);
		editText = (EditText)findViewById(R.id.editText);
		editText.addTextChangedListener(textWatcher);
	}
	
	private void registerScanner() {
		dataScanner = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		registerReceiver(dataScanner, intentFilter);
	}
	
	private void unregisterReceiver() {
		if (dataScanner != null) unregisterReceiver(dataScanner);
	}
	
	private TextWatcher textWatcher =  new TextWatcher(){
        public void onTextChanged(CharSequence s, int start, int before, int count){}
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    	public void afterTextChanged(Editable s){
    		tv_getdata_from_edittext.setText("Get data from EditText : " + editText.getText().toString());
    	}
    }; 
    
	private class DataReceiver extends BroadcastReceiver {
		String content = "";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				tv_getdata_from_scanner.setText("Get data from Scanner : " + content);
			}
				
		}
	}
}
