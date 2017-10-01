package application;

import application.controller.Program;
import application.model.Shop;
import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Assignment2 {
    public static void main(String[] args) {
        // Custom font creation
        try {
            Path fontPath = Paths.get("com.cn55.marvel/src/font/ProductSansRegular.ttf");
            Path fontBoldPath = Paths.get("com.cn55.marvel/src/font/ProductSansBold.ttf");
            //Returned font is of pt size 1
            Font productSansFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath.toString()));
            productSansFont.deriveFont(24f);
            Font productSansBoldFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontBoldPath.toString()));
            productSansBoldFont.deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(productSansFont);
            ge.registerFont(productSansBoldFont);
        } catch (IOException|FontFormatException e) {
            //e.printStackTrace();
        }
        uiChanges();
        Shop shop = Shop.getShopInstance();
        shop.getDataStore().importData();
        shop.getAuthenticator().importUsers();

        java.awt.EventQueue.invokeLater(Program::new);
    }

    private static void uiChanges() {
        // Change some of the Default Look and Feel
        UIManager.put("Panel.background", ColorFactory.blueGrey200());
        UIManager.put("Viewport.background", ColorFactory.blueGrey200());

        UIManager.put("Button.background", ColorFactory.red900());
        UIManager.put("Button.foreground", ColorFactory.grey50());
        UIManager.put("Button.font", FontFactory.buttonFont());
        UIManager.put("Button.disabledText", ColorFactory.grey500());

        UIManager.put("ComboBox.buttonBackground", ColorFactory.red500());
        UIManager.put("ComboBox.buttonHighlight", ColorFactory.grey50());
        UIManager.put("ComboBox.font", FontFactory.toolbarButtonFont());
        UIManager.put("ComboBox.foreground", ColorFactory.grey50());
        UIManager.put("ComboBox.background", ColorFactory.blueGrey500());
        UIManager.put("ComboBox.selectionBackground", ColorFactory.red500());
        UIManager.put("ComboBox.selectionForeground", ColorFactory.grey50());

        UIManager.put("OptionPane.background", ColorFactory.blueGrey200());
        UIManager.put("OptionPane.buttonFont", FontFactory.buttonFont());
        UIManager.put("OptionPane.messageFont", FontFactory.textPaneFont());
        UIManager.put("OptionPane.font", FontFactory.textPaneFont());
        UIManager.put("OptionPane.foreground", ColorFactory.red900());
        UIManager.put("OptionPane.messageForeground", ColorFactory.red900());
        UIManager.put("OptionPane.titleText", FontFactory.labelFont());

        UIManager.put("RadioButton.font", FontFactory.labelFont());
        UIManager.put("RadioButton.background", ColorFactory.blueGrey200());

        UIManager.put("ScrollBar.background", ColorFactory.red500());
        UIManager.put("ScrollBar.foreground", ColorFactory.red500());
        UIManager.put("ScrollBar.track", ColorFactory.red500());

        UIManager.put("Slider.altTrackColor", ColorFactory.red500());
        UIManager.put("Slider.focus", ColorFactory.grey500());
        UIManager.put("Slider.horizontalThumbIcon", new ImageIcon("com.cn55.marvel/src/img/slider_red500_24-custom.png"));
        UIManager.put("Slider.tickColour", ColorFactory.red500());

        UIManager.put("TabbedPane.selected", ColorFactory.red500());
        UIManager.put("TabbedPane.selectedForeground", ColorFactory.blueGrey500());

        UIManager.put("Table.selectionBackground", ColorFactory.red300());
        UIManager.put("Table.font", FontFactory.tableDataFont());
        UIManager.put("TableHeader.font", FontFactory.toolbarButtonFont());
        UIManager.put("TableHeader.background", ColorFactory.red500());
        UIManager.put("TableHeader.foreground", ColorFactory.grey50());

        UIManager.put("TextField.font", FontFactory.textFieldFont());
        UIManager.put("TextField.caretForeground", ColorFactory.red500());
    }
}
