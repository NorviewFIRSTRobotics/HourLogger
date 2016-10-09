package team1793.dialog;

import team1793.HourLogger;
import team1793.data.Member;

import javax.swing.*;

/**
 * Created by tyler on 10/8/16.
 */
public class EditMember extends JFrame{
    private JPanel rootPanel;

    public EditMember(Member member) {
        super("Editing " + member.getFullname());
        setContentPane(rootPanel);
        setLocation(HourLogger.frame.getMousePosition());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
}
