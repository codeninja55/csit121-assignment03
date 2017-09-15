package application.view.CustomComponents;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {
    private JPanel leftToolbar;
    private JPanel rightToolbar;

    public Toolbar() {
        leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20,20));
        rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));

        setLayout(new GridLayout(1,2));
        setBorder(Style.toolbarBorder("Actions"));

        add(leftToolbar);
        add(rightToolbar);
    }

    /*============================== MUTATORS  ==============================*/

    /*============================== ACCESSORS  ==============================*/
    public JPanel getLeftToolbar() {
        return leftToolbar;
    }

    public JPanel getRightToolbar() {
        return rightToolbar;
    }
}
