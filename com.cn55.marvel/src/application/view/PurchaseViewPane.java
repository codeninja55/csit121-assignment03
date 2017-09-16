package application.view;

import application.model.DataObservable;
import application.model.DataObserver;
import application.model.PurchaseModel.Purchase;
import application.model.PurchaseModel.SortPurchaseType;
import application.view.CustomComponents.*;
import application.view.FormFactory.PurchaseForm;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class PurchaseViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private final ToolbarButton createPurchaseBtn;
    private final ToolbarButton deletePurchaseBtn;
    private final ToolbarButton summaryBtn;
    private final JComboBox<String> sortPurchaseCombo;
    private final ToolbarButton viewPurchaseBtn;

    private final PurchaseTableModel purchaseTableModel;
    private final JTable purchaseTablePane;
    private PurchaseForm createPurchaseForm;
    private final ResultsPane resultsPane;

    private ToolbarButtonListener createPurchaseListener;
    //private ToolbarButtonListener deletePurchaseListener;
    private ToolbarButtonListener summaryListener;
    private ToolbarButtonListener viewPurchaseListener;

    /*============================== CONSTRUCTORS ==============================*/
    PurchaseViewPane() {
        Toolbar toolbar = new Toolbar();
        createPurchaseBtn = new ToolbarButton("Create", Style.createIcon());
        deletePurchaseBtn = new ToolbarButton("Delete", Style.deleteIconDisabled());
        summaryBtn = new ToolbarButton("Summary", Style.summaryIcon());
        viewPurchaseBtn = new ToolbarButton("View", Style.viewIcon());
        sortPurchaseCombo = new JComboBox<>();
        DefaultComboBoxModel<String> options = new DefaultComboBoxModel<>();

        purchaseTableModel = new PurchaseTableModel();
        purchaseTablePane = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(purchaseTablePane);

        resultsPane = new ResultsPane("PurchaseViewResultsPane");

        JPopupMenu tablePopup = new JPopupMenu();
        JMenuItem removePurchase = new JMenuItem("Delete Purchase");
        setLayout(new BorderLayout());

        /* Sort Purchases Combo Setup */
        options.addElement(SortPurchaseType.All.getName());
        options.addElement(SortPurchaseType.Card.getName());
        options.addElement(SortPurchaseType.Cash.getName());
        sortPurchaseCombo.setModel(options);
        sortPurchaseCombo.setSize(createPurchaseBtn.getPreferredSize());
        sortPurchaseCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Style.blue500()));
        sortPurchaseCombo.setSelectedIndex(0);

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createPurchaseBtn);
        toolbar.getLeftToolbar().add(deletePurchaseBtn);
        toolbar.getLeftToolbar().add(summaryBtn);
        deletePurchaseBtn.setBackground(Style.blueGrey800());
        deletePurchaseBtn.setEnabled(false);
        toolbar.getRightToolbar().add(viewPurchaseBtn);
        toolbar.getRightToolbar().add(sortPurchaseCombo);
        add(toolbar, BorderLayout.NORTH);

        tablePopup.add(removePurchase);

        add(tableScrollPane, BorderLayout.CENTER);
        add(resultsPane, BorderLayout.EAST);

        /* REGISTRATION OF TOOLBAR BUTTON LISTENERS */
        ToolbarListener handler = new ToolbarListener();
        createPurchaseBtn.addActionListener(handler);
        deletePurchaseBtn.addActionListener(handler);
        summaryBtn.addActionListener(handler);
        viewPurchaseBtn.addActionListener(handler);
    }

    /*============================== MUTATORS ==============================*/
    public void setCreatePurchaseListener(ToolbarButtonListener listener) {
        this.createPurchaseListener = listener;
    }

    //public void setDeletePurchaseListener(ToolbarButtonListener listener) {
    //    this.deletePurchaseListener = listener;
    //}

    public void setSummaryListener(ToolbarButtonListener listener) {
        this.summaryListener = listener;
    }

    public void setViewPurchaseListener(ToolbarButtonListener listener) {
        this.viewPurchaseListener = listener;
    }

    public void setCreatePurchaseForm(PurchaseForm createPurchaseForm) {
        this.createPurchaseForm = createPurchaseForm;
        this.add(createPurchaseForm, BorderLayout.WEST);
        createPurchaseForm.setVisible(true);
    }

    private void purchasesTableFormatter() {
        // Formatting for the table where it renders the text.
        purchaseTablePane.setRowHeight(45);
        purchaseTablePane.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        purchaseTablePane.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        purchaseTablePane.getColumnModel().getColumn(2).setCellRenderer(Style.centerRenderer());
        purchaseTablePane.getColumnModel().getColumn(3).setCellRenderer(Style.rightRenderer());
        purchaseTablePane.getColumnModel().getColumn(4).setCellRenderer(Style.centerRenderer());
    }

    public void setPurchaseTableModel() {
        purchaseTablePane.setModel(purchaseTableModel);
        purchaseTablePane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        purchasesTableFormatter();
        this.revalidate();
        this.repaint();
    }

    public void sortPurchaseTableMode(ArrayList<Purchase> purchases) {
        purchaseTableModel.setData(purchases);
        purchaseTableModel.fireTableDataChanged();
    }

    /* OBSERVER DESIGN PATTERN - DATA STORE OBSERVING IMPLEMENTATION */
    public void update() {
        ArrayList<Purchase> allPurchases = new ArrayList<>();
        allPurchases.addAll(dataDAO.getPurchaseUpdate(this).values());
        purchaseTableModel.setData(allPurchases);
        purchaseTableModel.fireTableDataChanged();
    }

    public void setSubject(DataObservable dataObservable) {
        this.dataDAO = dataObservable;
    }

    /*============================== ACCESSORS ==============================*/
    public PurchaseForm getCreatePurchaseForm() {
        if (createPurchaseForm != null) return createPurchaseForm;
        else return null;
    }

    public JComboBox<String> getSortPurchaseCombo() {
        return sortPurchaseCombo;
    }

    public JTable getPurchaseTablePane() {
        return purchaseTablePane;
    }

    public ResultsPane getResultsPane() {
        return resultsPane;
    }

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/
    /*============================ TOOLBAR LISTENER ===========================*/
    public class ToolbarListener extends MouseAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createPurchaseBtn) {
                if (createPurchaseListener != null) createPurchaseListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == viewPurchaseBtn) {
                if (viewPurchaseListener != null) viewPurchaseListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == deletePurchaseBtn) {
                // TODO - TEST CODE
                System.err.println("Purchase View Delete Purchase Not Impl Yet");
                System.out.println("Delete Purchase Button Pressed");
            } else if (e.getSource() == summaryBtn)
                if (summaryListener != null) summaryListener.toolbarButtonEventOccurred();
        }
    }

    /*============================= CardTableModel ============================*/
    public class PurchaseTableModel extends AbstractTableModel {

        private ArrayList<Purchase> purchases;
        private final String[] tableHeaders = {"Receipt ID","Card ID", "Card Type",
                "Total Amount","Purchase Time"};

        void setData (ArrayList<Purchase> purchases) { this.purchases = purchases; }

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
                    return purchase.getPurchaseTime();
            }
            return null;
        }
    }
}
