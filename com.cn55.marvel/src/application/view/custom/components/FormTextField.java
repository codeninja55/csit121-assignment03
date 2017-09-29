package application.view.custom.components;

import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormTextField extends JTextField {

    public FormTextField(int columns) {
        super(columns);
        setMinimumSize(getPreferredSize());
        setFont(FontFactory.textFieldFont());
        setVisible(false);

        this.addPropertyChangeListener(evt -> {
            if (!isEnabled() || !isEditable()) {
                setBackground(ColorFactory.blueGrey500());
                setForeground(ColorFactory.grey50());
                setDisabledTextColor(ColorFactory.grey600());
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setBackground(ColorFactory.blueGrey100());
            }
        });
    }
}
