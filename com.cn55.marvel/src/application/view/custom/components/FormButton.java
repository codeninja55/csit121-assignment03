package application.view.custom.components;

import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormButton extends JButton {
    public FormButton(String text, ImageIcon icon) {
        super(text);
        setIcon(icon);
        setFont(FontFactory.buttonFont());
        setBackground(ColorFactory.redA700());
        setForeground(ColorFactory.btnTextColor());
        Dimension dim = getPreferredSize();
        dim.height = 60;
        setMinimumSize(dim);
        setVisible(false);

        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (isEnabled()) setBackground(ColorFactory.blueGrey500());
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (isEnabled()) setBackground(ColorFactory.redA700());
            }
        });
    }
}
