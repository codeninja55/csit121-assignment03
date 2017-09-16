package application.view.CustomComponents;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolbarButton extends JButton {

    public ToolbarButton(String text) {
        super(text);

        setFont(Style.toolbarButtonFont());
        setBackground(Style.red500());
        setForeground(Style.btnTextColor());
        Dimension dim = new Dimension(280,50);
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setBackground(Style.blueGrey500());
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setBackground(Style.red500());
            }
        });
    }
}
