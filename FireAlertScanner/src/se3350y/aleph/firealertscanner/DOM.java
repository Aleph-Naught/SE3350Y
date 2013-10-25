package se3350y.aleph.firealertscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * A class to store the static DOM-related files for reading, editing, and writing XML.
 * @author Ben
 */
public class DOM {
	
	/**
	 * Set "Pass", "Fail", or other applicable values to an inspectionElement node.
	 * @param 	equipmentID The ID of the equipment containing the inspectionElement.
	 * @param 	inspectionElementName The name attribute of the target inspectionElement.
	 * @param 	passOrFail A string representing pass or fail (may be other things like "Yes" and "No").
	 * @param 	rootNode The root node of the document. In our case, Franchisee. getFirstChild() can be used on the {@link Document} to obtain this.
	 * @throws 	XPathExpressionException In the case that the XPath expression is invalid.
	 */
	public static void setPassFail(String equipmentID, String inspectionElementName, String passOrFail, Node rootNode) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Take the String values passed as arguments and put them into the XPath String.
		String path = "//*[@id='" + equipmentID + "']/*[@name='" + inspectionElementName + "']/@testResult";
		NodeList list = (NodeList) xpath.evaluate(path,
				rootNode, XPathConstants.NODESET);
		list.item(0).setTextContent(passOrFail);
	}
	
	/**
	 * Gets a {@link Document} object that represents the XML for editing.
	 * @param context Needed for Toast (might remove this because of redundancy).
	 * @return	Returns a {@link Document} representing the XML document.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document getDOM(Context context) throws SAXException, IOException, ParserConfigurationException{
		// Check SD card status
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
	
	/**
	 * Writes the newly modified XML to a file on the SD card.
	 * @param doc The Document representing the XML.
	 * @param context Needed for Toast (might remove this because of redundancy).
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public static void writeDOMResults(Document doc, Context context) throws TransformerException, FileNotFoundException{
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
			Toast.makeText(context,"Error creating file!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		StreamResult result = new StreamResult(modifiedFile);
		transformer.transform(source, result);
	}
}
