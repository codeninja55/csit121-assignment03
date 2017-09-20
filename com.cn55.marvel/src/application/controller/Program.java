package application.controller;

import application.controller.validator.*;
import application.model.cardModel.*;
import application.model.categoryModel.Category;
import application.model.DataDAO;
import application.model.purchaseModel.Purchase;
import application.model.purchaseModel.PurchaseType;
import application.model.purchaseModel.SortPurchaseType;
import application.model.Shop;
import application.view.CardViewPane;
import application.view.CategoriesViewPane;
import application.view.customComponents.FormFormattedTextField;
import application.view.customComponents.ResultsPane;
import application.view.customComponents.Style;
import application.view.builderFactory.*;
import application.view.MainFrame;
import application.view.PurchaseViewPane;

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

    public Program() {
        /* Singleton Design Pattern - Only one instance of Shop available */
        shop = Shop.getShopInstance();
        db = shop.getDataStore();
        db.readData();

        this.mainFrame = new MainFrame();

        /* Windows Closing Listener */
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            int confirmSave = JOptionPane.showConfirmDialog(mainFrame,"\n\nWould you like to save your session data?\n\n",
                    "Save on Exit", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);

            if (confirmSave == JOptionPane.OK_OPTION) {
                db.writeData();
            }
            System.gc(); // Garbage collector
            mainFrame.dispose();
            }
        });

        this.tabPane = mainFrame.getTabPane();
        tabPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // DESELECTED LISTENERS
                if (tabPane.getSelectedComponent() != purchaseViewPane) removePurchaseForms();
                if (tabPane.getSelectedComponent() != cardViewPane) removeCardForms();
                if (tabPane.getSelectedComponent() != categoriesViewPane) removeCategoryForms();
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

        setupCardViewHandlers();
        setupPurchaseViewHandlers();
        setupCategoriesViewHandlers();
    }

    /*============================== REGISTER AND HANDLE EVENTS ==============================*/
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
            if (cardViewPane.getCardTablePane().getSelectedRow() >= 0) {
                removeCardForms();
                int selectedRow = cardViewPane.getCardTablePane().getSelectedRow();
                final String cardID = (String)cardViewPane.getCardTablePane().getValueAt(selectedRow, 0);
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
            form.setCreatePurchaseListener(event -> {
                final JComboBox<String> type = event.getPurchaseTypeCombo();
                String receiptIDStr = event.getReceiptIDTextField().getText();
                final int receiptID = Integer.parseInt(receiptIDStr);
                final String cardID = getPurchaseFormCardID(event);
                HashMap<Integer, Category> categories = getPurchaseFormCategories(event);
                String resultsText = "";

                if (type.getSelectedItem() != null && cardID != null && categories != null) {
                    if (type.getSelectedItem().equals(PurchaseType.ExistingCardPurchase.getName())) {
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getPurchase(receiptID).toString();
                    } else if (type.getSelectedItem().equals(PurchaseType.NewCardPurchase.getName())) {
                        String cardType = null, name = null, email = null;
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
                    } else if (type.getSelectedItem().equals(PurchaseType.CashPurchase.getName())) {
                        shop.makePurchase(cardID, receiptID, categories);
                        resultsText = db.getPurchase(receiptID).toString();
                    }
                    removePurchaseForms();
                    showResults(purchaseViewPane, resultsText);
                } else {
                    event.getPurchaseErrorLabel().setVisible(true);
                }
            });
        });

        /*TOOLBAR | SUMMARY BUTTON*/
        purchaseViewPane.setSummaryListener(() -> {
            Predicate<Purchase> cashPurchases = e -> (e.getCardType().equals(CardType.Cash.getName()));

            double cashTotal = db.getAllPurchases().values().stream().filter(cashPurchases)
                    .mapToDouble(Purchase::getCategoriesTotal).sum();
            double cardTotal = db.getAllPurchases().values().stream().filter(p -> !p.getCardType().equals(CardType.Cash.getName()))
                    .mapToDouble(Purchase::getCategoriesTotal).sum();
            double allTotal = db.getAllPurchases().values().stream().mapToDouble(Purchase::getCategoriesTotal).sum();

            String resultsText = String.format("%n%s%n%n%s: $%.2f%n%n%s: $%.2f%n%n%s: $%.2f", "SUMMARY OF PURCHASES",
                    "Card Purchase Total", cardTotal, "Cash Purchase Total", cashTotal, "Total for All Purchases", allTotal);

            showResults(purchaseViewPane, resultsText);
        });

        /*TOOLBAR | VIEW  BUTTON*/
        purchaseViewPane.setViewPurchaseListener(() -> {
            if (purchaseViewPane.getPurchaseTablePane().getSelectedRow() >= 0) {
                int selectedRow = purchaseViewPane.getPurchaseTablePane().getSelectedRow();
                final int receiptID = (Integer)purchaseViewPane.getPurchaseTablePane().getValueAt(selectedRow, 0);

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
                            .filter(c -> c.getCardType().equals(CardType.Cash.getName()))
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
                .forEach(c -> {c.setVisible(false); categoriesViewPane.remove(c);});
    }

    /*============ ADDITIONAL CREATING PURCHASES METHODS ============*/
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
            e.getCategoriesMap().forEach((k,v) -> {
                String labelStr = k[0].getText();
                String catName = labelStr.substring(0, labelStr.indexOf(":"));
                Double catValue = ((Number)v.getValue()).doubleValue();

                defaultCategories.stream().filter(c -> c.getName().equals(catName)).forEach(c -> {
                    Category cloneCategory = new Category(c);
                    cloneCategory.setAmount(catValue);
                    purchaseCategories.put(c.getId(), cloneCategory);
                });
            });

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

    /*============================== ACCESSORS ==============================*/
    private String printCard(String cardID, String title) {
        String cText = db.getCard(cardID).toString();
        String pText = db.getAllPurchases().values().stream().filter(p -> p.getCardID() != null && p.getCardID().equals(cardID))
                .map(Purchase::toString).collect(Collectors.joining("\n"));
        return String.format("%s%n%s%n%s%n%s", title, cText,"PURCHASE(S)", pText);
    }
}
