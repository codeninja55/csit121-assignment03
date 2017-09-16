package application.view.CustomComponents;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormButton extends JButton {

    public FormButton(String text) {
        super(text);

        setFont(Style.buttonFont());
        setBackground(Style.red500());
        setForeground(Style.btnTextColor());
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
