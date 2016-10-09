package team1793.menu;

import team1793.HourLogger;
import team1793.dialog.LoginMember;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tyler on 10/8/16.
 */
public class MainMenu implements IMenu {
    private JButton loginButton;
    private JPanel rootPanel;
    private JButton viewMembersButton;

    public MainMenu() {
        viewMembersButton.addActionListener( e -> {
            HourLogger.setMenu(new ViewMembers());
        });
        loginButton.addActionListener(e -> {
            new LoginMember();
        });
    }

    @Override
    public Container getContentPane() {
        return rootPanel;
    }

    @Override
    public void update() {

    }
}
