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
	
	private TextView xmldata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmldisplay);
		
		xmldata = (TextView) findViewById(R.id.textView1);
		
		xmldata.setText("meow");
		
		

		
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
            	name = parser.getName();
            	break;
            }
            
            if(!(name==null))
            	break;
            
            eventType = parser.next();
		}
		
		
		
		
		xmldata.setText(name);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmldisplay, menu);
		return true;
	}

}
