package application.view.CustomComponents;

import javax.swing.*;
import java.text.Format;

public class FormFormattedTextField extends JFormattedTextField {
    public FormFormattedTextField(Format format) {
        super(format);
        setValue(0.00D);
        setColumns(20);
        setMinimumSize(getPreferredSize());
        setFont(Style.textFieldFont());
        //setBackground(Style.red100());
        setVisible(false);
    }
}
