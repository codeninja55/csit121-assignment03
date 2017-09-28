package application.view.summary;

import application.model.DataObservable;
import application.model.DataObserver;
import application.model.card.AdvancedCard;
import application.model.card.Card;
import application.model.card.CardType;
import application.model.category.Category;
import application.model.purchase.Purchase;
import application.view.custom_components.BaseForm;
import application.view.custom_components.FormButton;
import application.view.custom_components.FormLabel;
import application.view.custom_components.FormTextField;
import application.view.form_builder_factory.FormFactory;
import styles.ColorFactory;
import styles.FontFactory;
import styles.FormatterFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SummaryAnalyticsPane extends BaseForm implements FormFactory, DataObserver {
    private DataObservable dataDAO;
    private SummaryCategoriesTableModel categoriesTableModel;
    private FormTextField totalPurchasesMadeTextField;
    private FormTextField totalPurchasesTextField;
    private FormTextField cashPurchaseTextField;
    private FormTextField cardPurchasesTextField;
    private FormTextField totalCardsTextField;
    private FormTextField totalPointsTextField;
    private FormTextField totalBalanceTextField;

    SummaryAnalyticsPane() {
        super();
        super.setBorder("Analytics");
        setLayout(new GridLayout(2,1,0,10));
        Dimension dim = getPreferredSize();
        dim.width = 700;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());

        JPanel outputForm = new JPanel(new GridBagLayout());
        FormLabel purchasesLabel = new FormLabel("PURCHASES");
        FormLabel totalPurchasesMadeLabel = new FormLabel("Number of Purchases:");
        totalPurchasesMadeTextField = new FormTextField(20);
        FormLabel totalPurchasesLabel = new FormLabel("Total Amount:");
        totalPurchasesTextField = new FormTextField(20);
        FormLabel cashPurchasesLabel = new FormLabel("Cash Purchases:");
        cashPurchaseTextField = new FormTextField(20);
        FormLabel totalCardsLabel = new FormLabel("Number of Cards:");
        totalCardsTextField = new FormTextField(20);
        FormLabel cardPurchasesLabel = new FormLabel("Card Purchases:");
        cardPurchasesTextField = new FormTextField(20);
        FormLabel cardLabel = new FormLabel("CARDS");
        FormLabel totalPointsLabel = new FormLabel("Total Points:");
        totalPointsTextField = new FormTextField(20);
        FormLabel totalBalanceLabel = new FormLabel("Total Balance:");
        totalBalanceTextField = new FormTextField(20);
        FormLabel categoriesLabel = new FormLabel("CATEGORIES");
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
        outputForm.add(totalPurchasesMadeLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalPurchasesMadeTextField, gc);

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

        outputForm.add(new JSeparator(),gc);

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
        outputForm.add(totalCardsLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalCardsTextField, gc);

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

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalBalanceLabel, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        outputForm.add(totalBalanceTextField, gc);

        /*========== LAST ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 2;
        gc.gridx = 0; gc.gridy++; gc.weightx = 2; gc.weighty = 2;
        gc.insets = new Insets(60, 0,0,0);
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
                    c.setPreferredSize(new Dimension(300,50));
                    c.setBackground(ColorFactory.blueGrey200());
                    c.setForeground(ColorFactory.grey50());
                    c.setFont(FontFactory.labelFont());
                });

        Arrays.stream(outputForm.getComponents()).filter(c -> c instanceof FormLabel || c instanceof FormTextField)
                .forEach(c -> c.setVisible(true));

        add(outputForm);
        add(new JScrollPane(categoriesTable));
    }

    private void setPurchaseAnalytics(HashMap<Integer, Purchase> purchasesMap) {
        Predicate<Purchase> cashPredicate = e -> (e.getCardType().equals(CardType.Cash.name));

        DoubleSummaryStatistics purchaseStatistics = purchasesMap.values().stream().mapToDouble(Purchase::getCategoriesTotal)
                .summaryStatistics();
        totalPurchasesMadeTextField.setText(Double.toString(purchaseStatistics.getCount()));

        totalPurchasesTextField.setText(FormatterFactory.currencyFormat().format(purchaseStatistics.getSum()));

        double cashPurchases = purchasesMap.values().stream().filter(cashPredicate)
                .mapToDouble(Purchase::getCategoriesTotal).sum();
        cashPurchaseTextField.setText(FormatterFactory.currencyFormat().format(cashPurchases));

        double cardPurchases = purchasesMap.values().stream().filter(cashPredicate.negate())
                .mapToDouble(Purchase::getCategoriesTotal).sum();
        cardPurchasesTextField.setText(FormatterFactory.currencyFormat().format(cardPurchases));

    }

    private void setCardAnalytics(HashMap<String, Card> cardsMap) {
        DoubleSummaryStatistics cardsStatistics = cardsMap.values().stream().mapToDouble(Card::getPoints).summaryStatistics();
        double totalBalance = cardsMap.values().stream().filter(c -> c instanceof AdvancedCard)
                .mapToDouble(c -> ((AdvancedCard)c).getBalance()).sum();
        totalPointsTextField.setText(FormatterFactory.pointsFormat().format(cardsStatistics.getSum()));
        totalCardsTextField.setText(Double.toString(cardsStatistics.getCount()));
        totalBalanceTextField.setText(FormatterFactory.currencyFormat().format(totalBalance));
    }

    public void filterUpdate(HashMap<Integer, Category> categoriesMap, HashMap<Integer, Purchase> purchasesMap, HashMap<String, Card> cardsMap) {
        setPurchaseAnalytics(purchasesMap);
        setCardAnalytics(cardsMap);
        categoriesTableModel.setData(new ArrayList<>(categoriesMap.values()));
        categoriesTableModel.fireTableDataChanged();
    }

    public void update() {
        HashMap<Integer, Purchase> purchasesMap = new HashMap<>(dataDAO.getPurchaseUpdate(this));
        setPurchaseAnalytics(purchasesMap);
        HashMap<String, Card> cardsMap = new HashMap<>(dataDAO.getCardsUpdate(this));
        setCardAnalytics(cardsMap);
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

        void setData(ArrayList<Category> categories) { this.categories = categories; }

        public String getColumnName(int column) { return headers[column]; }

        public int getRowCount() { return (categories != null) ? categories.size() : 0; }

        public int getColumnCount() { return headers.length; }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Category category = categories.get(rowIndex);
            switch (columnIndex) {
                case 0: return category.getName();
                case 1: return FormatterFactory.currencyFormat().format(category.getTotalAmount());
            }
            return null;
        }
    }
}
