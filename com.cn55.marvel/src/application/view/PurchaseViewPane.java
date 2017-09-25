package application.view;

import application.model.DataObservable;
import application.model.DataObserver;
import application.model.purchaseModel.Purchase;
import application.model.purchaseModel.SortPurchaseType;
import application.view.builderFactory.PurchaseForm;
import application.view.customComponents.Style;
import application.view.customComponents.Toolbar;
import application.view.customComponents.ToolbarButton;
import application.view.customComponents.ToolbarButtonListener;
import application.view.jtableModels.PurchaseTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

@SuppressWarnings("UnnecessaryLocalVariable")
public class PurchaseViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private final ToolbarButton createPurchaseBtn;
    private final ToolbarButton deletePurchaseBtn;
    private final ToolbarButton summaryBtn;
    private final JComboBox<String> sortPurchaseCombo;
    private final ToolbarButton viewPurchaseBtn;
    private final PurchaseTableModel purchaseTableModel;
    private final JTable purchasesTable;
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
        purchasesTable = new JTable(purchaseTableModel);
        Style.purchasesTableFormatter(purchasesTable);

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
        deletePurchaseBtn.setBackground(Style.blueGrey700());
        deletePurchaseBtn.setEnabled(false);
        toolbar.getRightToolbar().add(viewPurchaseBtn);
        toolbar.getRightToolbar().add(sortPurchaseCombo);
        add(toolbar, BorderLayout.NORTH);

        tablePopup.add(removePurchase);

        add(new JScrollPane(purchasesTable), BorderLayout.CENTER);

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
        this.add(createPurchaseForm, BorderLayout.WEST);
        createPurchaseForm.setVisible(true);
    }

    public void sortPurchaseTableMode(ArrayList<Purchase> purchases) {
        purchaseTableModel.setData(purchases);
        purchaseTableModel.fireTableDataChanged();
    }

    /* OBSERVER DESIGN PATTERN - DATA STORE OBSERVING IMPLEMENTATION */
    public void update() {
        ArrayList<Purchase> allPurchases = new ArrayList<>(dataDAO.getPurchaseUpdate(this).values());
        purchaseTableModel.setData(allPurchases);
        purchaseTableModel.fireTableDataChanged();
    }

    public void setSubject(DataObservable dataObservable) {
        this.dataDAO = dataObservable;
    }

    /*============================== ACCESSORS ==============================*/
    public JComboBox<String> getSortPurchaseCombo() {
        return sortPurchaseCombo;
    }

    public JTable getPurchasesTable() {
        return purchasesTable;
    }

    /*============================== INNER CLASS ==============================*/
    /*============================ TOOLBAR LISTENER ===========================*/
    public class ToolbarListener extends MouseAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createPurchaseBtn) {
                if (createPurchaseListener != null)
                    createPurchaseListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == viewPurchaseBtn) {
                if (viewPurchaseListener != null)
                    viewPurchaseListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == deletePurchaseBtn) {
                // TODO - TEST CODE
                System.err.println("Purchase View Delete Purchase Not Impl Yet");
                System.out.println("Delete Purchase Button Pressed");
            } else if (e.getSource() == summaryBtn)
                if (summaryListener != null)
                    summaryListener.toolbarButtonEventOccurred();
        }
    }
}
