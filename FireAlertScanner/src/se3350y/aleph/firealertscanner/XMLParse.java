package se3350y.aleph.firealertscanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;


public class XMLParse {
	

	
	public void parse(Resources res) throws XPathExpressionException, IOException{
		
		 
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		String expression = "/Franchisee";

		//XmlPullParser parser = res.getXml(R.xml.inspectiondata);
		
		InputStream in=null;
		in = res.openRawResource(R.raw.inspectiondata);
		
		InputSource is = new InputSource(in);

		String stuff = null;
		
		NodeList nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
		
		for (int i = 0; i < nodes.getLength(); i++) {
		      Element franchisee = (Element) nodes.item(i);
		      stuff = franchisee.getAttribute("id");
		}
		
		return;

		
	
		
	}

}
