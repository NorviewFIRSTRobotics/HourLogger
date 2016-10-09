package team1793.data;

import team1793.HourLogger;
import team1793.utils.CSVUtils;
import team1793.utils.QRUtils;
import team1793.utils.TimeUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;
import java.util.Vector;
import java.util.function.BiFunction;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {

    public static BiFunction<String, String, String> toFullName = (first, last) -> String.format("%s %s", first, last);
    private static final int WAIT_TIME = 15;
    private String firstName, lastName;
    public HashMap<String, Day> days = new HashMap<>();
    private File qr;

    public Member(String firstName, String lastName) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
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
        String now = TimeUtils.getNow();
        String date = TimeUtils.getDateOnly(now);
        if(!days.containsKey(date)) {
            //sets login and logout as same time.
            int reply = JOptionPane.showConfirmDialog(null, "Do you need a bus pass?", "Bus Pass", JOptionPane.YES_NO_OPTION);
            addDay(now,now, reply == JOptionPane.YES_OPTION);
            JOptionPane.showMessageDialog(null,String.format("Successfully Logged at %s", now));
        }
        else {
            System.out.println("logging out " + now);
            Day day = days.get(date);
            // this means you have not logged out yet
            if(day.getLoginTime() == day.getLogoutTime()) {
                //if the current time is 15 minutes after the login time
                //TODO > 15
                int diff = TimeUtils.getMinuteSum(now) - day.getLoginTime();
                if(diff >= WAIT_TIME) {
                    day.setLogoutTime(TimeUtils.getMinuteSum(now));
                    JOptionPane.showMessageDialog(null,String.format("Successfully Logged out at %s", now));
                } else {
                    JOptionPane.showMessageDialog(null,String.format("You have to wait %d more minutes to logout", WAIT_TIME-diff));
                }
            } else {
                JOptionPane.showMessageDialog(null,String.format("You already logged out today at %s!",day.getFormattedLogoutTime()));
            }
        }
        save();
        load();
    }

    public void addDay(String loginTime, String logoutTime,boolean buspass) {
        String date = TimeUtils.areSameDate(loginTime, logoutTime) ? TimeUtils.getDateOnly(loginTime) : "";
        if (!date.isEmpty()) {
            Day day = new Day(TimeUtils.getMinuteSum(loginTime), TimeUtils.getMinuteSum(logoutTime));
            day.setNeedsBusPass(buspass);
            days.put(date, day);
        }
        save();
    }
    public void save() {
        CSVUtils.writeMemberFile(this);
    }

    public void load() {
        Member member = CSVUtils.readMemberFile(getSaveFile());
    }

    public List getFormattedDays() {
        return days.entrySet().stream().map( (e) -> "bus pass:"+ e.getValue().needsBusPass() + "," + e.getKey()+", login:"+ e.getValue().getFormattedLoginTime() + ", logout:"+ e.getValue().getFormattedLogoutTime() + "\n").collect(Vector::new,Vector::add,Vector::addAll);
    }

    public String getFullname() {
        return toFullName.apply(firstName, lastName);
    }

    public File getQR() {
        return qr;
    }

    public int getTotalMinutes() {
        OptionalInt total = days.values().stream().mapToInt( day -> day.getTimeLoggedIn()).reduce( (sum,n) -> sum+n);
        if(total.isPresent())
            return total.getAsInt();
        return 0;
    }

    public File getSaveFile() {
        return new File(HourLogger.saveDir, getFullname().replace(" ","_") + ".csv");
    }

    @Override
    public String toString() {
        return getFullname();
    }

    public boolean isName(String firstName, String lastName) {
        return firstName.trim().toLowerCase().equals(this.firstName) && lastName.trim().toLowerCase().equals(this.lastName);
    }

}
