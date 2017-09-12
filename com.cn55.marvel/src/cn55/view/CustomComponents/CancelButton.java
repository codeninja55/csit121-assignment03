package cn55.view.CustomComponents;

import javax.swing.*;
import java.awt.*;

public class CancelButton extends JButton{

    public CancelButton(String text) {
        super(text);

        setFont(Style.buttonFont());
        setForeground(Style.grey50());
        setBackground(Style.red500());
        Dimension dim = getPreferredSize();
        dim.height = 50;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
    }

}
