package application.view.custom_components;

import styles.FontFactory;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class MaterialRadioButton extends JRadioButton{

    public MaterialRadioButton(String text) {
        ImageIcon defaultIcon = new ImageIcon(
                "com.cn55.marvel/src/img/radio_button_off_red500_24-custom.png");
        ImageIcon selectedIcon = new ImageIcon(
                "com.cn55.marvel/src/img/radio_button_on_red500_24-custom.png");

        setText(text);
        setFont(FontFactory.labelFont());
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
