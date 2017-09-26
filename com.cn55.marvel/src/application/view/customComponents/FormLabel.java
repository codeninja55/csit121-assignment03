package application.view.customComponents;

import javax.swing.*;
import java.awt.*;

public class FormLabel extends JLabel {
    public FormLabel(String text) {
        super(text);
        setFont(Style.labelFont());
        setVisible(false);
    }

    public FormLabel (String text, Color foreground, Font font) {
        this(text);
        setFont(font);
        setForeground(foreground);
        setVisible(true);
    }
}
