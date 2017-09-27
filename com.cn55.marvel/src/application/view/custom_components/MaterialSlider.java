package application.view.custom_components;

import styles.ColorFactory;

import javax.swing.*;
import java.awt.*;

public class MaterialSlider extends JSlider {
    public MaterialSlider(int orientation, int min, int max, int start) {
        super(orientation, min, max, start);
        setForeground(Color.BLACK);
        setBackground(ColorFactory.blueGrey200());
        setMinorTickSpacing(20);
        setMajorTickSpacing(max * 5);
        setPaintLabels(true);
        setPaintTicks(true);
        setPaintTrack(true);

        Dimension dim = getPreferredSize();
        dim.width = 850;
        dim.height = 70;
        setMinimumSize(dim);
        setPreferredSize(dim);
    }
}
