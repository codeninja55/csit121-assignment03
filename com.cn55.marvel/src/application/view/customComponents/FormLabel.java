package application.view.customComponents;

import javax.swing.*;

public class FormLabel extends JLabel {

    public FormLabel(String text) {
        super(text);
        setFont(Style.labelFont());
        setVisible(false);
    }
}
