package application.view.custom_components;

import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolbarButton extends JButton {

    public ToolbarButton(String text, ImageIcon icon) {
        super(" "+text);

        setIcon(icon);
        setFont(FontFactory.toolbarButtonFont());
        setBackground(ColorFactory.redA700());
        setForeground(ColorFactory.btnTextColor());
        Dimension dim = new Dimension(280,60);
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(true);

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
