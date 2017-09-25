package application.view.customComponents;

import javax.swing.*;
import java.awt.*;

public class MaterialSlider extends JSlider {
    public MaterialSlider(int orientation, int min, int max, int start) {
        super(orientation, min, max, start);
        setForeground(Style.red500());
        setBackground(Style.blueGrey500());
        setPaintLabels(true);
        setPaintTicks(true);
        setMinorTickSpacing(1);
        setMajorTickSpacing(max);
        setPaintTrack(true);

        Dimension dim = getPreferredSize();
        dim.width = 1000;
        dim.height = 100;
        setMinimumSize(dim);
        setPreferredSize(dim);
    }
}
