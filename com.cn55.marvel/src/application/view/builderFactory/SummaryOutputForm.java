package application.view.builderFactory;

import application.model.DataObservable;
import application.model.DataObserver;
import application.model.category.Category;
import application.view.customComponents.BaseForm;
import application.view.customComponents.FormButton;
import application.view.customComponents.FormLabel;
import application.view.customComponents.FormTextField;
import styles.ColorFactory;
import styles.FontFactory;
import styles.Style;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryOutputForm extends BaseForm implements FormFactory, DataObserver {
    private DataObservable dataDAO;
    private SummaryCategoriesTableModel categoriesTableModel;

    SummaryOutputForm() {
        super();
        super.setBorder("Analytics");
        setLayout(new GridLayout(2,1,0,10));
        Dimension dim = getPreferredSize();
        dim.width = 900;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());

        JPanel outputForm = new JPanel(new GridBagLayout());
        FormLabel purchasesLabel = new FormLabel("PURCHASES");
        FormLabel totalPurchasesLabel = new FormLabel("Total Purchases:");
        FormTextField totalPurchasesTextField = new FormTextField(20);
        FormLabel cashPurchasesLabel = new FormLabel("Cash Purchases:");
        FormTextField cashPurchaseTextField = new FormTextField(20);
        FormLabel cardPurchasesLabel = new FormLabel("Card Purchases:");
        FormTextField cardPurchasesTextField = new FormTextField(20);
        FormLabel cardLabel = new FormLabel("CARDS");
        FormLabel totalPointsLabel = new FormLabel("Total Points:");
        FormLabel categoriesLabel = new FormLabel("CATEGORIES");
        FormTextField totalPointsTextField = new FormTextField(20);
        categoriesTableModel = new SummaryCategoriesTableModel();
        JTable categoriesTable = new JTable(categoriesTableModel);

        /* FORM AREA */
        GridBagConstraints gc = new GridBagConstraints();
        /*========== FIRST ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 3; gc.weighty = 0.1;
        gc.insets = new Insets(20, 0,30,0);
        purchasesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        purchasesLabel.setFont(FontFactory.toolbarButtonFont());
        outputForm.add(purchasesLabel, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalPurchasesLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalPurchasesTextField, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(cashPurchasesLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(cashPurchaseTextField, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(cardPurchasesLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(cardPurchasesTextField, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 2;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(30, 0,30,0);
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setFont(FontFactory.toolbarButtonFont());
        outputForm.add(cardLabel, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalPointsLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalPointsTextField, gc);

        /*========== LAST ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 2;
        gc.gridx = 0; gc.gridy++; gc.weightx = 2; gc.weighty = 2;
        gc.insets = new Insets(95, 0,0,0);
        categoriesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoriesLabel.setFont(FontFactory.toolbarButtonFont());
        outputForm.add(categoriesLabel, gc);

        /* Table Formatting */
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTable.setRowHeight(55);
        categoriesTable.setFont(FontFactory.labelFont());
        categoriesTable.setFillsViewportHeight(true);
        categoriesTable.setBackground(ColorFactory.blueGrey300());
        categoriesTable.setShowVerticalLines(false);
        categoriesTable.setShowHorizontalLines(false);
        categoriesTable.setShowGrid(false);
        categoriesTable.getTableHeader().setBackground(ColorFactory.blueGrey800());
        categoriesTable.setSelectionBackground(ColorFactory.blueGrey500());
        categoriesTable.setSelectionForeground(ColorFactory.grey50());

        Arrays.stream(outputForm.getComponents()).filter(c -> c instanceof FormTextField)
                .forEach(c -> {
                    ((FormTextField)c).setEditable(false);
                    ((FormTextField)c).setText("10001.00");
                    c.setPreferredSize(new Dimension(300,50));
                    c.setBackground(ColorFactory.blueGrey200());
                    c.setForeground(ColorFactory.grey50());
                    c.setFont(FontFactory.labelFont());
                });

        Arrays.stream(outputForm.getComponents()).filter(c -> c instanceof FormLabel || c instanceof FormButton || c instanceof FormTextField)
                .forEach(c -> c.setVisible(true));

        add(outputForm);
        add(new JScrollPane(categoriesTable));
    }

    public void update() {
        ArrayList<Category> categories = new ArrayList<>(dataDAO.getCategoriesUpdate(this).values());
        categoriesTableModel.setData(categories);
        categoriesTableModel.fireTableDataChanged();
    }

    public void setSubject(DataObservable dataObservable) {
        this.dataDAO = dataObservable;
    }

    /*============================== INNER CLASS ==============================*/
    class SummaryCategoriesTableModel extends AbstractTableModel {
        private ArrayList<Category> categories;
        private final String[] headers = {"Name", "Total Amount"};

        public void setData(ArrayList<Category> categories) { this.categories = categories; }

        public String getColumnName(int column) { return headers[column]; }

        public int getRowCount() { return categories.size(); }

        public int getColumnCount() { return headers.length; }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Category category = categories.get(rowIndex);
            switch (columnIndex) {
                case 0: return category.getName();
                case 1: return Style.currencyFormat().format(category.getTotalAmount());
            }

            return null;
        }
    }
}
