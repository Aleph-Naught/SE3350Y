package se3350y.aleph.ScanActivity;

import se3350y.aleph.MainDataEntry.TCPmodel;
import android.util.Log;
import android.widget.Toast;

public class Controller {

	String ServerIP = null;
	int ServerPort = 0;

	TCPmodel _TCPmodel = null;

	// Controller constructor
	private ClientView _view;

	public Controller(ClientView view) {
		_view = view;
	}

	// connect button click function
	public void ConnectBtnClick() {
		ServerPort = _view.GetServerPort();
		ServerIP = _view.GetServerIP();
		try {
			_TCPmodel = new TCPmodel(ServerIP, ServerPort);

			_view.setConnectBtnFalse();
			_view.setSendBtnTrue();
			_view.setDisconnectBtnTrue();

		} catch (Exception e) {
			Log.d("Error in ConnectBtnClick", e.toString());
			
		}
	}

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

}
