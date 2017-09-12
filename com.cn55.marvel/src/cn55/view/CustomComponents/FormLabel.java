package cn55.view.CustomComponents;

import javax.swing.JLabel;

public class FormLabel extends JLabel {

    public FormLabel(String text) {
        super(text);
        setFont(Style.labelFont());
        setVisible(false);
    }
}
