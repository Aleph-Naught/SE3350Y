package se3350y.aleph.ScanActivity;

public class spinnerPackage {
	
	String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	String attr;
	
	public spinnerPackage(String _path, String _attr){
		path = _path;
		attr = _attr;
	}

}
