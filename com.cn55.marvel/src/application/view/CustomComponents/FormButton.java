package application.view.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormButton extends JButton {

    public FormButton(String text, ImageIcon icon) {
        super(text);
        setIcon(icon);
        setFont(Style.buttonFont());
        setBackground(Style.red500());
        setForeground(Style.btnTextColor());
        Dimension dim = getPreferredSize();
        dim.height = 50;
        dim.width = 200;
        setPreferredSize(dim);
        setMinimumSize(dim);
        setVisible(false);

        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (isEnabled()) setBackground(Style.blueGrey500());
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (isEnabled()) setBackground(Style.red500());
            }
        });
    }
}
