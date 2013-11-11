package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

public class Client {
	private String id = null;
	private String name = null;
	private String address = null;
	private ArrayList<ClientContract> clientContract = new ArrayList<ClientContract>();;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ArrayList<ClientContract> getClientContract() {
		return clientContract;
	}
	public void setClientContract(ArrayList<ClientContract> clientContract) {
		this.clientContract = clientContract;
	}
	public void setClientContract(ClientContract clientContractInstance){
		clientContract.add(clientContractInstance);
	}

}
