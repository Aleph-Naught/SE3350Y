package se3350y.aleph.firealertscanner;

import android.os.Bundle;
import android.os.Environment;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.io.*;

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
		Intent intent = new Intent (this, ScanCodeDemo.class);
		startActivity(intent);
	}

	/* RECORD RESULTS */
	
	public void recordResults(View view){
		// if SD card is not available
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(getBaseContext(), "SD card not available.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Toast.makeText(getBaseContext(), "SD card is available! Ten outta ten", Toast.LENGTH_SHORT).show();
		File workingDir = Environment.getExternalStorageDirectory();
		File file = new File(workingDir,"/output.xml");
		
		if(!file.exists()) {
			try {
            	file.createNewFile();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),"Error creating file!",Toast.LENGTH_LONG).show();
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<Tom><Likes><Men><.jpg></.jpg></Men></Likes></Tom>");
			bw.close();
		} catch (IOException e) {
	             Toast.makeText(getApplicationContext(),"Error writing file!",Toast.LENGTH_LONG).show();
	     }
	}
	
}
