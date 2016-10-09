package team1793;

import team1793.data.Member;
import team1793.menu.IMenu;
import team1793.menu.ViewMembers;
import team1793.utils.CSVUtils;

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
public class HourLogger {
    public static final File mainDir = new File(System.getProperty("user.home") + "/HourLogger/");
    public static final File saveDir = new File(mainDir, "members");
    public static final File qrDir = new File(saveDir, "qr");

    public static List<Member> memberList = new ArrayList<>();

    public static JFrame frame;
    public static IMenu currentMenu;
    public static void main(String[] args) {
        Arrays.stream(saveDir.listFiles()).filter(f -> !f.isDirectory()).map(f -> CSVUtils.readMemberFile(f)).collect(Collectors.toCollection(() -> memberList));
        frame = new JFrame("Hour Logger");
        setMenu(new ViewMembers());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void setMenu(IMenu menu) {
        currentMenu = menu;
        frame.setContentPane(menu.getContentPane());
        frame.setSize(menu.getContentPane().getPreferredSize());
        frame.revalidate();
        frame.pack();
        currentMenu.update();
    }

    public static Member[] getMemberArray() {
        Member[] array = new Member[memberList.size()];
        for(int i = 0; i < array.length;i++)
            array[i] = memberList.get(i);
        return array;
    }

    public static Member getMemberFromName(String firstName, String lastName) {
        Optional<Member> member = memberList.stream().filter(m -> m.isName(firstName, lastName)).findFirst();
        if(member.isPresent())
            return member.get();
        return null;
    }


}
