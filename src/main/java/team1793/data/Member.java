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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by tyler on 10/7/16.
 */
public class Member implements Comparable<Member> {
    private static final int WAIT_TIME = 15;
    private String firstName, lastName, team;
    public TreeMap<String, Session> sessions = new TreeMap<String,Session>(TimeUtils.compareStringDate);
    private File qr;

    public Member(String firstName, String lastName, Team team) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
        this.team = team.getName();
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
        if (!sessions.containsKey(date)) {
            //sets login and logout as same time.
            int reply = JOptionPane.showConfirmDialog(null, "Do you need a bus pass?", "Bus Pass", JOptionPane.YES_NO_OPTION);
            addDay(dateTime, dateTime, reply == JOptionPane.YES_OPTION);
        } else {
            Session session = sessions.get(date);
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
    public String yesno(boolean b) {
        return b ? "Yes": "No";
    }
    public String needBuspass() {

        String date = TimeUtils.toString(TimeUtils.now(TimeUtils.dateFormat));
        Optional<Session> s = Optional.ofNullable(sessions.get(date));
        if(s.isPresent())
            return yesno(s.get().needsBusPass());
        return yesno(false);
    }
    public void setInfo(Object o, int r) {
        switch(r) {
            case 2:
                getSaveFile().delete();
                this.team = (String) o;
                save();
                break;
            default:
                break;
        }
    }

    public List getInfo() {
        return Arrays.asList(new Object[]{StringUtil.capitalize(firstName),StringUtil.capitalize(lastName), StringUtil.capitalize(team), needBuspass(), getMinutesToday(),getTotalMinutes()});
    }
    public void addDay(Date loginTime, Date logoutTime, boolean buspass) {
        Session session = new Session(TimeUtils.getMinuteSum(loginTime), TimeUtils.getMinuteSum(logoutTime));
        session.setNeedsBusPass(buspass);
        sessions.put(TimeUtils.toString(loginTime), session);
        save();
    }

    private void save() {
        CSVUtils.writeMemberFile(this);
        HourLogger.update();
    }

    public List getFormattedDays() {
        return sessions.entrySet().stream().map((e) -> "bus pass:" + e.getValue().needsBusPass() + "," + e.getKey() + ", login:" + e.getValue().getFormattedLoginTime() + ", logout:" + e.getValue().getFormattedLogoutTime() + "\n").collect(Vector::new, Vector::add, Vector::addAll);
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

    public File getSaveFile() {
        String name = this.team.toLowerCase() + "_" + getFullname().replace(" ", "_") + ".csv";
        return new File(Config.saveDir, name);
    }

    public int getMinutesToday() {
        String date = TimeUtils.toString(TimeUtils.now(TimeUtils.dateFormat));
        Optional<Session> s = Optional.ofNullable(sessions.get(date));
        if(s.isPresent())
            return s.get().getTimeLoggedIn();
        return 0;
    }
    public int getTotalMinutes() {
        OptionalInt total = sessions.values().stream().mapToInt(Session::getTimeLoggedIn).reduce((sum, n) -> sum + n);
        if(total.isPresent())
            return total.getAsInt();
        return 0;
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

    @Override
    public int compareTo(Member member) {
        if(member == null)
            return 0;
        return this.getFullname().compareTo(member.getFullname());
    }
}
