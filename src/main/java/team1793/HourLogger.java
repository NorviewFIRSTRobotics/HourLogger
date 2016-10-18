package team1793;

import team1793.data.MemberSet;
import team1793.swing.menu.IMenu;
import team1793.swing.menu.ViewMembers;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.net.URL;



public class HourLogger {
    public static MemberSet members = new MemberSet(Config.saveDir);

    public static JFrame frame;
    public static IMenu currentMenu;

    public static void main(String[] args) {
        Config.init();
        frame = new JFrame("Hour Logger");
        setMenu(new ViewMembers());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.validate();
        URL url = HourLogger.class.getResource("/pilots-logo.png");
        frame.setIconImage(new ImageIcon(url).getImage());
    }

    public static void setMenu(IMenu menu) {
        currentMenu = menu;
        frame.setContentPane(menu.getContentPane());
        frame.setSize(menu.getContentPane().getPreferredSize());
        frame.revalidate();
        frame.pack();
        currentMenu.update();
    }


    public static void update() {
        if(currentMenu != null) {
            currentMenu.update();
        }
    }

}
