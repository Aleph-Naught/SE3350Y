package se3350y.aleph.ScanActivity;

//import Sample.TCPClient.R;
import se3350y.aleph.firealertscanner.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientView extends Activity {
	private Button connectButton;
	private Button sendButton;
	private Button disconnectButton;

	private EditText portNumText;
	private EditText ipAddressText;
	private TextView XMLBoxText;

	Controller _controller;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_view);
		
		//map the variables to the view widgets
		connectButton = (Button)findViewById(R.id.button1);
		sendButton = (Button)findViewById(R.id.button2);
		disconnectButton = (Button)findViewById(R.id.button3);
 
		portNumText = (EditText)findViewById(R.id.editText1);
		ipAddressText = (EditText)findViewById(R.id.editText2);
		XMLBoxText = (TextView)findViewById(R.id.editText3);

		this.sendButton.setEnabled(false);
		this.disconnectButton.setEnabled(false);
		
		_controller = new Controller(this);

		
		//on "connectButton" click
		connectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_controller.ConnectBtnClick();	
			}
		});

				
		//on "sendButton" click
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_controller.SendBtnClick();
			}
		});

		//on "disconnectButton" click
		disconnectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_controller.DisconnectBtnClick();
			}
		});
	}

	public String GetServerIP() {
		return this.ipAddressText.getText().toString();
	}

	public String GetXMLText() {
		return XMLBoxText.getText().toString();
	}

	public int GetServerPort() {
		return Integer.parseInt(this.portNumText.getText().toString());
	}
	
	public void setConnectBtnFalse() {
		this.connectButton.setEnabled(false);
	}

	public void setConnectBtnTrue() {
		this.connectButton.setEnabled(true);
	}
	
	public void setSendBtnTrue() {
		this.sendButton.setEnabled(true);
	}

	public void setDisconnectBtnTrue() {
		this.disconnectButton.setEnabled(true);
	}
	
	public void setSendBtnFalse() {
		this.sendButton.setEnabled(false);
	}

	public void setDisconnectBtnFalse() {
		this.disconnectButton.setEnabled(false);
	}
}