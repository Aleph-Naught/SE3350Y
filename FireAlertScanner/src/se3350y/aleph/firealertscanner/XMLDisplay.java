package se3350y.aleph.firealertscanner;

import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;

public class XMLDisplay extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmldisplay);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmldisplay, menu);
		return true;
	}
}
