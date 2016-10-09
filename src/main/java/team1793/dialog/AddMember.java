package team1793.dialog;

import team1793.HourLogger;
import team1793.data.Member;

import javax.swing.*;
import java.awt.event.*;

public class AddMember extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstName,lastName;

    public AddMember() {
        setContentPane(contentPane);
        setModal(true);
        firstName.setName("First Name");
        lastName.setName("Last Name");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
        setVisible(true);
    }

    public static void addMember(String firstName, String lastName) {
        if(HourLogger.getMemberFromName(firstName,lastName) != null) {
            JOptionPane.showMessageDialog(null,String.format("Error:Member %s Already Exists",Member.toFullName.apply(firstName,lastName)));
            return;
        }
        Member member = new Member(firstName.trim(),lastName.trim());
        HourLogger.memberList.add(member);
        HourLogger.currentMenu.update();
    }

    private void onOK() {
        addMember(firstName.getText(),lastName.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
