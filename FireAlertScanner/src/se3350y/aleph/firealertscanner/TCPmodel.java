package se3350y.aleph.firealertscanner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class TCPmodel {
	Socket RTSPsocket;
	InetAddress ServerIPAdd = null;
	InetAddress ClientIPAdd = null;
	int RTSP_PORT;

	//RTSPmodel constructor
	public TCPmodel(String IP, int ServerPort) throws IOException {
		try {
			ServerIPAdd = InetAddress.getByName(IP);
			RTSPsocket = new Socket(ServerIPAdd, ServerPort);
		}
		catch (UnknownHostException e)
		{
			Log.d("Socket Connection", e.toString());
		}
	}

	//send data to server
	public void RTSPSend(String data) {
		try {
			final PrintWriter sendData = new PrintWriter(new BufferedWriter(new OutputStreamWriter(RTSPsocket.getOutputStream())), true);
			sendData.println(data);
		} catch (Exception e) {
			Log.d("Socket Send", e.toString());
		}
	}

	public void close() {
		try {
			RTSPsocket.close();
		} catch (IOException e) {
			Log.d("Socket Error", e.toString());
		}
	}
}
