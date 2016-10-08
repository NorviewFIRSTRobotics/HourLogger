package team1793.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by tyler on 10/7/16.
 */
public abstract class Menu extends JPanel {
    public Menu() {
        this.init();
    }
    public abstract  void init();
    public void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);
        add(button);
    }
}