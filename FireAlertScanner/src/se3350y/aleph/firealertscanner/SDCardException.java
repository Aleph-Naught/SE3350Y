package se3350y.aleph.firealertscanner;

public class SDCardException extends Exception {

	//Parameterless Constructor
    public SDCardException() {}

    //Constructor that accepts a message
    public SDCardException(String message)
    {
       super(message);
    }
}
