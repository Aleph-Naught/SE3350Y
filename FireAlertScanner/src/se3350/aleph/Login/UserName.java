package se3350.aleph.Login;

public class UserName {
	
	private static UserName mInstance = null;
	
	public String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected UserName(){}
	
	public static synchronized UserName getInstance(){
    	if(null == mInstance){
    		mInstance = new UserName();
    	}
    	return mInstance;
    }


}
