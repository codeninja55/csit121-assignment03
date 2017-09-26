package application.view.jtableModels;

import application.model.purchase.Purchase;
import application.view.customComponents.Style;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PurchaseTableModel extends AbstractTableModel {

    private ArrayList<Purchase> purchases;
    private final String[] tableHeaders = {"Receipt ID","Card ID", "Card Type",
            "Total Amount","Purchase Time"};

    public void setData (ArrayList<Purchase> purchases) { this.purchases = purchases; }

    public String getColumnName(int column) {
        return tableHeaders[column];
    }

    public int getRowCount() { return purchases.size(); }

    public int getColumnCount() { return tableHeaders.length; }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Purchase purchase = purchases.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return purchase.getReceiptID();
            case 1:
                return purchase.getCardID();
            case 2:
                return purchase.getCardType();
            case 3:
                return Style.currencyFormat().format(purchase.getCategoriesTotal());
            case 4:
                return purchase.getPurchaseTimeStr();
        }
        return null;
    }
}
