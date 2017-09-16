package application.view.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CancelButton extends JButton{

    public CancelButton(String text) {
        super(text);

        setFont(Style.buttonFont());
        setForeground(Style.grey50());
        setBackground(Style.red500());
        Dimension dim = getPreferredSize();
        dim.height = 50;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());

        this.addMouseListener(new MouseAdapter() {
            @Override
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
