package application.view.custom.components;

import styles.FontFactory;

import javax.swing.*;
import java.awt.*;

public class FormLabel extends JLabel {
    public FormLabel(String text) {
        super(text);
        setFont(FontFactory.labelFont());
        setVisible(false);
    }

    public FormLabel (String text, Color foreground, Font font) {
        this(text);
        setFont(font);
        setForeground(foreground);
        setVisible(true);
    }
}
