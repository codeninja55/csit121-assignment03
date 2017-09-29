package application.view.custom.components;
import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
public class ErrorLabel extends JLabel {
    public ErrorLabel(String text) {
        super(text);
        setFont(FontFactory.errorFont());
        setForeground(ColorFactory.redA700());
        setVisible(false);
    }
}