package team1793.swing;

import team1793.data.MemberSet;

import javax.swing.table.AbstractTableModel;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/17/16
 */
public class MemberTableModel extends AbstractTableModel {
    private String[] headers = new String[]{"First Name","Last Name","Team","Bus Pass"};
    private MemberSet members;
    public MemberTableModel(MemberSet members) {
        super();
        this.members = members;
        members.addListener(this::setMembers);
    }

    public void setMembers(MemberSet members) {
        this.members = members;
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return getValueAt(0,c).getClass();
    }

    @Override
    public String getColumnName(int i) {
        return headers[i];
    }

    @Override
    public int getRowCount() {
        return members.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public Object getValueAt(int r, int c) {
        return members.getMember(r).getInfo().get(c);
    }

    @Override
    public boolean isCellEditable(int r,int c) {
        return c == 2;
    }

    @Override
    public void setValueAt(Object o, int r, int c) {
        members.getMember(r).setInfo(o,c);
        fireTableCellUpdated(r,c);
    }
}
