package application.view.customComponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Style {

    public static Font tabPaneFont() { return  new Font("Product Sans", Font.BOLD, 42); }
    public static Font toolbarButtonFont() { return new Font("Product Sans", Font.BOLD, 30); }
    public static Font buttonFont() { return new Font("Product Sans", Font.BOLD, 28); }
    public static Font labelFont() { return new Font("Product Sans", Font.BOLD, 26); }
    public static Font comboboxFont() { return new Font("Product Sans", Font.PLAIN, 26); }
    public static Font titledBorderFont() { return new Font("Product Sans", Font.BOLD, 32); }
    public static Font textFieldFont() { return new Font("Product Sans", Font.PLAIN, 22); }
    public static Font textAreaFont() { return new Font("Product Sans", Font.PLAIN, 26); }
    public static Font tableDataFont() { return new Font("Product Sans", Font.PLAIN, 28); }
    public static Font textPaneFont() { return new Font("Product Sans", Font.BOLD,28); }
    public static Font errorFont() { return new Font("Product Sans", Font.BOLD, 26); }

    public static Color red50() { return new Color(255,235,238); }
    public static Color red100() { return new Color(255,205,210); }
    public static Color red200() { return new Color(239,154,154); }
    public static Color red300() { return new Color(229,115,115); }
    public static Color red400() { return new Color(239,83,80); }
    public static Color red500() { return new Color(244,67,54); }
    public static Color red600() { return new Color(229,57,53); }
    public static Color red700() { return new Color(211,47,47); }
    public static Color red800() { return new Color(198,40,40); }
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
    public static Color blueGrey600() { return new Color(84,110,122); }
    public static Color blueGrey700() { return new Color(69,90,100); }
    public static Color blueGrey800() { return new Color(55,71,89); }
    public static Color grey50() { return new Color(245,245,245); }
    public static Color grey500 () { return new Color(158,158,158); }
    public static Color grey600 () { return new Color(84,110,122); }
    public static Color grey700() { return new Color(97,97,97); }
    public static Color btnTextColor() { return new Color(245,245,245); }

    public static Border formBorder(String title) {
        /* BORDERS - Adding 3 Borders around the form */
        Border outInnerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(4, 4, 4, 4, Style.red500()),
                title,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                new Font("Product Sans", Font.BOLD, 32),
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
                new Font("Product Sans",Font.BOLD,32),
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

    public static NumberFormat currencyFormat() {
        NumberFormat formatter = DecimalFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }

    public static NumberFormat pointsFormat() {
        NumberFormat formatter = DecimalFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter;
    }

    public static ImageIcon homeIcon() { return new ImageIcon("com.cn55.marvel/src/img/home_white_48.png"); }
    public static ImageIcon cardIcon() {
        return new ImageIcon("com.cn55.marvel/src/img/card_membership_white_48.png");
    }
    public static ImageIcon purchaseIcon() { return new ImageIcon("com.cn55.marvel/src/img/shopping_cart_white_48.png"); }
    public static ImageIcon categoryIcon() { return new ImageIcon("com.cn55.marvel/src/img/view_list_white_48.png"); }
    public static ImageIcon createIcon() { return new ImageIcon("com.cn55.marvel/src/img/add_circle_white_36.png"); }
    public static ImageIcon deleteIcon() { return new ImageIcon("com.cn55.marvel/src/img/remove_circle_white_36.png"); }
    public static ImageIcon deleteActionIcon() { return new ImageIcon("com.cn55.marvel/src/img/delete_white_36.png"); }
    public static ImageIcon deleteIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/remove_circle_grey_36.png"); }
    public static ImageIcon searchIcon() {
        return new ImageIcon("com.cn55.marvel/src/img/search_white_36.png");
    }
    public static ImageIcon viewIcon() {
        return new ImageIcon("com.cn55.marvel/src/img/visibility_white_36.png");
    }
    public static ImageIcon summaryIcon() {
        return new ImageIcon("com.cn55.marvel/src/img/assessment_white_36.png");
    }
    public static ImageIcon cancelIcon() { return new ImageIcon("com.cn55.marvel/src/img/cancel_white_36.png");}
    public static ImageIcon cancelIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/cancel_grey_36.png");}
    public static ImageIcon clearIcon() { return new ImageIcon("com.cn55.marvel/src/img/clear_all_white_36.png");}
    public static ImageIcon addIcon() { return new ImageIcon("com.cn55.marvel/src/img/add_white_36.png");}
    public static ImageIcon addIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/add_grey_36.png");}
}
