package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ScanActivity extends Activity {
	
	InputStream in=null;
	
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
		 
		/**
		try {
			in = new FileInputStream(new File(Environment.getExternalStorageDirectory(),"/inspectiondata.xml"));
			Toast.makeText(getBaseContext(), "File read from SD card YEAH", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(getBaseContext(), "Can't read inspection file from SD Card.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}**/
		
		Spinner spinner = (Spinner) findViewById(R.id.floorSpinner);
		populate("/Franchisee/Client/clientContract/ServiceAddress/*", spinner, "name");
		
		spinner = (Spinner) findViewById(R.id.roomSpinner);
		 populate("/Franchisee/Client/clientContract/ServiceAddress/Floor/*", spinner, "id");
		 
		 //STUFF TO DO WITH EXPANDABLE LIST
		 
		// get the listview
	     expListView = (ExpandableListView) findViewById(R.id.expandableEquipmentList);
	     
	     // preparing list data
	     prepareListData();
	     
	     listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
	     
	     // setting list adapter
	     expListView.setAdapter(listAdapter);
		
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
			//Toast.makeText(getBaseContext(), "File read from SD card YEAH", Toast.LENGTH_LONG).show();
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
			// TODO Auto-generated catch block
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
	
	 private void prepareListData() {
		 
		 listDataHeader = new ArrayList<String>();
	       
		 listDataChild = new HashMap<String, List<String>>();
		 
		 
		//An array of strings to hold the names
	        ArrayList<String> options=new ArrayList<String>();
	        
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        
	        
	      //An element node to hold the current working node
			Element element = null;
			Element attrElement = null;
			List<String>  inspectionElement = new ArrayList<String>();
			
			NodeList attrNodes = null;
			
			
			//For each piece of equipment
			for (int i = 0; i < nodes.getLength(); i++) {
				
				inspectionElement = new ArrayList<String>();
				
				//Add node attribute to string array
			      element = (Element) nodes.item(i);
			      
			      //Add Equipment
			      listDataHeader.add(element.getNodeName());
			      
			    //Find Inspection Element Nodes
					try {
						attrNodes = (NodeList) xpath.evaluate("./*", element, XPathConstants.NODESET);
					} catch (XPathExpressionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			      
			    int fail = 0;
			     
			      
			      //get inspection element names
			      for(int j=0; j < attrNodes.getLength(); j++){
			    	  attrElement = (Element) attrNodes.item(j);
			    	  
			    	  String name = null;
			    	  fail = attrNodes.getLength();
			    	  
			    	  try{
			    	  name = attrElement.getAttribute("name");
			    	  }
			    	  catch(NullPointerException e){
			    		  e.printStackTrace();
			    	  }
			    	  
			    	  inspectionElement.add(name);
			      }
			      
			      
			      listDataChild.put(listDataHeader.get(i),inspectionElement ); // Header
			      
			      
			}
			
			/**
	        // Adding child data
	        List<String> top250 = new ArrayList<String>();
	        top250.add("The Shawshank Redemption");
	        top250.add("The Godfather");
	        top250.add("The Godfather: Part II");
	        top250.add("Pulp Fiction");
	        top250.add("The Good, the Bad and the Ugly");
	        top250.add("The Dark Knight");
	        top250.add("12 Angry Men");
	 
	        List<String> nowShowing = new ArrayList<String>();
	        nowShowing.add("The Conjuring");
	        nowShowing.add("Despicable Me 2");
	        nowShowing.add("Turbo");
	        nowShowing.add("Grown Ups 2");
	        nowShowing.add("Red 2");
	        nowShowing.add("The Wolverine");
	 
	        List<String> comingSoon = new ArrayList<String>();
	        comingSoon.add("2 Guns");
	        comingSoon.add("The Smurfs 2");
	        comingSoon.add("The Spectacular Now");
	        comingSoon.add("The Canyons");
	        comingSoon.add("Europa Report");
	 
	        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), nowShowing);
	        listDataChild.put(listDataHeader.get(2), comingSoon);
	        **/
	    }
	

}
