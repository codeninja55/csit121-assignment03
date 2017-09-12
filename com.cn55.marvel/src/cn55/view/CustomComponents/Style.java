package cn55.view.CustomComponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Style {
    public static Font buttonFont() { return new Font("Verdana", Font.BOLD, 24); }
    public static Font toolbarButtonFont() { return new Font("Verdana", Font.BOLD, 26); }
    public static Font labelFont() { return new Font("Verdana", Font.BOLD, 22); }
    public static Font comboboxFont() { return new Font("Verdana", Font.PLAIN, 20); }
    public static Font titledBorderFont() { return new Font("Verdana", Font.BOLD, 26); }
    public static Font textFieldFont() { return new Font("Monospaced", Font.PLAIN, 20); }
    public static Font textAreaFont() { return new Font("Monospaced", Font.PLAIN, 20); }
    public static Font tabPaneFont() { return  new Font("Verdana", Font.BOLD, 34); }
    public static Font tableDataFont() { return new Font("Monospaced", Font.PLAIN, 26); }
    public static Font textPaneFont() { return new Font("Monospaced", Font.BOLD,24); }
    public static Font errorFont() { return new Font("Monospaced", Font.BOLD, 25); }

    public static Color red100() { return new Color(255,205,210); }
    public static Color red300() { return new Color(229,115,115); }
    public static Color red500() { return new Color(244,67,54); }
    public static Color red900() { return new Color(183,28,28); }
    public static Color redA100() { return new Color(255,138,128); }
    public static Color redA200() { return new Color(255,82,82); }
    public static Color redA700() { return new Color(213,0,0); }
    public static Color orange500() { return new Color(255,152,0); }
    public static Color blue500() { return new Color(33,150,243); }
    public static Color blueGrey50() { return new Color(236,239,241); }
    public static Color blueGrey100() { return new Color(207,216,220); }
    public static Color blueGrey200() { return new Color(176,190,197); }
    public static Color blueGrey400() { return new Color(120,144,156); }
    public static Color blueGrey500() { return new Color(96,125,139); }
    public static Color blueGrey800() { return new Color(55,71,89); }
    public static Color grey50() { return new Color(245,245,245); }
    public static Color grey500 () { return new Color(158,158,158); }
    public static Color grey700() { return new Color(97,97,97); }
    public static Color btnTextColor() { return new Color(245,245,245); }

    public static Border formBorder(String title) {
        /* BORDERS - Adding 3 Borders around the form */
        Border outInnerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4, 4, 4, 4, Style.red500()),
                title,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                new Font("Verdana", Font.BOLD, 24),
                Style.red500());
        Border inInnerBorder = BorderFactory.createEmptyBorder(25, 25, 25, 25);
        Border innerBorder = BorderFactory.createCompoundBorder(outInnerBorder, inInnerBorder);
        Border outerBorder = BorderFactory.createEmptyBorder(1, 10, 10, 10);
        return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    }

    public static Border toolbarBorder(String title) {
        Border innerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4,4,4,4,Style.blueGrey800()),
                title,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                Style.titledBorderFont(),
                Style.blueGrey800());
        Border outerBorder = BorderFactory.createEmptyBorder(20,10,20,10);
        return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    }

    public static Border resultsPaneBorder() {
        Border outInnerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4,4,4,4, Style.blueGrey800()),
                "RESULTS",
                TitledBorder.CENTER,
                TitledBorder.ABOVE_TOP,
                new Font("Verdana",Font.BOLD,26),
                Style.grey50());
        Border inInnerBorder = BorderFactory.createEmptyBorder(15,15,15,15);
        Border innerBorder = BorderFactory.createCompoundBorder(outInnerBorder, inInnerBorder);
        Border outerBorder = BorderFactory.createEmptyBorder(1,10,10,10);
        return BorderFactory.createCompoundBorder(outerBorder,innerBorder);
    }

    public static DefaultTableCellRenderer centerRenderer() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        return centerRenderer;
    }

    public static DefaultTableCellRenderer leftRenderer() {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        return leftRenderer;
    }

    public static DefaultTableCellRenderer rightRenderer() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return rightRenderer;
    }

    public static void hoverEffect(ToolbarButton button, boolean mouseEntered) {
        if (mouseEntered) {
            button.setBackground(blueGrey500());
            //button.setForeground(red500());
        } else {
            button.setBackground(red500());
            //button.setForeground(grey50());
        }
    }
}
