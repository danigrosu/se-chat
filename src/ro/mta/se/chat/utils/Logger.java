package ro.mta.se.chat.utils;
import java.io.*;

/**
 * Main class
 * Created by Dani on 10/24/2015.
 */
public class Logger {

    private Logger() {

    }


    /**
     * Function log
     *
     * @param level   specified level
     * @param message specified message
     */
    public static synchronized void log(int level, String message) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            String out = "Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "\n" + Level.getLevelName(level) + "\n";
            if(Constants.LOG_TO_FILE == false) {
                System.out.println(out);
            }
            else {
                try {
                    FileWriter fw = new FileWriter(Constants.LOGGING_FILE,true);
                    fw.write(out);
                    fw.close();
                }
                catch (IOException e) {
                    System.out.println("File does not exist.");
                }
            }
        }
    }

    /**
     * Function log
     *
     * @param level     specified level
     * @param message   specified message
     * @param className specified class name
     */
    public static synchronized void log(int level, String message, String className) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            String out = "Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "ClassName: " + className +
                    "\n" + Level.getLevelName(level) + "\n";

            if(Constants.LOG_TO_FILE == false) {
                System.out.println(out);
            }
            else {
                try {
                    FileWriter fw = new FileWriter(Constants.LOGGING_FILE,true);
                    fw.write(out);
                    fw.close();
                }
                catch (IOException e){
                    System.out.println("File does not exist.");
                }
            }
        }
    }

    /**
     * @param level      specified level
     * @param message    specified message
     * @param className  specified class name
     * @param methodName specified method name
     */
    public static synchronized void log(int level, String message, String className, String methodName) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            String out = "Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + ", ClassName: " + className +
                    ", MethodName: " + methodName + "\n" + Level.getLevelName(level) + "\n";

            if(Constants.LOG_TO_FILE == false) {
                System.out.println(out);
            }
            else {
                try {
                    FileWriter fw = new FileWriter(Constants.LOGGING_FILE,true);
                    fw.write(out);
                    fw.close();
                }
                catch (IOException e){
                    System.out.println("File does not exist.");
                }
            }
        }
    }

    public static synchronized void log(int level, String message, Exception e) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            String out = "Log time: " + System.currentTimeMillis() +
                    ", Message: " + message +
                    ", Exception message: " + e.getMessage() + "\n";

            if(Constants.LOG_TO_FILE == false) {
                System.out.println(out);
            }
            else {
                try {
                    FileWriter fw = new FileWriter(Constants.LOGGING_FILE,true);
                    fw.write(out);
                    fw.close();
                }
                catch (IOException ex){
                    System.out.println("File does not exist.");
                }
            }
        }
    }

}
