package application.view.table.models;

import application.model.purchase.Purchase;
import styles.FormatterFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PurchaseTableModel extends AbstractTableModel {
    private ArrayList<Purchase> purchases;
    private final String[] tableHeaders = {"Purchase Time", "Receipt ID","Card ID", "Card Type", "Total Amount"};

    public void setData (ArrayList<Purchase> purchases) { this.purchases = purchases; }

    public String getColumnName(int column) {
        return tableHeaders[column];
    }

    public int getRowCount() { return (purchases != null) ? purchases.size() : 0; }

    public int getColumnCount() { return tableHeaders.length; }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Purchase purchase = purchases.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return purchase.getPurchaseTimeStr();
            case 1:
                return purchase.getReceiptID();
            case 2:
                return purchase.getCardID();
            case 3:
                return purchase.getCardType();
            case 4:
                return FormatterFactory.currencyFormat().format(purchase.getCategoriesTotal());
        }
        return null;
    }
}
