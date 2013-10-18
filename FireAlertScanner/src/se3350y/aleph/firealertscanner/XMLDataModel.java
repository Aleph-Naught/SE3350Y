package se3350y.aleph.firealertscanner;

public class XMLDataModel {
	
	
	private class Client {
		private int id = 0;
		private String name = null;
		private String address = null;
		
		public Client(int id, String name, String address) {
			super();
			this.id = id;
			this.name = name;
			this.address = address;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
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

	}

	private class ClientContract {
		private int id = 0;
		private int No = 0;
		private String startDate = null;
		private String endDate = null;
		private String terms = null;
		
		public ClientContract(int id, int no, String startDate, String endDate,
				String terms) {
			super();
			this.id = id;
			No = no;
			this.startDate = startDate;
			this.endDate = endDate;
			this.terms = terms;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getNo() {
			return No;
		}
		public void setNo(int no) {
			No = no;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getTerms() {
			return terms;
		}
		public void setTerms(String terms) {
			this.terms = terms;
		}
		
	}
	
	private class ServiceAddress {
		private String id = null;
		private String address = null;
		private String postal_code = null;
		private String contact = null;
		private String city = null;
		private String province = null;
		private String country = null;
		private String inspector_id = null;
		private String test_timestamp = null;
		
		public ServiceAddress(String id, String address, String postal_code,
				String contact, String city, String province, String country,
				String inspector_id, String test_timestamp) {
			super();
			this.id = id;
			this.address = address;
			this.postal_code = postal_code;
			this.contact = contact;
			this.city = city;
			this.province = province;
			this.country = country;
			this.inspector_id = inspector_id;
			this.test_timestamp = test_timestamp;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPostal_code() {
			return postal_code;
		}
		public void setPostal_code(String postal_code) {
			this.postal_code = postal_code;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getInspector_id() {
			return inspector_id;
		}
		public void setInspector_id(String inspector_id) {
			this.inspector_id = inspector_id;
		}
		public String getTest_timestamp() {
			return test_timestamp;
		}
		public void setTest_timestamp(String test_timestamp) {
			this.test_timestamp = test_timestamp;
		}
	}
	
	private class Floor {
		private String name = null;

		public Floor(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	private class Room {
		private String id = null;
		private int no = 0;
		public Room(String id, int no) {
			super();
			this.id = id;
			this.no = no;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		
	}
	
	private class Equipment {
		private String equipment_name = null;
		private int id = 0;
		private String location = null;
		private int size = 0;
		private String type = null;
		private String model = null;
		private String serial_no = null;
		private String manufacturing_date = null;
		public InspectionElement element = new InspectionElement();
		
		public Equipment(String equipment_name, int id, String location,
				int size, String type, String model, String serial_no,
				String manufacturing_date) {
			super();
			this.equipment_name = equipment_name;
			this.id = id;
			this.location = location;
			this.size = size;
			this.type = type;
			this.model = model;
			this.serial_no = serial_no;
			this.manufacturing_date = manufacturing_date;
		}
		public String getEquipment_name() {
			return equipment_name;
		}
		public void setEquipment_name(String equipment_name) {
			this.equipment_name = equipment_name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getSerial_no() {
			return serial_no;
		}
		public void setSerial_no(String serial_no) {
			this.serial_no = serial_no;
		}
		public String getManufacturing_date() {
			return manufacturing_date;
		}
		public void setManufacturing_date(String manufacturing_date) {
			this.manufacturing_date = manufacturing_date;
		}
	}
	
	private class InspectionElement {
		private String name = null;
		private String test_result = null;
		private String test_note = null;
		public InspectionElement() {
			super();
		}
		public InspectionElement(String name, String test_result,
				String test_note) {
			super();
			this.name = name;
			this.test_result = test_result;
			this.test_note = test_note;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTest_result() {
			return test_result;
		}
		public void setTest_result(String test_result) {
			this.test_result = test_result;
		}
		public String getTest_note() {
			return test_note;
		}
		public void setTest_note(String test_note) {
			this.test_note = test_note;
		}
	}
}