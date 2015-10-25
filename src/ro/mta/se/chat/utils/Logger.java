package ro.mta.se.chat.utils;

/**
 * Created by Dani on 10/24/2015.
 */
public class Logger {

    private int level = Level.INFO;
    private String name;

    public Logger(String name) {
        this.name = name;
    }

    /**
     * Function returns a new Logger
     *
     * @param message Logger message
     * @return Logger
     */
    public static Logger getLogger(String message) {
        return new Logger(message);
    }

    /**
     * Function returns current level of Logger
     *
     * @return int
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Function returns the Logger's name
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /** Function log
     * @param level specified level
     * @param message specified message
     */
    public void log(int level, String message) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "\n" + Level.getLevelName(level));
        }
    }

    /**
     * Function log
     * @param level specified level
     * @param message specified message
     * @param className specified class name
     */
    public void log(int level, String message, String className) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "ClassName: " + className +
                    "\n" + Level.getLevelName(level));
        }
    }

    /**
     *
     * @param level specified level
     * @param message specified message
     * @param className specified class name
     * @param methodName specified method name
     */
    public void log(int level, String message, String className, String methodName) {
        if (level >= Constants.GLOBAL_LOGGING_LEVEL) {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + ", ClassName: " + className +
                    ", MethodName: " + methodName + "\n" + Level.getLevelName(level));
        }
    }


}
