package team1793.data;

import com.sun.xml.internal.ws.util.StringUtils;
import team1793.HourLogger;
import team1793.utils.CSVUtils;
import team1793.utils.QRUtils;
import team1793.utils.TimeUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {
    private static final int WAIT_TIME = 0;

    private String firstName, lastName;
    private Team team;
    private final File qr;
    public HashMap<String, Day> days = new HashMap<>();

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
        String date = TimeUtils.now();
        Date dateTime = TimeUtils.now(TimeUtils.dateFormat);
        System.out.println(days);
        if(!days.containsKey(date)) {
            //sets login and logout as same time.
            int reply = JOptionPane.showConfirmDialog(null, "Do you need a bus pass?", "Bus Pass", JOptionPane.YES_NO_OPTION);
            addDay(dateTime,dateTime, reply == JOptionPane.YES_OPTION);
        }
        else {
            System.out.println("logging out " + dateTime);
            Day day = days.get(date);
            // this means you have not logged out yet
            if(day != null && day.getLoginTime() == day.getLogoutTime()) {
                //if the current time is 15 minutes after the login time
                int diff = TimeUtils.getMinuteSum(dateTime) - day.getLoginTime();
                if(diff >= WAIT_TIME) {
                    day.setLogoutTime(TimeUtils.getMinuteSum(dateTime));
                } else {
                    JOptionPane.showMessageDialog(null,String.format("You have to wait %d more minutes to logout", WAIT_TIME-diff));
                }
            } else {
                JOptionPane.showMessageDialog(null,String.format("You already logged out today at %s!",day.getFormattedLogoutTime()));
            }
        }
        save();
    }
    public void addDay(Date loginTime, Date logoutTime,boolean buspass) {
        if(loginTime.compareTo(logoutTime) == 0) {
            Day day = new Day(TimeUtils.getMinuteSum(loginTime), TimeUtils.getMinuteSum(logoutTime));
            day.setNeedsBusPass(buspass);
            days.put(TimeUtils.toString(loginTime), day);
        }
        save();
    }

    private void save() {
        CSVUtils.writeMemberFile(this);
    }

    public void setBusPass(String buspass) {
        Date date = getLastDay();
        if(date != null) {
            days.get(date).setNeedsBusPass(buspass.equals("Yes"));
        }
    }

    public void setFirstName(String firstName) {
        Team team = this.team;
        getSaveFile().delete();
        this.team = team;
        this.firstName = firstName;
        save();
    }

    public void setLastName(String lastName) {
        Team team = this.team;
        getSaveFile().delete();
        this.team = team;
        this.lastName = lastName;
        save();
    }

    public void setTeam(Team team) {
        getSaveFile().delete();
        this.team = team;
        save();
    }

    public List getMemberData() {
        Date last = getLastDay();
        String lastStr = "Never";
        String buspass = "No";
        if(last != null) {
            lastStr = TimeUtils.toString(last);
            buspass = days.get(lastStr).needsBusPass() ? "Yes" : "No";
        }
        return Arrays.asList(new Object[]{StringUtils.capitalize(firstName),StringUtils.capitalize(lastName),StringUtils.capitalize(team.getName()),lastStr, buspass,getTotalMinutes()});
    }

    public Date getLastDay() {
        List<Date> dates = days.keySet().stream().map(TimeUtils::fromStringToDate).sorted( (d1, d2) -> d1.compareTo(d2)).collect(Collectors.toList());
        if(dates.size() == 0)
            return null;
        return TimeUtils.getDateOnly(dates.get(dates.size()-1));
    }
    public List getFormattedDays() {
        //noinspection unchecked
        return days.entrySet().stream().map( (e) -> "bus pass:"+ e.getValue().needsBusPass() + "," + e.getKey()+", login:"+ e.getValue().getFormattedLoginTime() + ", logout:"+ e.getValue().getFormattedLogoutTime() + "\n").collect(Vector::new,Vector::add,Vector::addAll);
    }

    public static BiFunction<String, String, String> toFullName = (first, last) -> String.format("%s %s", first, last);
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
        File teamDir = new File(HourLogger.saveDir, this.team.getName().toLowerCase());
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

    public enum Team {
        PROGRAMMING("Programming"),
        MECHANICAL("Mechanical"),
        ELECTRICAL("Electrical"),
        SHOP("Shop"),
        MISSION("Mission"),
        DESIGN("Design"),
        UNKNOWN("Unknown");
        private String name;
        Team(String name) {
            this.name = name;
        }
        public static Team[] VALUES = new Team[]{PROGRAMMING,MECHANICAL,ELECTRICAL,SHOP,MISSION,DESIGN};
        public static Team getValue( String name) {
            name=StringUtils.capitalize(name);
            for(Team team: VALUES)
                if(name.equals(team.getName()))
                    return team;
            return UNKNOWN;
        }
        public String getName() {
            return name;
        }

    }
}
