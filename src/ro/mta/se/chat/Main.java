package ro.mta.se.chat;

import ro.mta.se.chat.exceptions.ConnectionLostException;
import ro.mta.se.chat.utils.*;

/**
 * Created by Dani on 10/24/2015.
 */
public class Main {

    public static void main(String[] args) {
        Main mainTest = new Main("Hello World!");
        //mainTest.sayHello();
        //mainTest.exceptionTest();
        mainTest.threadSafeTest();
    }

    String message;

    public Main(String message) {
        this.message = message;
    }

    /**
     * Testing
     */
    public void sayHello() {
        Logger.log(Level.INFO, "Hello logging looks fine!");
        Logger.log(Level.WARNING, "Hello logging is unsafe!", Main.class.getName());
        Logger.log(Level.ERROR, "Hello logging is broken!", Main.class.getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName());

        System.out.println("Dennis Ritchie said Hello, world.");
    }

    /**
     * Testing
     */
    public void exceptionTest() {
        try {
            throw new ConnectionLostException("Connection lost", 1);
        } catch (ConnectionLostException e) {
            Logger.log(Level.ERROR, "Connection problem", e);
        }
    }

    /**
     * Thread safe test
     */
    public void threadSafeTest() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Logger.log(Level.INFO, "Message " + i);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Logger.log(Level.INFO, "Thread1 error: " + e.getMessage());
                    }
                }
            }
        }).start();

        for (int i = 0; i < 5; i++){
            Logger.log(Level.WARNING, "Message " + i);
        }
    }

}
