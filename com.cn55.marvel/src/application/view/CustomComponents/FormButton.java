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
        setBackground(Style.redA700());
        setForeground(Style.btnTextColor());
        Dimension dim = getPreferredSize();
        dim.height = 60;
        dim.width = 100;
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
                if (isEnabled()) setBackground(Style.redA700());
            }
        });
    }
}
