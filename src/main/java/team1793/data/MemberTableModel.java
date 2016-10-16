package team1793.data;

import javax.swing.table.AbstractTableModel;

import static team1793.HourLogger.members;

/**
 * Created by tyler on 10/10/16.
 */
public class MemberTableModel extends AbstractTableModel {

    public enum MemberData {
        FIRST_NAME("First Name"), LAST_NAME("Last Name"), TEAM("Team"), LAST_LOGIN("Last Login"), BUS_PASS("Bus Pass"), TOTAL("Total Minutes");
        public static MemberData[] VALUES = values();

        private String name;

        MemberData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private static MemberData[] header = MemberData.VALUES;

    public String getColumnName(int var1x) {
        return header[var1x].getName();
    }

    public int getRowCount() {
        return members.size();
    }

    public int getColumnCount() {
        return header.length;
    }

    public Object getValueAt(int i, int j) {
        Member member = (Member) members.toArray()[i];
        return member.getMemberData().get(j);
    }

    public boolean isCellEditable(int var1x, int var2x) {
        return false;
    }
}
