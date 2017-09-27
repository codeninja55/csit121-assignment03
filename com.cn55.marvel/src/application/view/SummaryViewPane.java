package application.view;

import application.model.DataObservable;
import application.model.DataObserver;
import application.view.builderFactory.FormFactory;
import application.view.builderFactory.SummaryFilterForm;
import application.view.builderFactory.SummaryOutputForm;
import application.view.customComponents.*;
import application.view.jtableModels.CardTableModel;
import application.view.jtableModels.CategoriesTableModel;
import application.view.jtableModels.PurchaseTableModel;
import styles.ColorFactory;
import styles.IconFactory;
import styles.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private SummaryOutputForm outputForm;
    private PurchaseTableModel purchasesTableModel;
    private JTable purchasesTable;
    private CardTableModel cardsTableModel;
    private JTable cardsTable;
    private ToolbarButtonListener analyticsListener;

    SummaryViewPane() {
        setLayout(new BorderLayout());
        Toolbar toolbar = new Toolbar();
        ToolbarButton analyticsBtn = new ToolbarButton("Filter Data", IconFactory.analyticsIcon());

        outputForm = FormFactory.createOutputForm();
        purchasesTableModel = new PurchaseTableModel();
        purchasesTable = new JTable(purchasesTableModel);
        Style.purchasesTableFormatter(purchasesTable);
        cardsTableModel = new CardTableModel();
        cardsTable = new JTable(cardsTableModel);
        Style.cardTableFormatter(cardsTable);

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
        add(outputForm, BorderLayout.EAST);
        outputForm.setVisible(true);

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

    public void setSummaryForms(SummaryFilterForm filterForm) {
        this.add(filterForm, BorderLayout.WEST);
        filterForm.setVisible(true);
    }

    public void update() {
        purchasesTableModel.setData(new ArrayList<>(dataDAO.getPurchaseUpdate(this).values()));
        purchasesTableModel.fireTableDataChanged();
        cardsTableModel.setData(new ArrayList<>(dataDAO.getCardsUpdate(this).values()));
        cardsTableModel.fireTableDataChanged();
        // TODO - Add these to the form
        /*LocalDate firstPurchaseDate = dataDAO.getFirstPurchaseDate(this).toLocalDate();
        dateBeginPicker.setDate(firstPurchaseDate);
        LocalDate lastPurchaseDate = dataDAO.getLastPurchaseDate(this).toLocalDate();
        dateEndPicker.setDate(lastPurchaseDate);*/
    }

    public void setSubject(DataObservable dataObservable) { this.dataDAO = dataObservable; }

    /*============================== ACCESSORS ==============================*/
    public SummaryOutputForm getOutputForm() { return outputForm; }
}
