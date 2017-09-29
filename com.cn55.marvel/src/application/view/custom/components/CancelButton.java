package application.view.custom.components;

import styles.ColorFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CancelButton extends JButton{

    public CancelButton(String text) {
        super((" " + text));

        setIcon(IconFactory.cancelIcon());
        setFont(FontFactory.buttonFont());
        setForeground(ColorFactory.red50());
        setBackground(ColorFactory.redA700());
        Dimension dim = getPreferredSize();
        dim.height = 60;
        setPreferredSize(dim);
        setMinimumSize(dim);

        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (isEnabled()) {
                    setIcon(IconFactory.cancelIconDisabled());
                    setForeground(ColorFactory.blueGrey700());
                    setBackground(ColorFactory.red300());
                }
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (isEnabled()) {
                    setForeground(ColorFactory.red50());
                    setIcon(IconFactory.cancelIcon());
                    setBackground(ColorFactory.redA700());
                }
            }
        });
    }

}
