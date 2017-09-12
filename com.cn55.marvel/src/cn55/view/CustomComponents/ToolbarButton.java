package cn55.view.CustomComponents;

import javax.swing.JButton;
import java.awt.*;

public class ToolbarButton extends JButton {

    public ToolbarButton(String text) {
        super(text);

        setFont(Style.toolbarButtonFont());
        setBackground(Style.red500());
        setForeground(Style.btnTextColor());
        Dimension dim = new Dimension(280,50);
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}
