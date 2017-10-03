package application.controller;

import application.Shop;
import application.model.card.*;
import application.model.category.Category;
import application.model.dao.DataStoreDAO;
import application.model.purchase.Purchase;
import application.model.purchase.PurchaseType;
import application.model.purchase.SortPurchaseType;
import application.view.CardViewPane;
import application.view.CategoriesViewPane;
import application.view.MainFrame;
import application.view.PurchaseViewPane;
import application.view.custom.components.ProgressDialog;
import application.view.formbuilder.factory.*;
import application.view.summary.SummaryAnalyticsPane;
import application.view.summary.SummaryViewPane;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("SimplifyStreamApiCallChains")
public class Program {
    private final Shop shop;
    private final DataStoreDAO db;
    private final MainFrame mainFrame;
    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;
    private final SummaryViewPane summaryViewPane;

    public Program() {
        this.shop = Shop.getShopInstance();
        this.db = shop.getDataStore();

        this.mainFrame = new MainFrame();
        this.tabPane = mainFrame.getTabPane();
        this.cardViewPane = mainFrame.getCardViewPane();
        this.purchaseViewPane = mainFrame.getPurchaseViewPane();
        this.categoriesViewPane = mainFrame.getCategoriesViewPane();
        this.summaryViewPane = mainFrame.getSummaryViewPane();

        /* DataObserver Design Pattern - Registration */
        db.register(cardViewPane);
        cardViewPane.setSubject(db);
        db.register(purchaseViewPane);
        purchaseViewPane.setSubject(db);
        db.register(categoriesViewPane);
        categoriesViewPane.setSubject(db);
        db.register(summaryViewPane);
        summaryViewPane.setSubject(db);
        db.register(summaryViewPane.getAnalyticsPane());
        summaryViewPane.getAnalyticsPane().setSubject(db);

        tabPane.addChangeListener(e -> {
            if (tabPane.getSelectedComponent() != purchaseViewPane) removePurchaseForms();
            if (tabPane.getSelectedComponent() != cardViewPane) removeCardForms();
            if (tabPane.getSelectedComponent() != categoriesViewPane) removeCategoryForms();
            if (tabPane.getSelectedComponent() != summaryViewPane)
                summaryViewPane.getFilterForm().setVisible(false);
        });

        // These methods set up lambda anonymous functions that handles gui components.
        // Each method can be a single call of method callbacks or multiple calls.
        setupMainFrameHandlers();
        createCardHandler();
        searchCardsHandler();
        viewCardHandler();
        deleteCardHandler();
        sortCardsHandler();
        createPurchaseHandler();
        viewPurchaseHandler();
        sortPurchasesHandler();
        createCategoryHandler();
        deleteCategoryHandler();
        summaryViewHandler();
    }

    /*============================== REGISTER AND HANDLE EVENTS ==============================*/
    /*============================== MAIN FRAME HANDLERS ==============================*/
    private void setupMainFrameHandlers() {
        mainFrame.setSaveListener(e ->  {
            db.exportData(new ProgressDialog(mainFrame, "Saving Data", "Saving..."));
            shop.getAuthenticator().exportUsers();
        });

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exitApplication();
            }
        });

        mainFrame.getStartViewPane().setListener((username, password, signup) -> {
            if (signup) {
                shop.getAuthenticator().signup(username, password);
                mainFrame.getStartViewPane().setDefaults();
            } else {
                if (shop.getAuthenticator().login(username, password)) {
                    mainFrame.setSummaryViewPaneEnabled(true);
                    mainFrame.getStartViewPane().getLogoutBtn().setEnabled(true);
                    mainFrame.getStartViewPane().getNewUserOption().setEnabled(true);
                    mainFrame.getStartViewPane().setDefaults();
                } else {
                    System.out.println("NOT AUTHENTICATED");
                }
            }
        });

        mainFrame.setExitListener(e -> exitApplication());
    }

    /*============================== CARD VIEW HANDLERS ==============================*/
    /*TOOLBAR | CREATE CARD BUTTON*/
    private void createCardHandler() {
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
                if (CardType.AnonCard.equalsName(type)) {
                    newCard.put("name", null);
                    newCard.put("email", null);
                    newCard.put("cardType", CardType.AnonCard.name());
                } else {
                    newCard.put("name", name);
                    newCard.put("email", email);
                    if (CardType.BasicCard.equalsName(type))
                        newCard.put("cardType", CardType.BasicCard.name);
                    else if (CardType.PremiumCard.equalsName(type))
                        newCard.put("cardType", CardType.PremiumCard.name);
                }
                shop.makeCard(newCard);
                removeCardForms();
                showResults(cardViewPane, printCard(cardID, "CARD ADDED"));
            });
        });
    }

    /*TOOLBAR | SEARCH BUTTON*/
    private void searchCardsHandler() {
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
    }

    /*TOOLBAR | VIEW BUTTON*/
    private void viewCardHandler() {
        cardViewPane.setViewCardListener(() -> {
            if (cardViewPane.getCardsTable().getSelectedRow() >= 0) {
                removeCardForms();
                final int selectedRow = cardViewPane.getCardsTable().getSelectedRow();
                final String cardID = (String)cardViewPane.getCardsTable().getValueAt(selectedRow, 0);
                showResults(cardViewPane, printCard(cardID,"CARD"));
            }
        });
    }

    /*TOOLBAR | DELETE CARD BUTTON*/
    private void deleteCardHandler() {
        cardViewPane.setDeleteCardListener(() -> {
            removeCardForms();
            DeleteCardForm form = FormFactory.deleteCardForm();
            cardViewPane.setDeleteForm(form);

            // REGISTER A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteCardListener(e -> {
                final String cardID = e.getID();
                String[] btnOptions = {"Yes","No"};
                String message = "Are you sure you want to DELETE card: " + cardID + "\nThis cannot be undone."
                        + "\n\nAll purchasesMap for this card will be changed to CASH status.\n\n";

                showResults(cardViewPane, printCard(cardID, "DELETE CARD?"));
                int confirm = JOptionPane.showOptionDialog(mainFrame, // frame, can be null
                        message, // message
                        "Confirm Delete?", // title
                        JOptionPane.OK_CANCEL_OPTION, // button options
                        JOptionPane.WARNING_MESSAGE, // icon
                        IconFactory.warningIcon(), // use custom icon
                        btnOptions, // title of buttons
                        btnOptions[1] // default button title
                );

                if (confirm == JOptionPane.OK_OPTION) {
                    shop.deleteCard(cardID);
                }
                removeCardForms();
            });
        });
    }

    /*TOOLBAR | SORT COMBOBOX*/
    private void sortCardsHandler() {
        cardViewPane.getSortedCombo().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Card> sortedCardsList = db.getOrigCardsMap().values().parallelStream().map((Card c) -> c.clone(c))
                        .collect(Collectors.toCollection(ArrayList::new));
                if (e.getItem().equals("Sort..") || e.getItem().equals(SortCardType.CreatedOrder.name)) {
                    // Lambda version of sorting with Comparator comparing method
                    sortedCardsList.sort(Comparator.comparing(Card::getID));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.ReverseCreatedOrder.name)) {
                    // Lambda version of sorting with Comparator
                    sortedCardsList.sort((Card c1, Card c2) -> c2.getID().compareTo(c1.getID()));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.Name.name)) {
                    sortedCardsList.sort(((c1, c2) -> {
                        if (c1 instanceof AdvancedCard && c2 instanceof AdvancedCard)
                            return ((AdvancedCard) c1).getName().compareTo(((AdvancedCard) c2).getName());

                        return (c1 instanceof AnonCard && c2 instanceof AdvancedCard) ? -1 : 1;
                    }));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.Points.name)) {
                    // Lambda version of sorting with Comparator comparingDouble method
                    sortedCardsList.sort(Comparator.comparingDouble(Card::getPoints).reversed());
                    cardViewPane.updateTableData(sortedCardsList);
                }
            }
        });
    }

    /*============================== PURCHASE VIEW HANDLERS ==============================*/
    /*TOOLBAR | CREATE BUTTON*/
    private void createPurchaseHandler() {
        purchaseViewPane.setCreatePurchaseListener(() -> {
            removePurchaseForms();
            PurchaseForm form = FormFactory.createPurchaseForm(new ArrayList<>(db.getOrigCardsMap().values()),
                    new ArrayList<>(db.getOrigCategoriesMap().values()));
            purchaseViewPane.setCreatePurchaseForm(form);

            // FORM CREATE BUTTON
            form.setCreatePurchaseListener(v -> {
                final CardType cardType = v.getCardType();
                final String purchaseType = v.getPurchaseType();
                final int receiptID = v.getReceiptID();
                String cardID = (v.getCardID() == null || v.getCardID().isEmpty()) ? v.getExistingCardID() : v.getCardID() ;
                HashMap<Integer, Category> categories = new HashMap<>(v.getCategories());
                String resultsText = "";

                if (purchaseType.equals(PurchaseType.ExistingCardPurchase.toString())) {
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getCard(cardID).toString() + db.getPurchase(receiptID).toString();
                } else if (purchaseType.equals(PurchaseType.CashPurchase.name)) {
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getPurchase(receiptID).toString();
                } else if (purchaseType.equals(PurchaseType.NewCardPurchase.name)) {
                    String name = (!cardType.equals(CardType.AnonCard)) ? v.getCardName() : null;
                    String email = (!cardType.equals(CardType.AnonCard)) ? v.getCardEmail() : null;

                    HashMap<String, String> newCard = new HashMap<>();
                    newCard.put("name", name);
                    newCard.put("email", email);
                    newCard.put("cardType", cardType.name);
                    newCard.put("cardID", cardID);

                    shop.makeCard(newCard);
                    shop.makePurchase(cardID, receiptID, categories);
                    resultsText = db.getCard(cardID).toString() + db.getPurchase(receiptID).toString();
                }
                removePurchaseForms();
                showResults(purchaseViewPane, resultsText);
            });
        });
    }

    /*TOOLBAR | VIEW  BUTTON*/
    private void viewPurchaseHandler() {
        purchaseViewPane.setViewPurchaseListener(() -> {
            if (purchaseViewPane.getPurchasesTable().getSelectedRow() >= 0) {
                final int selectedRow = purchaseViewPane.getPurchasesTable().getSelectedRow();
                final int receiptID = (Integer)purchaseViewPane.getPurchasesTable().getValueAt(selectedRow, 1);
                String resultsText = db.getOrigPurchasesMap().values().stream().filter(p -> p.getReceiptID() == receiptID)
                        .map(Purchase::toString).collect(Collectors.joining("\n"));
                showResults(purchaseViewPane, resultsText);
            }
        });
    }

    /*TOOLBAR | SORT COMBOBOX*/
    private void sortPurchasesHandler() {
        purchaseViewPane.getSortPurchaseCombo().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Purchase> purchasesList = db.getOrigPurchasesMap().values().parallelStream().map(Purchase::new)
                        .collect(Collectors.toCollection(ArrayList::new));
                if (e.getItem().equals(SortPurchaseType.All.name)) {
                    purchaseViewPane.update();
                } else if (e.getItem().equals(SortPurchaseType.Card.name)) {
                    purchaseViewPane.sortPurchaseTableMode(purchasesList.stream()
                            .filter(c -> !c.getCardType().equals(CardType.Cash.name))
                            .collect(Collectors.toCollection(ArrayList::new))); // Negative cash validation
                } else if (e.getItem().equals(SortPurchaseType.Cash.name)) {
                    purchaseViewPane.sortPurchaseTableMode(purchasesList.stream()
                            .filter(c -> c.getCardType().equals(CardType.Cash.name))
                            .collect(Collectors.toCollection(ArrayList::new))); // Positive cash validation
                }
            }
        });
    }

    /*=========================== CATEGORIES VIEW HANDLERS ===========================*/
    /*TOOLBAR | CREATE CATEGORY BUTTON*/
    private void createCategoryHandler() {
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
    }

    /*TOOLBAR | DELETE CATEGORY BUTTON*/
    private void deleteCategoryHandler() {
        categoriesViewPane.setDeleteCategoryListener(() -> {
            removeCategoryForms();
            DeleteCategoryForm form = FormFactory.deleteCategoryForm();
            categoriesViewPane.setDeleteCategoryForm(form);

            //ADD A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteCardListener(e -> {
                final int categoryID = Integer.parseInt(e.getID());

                String[] btnOptions = {"Yes","Cancel"};
                String message = "Are you sure you want to DELETE Category: " + e.getID() + "\nThis cannot be undone."
                        + "\n\nAll purchasesMap for this category will be moved to Other category.\n\n";

                int confirm = JOptionPane.showOptionDialog(mainFrame, // frame, can be null
                        message, // message
                        "Confirm Delete?", // title
                        JOptionPane.OK_CANCEL_OPTION, // button options
                        JOptionPane.WARNING_MESSAGE, // icon
                        IconFactory.warningIcon(), // use custom icon
                        btnOptions, // title of buttons
                        btnOptions[1] // default button title
                );

                if (confirm == JOptionPane.OK_OPTION) { shop.deleteCategory(categoryID); }
                removeCategoryForms();
            });
        });
    }

    /*============================== SUMMARY VIEW HANDLERS ==============================*/
    private void summaryViewHandler() {
        summaryViewPane.setAnalyticsListener(() -> {
            SummaryFilterForm filterForm = summaryViewPane.getFilterForm();
            filterForm.setVisible(true);

            filterForm.setListener(this::filterActionPerformed);
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

    /*============================== PRINTING METHOD ==============================*/
    private String printCard(String cardID, String title) {
        String cText = db.getCard(cardID).toString();
        String pText = db.getOrigPurchasesMap().values().stream().filter(p -> p.getCardID() != null && p.getCardID().equals(cardID))
                .map(Purchase::toString).collect(Collectors.joining("\n"));
        return String.format("%s%n%s%n%s%n%s", title, cText,"PURCHASE(S)", pText);
    }

    /*============================== SUMMARY FILTER METHODS ==============================*/
    private void filterActionPerformed(SummaryView e) {
        SummaryAnalyticsPane analyticsPane = summaryViewPane.getAnalyticsPane();
        boolean filterProceed = false;

        // Clone each data structure so the filtering doesn't change other parts of the program.
        final HashMap<String, Card> clonedCardsMap = db.getOrigCardsMap().entrySet().parallelStream().map(c -> c.getValue().clone(c.getValue()))
                .collect(Collectors.toMap(Card::getID, c -> c.clone(c), (k, v) -> k, HashMap::new));

        final HashMap<Integer, Purchase> clonedPurchasesMap = db.getOrigPurchasesMap().entrySet().parallelStream()
                .map(p -> new Purchase(p.getValue())).collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k, v) -> k, HashMap::new));

        final HashMap<Integer, Category> clonedCategoriesMap = db.getOrigCategoriesMap().entrySet().parallelStream()
                .map(c -> new Category(c.getValue())).collect(Collectors.toMap(Category::getId, c -> c, (k, v) -> k, HashMap::new));

        // Filtered result maps that are passed to the View components
        final HashMap<Integer, Purchase> filteredPurchases;
        final HashMap<Integer, Category> filteredCategories;
        final HashMap<String, Card> filteredCards;

        final Predicate<Purchase> hoursPredicate = p -> (p.getPurchaseTime().getHour() == e.getHoursOption());
        final Predicate<Purchase> daysPredicate = p -> (p.getPurchaseTime().getDayOfWeek().getValue() == e.getDaysOption());
        // Using a chained predicates to always make sure the filter is between and equals to 2 dates
        final Predicate<Purchase> dateFromPredicate = p -> (p.getPurchaseTime().toLocalDate().isAfter(e.getDateFromOption()));
        final Predicate<Purchase> dateToPredicate = p -> (p.getPurchaseTime().toLocalDate().isBefore(e.getDateToOption()));
        final Predicate<Purchase> dateEqualsFromPredicate = p -> (p.getPurchaseTime().toLocalDate().isEqual(e.getDateFromOption()));
        final Predicate<Purchase> dateEqualsToPredicate = p -> (p.getPurchaseTime().toLocalDate().isEqual(e.getDateToOption()));
        final Predicate<Purchase> datePredicate = (dateEqualsFromPredicate.or(dateFromPredicate)).and(dateEqualsToPredicate.or(dateToPredicate));

        // Create a DoubleBiFunction that returns the total purchase amount for one category
        final ToDoubleBiFunction<Stream<Purchase>, Category> purchaseCategorySumBiFunction = (purchaseStream, category) -> purchaseStream
                .mapToDouble(p -> p.getCategories().get(category.getId()).getAmount()).sum();

        Function<Category, Category> function;

        if (e.getDaysOption() != 0 && e.getHoursOption() < 24) {
            /* Filter by date, day and hour */
            // Filter the clonedPurchasesMap based on the predicates
            filteredPurchases = clonedPurchasesMap.values().parallelStream().filter(datePredicate.and((daysPredicate.and(hoursPredicate))))
                    .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k, v) -> k, HashMap::new));

            // Use the purchaseCategorySumBiFunction to make a new category with a new Total Amount
            function = category -> new Category(category, purchaseCategorySumBiFunction.applyAsDouble(filteredPurchases.values().parallelStream(), category));

            // Get the filtered categories result based on the filtering done previously
            filteredCategories = clonedCategoriesMap.values().stream().map(function)
                    .collect(Collectors.toMap(Category::getId, c -> c, (k, v) -> k, HashMap::new));

            filterProceed = true;
        } else if (e.getDaysOption() != 0 && e.getHoursOption() == 24) {
            /* Filter by day and date */
            filteredPurchases = clonedPurchasesMap.values().stream().filter(datePredicate.and(daysPredicate))
                    .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k, v) -> k, HashMap::new));

            function = category -> new Category(category,
                    purchaseCategorySumBiFunction.applyAsDouble(filteredPurchases.values().parallelStream(), category));

            filteredCategories = clonedCategoriesMap.values().stream().map(function)
                    .collect(Collectors.toMap(Category::getId, c -> c, (k, v) -> k, HashMap::new));

            filterProceed = true;
        } else if (e.getDaysOption() == 0 && e.getHoursOption() < 24) {
            /* Filter by hour */
            filteredPurchases = clonedPurchasesMap.values().stream().filter(datePredicate.and(hoursPredicate))
                    .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k, v) -> k, HashMap::new));

            filteredCategories = clonedCategoriesMap.values().stream().map(c -> {
                double newTotal = filteredPurchases.values().stream().mapToDouble(p -> p.getCategories().get(c.getId()).getAmount()).sum();
                c.setTotalAmount(newTotal);
                return c;
            }).collect(Collectors.toMap(Category::getId, c -> c, (k, v) -> k, HashMap::new));

            filterProceed = true;
        } else if (e.getDaysOption() == 0 && e.getHoursOption() == 24) {
            /* Filter by dates only */
            filteredPurchases = clonedPurchasesMap.values().stream().parallel().filter(datePredicate)
                    .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k, v) -> k, HashMap::new));

            filteredCategories = clonedCategoriesMap.values().stream().map(c -> {
                double newTotal = filteredPurchases.values().stream().mapToDouble(p -> p.getCategories().get(c.getId()).getAmount()).sum();
                c.setTotalAmount(newTotal);
                return c;
            }).collect(Collectors.toMap(Category::getId, c -> c, (k, v) -> k, HashMap::new));

            filterProceed = true;
        } else {
            filteredCategories = null;
            filteredPurchases = null;
            // default option
            summaryViewPane.update();
            analyticsPane.update();
        }

        if (filterProceed) {
            filteredCards = filteredPurchases.values().parallelStream().filter(p -> !p.getCardType().equals(CardType.Cash.name))
                    .map(p -> clonedCardsMap.getOrDefault(p.getCardID(), null)).filter(Objects::nonNull)
                    .collect(Collectors.toMap(Card::getID, c -> c, (k,v) -> k, HashMap::new));

            summaryViewPane.filterTables(new ArrayList<>(filteredPurchases.values()), new ArrayList<>(filteredCards.values()));
            analyticsPane.filterUpdate(filteredCategories, filteredPurchases, filteredCards);
        }
        summaryViewPane.revalidate();
        summaryViewPane.repaint();
    }

    /*============================== EXIT METHOD ==============================*/
    private void exitApplication() {
        String[] options = {"Save and Exit", "Exit without Save", "Cancel Exit"};
        int response = JOptionPane.showOptionDialog(null,
                "\nWould you like to save your session data before exiting?\n\n\n\n",
                "Save on Exit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                IconFactory.warningIcon(), options, options[0]);

        if (response == 0) {
            db.exportData(new ProgressDialog(mainFrame, "Saving Data", "Saving..."));
            shop.getAuthenticator().exportUsers();
            System.gc();
            mainFrame.dispose();
        } else if (response == 1) {
            System.gc();
            mainFrame.dispose();
        }
    }
}
