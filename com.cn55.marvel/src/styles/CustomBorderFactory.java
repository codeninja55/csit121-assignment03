package styles;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class CustomBorderFactory {
    public static Border formBorder(String title) {
        /* BORDERS - Adding 3 Borders around the form */
        Border outInnerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4, 4, 4, 4, ColorFactory.red500()),
                title,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                new Font("Product Sans", Font.BOLD, 32),
                ColorFactory.red500());
        Border inInnerBorder = BorderFactory.createEmptyBorder(25, 25, 25, 25);
        Border innerBorder = BorderFactory.createCompoundBorder(outInnerBorder, inInnerBorder);
        Border outerBorder = BorderFactory.createEmptyBorder(1, 10, 10, 10);
        return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    }

    public static Border toolbarBorder(String title) {
        Border innerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4,4,4,4, ColorFactory.blueGrey800()),
                title,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                FontFactory.titledBorderFont(),
                ColorFactory.blueGrey800());
        Border outerBorder = BorderFactory.createEmptyBorder(20,10,20,10);
        return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    }

    public static Border resultsPaneBorder() {
        Border outInnerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4,4,4,4, ColorFactory.blueGrey800()),
                "RESULTS",
                TitledBorder.CENTER,
                TitledBorder.ABOVE_TOP,
                new Font("Product Sans",Font.BOLD,32),
                ColorFactory.grey50());
        Border inInnerBorder = BorderFactory.createEmptyBorder(15,15,15,15);
        Border innerBorder = BorderFactory.createCompoundBorder(outInnerBorder, inInnerBorder);
        Border outerBorder = BorderFactory.createEmptyBorder(1,10,10,10);
        return BorderFactory.createCompoundBorder(outerBorder,innerBorder);
    }
}
