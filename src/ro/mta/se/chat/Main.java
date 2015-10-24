package ro.mta.se.chat;
import ro.mta.se.chat.utils.*;
/**
 * Created by Dani on 10/24/2015.
 */
public class Main {
    private static Logger theLogger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Main mainTest = new Main("Hello World!");
        mainTest.sayHello();
    }

    String message;
    public Main(String message)
    {
        this.message = message;
    }

    public void sayHello()
    {
        theLogger.log(Level.INFO, "Hello logging looks fine!");
        theLogger.log(Level.WARNING, "Hello logging is unsafe!",Main.class.getName());
        theLogger.log(Level.ERROR, "Hello logging is broken!",Main.class.getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        System.out.println("Dennis Ritchie said Hello, world.");
    }
}
