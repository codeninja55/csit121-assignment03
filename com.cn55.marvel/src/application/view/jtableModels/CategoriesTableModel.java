package application.view.jtableModels;

import application.model.category.Category;
import styles.FormatterFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoriesTableModel extends AbstractTableModel {
    private ArrayList<Category> categories;
    private final String[] tableHeaders = {"ID", "Name", "Description", "Total Amount"};

    public void setData(ArrayList<Category> categories) {
        Collections.sort(categories);
        categories.sort(Comparator.comparingInt(Category::getId));
        this.categories = categories;
    }

    public String getColumnName(int column) { return tableHeaders[column]; }

    public int getRowCount() {
        return (categories != null) ? categories.size() : 0;
    }

    public int getColumnCount() {
        return tableHeaders.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Category category = categories.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return category.getId();
            case 1:
                return category.getName();
            case 2:
                return category.getDescription();
            case 3:
                return FormatterFactory.currencyFormat().format(category.getTotalAmount());
        }
        return null;
    }
}
