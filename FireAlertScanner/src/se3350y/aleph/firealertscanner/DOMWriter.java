package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;
import android.widget.Toast;

/**
 * A class to store the static DOM-related files for reading, editing, and writing XML.
 * @author Benjamin Schubert
 */
public class DOMWriter {
	DOMActivity _activity;
	
	
	public DOMWriter(DOMActivity a){
		_activity = a;
	}
	
	/**
	 * One function to write the inspection results from the Radio Buttons and Spinners to an XML file.<br>
	 * For now, the XML is written to {@link getExternalStorageDirectory()}.
	 * @param list The list of Equipment items to be written.
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void saveXML(ArrayList<Equipment> list) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerException{
		Document doc = getDOM();
		
		// Root node; in this case, Franchisee
		Node firstNode = doc.getFirstChild();
		
		for (int j=0; j<list.size(); j++){
			// Get the Equipment
			Equipment currEquip = list.get(j);
			
			// Have to hardcode the IDs for now
			//String[] IDs = {"33101", "33102", "77207", "88103"};
			String currID = currEquip.getId();
			ArrayList<inspectionElement> elementList = currEquip.getItems();
			
			// Get the inspectionElement within each Equipment
			for (int k=0; k<elementList.size(); k++){
				inspectionElement currElement = elementList.get(k);
				String passFail = null;
				String inspectName = currElement.getName();
				
				if (currElement.getClass().equals(ExtinguisherPassFailElement.class)){
					int x = ((ExtinguisherPassFailElement) currElement).getPassFail();
					if (x > 0){
						passFail = "Pass";
					}
					else if (x < 0){
						passFail = "Fail";
					}
					else passFail = "none";
				}
				
				else if (currElement.getClass().equals(FireHoseCabinetGoodPoorElement.class)) {
					int x = ((FireHoseCabinetGoodPoorElement) currElement).getGoodPoor();
					if (x == 0){
						passFail = "Good";
					}
					else if (x == 1){
						passFail = "Poor";
					}
					else passFail = "N/A";
				}
				
				else if (currElement.getClass().equals(FireHoseCabinetYesNoElement.class)) {
					int x = ((FireHoseCabinetYesNoElement) currElement).getYesNo();
					if (x > 0){
						passFail = "Yes";
					}
					else if (x < 0){
						passFail = "No";
					}
					else passFail = "none";
				}
				
				else if (currElement.getClass().equals(EmergencyLightYesNoElement.class)) {
					int x = ((EmergencyLightYesNoElement) currElement).getYesNo();
					if (x > 0){
						passFail = "Yes";
					}
					else if (x < 0){
						passFail = "No";
					}
					else passFail = "none";
				}
				
				// Modify the underlying node
				setPassFail(currID, inspectName, passFail, firstNode);
				setNotes(currID, inspectName, currElement.getNotes(), firstNode);
			}
		}
		
		// Write result
		writeDOMResults(doc);
	}
	
	public void setNotes(String equipmentID, String inspectionElementName, String notes, Node rootNode) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Take the String values passed as arguments and put them into the XPath String.
		String path = "//*[@id='" + equipmentID + "']/*[@name='" + inspectionElementName + "']/@testNote";
		NodeList list = (NodeList) xpath.evaluate(path,
				rootNode, XPathConstants.NODESET);
		list.item(0).setTextContent(notes);
	}
	
	/**
	 * Set "Pass", "Fail", or other applicable values to an inspectionElement node.
	 * @param 	equipmentID The ID of the equipment containing the inspectionElement.
	 * @param 	inspectionElementName The name attribute of the target inspectionElement.
	 * @param 	passOrFail A string representing pass or fail (may be other things like "Yes" and "No").
	 * @param 	rootNode The root node of the document. In our case, Franchisee. getFirstChild() can be used on the {@link Document} to obtain this.
	 * @throws 	XPathExpressionException In the case that the XPath expression is invalid.
	 */
	public void setPassFail(String equipmentID, String inspectionElementName, String passOrFail, Node rootNode) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Take the String values passed as arguments and put them into the XPath String.
		String path = "//*[@id='" + equipmentID + "']/*[@name='" + inspectionElementName + "']/@testResult";
		NodeList list = (NodeList) xpath.evaluate(path,
				rootNode, XPathConstants.NODESET);
		list.item(0).setTextContent(passOrFail);
	}
	
	/**
	 * Gets a {@link Document} object that represents the XML for editing.
	 * @return	A {@link Document} representing the XML document.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Document getDOM() throws SAXException, IOException, ParserConfigurationException{
		// Check SD card status
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			_activity.makeToast("SD card not available.", Toast.LENGTH_SHORT);
			return null;
		}
		
		File workingDir = Environment.getExternalStorageDirectory();
		File file = new File(workingDir,"/InspectionData.xml");
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		return doc;
	}
	
	/**
	 * Same as {@link getDOM()} except it returns the modified XML file saved by the inspection results.
	 * @return A {@link Document} representing the modified XML document.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Document getModifiedDOM() throws SAXException, IOException, ParserConfigurationException{
		// Check SD card status
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			_activity.makeToast("SD card not available.", Toast.LENGTH_SHORT);
			return null;
		}
		
		File workingDir = Environment.getExternalStorageDirectory();
		File file = new File(workingDir,"/Modified.xml");
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		return doc;
	}
	
	/**
	 * Writes the newly modified XML to a file on the SD card.
	 * @param doc The Document representing the XML.
	 * @param context Needed for Toast (might remove this because of redundancy).
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public void writeDOMResults(Document doc) throws TransformerException, FileNotFoundException{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(doc);
		// TODO Might change this to a different name, or pass the filename as a method argument.
		File modifiedFile = new File(Environment.getExternalStorageDirectory(),"/Modified.xml");
		
		if(modifiedFile.exists())
			// For now, we're overwriting.
			modifiedFile.delete();
		try {
        	modifiedFile.createNewFile();
		} catch (IOException e) {
			_activity.makeToast("Error creating file!",Toast.LENGTH_LONG);
			e.printStackTrace();
		}
		
		StreamResult result = new StreamResult(modifiedFile);
		transformer.transform(source, result);
		
		_activity.makeToast("File saved.", Toast.LENGTH_SHORT);
	}
}
