package team1793.swing;

import team1793.data.MemberSet;
import team1793.data.Team;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/17/16
 */
public class MemberTable extends JTable {

    public MemberTable(MemberSet members) {
        super(new MemberTableModel(members));
        setRowSorter(new TableRowSorter<>((MemberTableModel) getModel()));
        TableColumn teamColumn = getColumnModel().getColumn(2);
        teamColumn.setCellEditor(new DefaultCellEditor(Team.getComboBox()));
    }

}
