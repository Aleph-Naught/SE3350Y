package se3350y.aleph.ScanActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import se3350.aleph.Login.LoginActivity;
import se3350y.aleph.Listeners.OnInspectionChangedListener;
import se3350y.aleph.Listeners.OnSavedFinishedListener;
import se3350y.aleph.MainDataEntry.MainDataEntry;
import se3350y.aleph.MainDataEntry.getValuesPackage;
import se3350y.aleph.firealertscanner.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;


public class ScanActivity extends Activity implements OnItemSelectedListener, DOMActivity {
	
	private static final String XML_FILENAME = "/FireAlertScanner/InspectionData.xml";
	
	@Override
	public void onBackPressed(){
		if (changesMade){
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						saveResults(new View(getBaseContext()));
						ScanActivity.super.onBackPressed();
						break;
	
					case DialogInterface.BUTTON_NEGATIVE:
						ScanActivity.super.onBackPressed();
						break;
					}
				}
			};
	
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("There are unsaved changes for this room, do you want to save?").setPositiveButton("Yes", dialogClickListener)
			.setNegativeButton("No", dialogClickListener).show();
	
	
			changesMade = false;
			
		}
	}
	
	public void loadList(){
		
		// get the listview
		
		ExpandableListAdapter old = ExpAdapter;
		
		

		ArrayList<Equipment> list = SetStandardGroups();
		ExpandList = (ExpandableListView) findViewById(R.id.expandableEquipmentList);
		ExpListItems = list;
		ExpAdapter = new ExpandableListAdapter(ScanActivity.this, ExpListItems);
		ExpandList.setAdapter(ExpAdapter);

		ExpandList.setClickable(true);
		ExpandList.setLongClickable(true);

		ExpAdapter.setOnInspectionChangedListener(new OnInspectionChangedListener(){
			@Override
			public void onInspectionChanged() {
				// TODO Auto-generated method stub
				Log.i("Scan Activity","Inspection Change Made");
				changesMade = true;
				
			}});

		ExpandList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				long packedPosition = ExpandList.getExpandableListPosition(position);
				if (ExpandableListView.getPackedPositionType(packedPosition) == 
						ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
					// get item ID's
					int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
					int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

					// handle data 

					Log.i("Scan Activity", "Child position: " + childPosition);
					Log.i("Scan Activity", "Parent position: " + groupPosition);

					// return true as we are handling the event.
					Equipment tempEquip = (Equipment) ExpAdapter.getParent(groupPosition);

					Log.i("Scan Activity", tempEquip.getName());
					String details = new String();
					for(int i = 0; i < tempEquip.getDetails().size(); i++){
						details = details + tempEquip.getDetails().get(i);
					}

					String message = details;

					AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);

					builder.setTitle(tempEquip.getName());
					builder.setMessage(message);
					builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});

					AlertDialog alertDialog = builder.create();
					alertDialog.show();

					return true;
				}
				return false;
			}
		});
		
		if(!old.getSaving()){
			saveButton.setEnabled(true);
			saveButton.setText("Save");
		}
					
	}
	
	
	public class spinnerLoader extends AsyncTask<spinnerPackage, Void, ArrayAdapter<String> >{

		Spinner spinner;

		public spinnerLoader(Spinner _spinner){
			spinner = _spinner;
		}

		@Override
		protected ArrayAdapter<String> doInBackground(spinnerPackage... params) {
			
			return populate(params[0].getPath(), params[0].getAttr());
			
		}

		@Override
		protected void onPostExecute(ArrayAdapter<String> adapter ) {
			
			Spinner floorSpinner = (Spinner) findViewById(R.id.floorSpinner);
			Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
			
			spinner.setAdapter(adapter);
			
			if(spinner.getId() == R.id.floorSpinner){
				
				spinnerLoader sloader = new spinnerLoader(roomSpinner);
				
				
				sloader.execute(new spinnerPackage(path+"/Floor[@name='" + spinner.getSelectedItem() + "']/*", "id"));
			}
			else if(spinner.getId() == R.id.roomSpinner){
				
				/*
				listLoader loader = new listLoader(ExpAdapter);
				
				loader.execute();
				*/
				
				loadList();
				
				currentFloor = floorSpinner.getSelectedItemPosition();
				currentRoom = roomSpinner.getSelectedItemPosition();
				
				
			}
			
			currentFloor = floorSpinner.getSelectedItemPosition();
		}

	}
	
	DOMWriter dom;
	Node fromNode = null;
	String path = "";
	
	boolean changesMade = false;
	boolean falseTrigger = false;
	int loadDone = 0;

	InputStream in=null;

	private ExpandableListAdapter ExpAdapter;
	private ArrayList<Equipment> ExpListItems;
	private ExpandableListView ExpandList;

	public int currentFloor;
	public int currentRoom;
	
	//Stuff for barcode scanner
	String barcode = null;
	String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	DataReceiver dataScanner = new DataReceiver();
	
	Button saveButton;
	
	private String m_Text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
		saveButton = (Button) findViewById(R.id.saveButton);
		ExpandList = (ExpandableListView) findViewById(R.id.expandableEquipmentList);
		ExpAdapter = new ExpandableListAdapter(this, new ArrayList<Equipment>());
		
		setTitle("Ready to Scan...");
		
		saveButton.setText("Loading...");
		saveButton.setEnabled(false);


		Bundle b = getIntent().getExtras();
		path = b.getString("se3350y.aleph.firealertscanner.dataentry");
		Log.i("ScanActivity", "Received path: "+path);

		dom = new DOMWriter(this);
		
		
		
		//Populate Floor Spinner
		Spinner spinner = (Spinner) findViewById(R.id.floorSpinner);
		
		
		ExpListItems = new ArrayList<Equipment>();
		
		spinnerLoader sloader = new spinnerLoader(spinner);
		
		saveButton.setEnabled(false);
		sloader.execute(new spinnerPackage(path+"/*", "name"));
		
		

		spinner.setOnItemSelectedListener(this);
		((Spinner) findViewById(R.id.roomSpinner)).setOnItemSelectedListener(this);
		
		
	}


	public void saveResults(View view){
			
			final Button saveButton = (Button) findViewById(R.id.saveButton);
			
			saveButton.setEnabled(false);
			saveButton.setText("Saving...");
		
			final Spinner floorSpinner = (Spinner) findViewById(R.id.floorSpinner);
			String newPath = path+"/Floor[@name='";
			newPath += floorSpinner.getItemAtPosition(currentFloor);
			newPath += "']/Room[@id='";
			final Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
			newPath += roomSpinner.getItemAtPosition(currentRoom);
			newPath += "']";
			Log.i("ScanActivity", "Using path: "+newPath);
			
			ExpAdapter.setSaving(true);
			floorSpinner.setEnabled(false);
			roomSpinner.setEnabled(false);
			
			ExpAdapter.notifyDataSetChanged();
			
			
			dom = new DOMWriter(this);
			
			dom.setOnSavedFinishedListener(new OnSavedFinishedListener(){

				@Override
				public void OnSavedFinished() {
					// TODO Auto-generated method stub
					
					saveButton.setEnabled(true);
					saveButton.setText("Save");
					
					ExpAdapter.setSaving(false);
					
					ExpAdapter.notifyDataSetChanged();
					
					floorSpinner.setEnabled(true);
					roomSpinner.setEnabled(true);
					
					resetAsterisk();
					
				}
				
			});
			
			dom.execute(new savePackage(ExpListItems,  newPath));
		
		if(changesMade)
			ExpAdapter.notifyDataSetChanged();

		changesMade = false;

	}
	
	public void resetAsterisk(){
		
		for(Equipment currEquip: ExpListItems){			
			currEquip.setChanged(false);
			for(InspectionElement currElem: currEquip.getItems()){
				currElem.setChanged(false);
			}
		}
		
	}

	public void makeToast(String text, int duration){
		Toast.makeText(ScanActivity.this, text, duration).show();
	}


	@Override
	protected void onResume() {
		registerScanner();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterScanner();
		super.onDestroy();
	}


	private ArrayList<Equipment> SetStandardGroups() {
		
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		ArrayList<InspectionElement> tempInspectionElements = new ArrayList<InspectionElement>();
		Equipment tempEquipment;

		Object temp = null;

		//An xpath instance
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		

		//Creates an InputStream and opens the file, then casts to InputSource
		InputStream in=null;
		
		
		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(), XML_FILENAME));
			//Toast.makeText(getBaseContext(), "File read from SD card YEAH", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(getBaseContext(), "Can't read inspection file from SD Card.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		InputSource is = new InputSource(in);

		//Performs xpath and returns list of nodes
		NodeList nodes = null;

		Spinner floorSpinner = (Spinner) findViewById(R.id.floorSpinner);
		Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);

		try {
			// needs to be populated before we make the nodelist
			nodes = (NodeList) xpath.evaluate(path+"/Floor[@name='"+floorSpinner.getSelectedItem()
					+"']/Room[@id='"+roomSpinner.getSelectedItem()+"']/*", is, XPathConstants.NODESET);
			Log.i("ScanActivity","Using room: "+roomSpinner.getSelectedItem());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}



		//An element node to hold the current working node
		Element element = null;
		Element attrElement = null;

		NodeList attrNodes = null;


		//For each piece of equipment
		for (int i = 0; i < nodes.getLength(); i++) {

			tempInspectionElements = new ArrayList<InspectionElement>();

			tempEquipment = new Equipment();

			//Add node attribute to string array
			element = (Element) nodes.item(i);


			tempEquipment.setName(element.getNodeName());
			tempEquipment.setId(element.getAttribute("id"));
			tempEquipment.setLocation(element.getAttribute("location"));
			

			NamedNodeMap map = element.getAttributes();
			
			ArrayList<String> attributes = new ArrayList<String>();
			for(int j = 0; j < map.getLength(); j++){
				attributes.add(map.item(j).getNodeName().toUpperCase() + ": " + map.item(j).getNodeValue() + "\n");
			}
			tempEquipment.setDetails(attributes);


			//Find Inspection Element Nodes
			try {
				attrNodes = (NodeList) xpath.evaluate("./*", element, XPathConstants.NODESET);
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			//get inspection element names
			for(int j=0; j < attrNodes.getLength(); j++){
				attrElement = (Element) attrNodes.item(j);

				//Sees what object type it needs to be
				if(element.getNodeName().equals("Extinguisher")){
					temp = new ExtinguisherPassFailElement();

					//Sets the passfail if it's already been written to the file
					String testResult = attrElement.getAttribute("testResult");

					if(testResult.equals("Pass")){
						((ExtinguisherPassFailElement) temp).setPassFail(1);
						((InspectionElement) temp).setCompleted(true);
					}
					else if(testResult.equals("Fail")){
						((ExtinguisherPassFailElement) temp).setPassFail(-1);
						((InspectionElement) temp).setCompleted(true);
					}



				}
				else if(element.getNodeName().equals("FireHoseCabinet")){

					//There's two different input options for this one
					if(attrElement.getAttribute("name").equals("Hose Re-Rack") || attrElement.getAttribute("name").equals("Hydrostatic Test Due")){
						temp = new FireHoseCabinetYesNoElement();

						//Sets the yesno if it's already been written to the file
						String testResult = attrElement.getAttribute("testResult");

						if(testResult.equals("Yes")){
							((FireHoseCabinetYesNoElement) temp).setYesNo(1);
							((InspectionElement) temp).setCompleted(true);
						}
						else if(testResult.equals("No")){
							((FireHoseCabinetYesNoElement) temp).setYesNo(-1);
							((InspectionElement) temp).setCompleted(true);
						}


					}
					else{
						temp = new FireHoseCabinetGoodPoorElement(ScanActivity.this);

						//Sets the goodPoor if it's already been written to the file
						String testResult = attrElement.getAttribute("testResult");

						if(testResult.equals("Good")){
							((FireHoseCabinetGoodPoorElement) temp).setGoodPoor(0);
							((InspectionElement) temp).setCompleted(true);
						}
						else if(testResult.equals("Poor")){
							((FireHoseCabinetGoodPoorElement) temp).setGoodPoor(1);
							((InspectionElement) temp).setCompleted(true);
						}
					}
				}
				else if(element.getNodeName().equals("EmergencyLight")){

					temp = new EmergencyLightYesNoElement();

					//Sets the goodPoor if it's already been written to the file
					String testResult = attrElement.getAttribute("testResult");

					if(testResult.equals("Yes")){
						((EmergencyLightYesNoElement) temp).setYesNo(1);
						((InspectionElement) temp).setCompleted(true);
					}
					else if(testResult.equals("No")){
						((EmergencyLightYesNoElement) temp).setYesNo(-1);
						((InspectionElement) temp).setCompleted(true);
					}

				}
				else
					temp = null;

				if (temp!=null){
					((InspectionElement) temp).setName(attrElement.getAttribute("name"));
					tempInspectionElements.add((InspectionElement) temp);
				}

			}



			tempEquipment.setItems(tempInspectionElements);

			list.add(tempEquipment);

		}

		
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}

	private ArrayAdapter<String> populate(String expression, String attribute){

		//An array of strings to hold the names
		ArrayList<String> options=new ArrayList<String>();

		//An xpath instance
		XPath xpath = XPathFactory.newInstance().newXPath();

		//Creates an InputStream and opens the file, then casts to InputSource
		InputStream in=null;
		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(), XML_FILENAME));
		} catch (FileNotFoundException e) {
			Toast.makeText(getBaseContext(), "Can't read inspection file from SD Card.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		InputSource is = new InputSource(in);

		//Performs xpath and returns list of nodes
		NodeList nodes = null;

		try {
			nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}


		//An element node to hold the current working node
		Element franchisee = null;

		for (int i = 0; i < nodes.getLength(); i++) {
			//Add node attribute to string array
			franchisee = (Element) nodes.item(i);
			options.add(franchisee.getAttribute(attribute));
		}

		//Create array adapter to change spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		//return adapter
		return adapter;

	}

	public void promptSave(final AdapterView<?> parent, final View view, final int pos,
			final long id){

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					//Yes button clicked
					saveResults(new View(getBaseContext()));
					loadRoom(parent, view, pos, id);
					ExpAdapter.setSaving(true);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					//loadRoom(parent, view, pos, id);
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("There are unsaved changes for this room, do you want to save?").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).show();


		changesMade = false;
	}

	public void loadRoom(AdapterView<?> parent, View view, int pos,
			long id){
		
		

		Log.i("ScanActivity","OnItemSelected Triggered");
		//Value chosen in spinner that event happened at
		String spinnerValue = (String) parent.getItemAtPosition(pos);
		//Stores child spinner so that child spinners can be updated in a chain reaction
		Spinner spinner_child = null;

		//Reference to parent spinner
		Spinner spinner = (Spinner) parent;
		//Check to see what spinner event occured at
		if(spinner.getId() == R.id.floorSpinner)
		{        
			
			Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
			
			spinnerLoader sloader = new spinnerLoader(roomSpinner);
			
			sloader.execute(new spinnerPackage(path+"/Floor[@name='" + spinnerValue + "']/*", "id"));
			
			loadDone = 1;
			
			

		}
		else if (spinner.getId() == R.id.roomSpinner){
			
			/*
			listLoader loader = new listLoader(ExpAdapter);
			
			loader.execute();
			*/
			
			loadList();

			currentRoom = spinner.getSelectedItemPosition();

		}


	}

	//Spinner listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		loadDone++;
		
		boolean roomFinished = false;
		
		if(loadDone > 3){
			roomFinished = ExpAdapter.groupsCompleted();
		}
		
		if(falseTrigger){
			falseTrigger = false;
			return;
		}
		else if(!roomFinished && loadDone > 3){
			promptImcomplete(parent, view, pos, id);
		}
		else if(changesMade && loadDone > 3){
			promptSave(parent, view, pos, id);
		}
		else{
			saveButton.setText("Loading...");
			saveButton.setEnabled(false);
			loadRoom(parent, view, pos, id);
		}


	}

	private void promptImcomplete(final AdapterView<?> parent, final View view, final int pos,
			final long id) {
		// TODO Auto-generated method stub
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	saveResults(new View(getBaseContext()));
		        	loadRoom(parent, view, pos, id);
		        	ExpAdapter.setSaving(true);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	loadDone = 2;
		        	//Reference to parent spinner
		    		Spinner spinner = (Spinner) parent;
		    		//Check to see what spinner event occured at
		    		if(spinner.getId() == R.id.floorSpinner){
		    			loadDone = 1;
		    			spinner.setSelection(currentFloor);
		    			spinner = (Spinner) findViewById(R.id.roomSpinner);
		    			loadDone = 0;
		    			spinner.setSelection(currentRoom);
		    			
		    		}
		    		else if(spinner.getId() == R.id.roomSpinner){
		    			falseTrigger = true;
		    			spinner.setSelection(currentRoom);
		    		}
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Equipment in this room has not been inspected. Do you want to continue?").setPositiveButton("Yes and Save", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
	}




	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
	
	public void onManClick(View view){
        Log.i("Scan Activity", "Manual Button Click");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter barcode:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Equipment temp = new Equipment();

                        // -1 so that we can see whether it doesn't match any
                        int groupPos = -1;

                        for(int i = 0; i < ExpListItems.size(); i++)
                                ExpandList.collapseGroup(i);

                        for(int i = 0; i < ExpListItems.size(); i++){
                                temp = ExpListItems.get(i);
                                if(temp.getId().equals(m_Text)){
                                        groupPos = i;
                                        break;
                                }
                        }
                        if (groupPos >= 0){
                                ExpandList.expandGroup(groupPos);
                                ExpandList.setSelection(groupPos);
                        }
                        else Toast.makeText(ScanActivity.this, "No matches found.", Toast.LENGTH_SHORT).show();
                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                }
        });
        builder.show();

}


	public void expandGroup(String _group){

		String group = _group.substring(0,5);

		Equipment temp = new Equipment();

		int groupPos = -1;



		for(int i = 0; i < ExpListItems.size(); i++){
			temp = ExpListItems.get(i);
			if(temp.getId().equals(group)){
				groupPos = i;
				break;
			}
		}

		//if scanner returned something
		if(groupPos!=-1)
		{

			for(int i = 0; i < ExpListItems.size(); i++){
				ExpandList.collapseGroup(i);
			}

			ExpandList.expandGroup(groupPos);
			ExpandList.setSelection(groupPos);
		}

	}

	private void listExpansion (String equipmentNo) {
		Equipment temp = new Equipment();


		int groupPos = 0;

		for(int i = 0; i < ExpListItems.size(); i++)
			ExpandList.collapseGroup(i);

		for(int i = 0; i < ExpListItems.size(); i++){
			temp = ExpListItems.get(i);
			if(temp.getId().equals(equipmentNo)){
				groupPos = i;
				break;
			}
		}

		ExpandList.expandGroup(groupPos);
		ExpandList.setSelection(groupPos);
	}

	private void registerScanner() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		registerReceiver(dataScanner, intentFilter);
	}

	private void unregisterScanner() {
		if (dataScanner != null) unregisterReceiver(dataScanner);
	}

	private class DataReceiver extends BroadcastReceiver {

		private String content = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				listExpansion(content);
			}
		}		
	}
}
