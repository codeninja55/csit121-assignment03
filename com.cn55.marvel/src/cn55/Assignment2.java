package cn55;

import cn55.controller.Program;
import cn55.view.CustomComponents.Style;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * @author Dinh Che
 * Student Number: 5721970
 * Email: dbac496@uowmail.edu.au
 */

class Assignment2 {
    public static void main(String[] args) {

        // Custom font creation
        try {
            Path fontPath = Paths.get("com.cn55.marvel/src/cn55/font/ProductSansRegular.ttf");
            Path fontBoldPath = Paths.get("com.cn55.marvel/src/cn55/font/ProductSansBold.ttf");
            //Returned font is of pt size 1
            Font productSansFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath.toString()));
            productSansFont.deriveFont(24f);
            Font productSansBoldFont = Font.createFont(Font.BOLD, new File(fontBoldPath.toString()));
            productSansBoldFont.deriveFont(24f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(productSansFont);
            ge.registerFont(productSansBoldFont);

        } catch (IOException|FontFormatException e) {
            // Handle exception
        }

        // Loop through and get all the fonts available in the system
        /*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        for (Font font : fonts) {
            System.out.println(font.getName());
        }*/

        // Change some of the Default Look and Feel
        UIManager.put("Panel.background", Style.blueGrey200());
        UIManager.put("Viewport.background", Style.blueGrey200());

        UIManager.put("Button.background", Style.red900());
        UIManager.put("Button.foreground", Style.grey50());
        UIManager.put("Button.font", Style.buttonFont());
        UIManager.put("Button.disabledText", Style.grey500());

        UIManager.put("ComboBox.buttonBackground", Style.red500());
        UIManager.put("ComboBox.buttonHighlight", Style.grey50());
        UIManager.put("ComboBox.font", Style.toolbarButtonFont());
        UIManager.put("ComboBox.foreground", Style.grey50());
        UIManager.put("ComboBox.background", Style.blueGrey500());
        UIManager.put("ComboBox.selectionBackground", Style.red500());
        UIManager.put("ComboBox.selectionForeground", Style.grey50());

        UIManager.put("OptionPane.background", Style.blueGrey200());
        UIManager.put("OptionPane.buttonFont", Style.buttonFont());
        UIManager.put("OptionPane.messageFont", Style.textPaneFont());
        UIManager.put("OptionPane.font", Style.textPaneFont());
        UIManager.put("OptionPane.foreground", Style.red900());
        UIManager.put("OptionPane.messageForeground", Style.red900());
        UIManager.put("OptionPane.titleText", Style.labelFont());

        UIManager.put("RadioButton.font", Style.labelFont());
        UIManager.put("RadioButton.background", Style.blueGrey200());

        UIManager.put("ScrollBar.background", Style.red500());
        UIManager.put("ScrollBar.foreground", Style.red500());
        UIManager.put("ScrollBar.track", Style.red500());

        UIManager.put("TabbedPane.selected", Style.red500());
        UIManager.put("TabbedPane.selectedForeground", Style.blueGrey500());

        UIManager.put("Table.selectionBackground", Style.red300());
        UIManager.put("Table.font", Style.tableDataFont());
        UIManager.put("TableHeader.font", Style.toolbarButtonFont());
        UIManager.put("TableHeader.background", Style.red500());
        UIManager.put("TableHeader.foreground", Style.grey50());

        UIManager.put("TextField.selectionBackground", Style.grey500());
        UIManager.put("TextField.font", Style.textFieldFont());
        UIManager.put("TextField.caretForeground", Style.red500());

        /* Create and display the Program the safe Java way */
        java.awt.EventQueue.invokeLater(Program::new);
    }

}
