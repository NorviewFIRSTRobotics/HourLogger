package team1793;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tyler on 10/8/16.
 */
public class TimeUtil {
    public static long getEpochMillisecond() {
        return System.currentTimeMillis();
    }
    public static int getEpochDay() {
        return (int) (getEpochMillisecond()/86000000);
    }
    public static int getEpochMinute() {
        return (int) (getEpochMillisecond()/60000);
    }

    public static String getDate(long minute) {
        Date date = new Date(minute*60000);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String formatted = format.format(date);
        return formatted;
    }

    public static String getHour(long minute) {
        Date date = new Date(minute*60000);
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String formatted = format.format(date);
        return formatted;
    }
}
