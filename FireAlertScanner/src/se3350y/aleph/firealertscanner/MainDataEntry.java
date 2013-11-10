package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;

public class MainDataEntry extends Activity{

	ClientExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_data_entry);
		// Show the Up button in the action bar.
		setupActionBar();

		try {
			//Sets up the textview with the name and id of the franchisee logged in
			//Displayed in bold style
			TextView tv = (TextView) findViewById(R.id.Franchisee);
			tv.setText("Franchisee: " + getValues("/*[@name]", "name") + ", ID: " + getValues("/*[@id]", "id"));
			tv.setTypeface(null, Typeface.BOLD);
			Log.i("Main Data Entry", "Franchisee TextView set");
			//Catch all errors from xpath parsing
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.expandableClientList);

		// preparing list data
		prepareListData();

		listAdapter = new ClientExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

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

	private ArrayList<String> getValues(String expression, String attribute) throws XPathExpressionException{

		Log.i("Main Data Entry", "getValues() xPath parser called");

		//An array of strings to hold the attribute values
		ArrayList<String> ar = new ArrayList<String>();

		//An xpath instance
		XPath xpath = XPathFactory.newInstance().newXPath();

		//Creates an InputStream and opens the file, then casts to InputSource
		InputStream in=null;

		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(),"/InspectionData.xml"));
		} catch (FileNotFoundException e) {
			Log.i("Main data entry", "Can't read info from SD Card");
			e.printStackTrace();
		}

		InputSource is = new InputSource(in);
		Log.i("Main Data Entry", "Sucessfully read from SD Card");

		//Performs xpath and returns list of nodes
		NodeList nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
		Element element = null;

		//List of nodes stored in element data type
		for (int i = 0; i < nodes.getLength(); i++){
			element = (Element) nodes.item(i);
			//String of attribute values stored in array list
			ar.add(element.getAttribute(attribute));
		}

		Log.i("Main Data Entry", "Parsing completed");
		//Return the attribute values
		return ar;

	}


	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();


//		ArrayList<String> clientId= new ArrayList<String>();
//		ArrayList<String> clientName= new ArrayList<String>();
//		ArrayList<String> clientContractId= new ArrayList<String>();
//
//
//		Log.i("MainDataEntry.java", "prepare list reached");
//
//
//		try {
//			clientId = getValues("/Franchisee/*[@id]", "id");
//			clientName = getValues("/Franchisee/*[@name]", "name");
//			clientContractId= getValues("/Franchisee/Client/*[@id]", "id");
//
//
//		} catch (XPathExpressionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		for(int i = 0; i < clientId.size(); i++){
//			listDataHeader.add(clientName.get(i) + ": " + clientId.get(i));
//		}
//
//		for(int i = 0; i < clientId.size(); i++) {
//			listDataChild.put(listDataHeader.get(i), clientContractId);
//		}


	}
}
