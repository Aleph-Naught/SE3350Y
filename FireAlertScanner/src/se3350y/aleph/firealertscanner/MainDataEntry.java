package se3350y.aleph.firealertscanner;

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
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;

public class MainDataEntry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_data_entry);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		TextView tv = (TextView) findViewById(R.id.Franchisee);
		try {
			tv.setText("Franchisee: " + getValues("/*", "name").get(0));
			tv.setTypeface(null, Typeface.BOLD);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			//Create array adapter to change spinner
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/*", "name"));
			Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);
			//Sets spinner
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/*", "id"));
			spinner = (Spinner) findViewById(R.id.clientContractSpinner);
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract/*", "address"));
			spinner = (Spinner) findViewById(R.id.serviceAddressSpinner);
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract/ServiceAddress/*", "name"));
			spinner = (Spinner) findViewById(R.id.floorSpinner);
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract/ServiceAddress/Floor/*", "id"));
			spinner = (Spinner) findViewById(R.id.roomsSpinner);
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract/ServiceAddress/Floor/Room/*", "location"));
			spinner = (Spinner) findViewById(R.id.equiptmentSpinner);
			spinner.setAdapter(adapter);
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract/ServiceAddress/Floor/Room/*/*", "name"));
			spinner = (Spinner) findViewById(R.id.elementSpinner);
			spinner.setAdapter(adapter);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<String> getValues(String expression, String attribute) throws XPathExpressionException{
		
		//An array of strings to hold the names
		ArrayList<String> ar = new ArrayList<String>();
		
		//An xpath instance
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		//Creates an InputStream and opens the file, then casts to InputSource
		InputStream in=null;
		in = getResources().openRawResource(R.raw.inspectiondata);
		InputSource is = new InputSource(in);
		
		//Performs xpath and returns list of nodes
		NodeList nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
		
		
		//An element node to hold the current working node
		Element element = null;
		
		for (int i = 0; i < nodes.getLength(); i++) {
			//Add node attribute to string array
		      element = (Element) nodes.item(i);
		      ar.add(element.getAttribute(attribute));
		}
		
		return ar;
		
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
