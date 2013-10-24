package se3350y.aleph.firealertscanner;

import android.os.Bundle;
import android.os.Environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.dataInput.samplescanner.ScanCodeDemo;

public class MainActivity extends Activity {

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

	/* RECORD RESULTS */
	
	public static void setPassFail(String equipmentID, String inspectionElementID, String passOrFail, Node rootNode) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		String path = "//*[@id='" + equipmentID + "']/*[@name='" + inspectionElementID + "']/@testResult";
		NodeList list = (NodeList) xpath.evaluate(path,
				rootNode, XPathConstants.NODESET);
		list.item(0).setTextContent(passOrFail);
	}
	
	public static Document getDOM(Context context) throws SAXException, IOException, ParserConfigurationException{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(context, "SD card not available.", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		File workingDir = Environment.getExternalStorageDirectory();
		File file = new File(workingDir,"/InspectionData.xml");
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		return doc;
	}
	
	public static void writeDOMResults(Document doc, Context context) throws TransformerException{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(doc);
		File modifiedFile = new File(Environment.getExternalStorageState(),"/Modified.xml");
		
		if(modifiedFile.exists())
			modifiedFile.delete();
		try {
        	modifiedFile.createNewFile();
		} catch (IOException e) {
			Toast.makeText(context,"Error creating file!",Toast.LENGTH_LONG).show();
		}
		
		StreamResult result = new StreamResult(modifiedFile);
		transformer.transform(source, result);
	}
	
	public void recordResults(View view){
		// if SD card is not available
//		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//			Toast.makeText(getBaseContext(), "SD card not available.", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		
		try {
//		File workingDir = Environment.getExternalStorageDirectory();
//		File file = new File(workingDir,"/InspectionData.xml");
//		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = getDOM(getBaseContext());
		
		// Root node; in this case, Franchisee
		Node firstNode = doc.getFirstChild();

		setPassFail("33101", "6 Year Insp", "Fail", firstNode);
		setPassFail("33101", "Pull Pin", "Fail", firstNode);
		setPassFail("77207", "Cabinet Condition", "Good", firstNode);
		
		// Write result
//		Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		DOMSource source = new DOMSource(doc);
//		File modifiedFile = new File(workingDir,"/Modified.xml");
//		
//		if(modifiedFile.exists())
//			modifiedFile.delete();
//		try {
//        	modifiedFile.createNewFile();
//		} catch (IOException e) {
//			Toast.makeText(getApplicationContext(),"Error creating file!",Toast.LENGTH_LONG).show();
//		}
//		
//		StreamResult result = new StreamResult(modifiedFile);
//		transformer.transform(source, result);
		writeDOMResults(doc, getBaseContext());

		Toast.makeText(getBaseContext(), "Output file written.", Toast.LENGTH_SHORT).show();
		
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
		} catch (XPathExpressionException e){
			
		}
	}
	
}
