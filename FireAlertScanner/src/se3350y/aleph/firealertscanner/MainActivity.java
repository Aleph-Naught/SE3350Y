package se3350y.aleph.firealertscanner;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.dataInput.samplescanner.ScanCodeDemo;

public class MainActivity extends Activity {
	//DOMWriter dom = new DOMWriter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void scanActionSelected(View view) {
		// Create intent to call scanner activity
		Intent intent = new Intent (this, ScanCodeDemo.class);
		startActivity(intent);
	}
	
	/*// A test to verify XML reads, edits, and writes successfully.
	public void recordResults(View view){
		try {
		Document doc = dom.getDOM(getBaseContext());
		
		// Root node; in this case, Franchisee
		Node firstNode = doc.getFirstChild();

		dom.setPassFail("33101", "Weight", "Pass", firstNode);
		dom.setPassFail("33101", "Collar", "Fail", firstNode);
		dom.setPassFail("88103", "Requires Service or Repair", "Yes", firstNode);
		
		// Write result
		dom.writeDOMResults(doc, getBaseContext());

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
	}*/
	
}
