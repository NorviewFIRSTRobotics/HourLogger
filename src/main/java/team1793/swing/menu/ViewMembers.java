package team1793.swing.menu;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import team1793.Config;
import team1793.data.Member;
import team1793.swing.ClockLabel;
import team1793.swing.MemberTable;
import team1793.swing.dialog.AddMember;
import team1793.swing.dialog.CameraLogin;
import team1793.swing.dialog.LoginMember;
import team1793.swing.dialog.RemoveMember;
import team1793.utils.CSVUtils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static team1793.HourLogger.frame;
import static team1793.HourLogger.members;

/**
 * Created by tyler on 10/8/16.
 */
public class ViewMembers implements IMenu {
    private JPanel rootPanel;
    private JMenuBar menuBar;
    private JMenuItem addMember;
    private JMenuItem addMemberList;
    private JMenuItem removeMember;
    private JMenuItem loginButton;
    private JMenuItem cameraButton;
//    private JMenuItem configButton;


    private MemberTable memberTable;
    private JLabel memberQr;
    private JList memberDays;
    private ClockLabel clockLabel;
    private Member selectedMember;

    public ViewMembers() {
        $$$setupUI$$$();
        menuBar = new JMenuBar();
        JMenu memberMenu = new JMenu("Member");
        memberMenu.add(addMember = new JMenuItem("Add Member"));
        memberMenu.add(addMemberList = new JMenuItem("Add Member List"));
        memberMenu.add(removeMember = new JMenuItem("Remove Member"));
        JMenu loginMenu = new JMenu("Login/Logout");
        loginMenu.add(loginButton = new JMenuItem("Manual Login"));
        loginMenu.add(cameraButton = new JMenuItem("Camera Login"));
        menuBar.add(memberMenu);
        menuBar.add(loginMenu);
        frame.setJMenuBar(menuBar);

        update();
        ActionListener listener = e -> {
            String c = e.getActionCommand();
            switch (c) {
                case "addMember":
                    new AddMember();
                    break;
                case "removeMember":
                    new RemoveMember();
                    break;
                case "addMemberList":
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
                case "loginButton":
                    new LoginMember();
                    break;
                case "cameraButton":
                    new CameraLogin();
                    break;
                case "config":
                    chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("."));
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
        addMember.setActionCommand("addMember");
        removeMember.setActionCommand("removeMember");
        addMemberList.setActionCommand("addMemberList");
        loginButton.setActionCommand("loginButton");
        cameraButton.setActionCommand("cameraButton");

        addMember.addActionListener(listener);
        removeMember.addActionListener(listener);
        addMemberList.addActionListener(listener);
        loginButton.addActionListener(listener);
        cameraButton.addActionListener(listener);
        rootPanel.validate();

        memberTable.getSelectionModel().addListSelectionListener(event -> {
            updateMemberInfo();
        });
    }

    @Override
    public Container getContentPane() {
        return rootPanel;
    }

    @Override
    public void update() {
        updateMemberInfo();
    }

    public void updateMemberInfo() {
        if (memberTable.getSelectedRow() > -1) {
            selectedMember = members.getMember(memberTable.getSelectedRow());
            memberDays.setListData(selectedMember.getFormattedDays().toArray());
            if (selectedMember == null)
                return;
            BufferedImage qrCode;
            try {
                qrCode = ImageIO.read(selectedMember.getQR());
                memberQr.setIcon(new ImageIcon(qrCode));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void createUIComponents() {
        memberTable = new MemberTable(members);
        clockLabel = new ClockLabel("date-time");
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
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
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        rootPanel.add(scrollPane1, gbc);
        scrollPane1.setViewportView(memberTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-12236470));
        panel1.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(panel1, gbc);
        memberQr = new JLabel();
        memberQr.setText("");
        panel1.add(memberQr, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 100), null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        memberDays = new JList();
        scrollPane2.setViewportView(memberDays);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel1.add(clockLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, 50), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
