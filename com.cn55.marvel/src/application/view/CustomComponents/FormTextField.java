package application.view.CustomComponents;

import javax.swing.*;

public class FormTextField extends JTextField {

    public FormTextField(int columns) {
        super(columns);
        setMinimumSize(getPreferredSize());
        setFont(Style.textFieldFont());
        setVisible(false);
    }

    @SuppressWarnings("unused")
    public FormTextField(String text) {
        super(text);
        setMinimumSize(getPreferredSize());
        setFont(Style.textFieldFont());
        setVisible(false);
    }

    @SuppressWarnings("unused")
    public FormTextField(String text, int columns) {
        super(text, columns);
        setMinimumSize(getPreferredSize());
        setFont(Style.textFieldFont());
        setVisible(false);
    }
}
