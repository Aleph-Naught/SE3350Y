package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileInputStream;
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
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;

public class MainDataEntry extends Activity implements OnItemSelectedListener, DOMActivity{
	TCPController _tcpController = new TCPController(this);

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

			//Create array adapter to change spinners
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainDataEntry.this,android.R.layout.simple_spinner_item,getValues("/Franchisee/*[@name]", "name"));
			//Find the client spinner
			Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);
			//Set spinner text and sets item changed listener to wait for user selections
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);
			Log.i("Main Data Entry", "Client spinner adapter and listener set");
			//Sets client contract spinner with a listener
			spinner = (Spinner) findViewById(R.id.clientContractSpinner);
			spinner.setOnItemSelectedListener(this);
			Log.i("Main Data Entry", "Client contract spinner adapter and listener set");

			//Catch all errors from xpath parsing
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	//xpath parsing function
	//Takes expressions and attribute as parameters and returns an array list with attribute value
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
	
	public void launchTCP(View view){
//		Intent intent = new Intent (this, ClientView.class);
//		startActivity(intent);
		final Dialog dialog = new Dialog(MainDataEntry.this);
		dialog.setTitle("Send Results");
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_tcp, null));
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		
		Button okButton = (Button) dialog.findViewById(R.id.tcpOkButton);
		okButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// Retrieve the data from the Dialog
				String port = ((EditText)dialog.findViewById(R.id.portInput)).getText().toString();
				String ip = ((EditText)dialog.findViewById(R.id.ipInput)).getText().toString();
				_tcpController.Send(port, ip);
				dialog.dismiss();
			}
		});
		
		Button cancelButton = (Button) dialog.findViewById(R.id.tcpCancelButton);
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	public void makeToast(String text, int duration){
		Toast.makeText(MainDataEntry.this, text, duration).show();
	}
	
	public void getDataInput(View view) throws XPathExpressionException, IOException {
		
		Intent intent = new Intent(this, ScanActivity.class);
		startActivity(intent);
	}

	//Spinner listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		//Value chosen in spinner that event happened at
		String spinnerValue = (String) parent.getItemAtPosition(pos);
		//Stores child spinner so that child spinners can be updated in a chain reaction
		Spinner spinner_child = null;



		//Reference to parent spinner
		Spinner spinner = (Spinner) parent;
		//Check to see what spinner event occured at
		if(spinner.getId() == R.id.clientSpinner)
		{	
			Log.i("Main Data Entry", "Client spinner event triggered");
			//get child spinner  
			spinner_child = (Spinner) findViewById(R.id.clientContractSpinner);
			try{
				//update child spinner data
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainDataEntry.this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client[@name='" + spinnerValue + "']/*[@id]", "id"));
				spinner_child.setAdapter(adapter);
				Log.i("Main Data Entry", "Client contract spinner updated");
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
		else if(spinner.getId() == R.id.clientContractSpinner)
		{
			Log.i("Main Data Entry", "Client contract spinner triggered");
			//get child spinner
			spinner_child = (Spinner) findViewById(R.id.serviceAddressSpinner);
			try{
				//update child spinner data
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainDataEntry.this,android.R.layout.simple_spinner_item,getValues("/Franchisee/Client/clientContract[@id='" + spinnerValue + "']/*[@address]", "address"));
				spinner_child.setAdapter(adapter);
				Log.i("Main Data Entry", "Service address spinner updated");
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//Report when nothing has been selected in spinner
		Log.i("Main Data Entry", "Nothing selected called");
	}

}
