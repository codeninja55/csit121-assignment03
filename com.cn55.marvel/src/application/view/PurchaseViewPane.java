package application.view;

import application.model.dao.DataObservable;
import application.model.dao.DataObserver;
import application.model.purchase.Purchase;
import application.model.purchase.SortPurchaseType;
import application.view.formbuilder.factory.PurchaseForm;
import styles.ColorFactory;
import styles.IconFactory;
import styles.TableFormatterFactory;
import application.view.custom.components.Toolbar;
import application.view.custom.components.ToolbarButton;
import application.view.custom.components.ToolbarButtonListener;
import application.view.table.models.PurchaseTableModel;

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
    private final JComboBox<String> sortPurchaseCombo;
    private final ToolbarButton viewPurchaseBtn;
    private final PurchaseTableModel purchaseTableModel;
    private final JTable purchasesTable;
    private ToolbarButtonListener createPurchaseListener;
    //private ToolbarButtonListener deletePurchaseListener;
    private ToolbarButtonListener viewPurchaseListener;

    /*============================== CONSTRUCTORS ==============================*/
    PurchaseViewPane() {
        setLayout(new BorderLayout());
        Toolbar toolbar = new Toolbar();
        createPurchaseBtn = new ToolbarButton("Create", IconFactory.createIcon());
        deletePurchaseBtn = new ToolbarButton("Delete", IconFactory.deleteIconDisabled());
        viewPurchaseBtn = new ToolbarButton("View", IconFactory.viewIcon());
        sortPurchaseCombo = new JComboBox<>();
        DefaultComboBoxModel<String> options = new DefaultComboBoxModel<>();

        purchaseTableModel = new PurchaseTableModel();
        purchasesTable = new JTable(purchaseTableModel);
        TableFormatterFactory.purchasesTableFormatter(purchasesTable);

        /* Sort Purchases Combo Setup */
        options.addElement(SortPurchaseType.All.name);
        options.addElement(SortPurchaseType.Card.name);
        options.addElement(SortPurchaseType.Cash.name);
        sortPurchaseCombo.setModel(options);
        sortPurchaseCombo.setSize(createPurchaseBtn.getPreferredSize());
        sortPurchaseCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, ColorFactory.blue500()));
        sortPurchaseCombo.setSelectedIndex(0);

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createPurchaseBtn);
        toolbar.getLeftToolbar().add(deletePurchaseBtn);
        deletePurchaseBtn.setBackground(ColorFactory.blueGrey700());
        deletePurchaseBtn.setEnabled(false);
        toolbar.getRightToolbar().add(viewPurchaseBtn);
        toolbar.getRightToolbar().add(sortPurchaseCombo);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(purchasesTable), BorderLayout.CENTER);

        /* REGISTRATION OF TOOLBAR BUTTON LISTENERS */
        ToolbarListener handler = new ToolbarListener();
        createPurchaseBtn.addActionListener(handler);
        deletePurchaseBtn.addActionListener(handler);
        viewPurchaseBtn.addActionListener(handler);
    }

    /*============================== MUTATORS ==============================*/
    public void setCreatePurchaseListener(ToolbarButtonListener listener) {
        this.createPurchaseListener = listener;
    }

    //public void setDeletePurchaseListener(ToolbarButtonListener listener) {
    //    this.deletePurchaseListener = listener;
    //}

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
        purchaseTableModel.setData(new ArrayList<>(dataDAO.getPurchaseUpdate(this).values()));
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
            }
        }
    }
}
