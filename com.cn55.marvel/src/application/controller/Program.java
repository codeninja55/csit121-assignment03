package application.controller;

import application.controller.Validator.*;
import application.model.CardModel.*;
import application.model.CategoryModel.CategoriesReadImpl;
import application.model.CategoryModel.Category;
import application.model.*;
import application.model.DataStoreWriters.CardsWriteOut;
import application.model.DataStoreWriters.CategoriesWriteOut;
import application.model.DataStoreWriters.PurchasesWriteOut;
import application.model.PurchaseModel.Purchase;
import application.model.PurchaseModel.PurchaseType;
import application.model.PurchaseModel.PurchasesReadImpl;
import application.model.PurchaseModel.SortPurchaseType;
import application.view.CardView.CardForm;
import application.view.CardView.CardViewPane;
import application.view.CategoriesView.CategoriesForm;
import application.view.CategoriesView.CategoriesViewPane;
import application.view.CustomComponents.FormFormattedTextField;
import application.view.CustomComponents.ResultsPane;
import application.view.CustomComponents.Style;
import application.view.FormFactory.DeleteCardForm;
import application.view.FormFactory.DeleteCategoryForm;
import application.view.MainFrame;
import application.view.PurchaseView.PurchaseEvent;
import application.view.PurchaseView.PurchaseForm;
import application.view.PurchaseView.PurchaseViewPane;
import application.view.FormFactory.SearchForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

public class Program {

    private final Shop shop;
    private final DataDAO db;
    private final MainFrame mainFrame;
    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;

    private final WriteCSV writeCategories, writeCards, writePurchases;

    public Program() {
        /* Singleton Design Pattern - Only one instance of Shop available */
        shop = Shop.getShopInstance();
        db = shop.getDataStore();

        /* Strategy Design Pattern - Implementation of writing and reading buried in concrete classes */
        ReadCSV readCategoriesCSV = new CategoriesReadImpl();
        ReadCSV readCardsCSV = new CardsReadImpl();
        ReadCSV readPurchaseCSV = new PurchasesReadImpl();

        readCategoriesCSV.read();
        //new TestCode(shop);
        readCardsCSV.read();
        readPurchaseCSV.read();

        writeCategories = new CategoriesWriteOut();
        writeCards = new CardsWriteOut();
        writePurchases = new PurchasesWriteOut();

        this.mainFrame = new MainFrame();
        this.tabPane = mainFrame.getTabPane();

        /* Windows Closing Listener */
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                int confirmSave = JOptionPane.showConfirmDialog(mainFrame,"Would you like to save your session data?",
                        "Save on Exit", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);

                if (confirmSave == JOptionPane.OK_OPTION) {
                    writeCategories.writeOut();
                    writeCards.writeOut();
                    writePurchases.writeOut();
                }

                System.gc(); // Garbage collector
                mainFrame.dispose();
            }
        });

        /* DataObserver Design Pattern - Registration and initial update calls */
        this.cardViewPane = mainFrame.getCardViewPane();
        db.register(cardViewPane);
        cardViewPane.setSubject(db);
        cardViewPane.update();
        cardViewPane.setCardTableModel();

        this.purchaseViewPane = mainFrame.getPurchaseViewPane();
        db.register(purchaseViewPane);
        purchaseViewPane.setSubject(db);
        purchaseViewPane.update();
        purchaseViewPane.setPurchaseTableModel();

        this.categoriesViewPane = mainFrame.getCategoriesViewPane();
        db.register(categoriesViewPane);
        categoriesViewPane.setSubject(db);
        categoriesViewPane.update();
        categoriesViewPane.setCategoriesTableModel();

        setupViewListeners();
    }

    /*============================== REGISTER AND HANDLE EVENTS ==============================*/
    private void setupViewListeners() {
        /*TAB PANE LISTENER*/
        // This method removes forms from the selected pane if the user selects another pane
        tabPane.addChangeListener((ChangeEvent e) -> {
            // DESELECTED LISTENERS
            if (tabPane.getSelectedComponent() != purchaseViewPane) {
                purchaseViewPane.getResultsPane().setVisible(false);
                removePurchaseForms();
            } else if (tabPane.getSelectedComponent() != cardViewPane) {
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
            } else if (tabPane.getSelectedComponent() != categoriesViewPane) {
                removeCategoryForms();
            }
        });

        /*============================== CARD VIEW HANDLERS ==============================*/
        /*TOOLBAR | CREATE CARD BUTTON*/
        cardViewPane.setCreateCardListener(() -> {
            removeCardForms();
            cardViewPane.setCardForm(new CardForm());
            CardForm form = cardViewPane.getCardForm();
            cardViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);
            form.createBaseCreateCardForm();

            // Setup a text pane to put all the necessary data into
            ResultsPane resultsPane = cardViewPane.getResultsPane();
            resultsPane.setVisible(false);
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setCardViewMouseListeners();

            // ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            form.setCancelListener(() -> {
                cardViewPane.getCardForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
                removeResultsPane(resultsPane);
            });

            // ADD A CREATE BUTTON LISTENER AFTER CREATING FORM
            form.setCardListener(e -> {
                String type = (String)e.getCardTypeCombo().getSelectedItem();
                HashMap<String, String> newCard = new HashMap<>();
                String name = e.getCardNameTextField().getText();
                String email = e.getCardEmailTextField().getText();

                assert type != null;
                if (type.equals(CardType.AnonCard.getName())) {
                    newCard.put("name", "");
                    newCard.put("email", "");
                    newCard.put("cardType", CardType.AnonCard.getName());
                    shop.makeCard(newCard);
                } else if (type.equals(CardType.BasicCard.getName())) {
                    newCard.put("name", name);
                    newCard.put("email", email);
                    newCard.put("cardType", CardType.BasicCard.getName());
                    shop.makeCard(newCard);
                } else if (type.equals(CardType.PremiumCard.getName())) {
                    newCard.put("name", name);
                    newCard.put("email", email);
                    newCard.put("cardType", CardType.BasicCard.getName());
                    shop.makeCard(newCard);
                }

                showResultsPane(db.getCard(e.getCardIDTextField().getText()).toString(), resultsPane, resultsTextPane);
                removeCardForms();
            });
        });

        /*TOOLBAR | DELETE CARD BUTTON*/
        cardViewPane.setDeleteCardListener(() -> {
            removeCardForms();
            cardViewPane.setDeleteForm(new DeleteCardForm());
            DeleteCardForm form = cardViewPane.getDeleteForm();
            cardViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            // Setup a text pane to put all the necessary data into
            ResultsPane resultsPane = cardViewPane.getResultsPane();
            resultsPane.setVisible(false);
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setCardViewMouseListeners();

            // REGISTER A CANCEL BUTTON LISTENER AFTER CREATING FORM
            form.setCancelListener(() -> {
                cardViewPane.getDeleteForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
                removeResultsPane(resultsPane);
            });

            // REGISTER A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteListener(e -> {
                String cardID = e.getIdTextField().getText().toUpperCase();

                if (!cardID.isEmpty() && db.getAllCards().containsKey(cardID)) {
                    e.getErrorLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(false);
                    e.getDeleteErrorLabel().setVisible(false);

                    showResultsPane(db.getCard(cardID).toString(), resultsPane, resultsTextPane);
                    cardViewPane.revalidate();
                    cardViewPane.repaint();

                    String[] btnOptions = {"Yes","Cancel"};
                    String message = "Are you sure you want to DELETE card: " + cardID +
                            "\nThis cannot be undone." +
                            "\n\nAll purchases for this card will be changed to CASH status.\n\n";

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
                        removeResultsPane(resultsPane);
                    } else {
                        e.getIdTextField().setText(null);
                        e.getDeleteErrorLabel().setVisible(true);
                    }
                } else {
                    if (!db.getAllCards().containsKey(cardID)){
                        e.getErrorLabel().setVisible(false);
                        e.getRuleErrLabel().setVisible(true);
                    } else {
                        e.getRuleErrLabel().setVisible(false);
                        e.getErrorLabel().setVisible(true);
                    }

                    e.getDeleteErrorLabel().setVisible(true);
                    e.getIdTextField().setForeground(Style.redA700());
                    e.getIdLabel().setForeground(Style.redA700());
                }
            });
        });

        /*TOOLBAR | SEARCH BUTTON*/
        cardViewPane.setSearchCardListener(() -> {
            removeCardForms();
            cardViewPane.setSearchForm(new SearchForm());
            cardViewPane.add(cardViewPane.getSearchForm(), BorderLayout.WEST);
            cardViewPane.getSearchForm().setVisible(true);

            // ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            cardViewPane.getSearchForm().setCancelListener(() -> {
                cardViewPane.getSearchForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
            });

            // ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            cardViewPane.getSearchForm().setSearchListener(e -> {
                // Setup a text pane to put all the necessary data into
                ResultsPane resultsPane = cardViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setCardViewMouseListeners();

                String cardID = e.getSearchIDTextField().getText().toUpperCase();

                // SETUP VALIDATOR FOR CARD ID
                FormValidData input = new FormValidData();
                input.setCardID(cardID);
                FormRule cardIDRule = new CardIDRule();

                if (!cardID.isEmpty() && db.getAllCards().containsKey(cardID)) {
                    e.getErrorLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(false);

                    String cardText = db.getCard(cardID).toString();
                    StringBuilder purchaseText = new StringBuilder("");

                    db.getAllPurchases().values().forEach((p)->{
                        if (p.getCardID() != null && p.getCardID().equals(cardID)) {
                            purchaseText.append("\n");
                            purchaseText.append(p.toString());
                        }
                    });

                    String results = String.format("%s%n%n%s%n%n%s%s","CARD FOUND",
                            cardText,"PURCHASE(S)",purchaseText);

                    // Create the inner class ResultsTextPane and popular first.
                    // Then set the ResultsPane to visible and add the new ScrollPane
                    // Achieved in method below - showResultsPane()
                    showResultsPane(results, resultsPane, resultsTextPane);
                    cardViewPane.revalidate();
                    cardViewPane.repaint();

                    e.getSearchIDTextField().setText(null);
                } else { // If card does not exists, it will be a negative number so invoked below
                    if (!cardIDRule.validate(input)) {
                        e.getRuleErrLabel().setVisible(true);
                        e.getErrorLabel().setVisible(false);
                    } else {
                        e.getErrorLabel().setVisible(true);
                        e.getRuleErrLabel().setVisible(false);
                    }

                    removeResultsPane(resultsPane);
                    resultsPane.setVisible(false);
                    e.getSearchIDTextField().setForeground(Style.redA700());
                    e.getSearchIDLabel().setForeground(Style.redA700());
                }
            });
        });

        /*TOOLBAR | VIEW BUTTON*/
        cardViewPane.setViewCardListener(() -> {
            if (cardViewPane.getCardTablePane().getSelectedRow() >= 0) {
                removeCardForms();

                int selectedRow = cardViewPane.getCardTablePane().getSelectedRow();
                String cardID = (String)cardViewPane.getCardTablePane().getValueAt(selectedRow, 0);

                ResultsPane resultsPane = cardViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setCardViewMouseListeners();

                String cText = db.getCard(cardID).toString();
                StringBuilder pText = new StringBuilder("");

                db.getAllPurchases().values().forEach((p)-> {
                    if (p.getCardID() != null && p.getCardID().equals(cardID)) {
                        pText.append("\n");
                        pText.append(p.toString());
                    }
                });

                String textResults = String.format("%s%n%s%n%n%s%n%s","CARD",
                        cText,"PURCHASE(S)", pText);

                showResultsPane(textResults, resultsPane, resultsTextPane);
                cardViewPane.revalidate();
                cardViewPane.repaint();
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
                    // Lambda version of sorting with Comparator
                    sortedCardsList.sort(((c1, c2) -> {
                        if (c1 instanceof AdvancedCard && c2 instanceof AdvancedCard)
                            return ((AdvancedCard) c1).getName().compareTo(((AdvancedCard) c2).getName());

                        if (c1 instanceof AnonCard && c2 instanceof AdvancedCard)
                            return -1;
                        else
                            return 1;
                    }));
                    cardViewPane.updateTableData(sortedCardsList);
                } else if (e.getItem().equals(SortCardType.Points.getName())) {
                    // Lambda version of sorting with Comparator comparingDouble method
                    sortedCardsList.sort(Comparator.comparingDouble(Card::getPoints));
                    cardViewPane.updateTableData(sortedCardsList);
                }
            }
        });

        /*============================== PURCHASE VIEW HANDLERS ==============================*/
        /*TOOLBAR | CREATE BUTTON*/
        purchaseViewPane.setCreatePurchaseListener(() -> {
            removePurchaseForms();
            purchaseViewPane.setCreatePurchaseForm(new PurchaseForm());
            PurchaseForm form = purchaseViewPane.getCreatePurchaseForm();
            purchaseViewPane.add(form, BorderLayout.WEST);

            form.getPurchaseTypeCombo().setSelectedIndex(0);
            form.setGeneratedReceiptID(Generator.setReceiptID());
            form.setCardModel(db.getCardModel());
            form.setCategoriesList(new ArrayList<>(db.getAllCategories().values()));
            form.createBasePurchaseForm();
            form.setVisible(true);

            // SET UP A RESULTS PANE TO SHOW END RESULT
            ResultsPane resultsPane = purchaseViewPane.getResultsPane();
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setPurchaseViewPaneMouseListeners();

            // FORM CANCEL BUTTON
            form.setCancelPurchaseListener(() -> {
                form.setVisible(false);
                removePurchaseForms();
            });

            // FORM CREATE BUTTON
            form.setCreatePurchaseListener(event -> {
                JComboBox<String> type = event.getPurchaseTypeCombo();

                String receiptIDStr = event.getReceiptIDTextField().getText();
                int receiptID = Integer.parseInt(receiptIDStr);

                String cardID = getPurchaseFormCardID(event);
                HashMap<Integer, Category> categories = getPurchaseFormCategories(event);
                String resultsText;

                if (type.getSelectedItem() != null && cardID != null && categories != null) {
                    if (type.getSelectedItem().equals(PurchaseType.ExistingCardPurchase.getName())) {
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getPurchase(receiptID).toString();
                        showResultsPane(resultsText,resultsPane,resultsTextPane);
                        removePurchaseForms();
                    } else if (type.getSelectedItem().equals(PurchaseType.NewCardPurchase.getName())) {
                        String cardType = null;
                        String name = null;
                        String email = null;

                        if (event.getAnonCardRB().isSelected()) {
                            cardType = CardType.AnonCard.getName();
                        } else if (event.getBasicCardRB().isSelected()) {
                            cardType = CardType.BasicCard.getName();
                            name = event.getCardNameTextField().getText();
                            email = event.getCardEmailTextField().getText();
                        } else if (event.getPremiumCardRB().isSelected()) {
                            cardType = CardType.PremiumCard.getName();
                            name = event.getCardNameTextField().getText();
                            email = event.getCardEmailTextField().getText();
                        }

                        HashMap<String, String> newCard = new HashMap<>();
                        newCard.put("name", name);
                        newCard.put("email", email);
                        newCard.put("cardType", cardType);
                        newCard.put("cardID", cardID);

                        shop.makeCard(newCard);
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getCard(cardID).toString() + db.getPurchase(receiptID).toString();
                        showResultsPane(resultsText,resultsPane,resultsTextPane);
                        removePurchaseForms();
                    } else if (type.getSelectedItem().equals(PurchaseType.CashPurchase.getName())) {
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getPurchase(receiptID).toString();
                        showResultsPane(resultsText,resultsPane,resultsTextPane);
                        removePurchaseForms();
                    }
                } else {
                    event.getPurchaseErrorLabel().setVisible(true);
                }
            });
        });

        /*TOOLBAR | SUMMARY BUTTON*/
        purchaseViewPane.setSummaryListener(() -> {
            ResultsPane resultsPane = purchaseViewPane.getResultsPane();
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setPurchaseViewPaneMouseListeners();

            Predicate<Purchase> cashPurchases = e ->
                    (e.getCardType().equals(CardType.Cash.getName()));

            double cashTotal = db.getAllPurchases().values().stream()
                    .filter(cashPurchases)
                    .mapToDouble(Purchase::getCategoriesTotal)
                    .sum();

            Predicate<Purchase> cardPurchases = new Predicate<Purchase>() {
                public boolean test(Purchase purchase) {
                    return !purchase.getCardType().equals(CardType.Cash.getName());
                }
            };

            double cardTotal = db.getAllPurchases().values().stream()
                    .filter(cardPurchases)
                    .mapToDouble(Purchase::getCategoriesTotal)
                    .sum();

            double allTotal = db.getAllPurchases().values().stream()
                    .mapToDouble(Purchase::getCategoriesTotal)
                    .sum();

            String resultsText = String.format("%n%s%n%n%s: $%.2f%n%n%s: $%.2f%n%n%s: $%.2f", "SUMMARY OF PURCHASES",
                    "Card Purchase Total", cardTotal,
                    "Cash Purchase Total", cashTotal,
                    "Total for All Purchases", allTotal);

            showResultsPane(resultsText, resultsPane, resultsTextPane);
            purchaseViewPane.revalidate();
            purchaseViewPane.repaint();
        });

        /*TOOLBAR | VIEW  BUTTON*/
        purchaseViewPane.setViewPurchaseListener(() -> {
            if (purchaseViewPane.getPurchaseTablePane().getSelectedRow() >= 0) {
                int selectedRow = purchaseViewPane.getPurchaseTablePane().getSelectedRow();
                Integer receiptID = (Integer) purchaseViewPane.getPurchaseTablePane().getValueAt(selectedRow, 0);

                ResultsPane resultsPane = purchaseViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setPurchaseViewPaneMouseListeners();

                String resultsText = "";
                for (Purchase purchase : db.getAllPurchases().values()) {
                    if (purchase.getReceiptID() == receiptID)
                        resultsText = purchase.toString();
                }

                showResultsPane(resultsText, resultsPane, resultsTextPane);

            }
        });

        /*TOOLBAR | SORT COMBOBOX*/
        purchaseViewPane.getSortPurchaseCombo().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Purchase> tempPurchases = new ArrayList<>();
                if (e.getItem().equals(SortPurchaseType.All.getName())) {
                    purchaseViewPane.update();
                } else if (e.getItem().equals(SortPurchaseType.Card.getName())) {
                    // NEGATIVE CASH VALIDATION
                    for (Purchase item : db.getAllPurchases().values())
                        if (!item.getCardType().equals(CardType.Cash.getName()))
                            tempPurchases.add(item);
                    purchaseViewPane.sortPurchaseTableMode(tempPurchases);
                } else if (e.getItem().equals(SortPurchaseType.Cash.getName())) {
                    // POSITIVE CASH Validation
                    for (Purchase item : db.getAllPurchases().values())
                        if (item.getCardType().equals(CardType.Cash.getName()))
                            tempPurchases.add(item);
                    purchaseViewPane.sortPurchaseTableMode(tempPurchases);
                }
            }
        });

        /*=========================== CATEGORIES VIEW HANDLERS ===========================*/
        /*TOOLBAR | CREATE CATEGORY BUTTON*/
        categoriesViewPane.setCreateCategoryListener(() -> {
            removeCategoryForms();
            categoriesViewPane.setCreateCategoryForm(new CategoriesForm());
            CategoriesForm form = categoriesViewPane.getCreateCategoryForm();
            categoriesViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            // ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            form.setCancelListener(() -> {
                form.setVisible(false);
                removeCategoryForms();
            });

            // ADD A CREATE BUTTON LISTENER AFTER CREATING FORM
            form.setCreateCategoryListener(e -> {
                shop.makeCategory(new Category(e.getCategoryNameTextField().getText(),
                        e.getCategoryDescTextField().getText()));

                form.setVisible(false);
                removeCategoryForms();
            });
        });

        /*TOOLBAR | DELETE CATEGORY BUTTON*/
        categoriesViewPane.setDeleteCategoryListener(() -> {
            removeCategoryForms();
            categoriesViewPane.setDeleteCategoryForm(new DeleteCategoryForm());
            DeleteCategoryForm form = categoriesViewPane.getDeleteCategoryForm();

            categoriesViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            //ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM
            form.setCancelListener(() -> {
                form.setVisible(false);
                removeCategoryForms();
            });

            //ADD A DELETE BUTTON LISTENER AFTER CREATING FORM
            form.setDeleteListener(e -> {
                String categoryIDStr = e.getIdTextField().getText();
                int categoryID = Integer.parseInt(categoryIDStr);
                //SETUP VALIDATOR FOR CATEGORY ID
                FormValidData input = new FormValidData();
                input.setCategoryID(categoryIDStr);
                FormRule validIDRule = new CategoryIDRule();

                if (!validIDRule.validate(input)) {
                    e.getErrorLabel().setVisible(false);
                    e.getOthersDeleteErrLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(true);
                    e.getIdLabel().setForeground(Style.redA700());
                    e.getIdTextField().setForeground(Style.redA700());
                    e.getDeleteErrorLabel().setVisible(true);
                } else {
                    if (Integer.parseInt(categoryIDStr) == 100) {
                        // Do NOT allow user to delete category Others
                        e.getErrorLabel().setVisible(false);
                        e.getRuleErrLabel().setVisible(false);
                        e.getOthersDeleteErrLabel().setVisible(true);
                        e.getDeleteErrorLabel().setVisible(true);
                    } else if (db.getAllCategories().containsKey(categoryID)) {
                        e.getErrorLabel().setVisible(false);
                        e.getRuleErrLabel().setVisible(false);
                        e.getOthersDeleteErrLabel().setVisible(false);
                        e.getDeleteErrorLabel().setVisible(false);

                        String[] btnOptions = {"Yes","Cancel"};
                        String message = "Are you sure you want to DELETE Category: " + categoryIDStr +
                                "\nThis cannot be undone." +
                                "\n\nAll purchases for this category will be moved to Other category.\n\n";

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
                            shop.deleteCategory(categoryID);
                            form.setVisible(false);
                            removeCategoryForms();
                        } else {
                            e.getIdLabel().setForeground(Color.BLACK);
                            e.getIdTextField().setForeground(Color.BLACK);
                            e.getDeleteErrorLabel().setVisible(true);
                        }
                    } else {
                        e.getRuleErrLabel().setVisible(false);
                        e.getOthersDeleteErrLabel().setVisible(false);
                        e.getErrorLabel().setVisible(true);
                        e.getIdLabel().setForeground(Style.redA700());
                        e.getIdTextField().setForeground(Style.redA700());
                        e.getDeleteErrorLabel().setVisible(true);
                    }
                }

            });

        });

    }

    /*============================== MUTATORS  ==============================*/
    // Takes some arguments to create and display a ResultsPane to the right for results output
    private void showResultsPane(String text, ResultsPane resultsPane,
                                   ResultsPane.ResultsTextPane resultsTextPane) {
        resultsTextPane.setText(text);
        resultsPane.setVisible(true);
        resultsPane.setScrollPane(resultsTextPane);
        resultsPane.add(resultsPane.getScrollPane());
        resultsPane.getResultsTextPane().grabFocus();
        resultsPane.getResultsTextPane().setCaretPosition(0);
    }

    /*==================== REMOVING FORMS METHODS ====================*/
    // These methods all remove forms from their respective panes when they're not needed anymore
    private void removeCardForms() {
        for (Component comp : cardViewPane.getComponents()) {
            if (comp instanceof CardForm || comp instanceof DeleteCardForm || comp instanceof SearchForm) {
                comp.setVisible(false);
                if (comp instanceof CardForm && cardViewPane.getCardForm().getBaseCreateCardForm() != null)
                    cardViewPane.getCardForm().remove(cardViewPane.getCardForm().getBaseCreateCardForm());
                cardViewPane.remove(comp);
            }
        }
    }

    private void removePurchaseForms() {
        for (Component comp : purchaseViewPane.getComponents()) {
            if (comp instanceof PurchaseForm) {
                comp.setVisible(false);
                if (purchaseViewPane.getCreatePurchaseForm().getBaseCreatePurchaseForm() != null)
                    purchaseViewPane.getCreatePurchaseForm().remove(purchaseViewPane.getCreatePurchaseForm().getBaseCreatePurchaseForm());
                purchaseViewPane.remove(comp);
            }
        }
    }

    private void removeCategoryForms() {
        for (Component comp : categoriesViewPane.getComponents()) {
            if (comp instanceof CategoriesForm || comp instanceof DeleteCategoryForm)
                categoriesViewPane.remove(comp);
        }
    }

    private void removeResultsPane(ResultsPane resultsPane) {
        if (resultsPane.getScrollPane() != null && resultsPane.getResultsTextPane() != null) {
            resultsPane.remove(resultsPane.getResultsTextPane());
            resultsPane.remove(resultsPane.getScrollPane());
        }
    }

    /*=============== SETTING MOUSE LISTENERS METHODS ===============*/
    // These methods add a mouse listener to the JTable whenever a ResultsPane is added to the right
    private void setCardViewMouseListeners() {
        //SET UP A MOUSE LISTENER TO CLOSE PANEL WHEN CLICKING ON TABLE OR OUTER PANEL
        // Only add a new MouseListener if there are less than 3 in the MouseListener[]
        // Unknown reasons why there are already 2 other ones in a JTable
        if (cardViewPane.getCardTablePane().getMouseListeners().length < 3) {
            cardViewPane.getCardTablePane().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
                removeResultsPane(cardViewPane.getResultsPane());
                }
            });
        }
    }

    private void setPurchaseViewPaneMouseListeners() {
         // SET UP A MOUSE LISTENER TO CLOSE PANEL WHEN CLICKING ON TABLE OR OUTER PANEL
        // Only add a new MouseListener if there are less than 3 in the MouseListener[]
        // Unknown reasons why there are already 2 other ones in a JTable
        if (purchaseViewPane.getPurchaseTablePane().getMouseListeners().length < 3) {
            purchaseViewPane.getPurchaseTablePane().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    purchaseViewPane.getResultsPane().setVisible(false);
                    removePurchaseForms();
                    removeResultsPane(purchaseViewPane.getResultsPane());
                }
            });
        }
    }

    /*=============== ADDITIONAL CREATING PURCHASES METHODS ===============*/
    // Validates each category field of the form
    private boolean validateCatValueFields(HashMap<JLabel[], FormFormattedTextField> rawCategories) {
        boolean proceed = true;
        // SETUP VALIDATOR FOR CATEGORY AMOUNT
        FormValidData input = new FormValidData();
        FormRule catAmountRule = new CategoryAmountRule();

        for (HashMap.Entry<JLabel[], FormFormattedTextField> item : rawCategories.entrySet()) {
            input.setCatValueStr(item.getValue().getText());
            if (!catAmountRule.validate(input)) {
                item.getKey()[0].setForeground(Style.redA700());
                item.getKey()[1].setVisible(true);
                item.getValue().setForeground(Style.redA700());
                proceed = false;
            } else {
                item.getKey()[0].setForeground(Color.BLACK);
                item.getKey()[1].setVisible(false);
                item.getValue().setForeground(Color.BLACK);
            }
        }
        return proceed;
    }

    // If validation is successful, this method makes a clone of categories ready to be passed
    // to the makePurchase method in Shop
    private HashMap<Integer, Category> getPurchaseFormCategories(PurchaseEvent e) {
        ArrayList<Category> defaultCategories = new ArrayList<>(db.getAllCategories().values());
        HashMap<Integer, Category> purchaseCategories = new HashMap<>();

        if (validateCatValueFields(e.getCategoriesMap())) {
            for (HashMap.Entry<JLabel[], FormFormattedTextField> item : e.getCategoriesMap().entrySet()) {
                String labelStr = item.getKey()[0].getText();
                String catName = labelStr.substring(0, labelStr.indexOf(":"));
                Double catValue = ((Number)item.getValue().getValue()).doubleValue();

                defaultCategories.forEach((cat) -> {
                    if (cat.getName().equals(catName)) {
                        Category cloneCategory = new Category(cat);
                        cloneCategory.setAmount(catValue);
                        purchaseCategories.put(cat.getId(), cloneCategory);
                    }
                });
            }

            double checkTotal = purchaseCategories.values().stream().mapToDouble(Category::getAmount).sum();
            return (checkTotal <= 0) ? null : purchaseCategories;
        } else {
            return null;
        }
    }

    // If a new card is created with a purchase, this method validates and gets the Card ID
    // from the form and sends it back to the button handler
    private String getPurchaseFormCardID(PurchaseEvent event) {
        JComboBox<String> type = event.getPurchaseTypeCombo();

        if (type.getSelectedItem() != null) {
            if (type.getSelectedItem().equals(PurchaseType.ExistingCardPurchase.getName())) {
                return (String)event.getExistingCardCombo().getSelectedItem();
            } else if (type.getSelectedItem().equals(PurchaseType.NewCardPurchase.getName())) {
                String newCardID = event.getCardIDTextField().getText();

                //SETUP VALIDATOR FOR CARD ID
                FormValidData input = new FormValidData();
                FormRule cardIDRule = new CardIDRule();
                input.setCardID(newCardID);

                if (!cardIDRule.validate(input)) {
                    event.getCardIDTextField().setForeground(Style.redA700());
                    event.getCardIDLabel().setForeground(Style.redA700());
                    event.getCardIDErrorLabel().setVisible(true);
                    return null;
                } else {
                    return newCardID;
                }
            } else if (type.getSelectedItem().equals(PurchaseType.CashPurchase.getName())) {
                return CardType.Cash.getName();
            }
        }
        return null;
    }
}
