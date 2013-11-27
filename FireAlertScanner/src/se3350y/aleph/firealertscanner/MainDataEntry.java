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

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;

public class MainDataEntry extends Activity implements OnItemSelectedListener, DOMActivity{
	
	TCPController _tcpController = new TCPController(this);
	
	public class xmlLoader extends AsyncTask<getValuesPackage, Void, ArrayList<String> >{

		private final Spinner spinner;
		private final Button enterButton;
		
		public xmlLoader(Spinner _spinner, Button button){
			spinner = _spinner;
			enterButton = button;
		}
		
		@Override
		protected ArrayList<String> doInBackground(getValuesPackage... params) {
			// TODO Auto-generated method stub
			try {
				return getValues(params[0].getExpression(), params[0].getAttribute());
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> ar ) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainDataEntry.this,android.R.layout.simple_spinner_item, ar);
			adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			
			if(spinner.getId() == R.id.serviceAddressSpinner)
				enterButton.setEnabled(true);
			
			
			
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_data_entry);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Button enterButton = (Button) findViewById(R.id.enterButton);
		enterButton.setEnabled(false);
		

		try {
			//Sets up the textview with the name and id of the franchisee logged in
			//Displayed in bold style
			TextView tv = (TextView) findViewById(R.id.Franchisee);
			String name = getValues("/*[@name]", "name").toString(), id = getValues("/*[@id]", "id").toString();
			name = name.substring(1, name.length() - 1);
			id = id.substring(1, id.length() - 1);
			tv.setText("Franchisee: " + name + ", ID: " + id);
			tv.setTypeface(null, Typeface.BOLD);
			Log.i("Main Data Entry", "Franchisee TextView set");
			//TODO ask group what they think about this
			setTitle("Please Select...");
			
			Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);
		
			xmlLoader loader = new xmlLoader(spinner, new Button(this));
			
			loader.execute(new getValuesPackage("/Franchisee/*[@name]", "name"));

			spinner.setOnItemSelectedListener(this);
			
			spinner = (Spinner) findViewById(R.id.clientContractSpinner);
			
			spinner.setOnItemSelectedListener(this);

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
			
			final Activity a = this;
			final Context c = getApplicationContext();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("XML File Not Found");
			builder.setMessage("InspectionData.xml was not found on the SD card. Place the file on the SD card and restart the application.");
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					a.finish();
					Intent intent = new Intent(c, LoginActivity.class);
					
					// clear the stack
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					c.startActivity(intent);
				}
			});
			builder.show();
			
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Send Results");
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_tcp, null);
		final EditText portView = (EditText) dialogView.findViewById(R.id.portInput);
		final EditText ipView = (EditText) dialogView.findViewById(R.id.ipInput);
		builder.setView(dialogView);
		
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				// Retrieve the data from the Dialog
				String port = portView.getText().toString();
				String ip = ipView.getText().toString();
				_tcpController.Send(port, ip);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
		
		
	}
	
	public void makeToast(String text, int duration){
		Toast.makeText(MainDataEntry.this, text, duration).show();
	}
	
	public void getDataInput(View view) throws XPathExpressionException, IOException {
		
		Spinner client = (Spinner) findViewById(R.id.clientSpinner),
				serviceAddress = (Spinner) findViewById(R.id.serviceAddressSpinner);
		String clientSelection = client.getSelectedItem().toString(),
				serviceAddressSelection = serviceAddress.getSelectedItem().toString();
		
		Intent intent = new Intent(this, ScanActivity.class);
		String path = "/Franchisee/Client[@name='";
		Spinner clientSpinner = (Spinner) findViewById(R.id.clientSpinner);
		path += clientSpinner.getItemAtPosition(clientSpinner.getSelectedItemPosition());
		path += "']/clientContract[@id='";
		Spinner clientContractSpinner = (Spinner) findViewById(R.id.clientContractSpinner);
		path += clientContractSpinner.getItemAtPosition(clientContractSpinner.getSelectedItemPosition());
		path += "']/ServiceAddress[@address='";
		Spinner serviceAddressSpinner = (Spinner) findViewById(R.id.serviceAddressSpinner);
		path += serviceAddressSpinner.getItemAtPosition(serviceAddressSpinner.getSelectedItemPosition());
		path += "']";
		
		intent.putExtra("se3350y.aleph.firealertscanner.dataentry", path);
		Log.i("Main Data Entry","Put path: "+path);
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
		
		xmlLoader loader;
		
		Button enterButton = (Button) findViewById(R.id.enterButton);
		
		enterButton.setEnabled(false);
		
		//Reference to parent spinner
		Spinner spinner = (Spinner) parent;
		//Check to see what spinner event occured at
		if(spinner.getId() == R.id.clientSpinner)
		{	
			Log.i("Main Data Entry", "Client spinner event triggered");
			//get child spinner  
			spinner_child = (Spinner) findViewById(R.id.clientContractSpinner);
			
			loader = new xmlLoader(spinner_child, enterButton);
			
			
			loader.execute(new getValuesPackage("/Franchisee/Client[@name='" + spinnerValue + "']/*[@id]", "id"));

		}
		else if(spinner.getId() == R.id.clientContractSpinner)
		{
			Log.i("Main Data Entry", "Client contract spinner triggered");
			//get child spinner
			spinner_child = (Spinner) findViewById(R.id.serviceAddressSpinner);
			
			loader = new xmlLoader(spinner_child, enterButton);
			
			
			loader.execute(new getValuesPackage("/Franchisee/Client/clientContract[@id='" + spinnerValue + "']/*[@address]", "address"));

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//Report when nothing has been selected in spinner
		Log.i("Main Data Entry", "Nothing selected called");
	}

}
