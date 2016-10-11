package team1793;

import team1793.data.Member;
import team1793.menu.IMenu;
import team1793.menu.ViewMembers;
import team1793.utils.CSVUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.stream.Collectors;

import static team1793.Config.saveDir;


/**
 * Created by tyler on 10/7/16.
 */
public class HourLogger {
    public static List<Member> memberList = new ArrayList<>();

    public static JFrame frame;
    public static IMenu currentMenu;

    public static void main(String[] args) {
        Config.init();
        HourLogger.loadMembers();
        HourLogger.sort();
        frame = new JFrame("Hour Logger");
        setMenu(new ViewMembers());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.validate();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
//                memberList.stream().forEach(CSVUtils::writeMemberFile);
            }
        });
    }

    public static void setMenu(IMenu menu) {
        currentMenu = menu;
        frame.setContentPane(menu.getContentPane());
        frame.setSize(menu.getContentPane().getPreferredSize());
        frame.revalidate();
        currentMenu.update();
        frame.pack();
        frame.validate();
    }

    public static Member[] getMemberArray() {
        Member[] array = new Member[memberList.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = memberList.get(i);
        return array;
    }

    public static Member getMemberFromName(String firstName, String lastName) {
        Optional<Member> member = memberList.stream().filter(m -> m.isName(firstName, lastName)).findFirst();
        if (member.isPresent())
            return member.get();
        return null;
    }

    public static void loadMembers() {
        saveDir.mkdirs();
        Arrays.stream(saveDir.listFiles()).filter(f -> !f.isDirectory()).map(f -> CSVUtils.readMemberFile(f)).collect(Collectors.toCollection(() -> memberList));
    }

    public static void sort() {
        Collections.sort(memberList, (m1, m2) -> m1.getFullname().compareTo(m2.getFullname()));
    }

}
