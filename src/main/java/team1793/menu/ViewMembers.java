package team1793.menu;

import team1793.HourLogger;
import team1793.Member;
import team1793.dialog.AddMember;
import team1793.dialog.EditMember;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by tyler on 10/8/16.
 */
public class ViewMembers implements IMenu {
    private JList<Member> memberList;
    private JPanel rootPanel;
    private JSplitPane split;
    private JTextPane memberInfo;
    private JButton addMember;
    private JButton removeMember;
    private JButton back;
    private JLabel memberQR;
    private JButton editMember;

    public ViewMembers() {
        update();
        memberInfo.setEditable(false);
        memberList.addListSelectionListener( e -> {
            updateInfo();
        });
        ActionListener listener = e -> {
            String c = e.getActionCommand();
            if(c.equals("back")) {
                HourLogger.setMenu(new MainMenu());
            } else if(c.equals("addMember")) {
                new AddMember();
            } else if(c.equals("removeMember")) {
                int n = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to remove this member?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if(n == 0) {
                    for(Member member: memberList.getSelectedValuesList()) {
                        HourLogger.memberList.remove(member);
                    }
                    update();

                }
            } else if(c.equals("editMember")) {
                if(memberList.getSelectedValue() != null) {
                    System.out.println("wevwevwev");
                    new EditMember(memberList.getSelectedValue());
                }
            }
        };
        back.addActionListener(listener);
        addMember.addActionListener(listener);
        removeMember.addActionListener(listener);
        editMember.addActionListener(listener);

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public Container getContentPane() {
        return rootPanel;
    }

    public void updateInfo() {
        if(memberList.getSelectedValue() != null) {
            Member member = memberList.getSelectedValue();
            BufferedImage qrCode = null;
            try {
                qrCode = ImageIO.read(member.getQR());
                memberQR.setIcon(new ImageIcon(qrCode));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            memberInfo.setText("Total Minutes:"+String.valueOf(member.getTotalMinutes()));
        } else {
            memberInfo.setText("");
            memberQR.setIcon(null);
        }
    }
    @Override
    public void update() {
        memberList.setListData(HourLogger.getMemberArray());
        updateInfo();
    }
}
