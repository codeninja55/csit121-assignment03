package application.controller;

import application.model.DataDAO;
import application.model.Shop;
import application.model.card.*;
import application.model.category.Category;
import application.model.purchase.Purchase;
import application.model.purchase.PurchaseType;
import application.model.purchase.SortPurchaseType;
import application.view.*;
import application.view.builderFactory.*;
import application.view.SummaryViewPane;
import application.view.customComponents.ProgressDialog;
import application.view.customComponents.ResultsPane;
import styles.IconFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Program {
    private final Shop shop;
    private final DataDAO db;
    private final MainFrame mainFrame;
    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;
    private final SummaryViewPane summaryViewPane;
    private ProgressDialog progressDialog;

    public Program() {
        /* Singleton Design Pattern - Only one instance of Shop available */
        shop = Shop.getShopInstance();
        db = shop.getDataStore();
        db.readData();

        this.mainFrame = new MainFrame();
        this.progressDialog = new ProgressDialog(mainFrame);

        this.tabPane = mainFrame.getTabPane();

        /* DataObserver Design Pattern - Registration and initial update calls */
        this.cardViewPane = mainFrame.getCardViewPane();
        db.register(cardViewPane);
        cardViewPane.setSubject(db);
        cardViewPane.update();

        this.purchaseViewPane = mainFrame.getPurchaseViewPane();
        db.register(purchaseViewPane);
        purchaseViewPane.setSubject(db);
        purchaseViewPane.update();

        this.categoriesViewPane = mainFrame.getCategoriesViewPane();
        db.register(categoriesViewPane);
        categoriesViewPane.setSubject(db);
        categoriesViewPane.update();

        this.summaryViewPane = mainFrame.getSummaryViewPane();
        db.register(summaryViewPane);
        summaryViewPane.setSubject(db);
        summaryViewPane.update();
        db.register(summaryViewPane.getAnalyticsPane());
        summaryViewPane.getAnalyticsPane().setSubject(db);
        summaryViewPane.getAnalyticsPane().update();

        tabPane.addChangeListener(e -> {
            if (tabPane.getSelectedComponent() != purchaseViewPane) removePurchaseForms();
            if (tabPane.getSelectedComponent() != cardViewPane) removeCardForms();
            if (tabPane.getSelectedComponent() != categoriesViewPane) removeCategoryForms();
            if (tabPane.getSelectedComponent() != summaryViewPane)
                summaryViewPane.getFilterForm().setVisible(false);
        });

        setupMainFrameHandlers();
        setupCardViewHandlers();
        setupPurchaseViewHandlers();
        setupCategoriesViewHandlers();
        setupSummaryViewHandlers();
    }

    /*============================== REGISTER AND HANDLE EVENTS ==============================*/
    /*============================== MAIN FRAME HANDLERS ==============================*/
    private void setupMainFrameHandlers() {
        mainFrame.setSaveListener(e -> {
            progressDialog.setVisible(true);
            db.writeData();
            progressDialog.setVisible(false);
        });

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exitApplication();
            }
        });

        mainFrame.setExitListener(e -> exitApplication());
    }

    /*============================== CARD VIEW HANDLERS ==============================*/
    private void setupCardViewHandlers() {
        /*TOOLBAR | CREATE CARD BUTTON*/
        cardViewPane.setCreateCardListener(() -> {
            removeCardForms();
            CardForm form = FormFactory.createCardForm();
            cardViewPane.setCardForm(form);

            // ADD A CREATE BUTTON LISTENER AFTER CREATING FORM
            form.setCardListener(e -> {
                final String cardID = e.getCardID();
                final String type = e.getCardType();
                final String name = e.getCardName();
                final String email = e.getCardEmail();
                HashMap<String, String> newCard = new HashMap<>();

                assert type != null;
                if (type.equals(CardType.AnonCard.getName())) {
                    newCard.put("name", null);
                    newCard.put("email", null);
                    newCard.put("cardType", CardType.AnonCard.getName());
                } else {
                    newCard.put("name", name);
                    newCard.put("email", email);
                    if (type.equals(CardType.BasicCard.getName()))
                        newCard.put("cardType", CardType.BasicCard.getName());
                    else if (type.equals(CardType.PremiumCard.getName()))
                        newCard.put("cardType", CardType.PremiumCard.getName());
                }
                shop.makeCard(newCard);
                removeCardForms();
                showResults(cardViewPane, printCard(cardID, "CARD ADDED"));
            });
        });

        /*TOOLBAR | DELETE CARD BUTTON*/
        cardViewPane.setDeleteCardListener(() -> {
            removeCardForms();
            DeleteCardForm form = FormFactory.deleteCardForm();
            cardViewPane.setDeleteForm(form);

            // REGISTER A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteCardListener(e -> {
                final String cardID = e.getID();
                String[] btnOptions = {"Yes","No"};
                String message = "Are you sure you want to DELETE card: " + cardID + "\nThis cannot be undone."
                        + "\n\nAll purchases for this card will be changed to CASH status.\n\n";

                showResults(cardViewPane, printCard(cardID, "DELETE CARD?"));
                int confirm = JOptionPane.showOptionDialog(mainFrame, // frame, can be null
                        message, // message
                        "Confirm Delete?", // title
                        JOptionPane.OK_CANCEL_OPTION, // button options
                        JOptionPane.WARNING_MESSAGE, // icon
                        null, // do not use custom icon
                        btnOptions, // title of buttons
                        btnOptions[1] // default button title
                );

                if (confirm == JOptionPane.OK_OPTION) {
                    shop.deleteCard(cardID);
                    // Purchases by this card will be changed to cash
                    shop.convertPurchase(cardID);
                    removeCardForms();
                } else {
                    removeCardForms();
                }
            });
        });

        /*TOOLBAR | SEARCH BUTTON*/
        cardViewPane.setSearchCardListener(() -> {
            removeCardForms();
            SearchCardForm form = FormFactory.searchCardForm();
            cardViewPane.setSearchCardForm(form);

            // ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            form.setSearchListener(e -> {
                final String cardID = e.getID();
                showResults(cardViewPane, printCard(cardID,"CARD FOUND"));
            });
        });

        /*TOOLBAR | VIEW BUTTON*/
        cardViewPane.setViewCardListener(() -> {
            if (cardViewPane.getCardsTable().getSelectedRow() >= 0) {
                removeCardForms();
                int selectedRow = cardViewPane.getCardsTable().getSelectedRow();
                final String cardID = (String)cardViewPane.getCardsTable().getValueAt(selectedRow, 0);
                showResults(cardViewPane, printCard(cardID,"CARD"));
            }
        });

        /*TOOLBAR | SORT COMBOBOX*/
        cardViewPane.getSortedCombo().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Card> sortedCardsList = new ArrayList<>();
                sortedCardsList.addAll(db.getAllCards().values());
                if (e.getItem().equals("Sort..") || e.getItem().equals(SortCardType.CreatedOrder.getName())) {
                    // Lambda version of sorting with Comparator comparing method
                    sortedCardsList.sort(Comparator.comparing(Card::getID));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.ReverseCreatedOrder.getName())) {
                    // Lambda version of sorting with Comparator
                    sortedCardsList.sort((Card c1, Card c2) -> c2.getID().compareTo(c1.getID()));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.Name.getName())) {
                    sortedCardsList.sort(((c1, c2) -> {
                        if (c1 instanceof AdvancedCard && c2 instanceof AdvancedCard)
                            return ((AdvancedCard) c1).getName().compareTo(((AdvancedCard) c2).getName());

                        return (c1 instanceof AnonCard && c2 instanceof AdvancedCard) ? -1 : 1;
                    }));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.Points.getName())) {
                    // Lambda version of sorting with Comparator comparingDouble method
                    sortedCardsList.sort(Comparator.comparingDouble(Card::getPoints).reversed());
                    cardViewPane.updateTableData(sortedCardsList);
                }
            }
        });
    }

    /*============================== PURCHASE VIEW HANDLERS ==============================*/
    private void setupPurchaseViewHandlers() {
        /*TOOLBAR | CREATE BUTTON*/
        purchaseViewPane.setCreatePurchaseListener(() -> {
            removePurchaseForms();
            PurchaseForm form = FormFactory.createPurchaseForm(new ArrayList<>(db.getAllCards().values()),
                    new ArrayList<>(db.getAllCategories().values()));
            purchaseViewPane.setCreatePurchaseForm(form);

            // FORM CREATE BUTTON
            form.setCreatePurchaseListener(v -> {
                final CardType cardType = v.getCardType();
                final String purchaseType = v.getPurchaseType();
                final int receiptID = v.getReceiptID();
                String cardID = (v.getCardID() == null || v.getCardID().isEmpty()) ? v.getExistingCardID() : v.getCardID() ;
                HashMap<Integer, Category> categories = new HashMap<>(v.getCategories());
                String resultsText = "";

                if (purchaseType.equals(PurchaseType.ExistingCardPurchase.getName())) {
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getCard(cardID).toString() + db.getPurchase(receiptID).toString();
                } else if (purchaseType.equals(PurchaseType.CashPurchase.getName())) {
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getPurchase(receiptID).toString();
                } else if (purchaseType.equals(PurchaseType.NewCardPurchase.getName())) {
                    String name = (!cardType.equals(CardType.AnonCard)) ? v.getCardName() : null;
                    String email = (!cardType.equals(CardType.AnonCard)) ? v.getCardEmail() : null;

                    HashMap<String, String> newCard = new HashMap<>();
                    newCard.put("name", name);
                    newCard.put("email", email);
                    newCard.put("cardType", cardType.getName());
                    newCard.put("cardID", cardID);

                    shop.makeCard(newCard);
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getCard(cardID).toString() + db.getPurchase(receiptID).toString();
                }
                removePurchaseForms();
                showResults(purchaseViewPane, resultsText);
            });
        });

        /*TOOLBAR | VIEW  BUTTON*/
        purchaseViewPane.setViewPurchaseListener(() -> {
            if (purchaseViewPane.getPurchasesTable().getSelectedRow() >= 0) {
                int selectedRow = purchaseViewPane.getPurchasesTable().getSelectedRow();
                final int receiptID = (Integer)purchaseViewPane.getPurchasesTable().getValueAt(selectedRow, 0);

                String resultsText = db.getAllPurchases().values().stream().filter(p -> p.getReceiptID() == receiptID)
                        .map(Purchase::toString).collect(Collectors.joining("\n"));

                showResults(purchaseViewPane, resultsText);
            }
        });

        /*TOOLBAR | SORT COMBOBOX*/
        purchaseViewPane.getSortPurchaseCombo().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Purchase> purchasesList = new ArrayList<>(db.getAllPurchases().values());
                if (e.getItem().equals(SortPurchaseType.All.getName())) {
                    purchaseViewPane.update();
                } else if (e.getItem().equals(SortPurchaseType.Card.getName())) {
                    purchaseViewPane.sortPurchaseTableMode(purchasesList.stream()
                            .filter(c -> !c.getCardType().equals(CardType.Cash.getName()))
                            .collect(Collectors.toCollection(ArrayList::new))); // Negative cash validation
                } else if (e.getItem().equals(SortPurchaseType.Cash.getName())) {
                    purchaseViewPane.sortPurchaseTableMode(purchasesList.stream()
                            .filter(c -> c.getCardType().equals(CardType.Cash.getName()))
                            .collect(Collectors.toCollection(ArrayList::new))); // Positive cash validation
                }
            }
        });
    }

    /*=========================== CATEGORIES VIEW HANDLERS ===========================*/
    private void setupCategoriesViewHandlers() {
        /*TOOLBAR | CREATE CATEGORY BUTTON*/
        categoriesViewPane.setCreateCategoryListener(() -> {
            removeCategoryForms();
            CategoriesForm form = FormFactory.createCategoryForm();
            categoriesViewPane.setCreateCategoryForm(form);

            // ADD A CREATE BUTTON LISTENER AFTER CREATING FORM
            form.setCreateCategoryListener(e -> {
                shop.makeCategory(new Category(e.getCategoryName(), e.getDescription()));
                removeCategoryForms();
            });
        });

        /*TOOLBAR | DELETE CATEGORY BUTTON*/
        categoriesViewPane.setDeleteCategoryListener(() -> {
            removeCategoryForms();
            DeleteCategoryForm form = FormFactory.deleteCategoryForm();
            categoriesViewPane.setDeleteCategoryForm(form);

            //ADD A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteCardListener(e -> {
                final int categoryID = Integer.parseInt(e.getID());

                String[] btnOptions = {"Yes","Cancel"};
                String message = "Are you sure you want to DELETE Category: " + e.getID() + "\nThis cannot be undone."
                        + "\n\nAll purchases for this category will be moved to Other category.\n\n";

                int confirm = JOptionPane.showOptionDialog(mainFrame, // frame, can be null
                        message, // message
                        "Confirm Delete?", // title
                        JOptionPane.OK_CANCEL_OPTION, // button options
                        JOptionPane.WARNING_MESSAGE, // icon
                        null, // do not use custom icon
                        btnOptions, // title of buttons
                        btnOptions[1] // default button title
                );

                if (confirm == JOptionPane.OK_OPTION) { shop.deleteCategory(categoryID); }
                removeCategoryForms();
            });
        });
    }

    /*============================== SUMMARY VIEW HANDLERS ==============================*/
    private void setupSummaryViewHandlers() {
        /*summaryViewPane.setRefreshListener((SummaryView e) -> {
            ArrayList<Category> clonedCategories = db.getAllCategories().values().stream().map(Category::new)
                    .collect(Collectors.toCollection(ArrayList::new));

            Predicate<Purchase> hoursPredicate = p -> (p.getPurchaseTime().getHour() == e.getHoursOption());
            Predicate<Purchase> daysPredicate = p -> (p.getPurchaseTime().getDayOfWeek().getValue() == e.getDaysOption());

            if (e.getTableOption().equals("Categories")) {
                if (e.getDaysOption() != 0 && e.getHoursOption() < 24) {
                    summaryViewPane.getCategoryTableModel().setData(summaryByDayAndHour(clonedCategories, daysPredicate, hoursPredicate));
                } else if (e.getDaysOption() != 0 && e.getHoursOption() == 24) {
                    summaryViewPane.getCategoryTableModel().setData(summaryByDay(clonedCategories, daysPredicate));
                } else if (e.getDaysOption() == 0 && e.getHoursOption() < 24) {
                    summaryViewPane.getCategoryTableModel().setData(summaryByHours(clonedCategories, hoursPredicate));
                } else {
                    // default option
                    summaryViewPane.getCategoryTableModel().setData(new ArrayList<>(db.getAllCategories().values()));
                }

                summaryViewPane.revalidate();
                summaryViewPane.repaint();
            }
        });*/

        summaryViewPane.setAnalyticsListener(() -> {
            SummaryFilterForm filterForm = summaryViewPane.getFilterForm();
            filterForm.setVisible(true);

            filterForm.setListener(e -> {
                // TODO
                System.out.println("Filtering Not yet Impl");
            });
        });
    }

    /*============================== MUTATORS  ==============================*/
    private void showResults(JPanel viewPane, String resultsText) {
        ResultsPane resultsPane = new ResultsPane.ResultsPaneBuilder(resultsText).build();
        viewPane.add(resultsPane, BorderLayout.EAST);
        resultsPane.setVisible(true);
    }

    /*==================== REMOVING FORMS METHODS ====================*/
    // These methods all remove forms from their respective panes when they're not needed anymore
    // NOTE: All forms have a Component Listener that removes itself from the Parent if
    // the component is set to hidden.
    private void removeCardForms() {
        Arrays.stream(cardViewPane.getComponents()).filter(c -> c instanceof FormFactory || c instanceof ResultsPane)
                .forEach(c -> c.setVisible(false));
    }

    private void removePurchaseForms() {
        Arrays.stream(purchaseViewPane.getComponents()).filter(c -> c instanceof FormFactory || c instanceof ResultsPane)
                .forEach(c -> c.setVisible(false));
    }

    private void removeCategoryForms() {
        Arrays.stream(categoriesViewPane.getComponents()).filter(c -> c instanceof FormFactory)
                .forEach(c -> c.setVisible(false));
    }

    /*============================== ACCESSORS ==============================*/
    private String printCard(String cardID, String title) {
        String cText = db.getCard(cardID).toString();
        String pText = db.getAllPurchases().values().stream().filter(p -> p.getCardID() != null && p.getCardID().equals(cardID))
                .map(Purchase::toString).collect(Collectors.joining("\n"));
        return String.format("%s%n%s%n%s%n%s", title, cText,"PURCHASE(S)", pText);
    }

    /*============================== SUMMARY FILTER METHODS ==============================*/
    private ArrayList<Category> summaryByDay(ArrayList<Category> categories, Predicate<Purchase> daysPredicate) {
        categories.forEach((Category c) -> {
            double newTotal = db.getAllPurchases().values().stream().filter(daysPredicate)
                    .mapToDouble(p -> p.getCategories().get(c.getId()).getAmount()).sum();
            c.updateTotalAmount(newTotal);
        });

        return categories;
    }

    private ArrayList<Category> summaryByHours(ArrayList<Category> categories, Predicate<Purchase> hoursPredicate) {
        categories.forEach(c -> {
            double newTotal = db.getAllPurchases().values().stream().filter(hoursPredicate)
                    .mapToDouble(p -> p.getCategories().get(c.getId()).getAmount()).sum();
            c.updateTotalAmount(newTotal);
        });

        return categories;
    }

    private ArrayList<Category> summaryByDayAndHour(ArrayList<Category> categories, Predicate<Purchase> daysPredicate,
                                                    Predicate<Purchase> hoursPredicate) {
        categories.forEach(c -> {
            double newTotal = db.getAllPurchases().values().stream().filter(daysPredicate.and(hoursPredicate))
                    .mapToDouble(p -> p.getCategories().get(c.getId()).getAmount()).sum();
            c.updateTotalAmount(newTotal);
        });

        return categories;
    }

    private void exitApplication() {
        String[] options = {"Save and Exit", "Exit without Save", "Cancel Exit"};
        int response = JOptionPane.showOptionDialog(null,
                "\nWould you like to save your session data before exiting?\n\n\n\n",
                "Save on Exit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                IconFactory.warningIcon(), options, options[0]);

        if (response == 0) {
            db.writeData();
            // Add a loading bar
        } else if (response == 1) {
            System.gc();
            System.exit(0);
        }
    }

}
