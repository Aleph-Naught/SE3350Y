package se3350y.aleph.firealertscanner;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.dataInput.samplescanner.ScanCodeDemo;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void scanActionSelected(View view) {
		// Create intent to call scanner activity
		Intent intent = new Intent (this, XMLDisplay.class);
		startActivity(intent);
	}

}
