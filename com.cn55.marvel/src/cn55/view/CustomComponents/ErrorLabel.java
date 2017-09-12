package cn55.view.CustomComponents;

import javax.swing.JLabel;

public class ErrorLabel extends JLabel {

    public ErrorLabel(String text) {
        super(text);
        setFont(Style.errorFont());
        setForeground(Style.redA700());
        setVisible(false);
    }
}