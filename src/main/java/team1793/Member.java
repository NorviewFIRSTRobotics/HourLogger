package team1793;

import java.io.File;
import java.util.*;

/**
 * Created by tyler on 10/7/16.
 */
public class Member {
    private String firstName,lastName;
    public HashMap<Integer,Day> days = new HashMap<>();
    private File qr;
    public Member(String firstName,String lastName) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
        qr = QRManager.generateQR(getFullname());
    }

    public void sync() {
        CSVManager.writeMemberFile(this);
        setValues(CSVManager.readMemberFile(new File(HourLogger.saveDir, getFullname()+".csv")));
    }

    public void setValues(Member member) {
        if(member == null)
            return;
        this.firstName = member.firstName;
        this.lastName = member.lastName;
        this.days = member.days;
    }
    public String getFullname() {
        return String.format("%s %s", this.firstName,this.lastName);    }

    public boolean isName(String name) {
        return name.toLowerCase().equals(getFullname());
    }
    @Override
    public String toString() {
        return getFullname();
    }

    public HashMap<Integer, Day> getDays() {
        return days;
    }

    public List getData() {
        List list = new ArrayList();
        for (Map.Entry<Integer, Day> entry : getDays().entrySet()) {
            Day day = entry.getValue();
            int date = entry.getKey();
            Object[] o = new Object[]{date, day.getLoginTime(), day.getLoginTime(), day.getTotalMinutes()};
            list.add(o);
        }
        return list;
    }

    public File getQR() {
        return qr;
    }

    public int getTotalMinutes() {
        return 100;
    }
}
