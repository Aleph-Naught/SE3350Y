package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

public class ClientContract {
	private String contractId = null;
	private String contractNo = null;
	private String contractStartDate = null;
	private String contractEndDate = null;
	private String contractTerms = null;
	private ArrayList<ServiceAddress> serviceAddress = null;
	
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getContractTerms() {
		return contractTerms;
	}
	public void setContractTerms(String contractTerms) {
		this.contractTerms = contractTerms;
	}
	public ArrayList<ServiceAddress> getServiceAddress() {
		return serviceAddress;
	}
	public void setServiceAddress(ArrayList<ServiceAddress> serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	public void setServiceAddress(ServiceAddress serviceAddressInstance){
		this.serviceAddress.add(serviceAddressInstance);
	}
	
}
