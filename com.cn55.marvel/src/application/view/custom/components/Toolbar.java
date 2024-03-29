package application.view.custom.components;

import styles.CustomBorderFactory;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {
    private final JPanel leftToolbar;
    private final JPanel rightToolbar;

    public Toolbar() {
        leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20,20));
        rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        setLayout(new GridLayout(1,2));
        setBorder(CustomBorderFactory.toolbarBorder("Actions"));
        add(leftToolbar);
        add(rightToolbar);
    }

    /*============================== ACCESSORS  ==============================*/
    public JPanel getLeftToolbar() {
        return leftToolbar;
    }
    public JPanel getRightToolbar() {
        return rightToolbar;
    }
}
