package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dataInput.samplescanner.ScanCodeDemo;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class ScanActivity extends Activity implements OnItemSelectedListener {
	
	InputStream in=null;
	
	private ExpandableListAdapter ExpAdapter;
	private ArrayList<Equipment> ExpListItems;
	private ExpandableListView ExpandList;
	
	//Flag to stop onCreate() from auto populating spinners
    private boolean spinner_flag = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		//Populate Floor Spinner
		Spinner spinner = (Spinner) findViewById(R.id.floorSpinner);
		populate("/Franchisee/Client/clientContract/ServiceAddress/*", spinner, "name");
		
		spinner.setOnItemSelectedListener(this);

		
		
		/*
		spinner = (Spinner) findViewById(R.id.roomSpinner);
		 populate("/Franchisee/Client/clientContract/ServiceAddress/Floor/*", spinner, "id");
		 */
		 
		 //STUFF TO DO WITH EXPANDABLE LIST
		 
		// get the listview
	     ExpandList = (ExpandableListView) findViewById(R.id.expandableEquipmentList);
	     ExpListItems = SetStandarGroups();
	     ExpAdapter = new ExpandableListAdapter(ScanActivity.this, ExpListItems);
	     ExpandList.setAdapter(ExpAdapter);
	     
	     // preparing list data
	     //prepareListData();
	     
	     //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
	     
	     // setting list adapter
	     //expListView.setAdapter(listAdapter);
		
	}

	private ArrayList<Equipment> SetStandarGroups() {
		
		ArrayList<Equipment> list = new ArrayList<Equipment>();
    	ArrayList<inspectionElement> tempInspectionElements = new ArrayList<inspectionElement>();
    	Equipment tempEquipment;
    	
    	Object temp = null;
    	
    	//Sets up temp objects
    	ExtinguisherPassFailElement tempExtinguisher;
    	FireHoseCabinetGoodPoorElement tempFireHoseCabinetGoodPoor;
    	
    	//An xpath instance
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        //Creates an InputStream and opens the file, then casts to InputSource
        InputStream in=null;
		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(),"/inspectiondata.xml"));
			//Toast.makeText(getBaseContext(), "File read from SD card YEAH", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(getBaseContext(), "Can't read inspection file from SD Card.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		InputSource is = new InputSource(in);
        
        //Performs xpath and returns list of nodes
        NodeList nodes = null;
        
		try {
			nodes = (NodeList) xpath.evaluate("/Franchisee/Client/clientContract/ServiceAddress/Floor/Room/*", is, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
        
        
      //An element node to hold the current working node
		Element element = null;
		Element attrElement = null;
		
		NodeList attrNodes = null;
		
		
		//For each piece of equipment
		for (int i = 0; i < nodes.getLength(); i++) {
			
			tempInspectionElements = new ArrayList<inspectionElement>();
			
			tempEquipment = new Equipment();
			
			//Add node attribute to string array
		      element = (Element) nodes.item(i);
		      
		      
		      tempEquipment.setName(element.getNodeName());
		      
		      if(element.getNodeName().equals("Extinguisher"))
		    	  temp = new ExtinguisherPassFailElement();
		      else if(element.getNodeName().equals("FireHoseCabinet"))
	    		  temp = new FireHoseCabinetGoodPoorElement();
		      
		    //Find Inspection Element Nodes
				try {
					attrNodes = (NodeList) xpath.evaluate("./*", element, XPathConstants.NODESET);
				} catch (XPathExpressionException e1) {
					e1.printStackTrace();
				}
			     
		      
		      //get inspection element names
		      for(int j=0; j < attrNodes.getLength(); j++){
		    	  attrElement = (Element) attrNodes.item(j);
		    	  
		    	  //Sees what object type it needs to be
		    	  if(element.getNodeName().equals("Extinguisher"))
			    	  temp = new ExtinguisherPassFailElement();
		    	  else if(element.getNodeName().equals("FireHoseCabinet"))
		    		  temp = new FireHoseCabinetGoodPoorElement();
		    	  else
		    		  temp = null;
		    	  
		    	  if (temp!=null){
			    	  ((inspectionElement) temp).setName(attrElement.getAttribute("name"));
			    	  tempInspectionElements.add((inspectionElement) temp);
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
	
	private void populate(String expression, Spinner spinner, String attribute){
        
        //An array of strings to hold the names
        ArrayList<String> options=new ArrayList<String>();
        
        //An xpath instance
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        //Creates an InputStream and opens the file, then casts to InputSource
        InputStream in=null;
		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(),"/inspectiondata.xml"));
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
        
        //Sets spinner
        spinner.setAdapter(adapter);
        
	}

	public void launchScan(View view){
		Intent intent = new Intent(this, ScanCodeDemo.class);
		startActivity(intent);
	}
	
	public void manual(View view){
		// COPIED DIRECTLY FROM MAINACTIVITY
		// TODO It seems that MainActivity has become redundant. Should revise.
		String extinguisherID = "33101";
		Equipment testEquip = ExpListItems.get(0);
		inspectionElement testElement = testEquip.getItems().get(0);
		String passFail = null;
		String inspectName = testElement.getName();
		if (testElement.getClass().equals(ExtinguisherPassFailElement.class)){
			int x = ((ExtinguisherPassFailElement) testElement).getPassFail();
			if (x > 0){
				passFail = "Pass";
			}
			else if (x < 0){
				passFail = "Fail";
			}
			else passFail = "none";
		}
		else {
			int x = ((FireHoseCabinetGoodPoorElement) testElement).getGoodPoor();
			if (x > 0){
				passFail = "Good";
			}
			else if (x < 0){
				passFail = "Poor";
			}
			else passFail = "none";
		}
		
		//Toast.makeText(getBaseContext(), passFail, Toast.LENGTH_SHORT).show();
		
		try {
			Document doc = DOM.getDOM(getBaseContext());
			
			// Root node; in this case, Franchisee
			Node firstNode = doc.getFirstChild();

			DOM.setPassFail("33101", inspectName, passFail, firstNode);
			
			// Write result
			DOM.writeDOMResults(doc, getBaseContext());

			Toast.makeText(getBaseContext(), "Output file written.", Toast.LENGTH_SHORT).show();
			
			// SO MANY EXCEPTIONS
			} catch(IOException e){
				Toast.makeText(getApplicationContext(),"IOException occurred.",Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SAXException e){
				Toast.makeText(getApplicationContext(),"SAXException occurred.",Toast.LENGTH_LONG).show();
			} catch (ParserConfigurationException e){
				Toast.makeText(getApplicationContext(),"ParserConfigurationException occurred.",Toast.LENGTH_LONG).show();
			} catch (TransformerConfigurationException e){
				Toast.makeText(getApplicationContext(),"TransformerConfigurationException occurred.",Toast.LENGTH_LONG).show();
			} catch (TransformerException e){
				Toast.makeText(getApplicationContext(),"TransformerException occurred.",Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (XPathExpressionException e){
				Toast.makeText(getApplicationContext(),"XPathExpressionException occurred.",Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
	    }
		
	//Spinner listener
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                    long id) {
		
		Log.i("ScanActivity","OnItemSelected Triggered");
            //Value chosen in spinner that event happened at
            String spinnerValue = (String) parent.getItemAtPosition(pos);
            //Stores child spinner so that child spinners can be updated in a chain reaction
            Spinner spinner_child = null;

            //check flag so that onCreate() doesn't populate everything right away
            if (spinner_flag == false){
                    spinner_flag = true;
                    Log.i("Main Data Entry", "Spinner_flag triggered");
            } else {
            	
            	Log.i("ScanActivity", "Got into else");

                    //Reference to parent spinner
                    Spinner spinner = (Spinner) parent;
                    //Check to see what spinner event occured at
                    if(spinner.getId() == R.id.floorSpinner)
                    {        
                            Log.i("Main Data Entry", "floor spinner event triggered");
                            //get child spinner  
                            spinner_child = (Spinner) findViewById(R.id.roomSpinner);
                            //update child spinner data
							populate("/Franchisee/Client/clientContract/ServiceAddress/Floor[@name='" + spinnerValue + "']/*",spinner_child,"id");
							Log.i("Main Data Entry", "floor contract spinner updated");

                    }
                    else
                    {
                            
                    }
            }
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	

}
