package team1793.data;

import javax.swing.table.AbstractTableModel;
import java.util.List;

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

    private List<Member> members;
    private MemberData[] header;

    public MemberTableModel(List<Member> members) {
        this.members = members;
        this.header = MemberData.VALUES;
    }

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
        Member member = members.get(i);
        return member.getMemberData().get(j);
    }

    public boolean isCellEditable(int var1x, int var2x) {
        return true;
    }

    @Override
    public void setValueAt(Object o, int i, int j) {
        switch(MemberData.VALUES[j]) {
            case FIRST_NAME:
                members.get(i).setFirstName((String) o);
            case LAST_NAME:
                members.get(i).setLastName((String) o);
            case TEAM:
                members.get(i).setTeam(Member.Team.getValue((String) o));
                break;
            case BUS_PASS:
                members.get(i).setBusPass((String)o);
                break;
            default:break;
        }
    }
}
