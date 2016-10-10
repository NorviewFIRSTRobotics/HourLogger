package team1793.data;

import team1793.HourLogger;
import team1793.utils.CSVUtils;
import team1793.utils.QRUtils;
import team1793.utils.TimeUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {

    private static final int WAIT_TIME = 15;
    public static BiFunction<String, String, String> toFullName = (first, last) -> String.format("%s %s", first, last);
    private final String firstName, lastName;
    private final Team team;
    private final File qr;
    public HashMap<Date, Day> days = new HashMap<>();

    public Member(String firstName, String lastName, Team team) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
        this.team = team;
        if(!getSaveFile().exists()) try {
            getSaveFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        qr = QRUtils.generateQR(getFullname());
    }
    //login or logout depending on state
    public void loginlogout() {
        System.out.println("Attempting to login/logout");
        Date date = TimeUtils.now(TimeUtils.dateFormat),dateTime = TimeUtils.now(TimeUtils.dateTimeFormat);
        if(!days.containsKey(date)) {
            //sets login and logout as same time.
            int reply = JOptionPane.showConfirmDialog(null, "Do you need a bus pass?", "Bus Pass", JOptionPane.YES_NO_OPTION);
            addDay(dateTime,dateTime, reply == JOptionPane.YES_OPTION);
//            JOptionPane.showMessageDialog(null,String.format("Successfully Logged at %s", now));
        }
        else {
            System.out.println("logging out " + dateTime);
            Day day = days.get(date);
            // this means you have not logged out yet
            if(day.getLoginTime() == day.getLogoutTime()) {
                //if the current time is 15 minutes after the login time
                int diff = TimeUtils.getMinuteSum(dateTime) - day.getLoginTime();
                if(diff >= WAIT_TIME) {
                    day.setLogoutTime(TimeUtils.getMinuteSum(dateTime));
//                    JOptionPane.showMessageDialog(null,String.format("Successfully Logged out at %s", now));
                } else {
                    JOptionPane.showMessageDialog(null,String.format("You have to wait %d more minutes to logout", WAIT_TIME-diff));
                }
            } else {
                JOptionPane.showMessageDialog(null,String.format("You already logged out today at %s!",day.getFormattedLogoutTime()));
            }
        }
        save();
    }
    //              mm/dd/yyyy HH:mm
    public void addDay(Date loginTime, Date logoutTime,boolean buspass) {
//        String date = TimeUtils.areSameDate(loginTime, logoutTime) ? TimeUtils.getDateOnly(loginTime) : "";

        if(loginTime.compareTo(logoutTime) == 0) {
            Day day = new Day(TimeUtils.getMinuteSum(loginTime), TimeUtils.getMinuteSum(logoutTime));
            day.setNeedsBusPass(buspass);
            days.put(TimeUtils.getDateOnly(loginTime), day);
        }
        save();
    }

    private void save() {
        CSVUtils.writeMemberFile(this);
    }


    public List getFormattedDays() {
        //noinspection unchecked
        return days.entrySet().stream().map( (e) -> "bus pass:"+ e.getValue().needsBusPass() + "," + e.getKey()+", login:"+ e.getValue().getFormattedLoginTime() + ", logout:"+ e.getValue().getFormattedLogoutTime() + "\n").collect(Vector::new,Vector::add,Vector::addAll);
    }

    public String getFullname() {
        return toFullName.apply(firstName, lastName);
    }

    public File getQR() {
        return qr;
    }

    public int getTotalMinutes() {
        OptionalInt total = days.values().stream().mapToInt(Day::getTimeLoggedIn).reduce((sum, n) -> sum + n);
        if(total.isPresent())
            return total.getAsInt();
        return 0;
    }

    public File getSaveFile() {
        File teamDir = new File(HourLogger.saveDir, team.toString());
        teamDir.mkdirs();
        return new File(teamDir,getFullname().replace(" ","_") + ".csv");
    }

    @Override
    public String toString() {
        return getFullname();
    }

    public boolean isName(String firstName, String lastName) {
        return firstName.trim().toLowerCase().equals(this.firstName) && lastName.trim().toLowerCase().equals(this.lastName);
    }

    public enum Team implements Serializable {
        PROGRAMMING,
        MECHANICAL,
        ELECTRICAL,
        SHOP,
        MISSION,
        DESIGN;
        public static Team[] VALUES = new Team[]{PROGRAMMING,MECHANICAL,ELECTRICAL,SHOP,MISSION,DESIGN};
        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
