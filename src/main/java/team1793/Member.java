package team1793;

import java.util.HashMap;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {
    private String fullname;
    private HashMap<Integer,Day> days = new HashMap<>();
    public Member(String name) {
        fullname = name;
    }
    public boolean isLoggedIn() {
        return days.containsKey(TimeUtil.getEpochDay());
    }
    public void login() {
        int currentDay = TimeUtil.getEpochDay();
        if(!days.containsKey(currentDay)) {
            System.out.println("login!");
            Day day = new Day(TimeUtil.getEpochMinute());
            days.put(currentDay,day);
        } else {
            System.out.println("logged in already!");
            //already logged in
        }
        HourLogger.sync();
    }
    public void logout() {
        int currentDay = TimeUtil.getEpochDay();
        if(!days.containsKey(currentDay)) {
            //never logged in
        } else {
            System.out.println("logging out");
            Day day = days.get(currentDay);
            day.setLogoutTime(TimeUtil.getEpochMinute());
        }
        HourLogger.sync();
    }

    public String getFullname() {
        return fullname;
    }

    public HashMap<Integer, Day> getDays() {
        return days;
    }
}
