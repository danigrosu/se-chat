package ro.mta.se.chat.exceptions;

/**
 * Created by Dani on 11/1/2015.
 */
public class ConnectionRefusedException extends Exception {
    /**
     * Exception message
     */
    String message;
    /**
     * Exception code
     */
    int code;

    /**
     * Constructor
     *
     * @param message
     * @param code
     */
    public ConnectionRefusedException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    /**
     * This method prints the massage.
     */
    public void print() {
        System.out.println("Code: " + code + "\n" + message);
    }

    /**
     * This method overrides getMessage in Exception class
     *
     * @return Exception message
     */
    public String getMessage() {
        return this.message;
    }
}
