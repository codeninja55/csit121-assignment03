package cn55.controller;

import cn55.model.CardModel.*;
import cn55.model.*;
import cn55.model.DataStoreModel;

import java.awt.*;
import java.util.*;

public class Program {

    private final Shop shop;
    private final DataStoreModel db;
    /*private final MainFrame mainFrame;
    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;*/

    public Program() {
        shop = new Shop();
        /* Singleton Design Pattern - Only one instance of PersistentData available */
        db = DataStoreModel.getDataStoreInstance();
        createTestCode(shop);
        //createTooManyCategories();
        db.writeCards();
        db.writePurchases();
        db.writeCategories();

        

        /*this.mainFrame = new MainFrame();
        this.tabPane = mainFrame.getTabPane();

        *//* REGISTRATION AND INITIAL UPDATE CALLS FOR DATABASE OBSERVER PATTERN *//*
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

        setupViewListeners();*/
    }

    private ArrayList<Component> getAllComponents(final Container container) {
        // REFERENCE: http://www.java2s.com/Code/Java/Swing-JFC/GetAllComponentsinacontainer.html
        Component[] comps = container.getComponents();
        ArrayList<Component> compList = new ArrayList<>();

        for (Component c : comps) {
            compList.add(c);
            if (c instanceof Container)
                compList.addAll(getAllComponents((Container) c));
        }

        return compList;
    }

    /* TODO - REMOVE TEST CODE */
    @SuppressWarnings("ConstantConditions")
    private Double generateRandomValue() {
        Random randomValueObj = new Random();
        ArrayList<Double> valueOptions = new ArrayList<>();
        valueOptions.add(0D);
        valueOptions.add(10D);
        valueOptions.add(20D);
        valueOptions.add(30D);
        valueOptions.add(40D);
        valueOptions.add(50D);
        valueOptions.add(60D);
        valueOptions.add(70D);
        valueOptions.add(80D);
        valueOptions.add(90D);
        valueOptions.add(100D);
        valueOptions.add(200D);
        valueOptions.add(300D);
        valueOptions.add(400D);
        valueOptions.add(500D);

        return valueOptions.get(randomValueObj.nextInt(valueOptions.size()));
    }

    /* TODO - REMOVE TEST CODE */
    private HashMap<Integer, Category> generateRandomCategoriesMap() {
        HashMap<Integer, Category> testingCategoryMap = new HashMap<>();
        db.getCategories().forEach((cat) -> testingCategoryMap.put(cat.getId(), new Category(cat)));
        testingCategoryMap.forEach((k,v)->v.setAmount(generateRandomValue()));

        return testingCategoryMap;
    }

    /* TODO - REMOVE TEST CODE */
    private void testMakePurchases(int numOfPurchases, String id) {
        for (int i = 0; i < numOfPurchases; i++)
            shop.makePurchase(id, DataStoreModel.generateReceiptID(), generateRandomCategoriesMap());
    }

    /* TODO - REMOVE TEST CODE */
    private void createTestCode(Shop shop) {

        // Cash purchase test
        testMakePurchases(4, "Cash");

        // AnonCard Test
        db.addCards(new AnonCard()); // MC10001
        testMakePurchases(1, "MC10001");

        db.addCards(new AnonCard()); // MC10002
        testMakePurchases(2, "MC10002");

        // BasicCard Test
        db.addCards(new BasicCard("Natasha Romanov",
                "blackwidow@avengers.team", 0));
        testMakePurchases(2,"MC10003");

        // More Cash purchases
        testMakePurchases(3, "Cash");

        // BasicCard Test 2
        db.addCards(new BasicCard("Steve Rogers",
                "captain_a@avengers.team",0D));
        testMakePurchases(2, "MC10004");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // More Romanov purchases
        testMakePurchases(5,"MC10003");

        // PremiumCard Test
        db.addCards(new PremiumCard("Tony Stark",
                "ironman@avengers.team",0));

        testMakePurchases(5,"MC10005");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // BasicCard Test 2
        db.addCards(new BasicCard("Bruce Banner", "hulk@avengers.team", 0D));
        testMakePurchases(2,"MC10006");

        // PremiumCard Test 2
        db.addCards(new PremiumCard("Nick Fury",
                "nick@shield.com",0));

        testMakePurchases(3, "MC10007");

        db.addCards(new AnonCard()); //MC10008
        testMakePurchases(2,"MC10008");

        db.addCards(new AnonCard()); //MC10009
        db.getCards().get(7).calcPoints(300D);

        db.addCards(new BasicCard("Hank Pym","ants@avengers.team",0)); // MC10009
        db.addCards(new BasicCard("Peter Parker", "spidey@avengers.team",0)); // MC10010
        db.addCards(new PremiumCard("Danny Rand","danny@randcorp.com",5000)); // MC10011
        testMakePurchases(3, "MC10012");
        db.addCards(new PremiumCard("Professor Charles Xavier", "x@xmen.com", 1238798)); // MC100012
        testMakePurchases(2,"MC10013");

        db.addCards(new BasicCard("Matthew Murdock","thedevil@hellskitchen.com", 666));
        db.addCards(new BasicCard("Thor Odinson", "thor@asgard.com",9000));

        testMakePurchases(2,"MC10005");
        shop.makePurchase("Cash", DataStoreModel.generateReceiptID(),
                generateRandomCategoriesMap());

        testMakePurchases(2,"MC10016");
        testMakePurchases(2,"MC10005");
        testMakePurchases(2,"MC10019");

        shop.makePurchase("Cash", DataStoreModel.generateReceiptID(),
                generateRandomCategoriesMap());

        db.addCards(new BasicCard("Clint Barton", "better_than_arrow@marvel.com", 500));
        db.addCards(new AnonCard());
        testMakePurchases(2,"MC10017");
        db.addCards(new PremiumCard("Oliver Queen", "the_best_arrow_user@dc.comics", 1500));
        testMakePurchases(3,"MC10018");
        db.addCards(new BasicCard("Loki", "the-baddest-dude@asgard.com", 0));
        testMakePurchases(2,"MC10017");
        db.addCards(new AnonCard());
        db.addCards(new AnonCard());
        db.addCards(new PremiumCard("Pepper Potts", "ceo@starkindustries.com", 1987234));
        testMakePurchases(1,"MC10005");
        db.addCards(new AnonCard());
        db.addCards(new AnonCard());
        db.addCards(new PremiumCard("King T'Challa", "tokenman@wakanda.africa", 105023));
        db.addCards(new AnonCard());
        db.addCards(new BasicCard("Logan", "wolverine@xmen.com", 0));
    }

    /* TODO - REMOVE TEST CODE */
    private void createTooManyCategories() {
        for (int i = 0; i < 35; i++)
            db.addCategory(new Category(String.format("%s%d","Testing", i)));
    }

    /*============================== REGISTER AND HANDLE EVENTS ==============================*//*
    private void setupViewListeners() {
        *//* TAB PANE LISTENER *//*
        // This method removes forms from the selected pane if the user selects another pane
        tabPane.addChangeListener((ChangeEvent e) -> {
            *//* DESELECTED LISTENERS *//*
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

        *//*============================== CARD VIEW HANDLERS ==============================*//*
        *//* TOOLBAR | CREATE CARD BUTTON *//*
        cardViewPane.setCreateCardListener(() -> {
            removeCardForms();
            cardViewPane.setCardForm(new CardForm());
            CardForm form = cardViewPane.getCardForm();
            cardViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);
            form.createBaseCreateCardForm();

            *//* Setup a text pane to put all the necessary data into *//*
            ResultsPane resultsPane = cardViewPane.getResultsPane();
            resultsPane.setVisible(false);
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setCardViewMouseListeners();

            *//* ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            form.setCancelListener(() -> {
                cardViewPane.getCardForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
                removeResultsPane(resultsPane);
            });

            *//* ADD A CREATE BUTTON LISTENER AFTER CREATING FORM *//*
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

                int cardIndex = db.getCardMap().get(e.getCardIDTextField().getText().toUpperCase());
                showResultsPane(db.getCards().get(cardIndex).toString(), resultsPane, resultsTextPane);
                removeCardForms();
            });
        });

        *//* TOOLBAR | DELETE CARD BUTTON *//*
        cardViewPane.setDeleteCardListener(() -> {
            removeCardForms();
            cardViewPane.setDeleteForm(new DeleteCardForm());
            DeleteCardForm form = cardViewPane.getDeleteForm();
            cardViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            *//* Setup a text pane to put all the necessary data into *//*
            ResultsPane resultsPane = cardViewPane.getResultsPane();
            resultsPane.setVisible(false);
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setCardViewMouseListeners();

            *//* REGISTER A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            form.setCancelListener(() -> {
                cardViewPane.getDeleteForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
                removeResultsPane(resultsPane);
            });

            *//* REGISTER A DELETE BUTTON LISTENER AFTER CREATING FORM *//*
            form.setDeleteListener(e -> {
                String cardID = e.getIdTextField().getText().toUpperCase();

                *//* SETUP VALIDATOR FOR CARD ID *//*
                FormValidData input = new FormValidData();
                input.setCardID(cardID);
                FormRule rule = new CardIDRule();
                ExistsRule existsRule = new CardExistsRule();

                int cardIndex = existsRule.existsValidating(input);

                if (!cardID.isEmpty() && cardIndex >= 0) {
                    e.getErrorLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(false);
                    e.getDeleteErrorLabel().setVisible(false);

                    showResultsPane(db.getCards().get(cardIndex).toString(), resultsPane, resultsTextPane);
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
                        shop.deleteCard(cardIndex);
                        // Purchases by this card will be changed to cash
                        shop.convertPurchase(cardID);
                        removeCardForms();
                        removeResultsPane(resultsPane);
                    } else {
                        e.getIdTextField().setText(null);
                        e.getDeleteErrorLabel().setVisible(true);
                    }
                } else {
                    if (!rule.validate(input)){
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

        *//* TOOLBAR | SEARCH BUTTON *//*
        cardViewPane.setSearchCardListener(() -> {
            removeCardForms();
            cardViewPane.setSearchForm(new SearchForm());
            cardViewPane.add(cardViewPane.getSearchForm(), BorderLayout.WEST);
            cardViewPane.getSearchForm().setVisible(true);

            *//* ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            cardViewPane.getSearchForm().setCancelListener(() -> {
                cardViewPane.getSearchForm().setVisible(false);
                cardViewPane.getResultsPane().setVisible(false);
                removeCardForms();
            });

            *//* ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            cardViewPane.getSearchForm().setSearchListener(e -> {
                *//* Setup a text pane to put all the necessary data into *//*
                ResultsPane resultsPane = cardViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setCardViewMouseListeners();

                String cardID = e.getSearchIDTextField().getText().toUpperCase();

                *//* SETUP VALIDATOR FOR CARD ID *//*
                FormValidData input = new FormValidData();
                input.setCardID(cardID);
                FormRule cardIDRule = new CardIDRule();
                ExistsRule cardExistsRule = new CardExistsRule();

                int cardIndex = cardExistsRule.existsValidating(input);

                if (!cardID.isEmpty() && cardIndex >= 0) {
                    e.getErrorLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(false);

                    String cardText = db.getCards().get(cardIndex).toString();
                    StringBuilder purchaseText = new StringBuilder("");

                    db.getPurchases().forEach((p)->{
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

        *//* TOOLBAR | VIEW BUTTON *//*
        cardViewPane.setViewCardListener(() -> {
            if (cardViewPane.getCardTablePane().getSelectedRow() >= 0) {
                removeCardForms();

                int selectedRow = cardViewPane.getCardTablePane().getSelectedRow();
                String cardID = (String)cardViewPane.getCardTablePane().getValueAt(selectedRow, 0);

                ResultsPane resultsPane = cardViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setCardViewMouseListeners();

                String cText = db.getCards().get(db.getCardMap().get(cardID)).toString();
                StringBuilder pText = new StringBuilder("");

                db.getPurchases().forEach((p)-> {
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

        *//* TOOLBAR | SORT COMBOBOX *//*
        cardViewPane.getSortedCombo().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem().equals("Sort..") || e.getItem().equals(SortCardType.CreatedOrder.getName()))
                    // Lambda version of sorting with Comparator comparing method
                    db.getCards().sort(Comparator.comparing(Card::getID));
                else if (e.getItem().equals(SortCardType.ReverseCreatedOrder.getName()))
                    // Lambda version of sorting with Comparator
                    db.getCards().sort((Card c1, Card c2)->c2.getID().compareTo(c1.getID()));
                else if (e.getItem().equals(SortCardType.Name.getName()))
                    // Lambda version of sorting with Comparator
                    db.getCards().sort(((c1, c2) -> {
                        if (c1 instanceof AdvancedCard && c2 instanceof AdvancedCard)
                            return ((AdvancedCard) c1).getName().compareTo(((AdvancedCard) c2).getName());

                        if (c1 instanceof AnonCard && c2 instanceof AdvancedCard)
                            return -1;
                        else
                            return 1;
                    }));
                else if (e.getItem().equals(SortCardType.Points.getName()))
                    // Lambda version of sorting with Comparator comparingDouble method
                    db.getCards().sort(Comparator.comparingDouble(Card::getPoints));
                db.notifyObservers();
            }
        });

        *//*============================== PURCHASE VIEW HANDLERS ==============================*//*
        *//* TOOLBAR | CREATE BUTTON *//*
        purchaseViewPane.setCreatePurchaseListener(() -> {
            removePurchaseForms();
            purchaseViewPane.setCreatePurchaseForm(new PurchaseForm());
            PurchaseForm form = purchaseViewPane.getCreatePurchaseForm();
            purchaseViewPane.add(form, BorderLayout.WEST);

            form.getPurchaseTypeCombo().setSelectedIndex(0);
            form.setGeneratedReceiptID(PersistentData.generateReceiptID());
            form.setCardModel(db.getCardModel());
            form.setCategoriesList(db.getCategories());
            form.createBasePurchaseForm();
            form.setVisible(true);

            *//* SET UP A RESULTS PANE TO SHOW END RESULT *//*
            ResultsPane resultsPane = purchaseViewPane.getResultsPane();
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setPurchaseViewPaneMouseListeners();

            *//* FORM CANCEL BUTTON *//*
            form.setCancelPurchaseListener(() -> {
                form.setVisible(false);
                removePurchaseForms();
            });

            *//* FORM CREATE BUTTON *//*
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
                        resultsText = db.getPurchases().get(db.getPurchaseMap().get(receiptID)).toString();
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
                        resultsText = db.getCards().get(db.getCardMap().get(cardID)).toString() +
                                db.getPurchases().get(db.getPurchaseMap().get(receiptID));
                        showResultsPane(resultsText,resultsPane,resultsTextPane);
                        removePurchaseForms();
                    } else if (type.getSelectedItem().equals(PurchaseType.CashPurchase.getName())) {
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getPurchases().get(db.getPurchaseMap().get(receiptID)).toString();
                        showResultsPane(resultsText,resultsPane,resultsTextPane);
                        removePurchaseForms();
                    }
                } else {
                    event.getPurchaseErrorLabel().setVisible(true);
                }
            });
        });

        *//* TOOLBAR | SUMMARY BUTTON *//*
        purchaseViewPane.setSummaryListener(() -> {
            ResultsPane resultsPane = purchaseViewPane.getResultsPane();
            resultsPane.setResultsTextPane();
            ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
            setPurchaseViewPaneMouseListeners();

            double cashTotal = 0;
            double cardTotal = 0;
            double allTotal = 0;

            for (Purchase purchase : db.getPurchases()) {
                double purchaseTotal = purchase.getCategoriesTotal();
                if (purchase.getCardType().equals(CardType.Cash.getName()))
                    cashTotal += purchaseTotal;
                else
                    cardTotal += purchaseTotal;
                allTotal += purchaseTotal;
            }

            String resultsText = String.format("%n%n%s%n%n%s: $%.2f%n%n%s: $%.2f%n%n%s: $%.2f", "SUMMARY OF PURCHASES",
                    "Card Purchase Total", cardTotal,
                    "Cash Purchase Total", cashTotal,
                    "Total for All Purchases", allTotal);

            showResultsPane(resultsText, resultsPane, resultsTextPane);
            purchaseViewPane.revalidate();
            purchaseViewPane.repaint();
        });

        *//* TOOLBAR | VIEW  BUTTON *//*
        purchaseViewPane.setViewPurchaseListener(() -> {
            if (purchaseViewPane.getPurchaseTablePane().getSelectedRow() > 0) {
                int selectedRow = purchaseViewPane.getPurchaseTablePane().getSelectedRow();
                Integer receiptID = (Integer) purchaseViewPane.getPurchaseTablePane().getValueAt(selectedRow, 0);

                ResultsPane resultsPane = purchaseViewPane.getResultsPane();
                resultsPane.setResultsTextPane();
                ResultsPane.ResultsTextPane resultsTextPane = resultsPane.getResultsTextPane();
                setPurchaseViewPaneMouseListeners();

                String resultsText = "";
                for (Purchase purchase : db.getPurchases()) {
                    if (purchase.getReceiptID() == receiptID)
                        resultsText = purchase.toString();
                }

                showResultsPane(resultsText, resultsPane, resultsTextPane);

            }
        });

        *//* TOOLBAR | SORT COMBOBOX *//*
        purchaseViewPane.getSortPurchaseCombo().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ArrayList<Purchase> tempPurchases = new ArrayList<>();
                if (e.getItem().equals(SortPurchaseType.All.getName())) {
                    purchaseViewPane.update();
                } else if (e.getItem().equals(SortPurchaseType.Card.getName())) {
                    *//* NEGATIVE CASH VALIDATION *//*
                    for (Purchase item : db.getPurchases())
                        if (item.getCardID() != null) tempPurchases.add(item);
                    purchaseViewPane.sortPurchaseTableMode(tempPurchases);
                } else if (e.getItem().equals(SortPurchaseType.Cash.getName())) {
                    *//* POSITIVE CASH Validation *//*
                    for (Purchase item : db.getPurchases())
                        if (item.getCardID() == null) tempPurchases.add(item);
                    purchaseViewPane.sortPurchaseTableMode(tempPurchases);
                }
            }
        });

        *//*=========================== CATEGORIES VIEW HANDLERS ===========================*//*
        *//* TOOLBAR | CREATE CATEGORY BUTTON *//*
        categoriesViewPane.setCreateCategoryListener(() -> {
            removeCategoryForms();
            categoriesViewPane.setCreateCategoryForm(new CategoriesForm());
            CategoriesForm form = categoriesViewPane.getCreateCategoryForm();
            categoriesViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            *//* ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            form.setCancelListener(() -> {
                form.setVisible(false);
                removeCategoryForms();
            });

            *//* ADD A CREATE BUTTON LISTENER AFTER CREATING FORM *//*
            form.setCreateCategoryListener(e -> {
                shop.makeCategory(new Category(e.getCategoryNameTextField().getText(),
                        e.getCategoryDescTextField().getText()));

                form.setVisible(false);
                removeCategoryForms();
            });
        });

        *//* TOOLBAR | DELETE CATEGORY BUTTON *//*
        categoriesViewPane.setDeleteCategoryListener(() -> {
            removeCategoryForms();
            categoriesViewPane.setDeleteCategoryForm(new DeleteCategoryForm());
            DeleteCategoryForm form = categoriesViewPane.getDeleteCategoryForm();

            categoriesViewPane.add(form, BorderLayout.WEST);
            form.setVisible(true);

            *//* ADD A CANCEL BUTTON LISTENER AFTER CREATING FORM *//*
            form.setCancelListener(() -> {
                form.setVisible(false);
                removeCategoryForms();
            });

            *//* ADD A DELETE BUTTON LISTENER AFTER CREATING FORM *//*
            form.setDeleteListener(e -> {
                String categoryIDStr = e.getIdTextField().getText();

                *//* SETUP VALIDATOR FOR CATEGORY ID *//*
                FormValidData input = new FormValidData();
                input.setCategoryID(categoryIDStr);
                FormRule validIDRule = new CategoryIDRule();
                ExistsRule existsIDRule = new CategoryExistsRule();

                if (!validIDRule.validate(input)) {
                    e.getErrorLabel().setVisible(false);
                    e.getOthersDeleteErrLabel().setVisible(false);
                    e.getRuleErrLabel().setVisible(true);
                    e.getIdLabel().setForeground(Style.redA700());
                    e.getIdTextField().setForeground(Style.redA700());
                    e.getDeleteErrorLabel().setVisible(true);
                } else {
                    int categoryIndex = existsIDRule.existsValidating(input);

                    if (Integer.parseInt(categoryIDStr) == 100) {
                        // Do NOT allow user to delete category Others
                        e.getErrorLabel().setVisible(false);
                        e.getRuleErrLabel().setVisible(false);
                        e.getOthersDeleteErrLabel().setVisible(true);
                        e.getDeleteErrorLabel().setVisible(true);
                    } else if (categoryIndex >= 0) {
                        int categoryID = Integer.parseInt(categoryIDStr);

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

    *//*============================== MUTATORS  ==============================*//*
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

    *//*==================== REMOVING FORMS METHODS ====================*//*
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
    *//*===============================================================*//*

    *//*=============== SETTING MOUSE LISTENERS METHODS ===============*//*
    // These methods add a mouse listener to the JTable whenever a ResultsPane is added to the right
    private void setCardViewMouseListeners() {
        *//* SET UP A MOUSE LISTENER TO CLOSE PANEL WHEN CLICKING ON TABLE OR OUTER PANEL*//*
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
        *//* SET UP A MOUSE LISTENER TO CLOSE PANEL WHEN CLICKING ON TABLE OR OUTER PANEL*//*
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
    *//*===============================================================*//*

    *//*=============== ADDITIONAL CREATING PURCHASES METHODS ===============*//*
    // Validates each category field of the form
    private boolean validateCatValueFields(HashMap<JLabel[], FormFormattedTextField> rawCategories) {
        boolean proceed = true;
        *//* SETUP VALIDATOR FOR CATEGORY AMOUNT *//*
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
        ArrayList<Category> defaultCategories = db.getCategories();
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

                *//* SETUP VALIDATOR FOR CARD ID *//*
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
    *//*====================================================================*/

}
