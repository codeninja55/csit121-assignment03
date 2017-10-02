package application.view.summary;

import application.model.dao.DataObservable;
import application.model.dao.DataObserver;
import application.model.card.Card;
import application.model.purchase.Purchase;
import application.view.custom.components.Toolbar;
import application.view.custom.components.ToolbarButton;
import application.view.custom.components.ToolbarButtonListener;
import application.view.formbuilder.factory.FormFactory;
import application.view.formbuilder.factory.SummaryFilterForm;
import application.view.table.models.CardTableModel;
import application.view.table.models.PurchaseTableModel;
import styles.ColorFactory;
import styles.IconFactory;
import styles.TableFormatterFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private final SummaryAnalyticsPane analyticsPane;
    private final SummaryFilterForm filterForm;
    private final PurchaseTableModel purchasesTableModel;
    private final JTable purchasesTable;
    private final CardTableModel cardsTableModel;
    private final JTable cardsTable;
    private ToolbarButtonListener analyticsListener;

    public SummaryViewPane() {
        setLayout(new BorderLayout());
        Toolbar toolbar = new Toolbar();
        ToolbarButton analyticsBtn = new ToolbarButton("Filter Data", IconFactory.analyticsIcon());

        analyticsPane = new SummaryAnalyticsPane();
        filterForm = FormFactory.createSummaryFilterForm(this, analyticsPane);
        purchasesTableModel = new PurchaseTableModel();
        purchasesTable = new JTable(purchasesTableModel);
        TableFormatterFactory.purchasesTableFormatter(purchasesTable);
        cardsTableModel = new CardTableModel();
        cardsTable = new JTable(cardsTableModel);
        TableFormatterFactory.cardTableFormatter(cardsTable);

        /* TABLE VIEW COMBO BOX */
        String[] tableOptions = {"Purchases", "Cards"};
        JComboBox<String> tableViewCombo = new JComboBox<>(tableOptions);
        tableViewCombo.setPreferredSize(analyticsBtn.getPreferredSize());
        tableViewCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, ColorFactory.blueGrey500()));
        tableViewCombo.setSelectedIndex(0);

        toolbar.getLeftToolbar().add(analyticsBtn);
        toolbar.getRightToolbar().add(tableViewCombo);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(purchasesTable), BorderLayout.CENTER);
        add(analyticsPane, BorderLayout.EAST);
        add(filterForm, BorderLayout.WEST);
        analyticsPane.setVisible(true);

        tableViewCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Arrays.stream(super.getComponents()).filter(c -> c instanceof JScrollPane).forEach(this::remove);
                if (e.getItem().equals("Purchases")) {
                    super.add(new JScrollPane(purchasesTable), BorderLayout.CENTER);
                } else if (e.getItem().equals("Cards")) {
                    super.add(new JScrollPane(cardsTable), BorderLayout.CENTER);
                }
                super.revalidate();
                super.repaint();
            }
        });

        analyticsBtn.addActionListener(e -> {
            if (analyticsListener != null) analyticsListener.toolbarButtonEventOccurred();
        });
    }

    /*============================== MUTATORS ==============================*/
    public void setAnalyticsListener(ToolbarButtonListener analyticsListener) { this.analyticsListener = analyticsListener; }

    public void filterTables(ArrayList<Purchase> purchases, ArrayList<Card> cards) {
        purchasesTableModel.setData(purchases);
        purchasesTableModel.fireTableDataChanged();
        cardsTableModel.setData(cards);
        cardsTableModel.fireTableDataChanged();
    }

    public void update() {
        purchasesTableModel.setData(new ArrayList<>(dataDAO.getPurchaseUpdate(this).values()));
        purchasesTableModel.fireTableDataChanged();
        cardsTableModel.setData(new ArrayList<>(dataDAO.getCardsUpdate(this).values()));
        cardsTableModel.fireTableDataChanged();
        LocalDate firstPurchaseDate = dataDAO.getFirstPurchaseDate(this).toLocalDate();
        LocalDate lastPurchaseDate = dataDAO.getLastPurchaseDate(this).toLocalDate();
        filterForm.setPurchaseDateBounds(firstPurchaseDate, lastPurchaseDate);
    }

    public void setSubject(DataObservable dataObservable) { this.dataDAO = dataObservable; }

    /*============================== ACCESSORS ==============================*/
    public SummaryFilterForm getFilterForm() { return filterForm; }

    public SummaryAnalyticsPane getAnalyticsPane() { return analyticsPane; }
}
