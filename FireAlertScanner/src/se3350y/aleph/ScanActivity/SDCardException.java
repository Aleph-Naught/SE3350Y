package se3350y.aleph.ScanActivity;

public class SDCardException extends Exception {

	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public SDCardException() {}

    //Constructor that accepts a message
    public SDCardException(String message)
    {
       super(message);
    }
}
