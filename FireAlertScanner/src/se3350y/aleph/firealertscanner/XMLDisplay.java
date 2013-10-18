package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;



import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.app.Activity;
import android.content.res.AssetManager;

public class XMLDisplay extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmldisplay);
		List<se3350y.aleph.firealertscanner.XMLParser.Entry> entries = null;
		InputStream raw = null;
		XMLParser XmlParser = new XMLParser();
		
		try {
			AssetManager am = this.getAssets();
			raw = am.open("inspectiondata.xml");
            entries = XmlParser.parse(raw);
            TextView tc = (TextView) findViewById(R.id.xml_textview);
            tc.setText(entries.get(0).toString());
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (raw != null) {
                try {
					raw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmldisplay, menu);
		return true;
	}
}
