package application.view.customComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormTextField extends JTextField {

    public FormTextField(int columns) {
        super(columns);
        setMinimumSize(getPreferredSize());
        setFont(Style.textFieldFont());
        setVisible(false);

        this.addPropertyChangeListener(evt -> {
            if (!isEnabled() || !isEditable()) {
                setBackground(Style.blueGrey500());
                setForeground(Style.grey50());
                setDisabledTextColor(Style.grey600());
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setBackground(Style.blueGrey100());
            }
        });
    }
}
