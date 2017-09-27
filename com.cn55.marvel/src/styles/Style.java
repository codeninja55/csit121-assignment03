package styles;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class Style {

    private static DefaultTableCellRenderer centerRenderer() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        return centerRenderer;
    }

    private static DefaultTableCellRenderer leftRenderer() {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        return leftRenderer;
    }

    private static DefaultTableCellRenderer rightRenderer() {
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

    public static void cardTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(1).setPreferredWidth(5);
        table.getColumnModel().getColumn(2).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(Style.rightRenderer());
        table.getColumnModel().getColumn(4).setPreferredWidth(5);
        table.getColumnModel().getColumn(5).setCellRenderer(Style.rightRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(5);
    }

    public static void purchasesTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(Style.rightRenderer());
    }

    public static void categoriesTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(Style.leftRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(Style.rightRenderer());
    }

}
