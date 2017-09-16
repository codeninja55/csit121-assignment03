package application.view.CustomComponents;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class FormRadioButton extends JRadioButton{

    public FormRadioButton(String text) {
        ImageIcon defaultIcon = new ImageIcon(
                "com.cn55.marvel/src/img/radio_button_on_white_24.png");
        ImageIcon selectedIcon = new ImageIcon(
                "com.cn55.marvel/src/img/radio_button_on_grey_24.png");

        setText(text);
        setFont(Style.labelFont());
        setVisible(true);
        setIcon(defaultIcon);

        this.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setIcon(selectedIcon);
            } else {
                setIcon(defaultIcon);
            }
        });
    }
}
