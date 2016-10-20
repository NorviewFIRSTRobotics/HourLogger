package team1793.data;

import team1793.utils.CSVUtils;
import team1793.utils.StringUtil;

import javax.swing.JOptionPane;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/16/16
 */
public class MemberSet extends TreeSet<Member> {
    public List<SetListener> eventListeners = new ArrayList<>();
    /**
     * Constructor to set default {@link java.util.Comparator} to sort MemberSet by {@link Member#getFullname}
     * @param loadDirectory     {@link File} to path that {@link MemberSet} loads Members from {@link CSVUtils}
     */
    public MemberSet(File loadDirectory) {
        super((m1, m2) -> m1.getFullname().compareTo(m2.getFullname()));
        loadFromFiles(loadDirectory);
    }

    /**
     * @param firstName first name of member, preferably all lowercase
     * @param lastName  last name of member, preferably all lowercase
     * @param team      team that member is on as a {@link Team#getName()}
     */
    public boolean addMember(final String firstName, final String lastName, final String team) {
        if (getMemberFromName(firstName, lastName) != null) {
            JOptionPane.showMessageDialog(null, String.format("Error:Member %s Already Exists", StringUtil.concat(' ', StringUtil.StringFormat.CAPITALIZED, firstName, lastName)));
            return false;
        }
        Member member = new Member(firstName.trim(), lastName.trim(), Team.getValue(team));
        return add(member);
//      HourLogger.currentMenu.update();
    }


    /**
     * @param firstName member's first name to find
     * @param lastName member's last name to find
     * @return Member with firstName and lastName, if not found null
     */
    public Member getMemberFromName(String firstName, String lastName) {
        Optional<Member> member = this.stream().filter(m -> m.isName(firstName, lastName)).findFirst();
        if (member.isPresent())
            return member.get();
        return null;
    }
    /**
     * @param fullName {@link Member#getFullname()}
     * @return Member with getFullname(), if not found null
     */
    public Member getMemberFromName(String fullName) {
        Optional<Member> member = stream().filter(m -> m.isName(fullName)).findFirst();
        if(member.isPresent())
            return member.get();
        return null;
    }

    public Member getMember(int i) {
        Object[] array = toArray();
        if(array.length > i)
            return (Member) array[i];
        return null;
    }

    public void loadFromFiles(File loadDirectory) {
        if(!loadDirectory.exists())
            loadDirectory.mkdirs();
        if(loadDirectory.listFiles() == null)
            return;
        Arrays.stream(loadDirectory.listFiles()).filter(f -> !f.isDirectory() && f != null).map(f -> CSVUtils.readMemberFile(f)).collect(Collectors.toCollection(() -> this));
    }

    @Override
    public boolean add(Member member) {
        boolean add = super.add(member);
        if(add)
            triggerEvent();
        return add;
    }

    @Override
    public boolean remove(Object o) {
        triggerEvent();
        assert o instanceof Member;
        ((Member) o).getSaveFile().delete();
        return super.remove(o);
    }
    public void triggerEvent() {
        eventListeners.forEach(eventListener -> eventListener.contentsChanged(this));
    }

    public void addListener(SetListener listener) {
        eventListeners.add(listener);
    }
}
