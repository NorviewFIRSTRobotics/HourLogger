package team1793.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tyler on 10/9/16.
 */
public class TimeUtils {
    private final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private static Calendar fromString(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getDayOfMonth(String date) {
        //noinspection ConstantConditions
        return fromString(date).get(Calendar.DAY_OF_MONTH);
    }

    private static int getMonth(String date) { //noinspection ConstantConditions
        return fromString(date).get(Calendar.MONTH) + 1;
    }

    private static int getYear(String date) { //noinspection ConstantConditions
        return fromString(date).get(Calendar.YEAR);
    }

    public static String getDateOnly(String date) {
        return String.format("%s/%s/%s",getMonth(date),getDayOfMonth(date),getYear(date));
    }


    private static int getHour(String date) { //noinspection ConstantConditions
        return fromString(date).get(Calendar.HOUR_OF_DAY);
    }

    private static int getMinute(String date) {
        //noinspection ConstantConditions
        return fromString(date).get(Calendar.MINUTE);
    }

    public static int getMinuteSum(String date) {
        int hours = getHour(date);
        int minute = getMinute(date);
        return minute + (hours * 60);
    }

    public static boolean areSameDate(String date1, String date2) {
        return getDateOnly(date1).equals(getDateOnly(date2));
    }

    public static String getNow() {
        return dateFormat.format(new Date());
    }
}
