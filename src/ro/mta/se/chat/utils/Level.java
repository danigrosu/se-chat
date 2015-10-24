package ro.mta.se.chat.utils;

/**
 * Created by Dani on 10/24/2015.
 */
public class Level {
    public static final int OFF     = 0;
    public static final int VERBOSE = 1;
    public static final int DEBUG   = 2;
    public static final int INFO    = 3;
    public static final int WARNING = 4;
    public static final int ERROR   = 5;
    public static final int ALL     = 6;

    public static String getLevelName(int level)
    {
        switch (level)
        {
            case 0:return "OFF";
            case 1:return "VERBOSE";
            case 2:return "DEBUG";
            case 3:return "INFO";
            case 4:return "WARNING";
            case 5:return "ERROR";
            case 6:return "ALL";
        }
        return null;
    }
}
