package ro.mta.se.chat.utils;

/**
 * Created by Dani on 10/24/2015.
 */
public class Logger {
    private int level = Level.INFO;
    private String name;
    public Logger(String name)
    {
        this.name = name;
    }


    public static Logger getLogger(String message)
    {
        return new Logger(message);
    }
    public int getLevel()
    {
        return this.level;
    }
    public String getName()
    {
        return this.name;
    }

    public void log(int level, String message)
    {
        if(level >= Constants.GLOBAL_LOGGING_LEVEL)
        {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "\n" + Level.getLevelName(level));
        }
    }
    public void log(int level, String message, String className)
    {
        if(level >= Constants.GLOBAL_LOGGING_LEVEL)
        {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + "ClassName: " + className +
                    "\n" + Level.getLevelName(level));
        }
    }
    public void log(int level, String message, String className, String methodName)
    {
        if(level >= Constants.GLOBAL_LOGGING_LEVEL)
        {
            System.out.println("Log time: " + System.currentTimeMillis() +
                    ", Message: " + message + ", ClassName: " + className +
                    ", MethodName: " + methodName + "\n" + Level.getLevelName(level));
        }
    }


}
