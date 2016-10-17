package team1793.data;

import team1793.Config;
import team1793.HourLogger;
import team1793.utils.CSVUtils;
import team1793.utils.QRUtils;
import team1793.utils.StringUtil;
import team1793.utils.TimeUtils;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.TreeMap;
import java.util.Vector;

import static team1793.utils.StringUtil.capitalize;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {
    private static final int WAIT_TIME = 0;

    private String firstName, lastName;
    private Team team;
    public TreeMap<String, Session> days = new TreeMap<String,Session>(TimeUtils.compareStringDate);
    private File qr;

    public Member(String firstName, String lastName, Team team) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
        this.team = team;

        if (!getSaveFile().exists()) try {
            getSaveFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        qr = QRUtils.generateQR(getFullname());
    }

    //login or logout depending on state
    public void loginlogout() {
        String date = TimeUtils.now();
        Date dateTime = TimeUtils.now(TimeUtils.dateFormat);
        if (!days.containsKey(date)) {
            //sets login and logout as same time.
            int reply = JOptionPane.showConfirmDialog(null, "Do you need a bus pass?", "Bus Pass", JOptionPane.YES_NO_OPTION);
            addDay(dateTime, dateTime, reply == JOptionPane.YES_OPTION);
        } else {
            Session session = days.get(date);
            // this means you have not logged out yet
            if (session != null && !session.hasLoggedOut()) {
                //if the current time is 15 minutes after the login time
                int diff = TimeUtils.getMinuteSum(dateTime) - session.getLoginTime();
                if (diff >= WAIT_TIME) {
                    session.setLogoutTime(TimeUtils.getMinuteSum(dateTime));
                    JOptionPane.showMessageDialog(null, String.format("%s has successfully logged out at %s", getFormattedFullname(), session.getFormattedLogoutTime()));
                } else {
                    JOptionPane.showMessageDialog(null, String.format("You have to wait %d more minutes to logout", WAIT_TIME - diff));
                }
            } else {
                JOptionPane.showMessageDialog(null, String.format("You already logged out today at %s!", session.getFormattedLogoutTime()));
            }
        }
        save();
    }

    public void addDay(Date loginTime, Date logoutTime, boolean buspass) {
        Session session = new Session(TimeUtils.getMinuteSum(loginTime), TimeUtils.getMinuteSum(logoutTime));
        session.setNeedsBusPass(buspass);
        days.put(TimeUtils.toString(loginTime), session);
        save();
    }

    private void save() {
        CSVUtils.writeMemberFile(this);
        HourLogger.update();
    }

    public void setBusPass(String buspass) {
        Session session = days.get(getNthLogin(0));
        if(session == null)
            return;
        session.setNeedsBusPass(buspass.equals("Yes"));
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
        String last = getNthLogin(0) == null ? "Never": getNthLogin(0);
        String buspass = "No";
        Session session = days.get(last);
        buspass =  session != null && session.needsBusPass() ? "Yes" : "No";
        return Arrays.asList(new Object[]{capitalize(firstName), capitalize(lastName), capitalize(team.getName()), last , buspass, getTotalMinutes()});
    }

    public String getNthLogin(int n) {
        List<String> dates = new ArrayList<>(days.keySet());
        if(dates.isEmpty())
            return null;
        return dates.get(n);
    }

    public String getNthLogout(int n) {
        if( n > 2 || n >= days.size()) return null;
        Session session = days.get(getNthLogin(n));
        if(session == null || !session.hasLoggedOut())
            return getNthLogout(n+1);
        return getNthLogin(n);
    }

    public List getFormattedDays() {
        //noinspection unchecked
        return days.entrySet().stream().map((e) -> "bus pass:" + e.getValue().needsBusPass() + "," + e.getKey() + ", login:" + e.getValue().getFormattedLoginTime() + ", logout:" + e.getValue().getFormattedLogoutTime() + "\n").collect(Vector::new, Vector::add, Vector::addAll);
    }

    public String getFormattedFullname() {
        return StringUtil.concat(' ', StringUtil.StringFormat.CAPITALIZED,firstName,lastName);
    }
    public String getFullname() {
        return StringUtil.concat(' ', StringUtil.StringFormat.LOWERCASE, firstName, lastName);
    }

    public File getQR() {
        return qr;
    }

    public int getTotalMinutes() {
        OptionalInt total = days.values().stream().mapToInt(Session::getTimeLoggedIn).reduce((sum, n) -> sum + n);
        if (total.isPresent())
            return total.getAsInt();
        return 0;
    }

    public File getSaveFile() {
        String name = this.team.getName().toLowerCase() + "_" + getFullname().replace(" ", "_") + ".csv";
        return new File(Config.saveDir, name);
    }

    @Override
    public String toString() {
        return getFullname();
    }

    public boolean isName(String firstName, String lastName) {
        return firstName.trim().equalsIgnoreCase(this.firstName) && lastName.trim().equalsIgnoreCase(this.lastName);
    }
    public boolean isName(String fullName) {
        return fullName.equalsIgnoreCase(this.getFullname());
    }
}
