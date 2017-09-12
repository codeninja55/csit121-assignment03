package cn55;

import cn55.controller.Program;
import cn55.view.CustomComponents.Style;

import javax.swing.*;

/*
 * @author Dinh Che
 * Student Number: 5721970
 * Email: dbac496@uowmail.edu.au
 */

class Assignment2 {
    public static void main(String[] args) {
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
        UIManager.put("TableHeader.font", Style.buttonFont());
        UIManager.put("TableHeader.background", Style.red500());
        UIManager.put("TableHeader.foreground", Style.grey50());

        UIManager.put("TextField.selectionBackground", Style.grey500());
        UIManager.put("TextField.font", Style.textFieldFont());
        UIManager.put("TextField.caretForeground", Style.red500());

        /* Create and display the Program the safe Java way */
        java.awt.EventQueue.invokeLater(Program::new);
    }

}
