package application.view;
import application.model.DataObservable;
import application.model.DataObserver;
import application.model.categoryModel.Category;
import application.view.customComponents.Style;
import application.view.customComponents.Toolbar;
import application.view.customComponents.ToolbarButton;
import application.view.jtableModels.CardTableModel;
import application.view.jtableModels.CategoriesTableModel;
import application.view.jtableModels.PurchaseTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;

    private CategoriesTableModel categoriesTableModel;
    private JTable categoriesTable;
    private PurchaseTableModel purchasesTableModel;
    private JTable purchasesTable;
    private CardTableModel cardsTableModel;
    private JTable cardsTable;

    SummaryViewPane() {
        setLayout(new BorderLayout());
        Toolbar toolbar = new Toolbar();
        ToolbarButton refreshTableBtn = new ToolbarButton("Refresh", Style.refreshIcon());

        categoriesTableModel = new CategoriesTableModel();
        categoriesTable = new JTable(categoriesTableModel);
        Style.categoriesTableFormatter(categoriesTable);
        purchasesTableModel = new PurchaseTableModel();
        purchasesTable = new JTable(purchasesTableModel);
        Style.purchasesTableFormatter(purchasesTable);
        cardsTableModel = new CardTableModel();
        cardsTable = new JTable(cardsTableModel);
        Style.cardTableFormatter(cardsTable);

        /* TABLE VIEW COMBO BOX */
        String[] tableOptions = {"Categories", "Purchases", "Cards"};
        JComboBox<String> tableViewCombo = new JComboBox<>(tableOptions);
        tableViewCombo.setPreferredSize(refreshTableBtn.getPreferredSize());
        tableViewCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Style.blueGrey500()));
        tableViewCombo.setSelectedIndex(0);

        /* TOOLBAR */
        // Date Picker FROM
        // Date Picker TO
        // Days Slider
        // Time of day slider
        toolbar.getRightToolbar().add(refreshTableBtn);
        toolbar.getRightToolbar().add(tableViewCombo);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(categoriesTable), BorderLayout.CENTER);

        tableViewCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Arrays.stream(super.getComponents()).filter(c -> c instanceof JScrollPane).forEach(this::remove);
                if (e.getItem().equals("Categories")) {
                    super.add(new JScrollPane(categoriesTable), BorderLayout.CENTER);
                } else if (e.getItem().equals("Purchases")) {
                    super.add(new JScrollPane(purchasesTable), BorderLayout.CENTER);
                } else if (e.getItem().equals("Cards")) {
                    super.add(new JScrollPane(cardsTable), BorderLayout.CENTER);
                }
                super.revalidate();
                super.repaint();
            }
        });
    }

    /*============================== MUTATORS ==============================*/
    public void update() {
        categoriesTableModel.setData(new ArrayList<>(dataDAO.getCategoriesUpdate(this).values()));
        categoriesTableModel.fireTableDataChanged();
        purchasesTableModel.setData(new ArrayList<>(dataDAO.getPurchaseUpdate(this).values()));
        purchasesTableModel.fireTableDataChanged();
        cardsTableModel.setData(new ArrayList<>(dataDAO.getCardsUpdate(this).values()));
        cardsTableModel.fireTableDataChanged();
    }

    public void setSubject(DataObservable dataObservable) { this.dataDAO = dataObservable; }
}
