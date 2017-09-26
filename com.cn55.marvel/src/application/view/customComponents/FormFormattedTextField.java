package application.view.customComponents;

import styles.FontFactory;

import javax.swing.*;
import java.text.Format;

public class FormFormattedTextField extends JFormattedTextField {
    public FormFormattedTextField(Format format) {
        super(format);
        setValue(0.00D);
        setColumns(20);
        setMinimumSize(getPreferredSize());
        setFont(FontFactory.textFieldFont());
        setVisible(false);
    }
}
