package application.view.customComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CancelButton extends JButton{

    public CancelButton(String text) {
        super((" " + text));

        setIcon(Style.cancelIcon());
        setFont(Style.buttonFont());
        setForeground(Style.red50());
        setBackground(Style.redA700());
        Dimension dim = getPreferredSize();
        dim.height = 60;
        setPreferredSize(dim);
        setMinimumSize(dim);

        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (isEnabled()) {
                    setIcon(Style.cancelIconDisabled());
                    setForeground(Style.blueGrey700());
                    setBackground(Style.red300());
                }
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (isEnabled()) {
                    setForeground(Style.red50());
                    setIcon(Style.cancelIcon());
                    setBackground(Style.redA700());
                }
            }
        });
    }

}
