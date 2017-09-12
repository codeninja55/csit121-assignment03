package cn55.view.CustomComponents;

import javax.swing.JButton;

public class FormButton extends JButton {

    public FormButton(String text) {
        super(text);

        setFont(Style.buttonFont());
        setBackground(Style.red500());
        setForeground(Style.btnTextColor());
        setVisible(false);
    }
}
