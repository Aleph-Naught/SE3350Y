package se3350y.aleph.MainDataEntry;

public class getValuesPackage {
	
	String expression;
	String attribute;

	public getValuesPackage(String _expression, String _attribute){
		expression = _expression;
		attribute = _attribute;
	}
	
	public String getExpression(){
		return expression;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	

}
