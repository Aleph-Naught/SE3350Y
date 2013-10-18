package se3350y.aleph.firealertscanner;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class XMLDisplay extends Activity {
	
	public String name;
	
	public String id;
	
	private TextView xmldata;
	
	private TextView xmlequipType;
	private TextView xmllocation;
	private TextView xmlsize;
	private TextView xmltype;
	private TextView xmlmodel;
	private TextView xmlserialNo;
	
	private String equipType;
	private String location;
	private String size;
	private String type;
	private	String model;
	private String serialNo;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmldisplay);
		
		
		xmlequipType = (TextView) findViewById(R.id.equipType);
		xmllocation = (TextView) findViewById(R.id.location);
		xmlsize = (TextView) findViewById(R.id.size);
		xmltype = (TextView) findViewById(R.id.type);
		xmlmodel = (TextView) findViewById(R.id.model);
		xmlserialNo = (TextView) findViewById(R.id.SerialNo);
		
		
		id = "33101";
		
		

		
	}
	
	public void parse(View view){
		
		
		XmlPullParserFactory pullParserFactory;
		
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			    InputStream in_s = getApplicationContext().getAssets().open("InspectionData.xml");
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	            parser.setInput(in_s, null);

	            parseXML(parser);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
		// TODO Auto-generated method stub
		
		int eventType = parser.getEventType();
		
		name = null;
		

		while (eventType != XmlPullParser.END_DOCUMENT){
            
            switch (eventType){
            
            case XmlPullParser.START_TAG:
            	
            	
            	if(parser.getName().equals("Room")){
            		//Found Room
            		
            		eventType = parser.nextTag();
            		
            		while(!parser.getName().equals("Room")){ //While room end tag is not found
            			
	            		switch(eventType){
	            		
	            		
	            		case XmlPullParser.START_TAG:
	            			if(parser.getAttributeCount() > 2 && parser.getAttributeName(0).equals("id") && parser.getAttributeValue(0).equals(id)){
	            				name = parser.getName();
	            				equipType = parser.getName();
	            				location = parser.getAttributeValue(1);
	            				size = parser.getAttributeValue(2);
	            				type = parser.getAttributeValue(3);
	            				model = parser.getAttributeValue(4);
	            				serialNo = parser.getAttributeValue(5);
	            				
	            				break; //Break room loop
	            			}
	            			
	            			
	     
	            		}
	            		
	            		if(!name.equals(null))
	            		
	            		eventType = parser.nextTag();
	            			
            		}
            		//break; //Done searching Room
            	}
            	
            }
            
            if(!(name==null))
            	break;
            
            eventType = parser.next();
		}
		
		
		
		
		
		xmlequipType.setText(equipType);
		xmllocation.setText(location);
		xmlsize.setText(size);
		xmltype.setText(type);
		xmlmodel.setText(model);
		xmlserialNo.setText(serialNo);
		
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmldisplay, menu);
		return true;
	}

}
