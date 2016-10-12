package team1793.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by tyler on 10/9/16.
 */
public class TimeUtils {
    public final static DateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    public final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static Comparator<String> compareStringDate = (date1, date2) -> (-1*fromStringToDate(date1).compareTo(fromStringToDate(date2)));

    private static Calendar fromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private static int getDayOfMonth(Date date) {
        //noinspection ConstantConditions
        return fromDate(date).get(Calendar.DAY_OF_MONTH);
    }

    private static int getMonth(Date date) { //noinspection ConstantConditions
        return fromDate(date).get(Calendar.MONTH) + 1;
    }

    private static int getYear(Date date) { //noinspection ConstantConditions
        return fromDate(date).get(Calendar.YEAR);
    }

    private static int getHour(Date date) { //noinspection ConstantConditions
        return fromDate(date).get(Calendar.HOUR_OF_DAY);
    }

    private static int getMinute(Date date) {
        return fromDate(date).get(Calendar.MINUTE);
    }

    public static Date getDateOnly(Date date) {
        dateFormat.format(date);
        return date;
    }

    public static int getMinuteSum(Date date) {
        int hours = getHour(date);
        int minute = getMinute(date);
        return minute + (hours * 60);
    }

    public static String now() {
        return dateFormat.format(new Date());
    }
    public static Date now(DateFormat format) {
        Date date;
        format.format(date = new Date());
        return date;
    }

    public static Date fromStringToDateTime(String str) {
        return fromString(str, dateTimeFormat);
    }

    public static Date fromStringToDate(String str) {
        return fromString(str, dateFormat);
    }
    public static Date fromString(String str, DateFormat format) {
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(Date date) {
        return dateFormat.format(date);
    }



}