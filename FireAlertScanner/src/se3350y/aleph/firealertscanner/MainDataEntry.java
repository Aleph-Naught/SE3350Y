package se3350y.aleph.firealertscanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;

import se3350y.aleph.firealertscanner.XMLParse;

public class MainDataEntry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_data_entry);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);
		
		
		try {
			populate("/Franchisee/*", spinner);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		spinner = (Spinner) findViewById(R.id.locationsSpinner);
		
		try {
			populate("/Franchisee/Client/clientContract/ServiceAddress/Floor/Room/Extinguisher/*", spinner);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		/**
		Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_example_content,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner = (Spinner) findViewById(R.id.locationsSpinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_example_content,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner = (Spinner) findViewById(R.id.roomsSpinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_example_content,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner = (Spinner) findViewById(R.id.equiptmentSpinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_example_content,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		**/
	}
	
	private void populate(String expression, Spinner spinner) throws XPathExpressionException{
		
		//An array of strings to hold the names
		ArrayList<String> options=new ArrayList<String>();
		
		//An xpath instance
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		//Creates an InputStream and opens the file, then casts to InputSource
		InputStream in=null;
		in = getResources().openRawResource(R.raw.inspectiondata);
		InputSource is = new InputSource(in);
		
		//Performs xpath and returns list of nodes
		NodeList nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
		
		
		//An element node to hold the current working node
		Element franchisee = null;
		
		for (int i = 0; i < nodes.getLength(); i++) {
			//Add node attribute to string array
		      franchisee = (Element) nodes.item(i);
		      options.add(franchisee.getAttribute("name"));
		}
		
		//Create array adapter to change spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
		
		//Sets spinner
		spinner.setAdapter(adapter);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_data_entry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void getDataInput(View view) throws XPathExpressionException, IOException {
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
