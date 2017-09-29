package styles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableFormatterFactory {

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

    public static void cardTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(1).setPreferredWidth(5);
        table.getColumnModel().getColumn(2).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(TableFormatterFactory.rightRenderer());
        table.getColumnModel().getColumn(4).setPreferredWidth(5);
        table.getColumnModel().getColumn(5).setCellRenderer(TableFormatterFactory.rightRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(5);
    }

    public static void purchasesTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(TableFormatterFactory.rightRenderer());
    }

    public static void categoriesTableFormatter(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(45);
        table.getColumnModel().getColumn(0).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(TableFormatterFactory.centerRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(TableFormatterFactory.leftRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(TableFormatterFactory.rightRenderer());
    }

}
