package se3350y.aleph.firealertscanner;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class TCPController {

	String ServerIP = null;
	int ServerPort = 0;

	TCPmodel _TCPmodel = null;

	// Controller constructor
	private MainDataEntry _view;

	public TCPController(MainDataEntry view) {
		_view = view;
	}

	// connect button click function
	public void Send() {
		Resources res = _view.getResources();
		ServerPort = Integer.parseInt(res.getString(R.string.port));
		ServerIP = res.getString(R.string.ip);
		Log.i("TCPController", Integer.toString(ServerPort));
		Log.i("TCPController", ServerIP);
		try {
			_TCPmodel = new TCPmodel(ServerIP, ServerPort);

//			_view.setConnectBtnFalse();
//			_view.setSendBtnTrue();
//			_view.setDisconnectBtnTrue();
			
			DOMWriter dom = new DOMWriter(_view);
			Document doc = dom.getModifiedDOM();
			DOMSource domSource = new DOMSource(doc);
	        StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.transform(domSource, result);
	        String txt = writer.toString();
	        _TCPmodel.RTSPSend(txt);
			
			_TCPmodel.close();

		} catch (Exception e) {
			Log.d("Error in ConnectBtnClick", e.toString());
			Toast.makeText(_view.getBaseContext(), "TCP failed to connect.", Toast.LENGTH_SHORT).show();
			
		}
	}
/*
	// Send button click function
	public void SendBtnClick() {
		String txt = _view.GetXMLText();
		_TCPmodel.RTSPSend(txt); // send message to server
	}

	// Disconnect button click function
	public void DisconnectBtnClick() {
		_TCPmodel.close();
		_view.setConnectBtnTrue();
		_view.setSendBtnFalse();
		_view.setDisconnectBtnFalse();
	}
*/
}
