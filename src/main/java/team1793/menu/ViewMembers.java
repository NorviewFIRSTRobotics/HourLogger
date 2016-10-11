package team1793.menu;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import team1793.Config;
import team1793.data.Member;
import team1793.data.MemberTableModel;
import team1793.dialog.AddMember;
import team1793.dialog.CameraLogin;
import team1793.dialog.LoginMember;
import team1793.dialog.RemoveMember;
import team1793.utils.CSVUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static team1793.HourLogger.memberList;

/**
 * Created by tyler on 10/8/16.
 */
public class ViewMembers implements IMenu {
    private JPanel rootPanel;

    private JButton addMember;
    private JButton addMemberList;
    private JButton removeMember;
    private JButton loginButton;
    private JButton cameraButton;

    private JTable memberTable;
    private JButton configButton;
    private JLabel memberQr;
    private JList memberDays;
    private Member selectedMember;
    public ViewMembers() {
        $$$setupUI$$$();
        update();
        ActionListener listener = e -> {
            String c = e.getActionCommand();
            switch (c) {
                case "addMember":
                    new AddMember();
                    break;
                case "removeMember":
                    int[] rows = memberTable.getSelectedRows();
                    new RemoveMember(IntStream.range(0, rows.length).mapToObj(i -> memberList.get(i)).collect(Collectors.toList()), this);

                    break;
                case "addMany":
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(rootPanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        CSVUtils.addMemberList(file);
                        System.out.println("You chose to open this file: " +
                                chooser.getSelectedFile().getName());
                    }
                    break;
                case "login":
                    new LoginMember();
                    break;
                case "cameraLogin":
                    new CameraLogin();
                    break;
                case "config":
                    chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new java.io.File("."));
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);
                    returnVal = chooser.showOpenDialog(rootPanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        Config.setSaveDir(new File(chooser.getSelectedFile().getName()));
                    }
                    break;
            }
        };

        addMember.addActionListener(listener);
        removeMember.addActionListener(listener);
        addMemberList.addActionListener(listener);
        loginButton.addActionListener(listener);
        cameraButton.addActionListener(listener);
        configButton.addActionListener(listener);
        rootPanel.validate();

        memberTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (memberTable.getSelectedRow() > -1) {
                    selectedMember = memberList.get(memberTable.getSelectedRow());

                    memberDays.setListData(selectedMember.getFormattedDays().toArray());

                    if(selectedMember == null)
                        return;
                    BufferedImage qrCode;
                    try {
                        qrCode = ImageIO.read(selectedMember.getQR());
                        memberQr.setIcon(new ImageIcon(qrCode));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    memberQr.setIcon(null);
                }
            }
        });
        memberQr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
//                if(selectedMember != null && mouseEvent.getButton() == MouseEvent.BUTTON1)
//                    HourLogger.print(selectedMember.getQR());
            }
        });
    }

    @Override
    public Container getContentPane() {
        return rootPanel;
    }

    @Override
    public void update() {
        setTableData();

    }

    public void setTableData() {
        memberTable.setModel(new MemberTableModel(memberList));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(new Color(-12828863));
        rootPanel.setEnabled(true);
        rootPanel.setMaximumSize(new Dimension(-1, -1));
        rootPanel.setMinimumSize(new Dimension(500, 400));
        rootPanel.setPreferredSize(new Dimension(500, 400));
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        rootPanel.add(scrollPane1, gbc);
        memberTable = new JTable();
        scrollPane1.setViewportView(memberTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        panel1.setBackground(new Color(-12828863));
        panel1.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-12236470));
        panel2.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(panel2, gbc);
        addMemberList = new JButton();
        addMemberList.setActionCommand("addMany");
        addMemberList.setFont(new Font(addMemberList.getFont().getName(), addMemberList.getFont().getStyle(), 11));
        addMemberList.setText("Add Members from File");
        panel2.add(addMemberList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        addMember = new JButton();
        addMember.setActionCommand("addMember");
        addMember.setText("Add Member");
        panel2.add(addMember, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMember = new JButton();
        removeMember.setActionCommand("removeMember");
        removeMember.setText("Remove Member");
        panel2.add(removeMember, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginButton = new JButton();
        loginButton.setActionCommand("login");
        loginButton.setText("Manual Login/Logout");
        panel2.add(loginButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cameraButton = new JButton();
        cameraButton.setActionCommand("cameraLogin");
        cameraButton.setLabel("QR Login/Logout");
        cameraButton.setText("QR Login/Logout");
        panel2.add(cameraButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
