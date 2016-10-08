package team1793;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by tyler on 10/7/16.
 */
public class HourLogger extends JFrame {
    public static HourLogger INSTANCE;
    public static final File mainDir = new File(System.getProperty("user.home") + "/team1793.HourLogger/");
    public static final File saveDir = new File(mainDir, "members");
    public static final File qrDir = new File(saveDir, "qr");
        public static List<String> memberNames = Arrays.<String>asList(
                "Tyler Marshall","Gunner Greene","Shelby Strickland").stream().map( m -> m.toLowerCase().replace(" ","_")).collect(Collectors.toList());
    public static List<Member> members = new ArrayList<>();
    static {
        memberNames.stream().forEach(QRManager::generateQR);
        memberNames.stream().map(Member::new).forEach(members::add);
    }
    private Menu currentMenu = null;
    private Menu mainMenu= new Menu() {
        @Override
        public void init() {
            setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
            addButton("Login", e -> {
                String name = JOptionPane.showInputDialog("Login").toLowerCase().replace(" ","_");
                if(memberNames.contains(name)) {
                    Optional<Member> m1 = members.stream().filter(m -> m.getFullname().equals(name)).findFirst();
                    if(m1.isPresent()) {
                        m1.get().login();
                    }
                }
            });
            addButton("Logout", e -> {
                String name = JOptionPane.showInputDialog("Logout").toLowerCase().replace(" ","_");
                if(memberNames.contains(name)) {
                    Optional<Member> m1 = members.stream().filter(m -> m.getFullname().equals(name)).findFirst();
                    if(m1.isPresent()) {
                        Member member = m1.get();
                        if(member.isLoggedIn()) {
                            member.logout();
                        }
                    }
                }
            });
            addButton("View Members", e -> {
                setMenu(nextMenu);
            });
        }
    };

    public static void sync() {
        members.forEach(CSVManager::writeCSVFiles);
    }
    private Menu nextMenu= new Menu() {
        @Override
        public void init() {
            setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
            addButton("HELLO", e -> System.out.println("TEST"));
            addButton("Back", e -> {
                setMenu(mainMenu);
            });
        }
    };


    public HourLogger() {
        setTitle("Hour Logger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMenu(mainMenu);
        revalidate();
        pack();
        setVisible(true);
    }

    public void setMenu(Menu menu) {
        currentMenu = menu;
        getContentPane().removeAll();
        getContentPane().add(currentMenu);
        revalidate();
        pack();
    }

    public static HourLogger getInstance() {
        return new HourLogger();
    }

    public static void main(String[] args) {
        INSTANCE = getInstance();
    }
}
