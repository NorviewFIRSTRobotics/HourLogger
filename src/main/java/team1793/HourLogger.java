package team1793;

import team1793.data.Member;
import team1793.menu.IMenu;
import team1793.menu.ViewMembers;
import team1793.utils.CSVUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by tyler on 10/7/16.
 */
public class HourLogger {
    public static final File mainDir = new File(System.getProperty("user.home") + "/HourLogger/");
    public static final File saveDir = new File(mainDir, "members");
    public static final File qrDir = new File(mainDir, "qr");

    public static List<Member> memberList = new ArrayList<>();

    public static JFrame frame;
    public static IMenu currentMenu;

    public static void main(String[] args) {
        HourLogger.loadMembers();
        HourLogger.sort();
        frame = new JFrame("Hour Logger");
        setMenu(new ViewMembers());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.validate();
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
    public static void loadMembers() {
        saveDir.mkdirs();
        try {
            Files.walk(saveDir.toPath()).map(p -> p.toFile()).filter(f -> f.getName().endsWith("csv")).map(CSVUtils::readMemberFile).collect(Collectors.toCollection(() -> memberList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sort() {
        Collections.sort(memberList, (m1, m2) -> m1.getFullname().compareTo(m2.getFullname()));
    }


}
