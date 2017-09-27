package application.view.builderFactory;

import application.view.customComponents.BaseForm;
import application.view.customComponents.FormButton;
import application.view.customComponents.FormLabel;
import application.view.customComponents.FormTextField;
import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;

public class SummaryOutputForm extends BaseForm implements FormFactory {

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
        SummaryCategoriesTableModel categoriesTableModel = new SummaryCategoriesTableModel();
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

    /*============================== INNER CLASS ==============================*/
    class SummaryCategoriesTableModel extends AbstractTableModel {

        public int getRowCount() {
            return 0;
        }

        public int getColumnCount() {
            return 0;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;
        }
    }
}
