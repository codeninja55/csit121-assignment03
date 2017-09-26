package application.view.builderFactory;

import application.controller.validator.CategoryAmountRule;
import application.controller.validator.FormRule;
import application.controller.validator.FormValidData;
import application.model.Generator;
import application.model.card.Card;
import application.model.card.CardType;
import application.model.category.Category;
import application.model.purchase.PurchaseType;
import application.view.customComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class PurchaseForm extends BaseForm implements FormFactory, PurchaseFormView {
    private final int generatedReceiptID;
    private final DefaultComboBoxModel<String> existingCardModel;
    private final ArrayList<Category> categoriesList;
    private final HashMap<JLabel[], FormFormattedTextField> purchaseCategoriesMap;
    private final JComboBox<String> purchaseTypeCombo;
    private final DefaultComboBoxModel<String> options;
    private final JPanel baseCreatePurchaseForm;
    private final FormLabel receiptIDLabel;
    private final FormTextField receiptIDTextField;
    private final FormLabel cardIDLabel;
    private final FormTextField cardIDTextField;
    private final FormLabel existingCardLabel;
    private final JComboBox<String> existingCardCombo;

    private final FormLabel cardTypeLabel;
    private final MaterialRadioButton anonCardRB;
    private final MaterialRadioButton basicCardRB;
    private final MaterialRadioButton premiumCardRB;

    private final FormLabel cardNameLabel;
    private final FormTextField cardNameTextField;
    private final ErrorLabel nameErrLabel;
    private final FormLabel cardEmailLabel;
    private final FormTextField cardEmailTextField;
    private final ErrorLabel emailErrLabel;
    private final ErrorLabel categoryEmptyErrLabel;

    private PurchaseListener createPurchaseListener;
    private final FormButton createBtn;
    private final FormButton clearBtn;
    private final ErrorLabel purchaseErrorLabel;

    /*============================== CONSTRUCTORS ==============================*/
    private PurchaseForm(PurchaseFormBuilder builder) {
        super();
        super.setBorder("New Purchase");
        super.setCancelBtn("Cancel Create Purchase");

        baseCreatePurchaseForm = new JPanel(new GridBagLayout());
        purchaseTypeCombo = new JComboBox<>();
        options = new DefaultComboBoxModel<>();
        purchaseCategoriesMap = new HashMap<>();

        /* NOTE: All FormLabels and FormTextField are hidden by default */
        receiptIDLabel = new FormLabel("Receipt ID: ");
        receiptIDTextField = new FormTextField(20);
        cardIDLabel = new FormLabel("Card ID: ");
        cardIDTextField = new FormTextField(20);

        existingCardLabel = new FormLabel("Select Existing Card: ");
        existingCardCombo = new JComboBox<>();
        existingCardModel = builder.existingCardModel;
        categoriesList = builder.categoriesList;
        generatedReceiptID = builder.generatedReceiptID;

        cardTypeLabel = new FormLabel("Card Type: ");
        ButtonGroup cardTypeRBGroup = new ButtonGroup();
        anonCardRB = new MaterialRadioButton(CardType.AnonCard.getName());
        basicCardRB = new MaterialRadioButton(CardType.BasicCard.getName());
        premiumCardRB = new MaterialRadioButton(CardType.PremiumCard.getName());

        cardNameLabel = new FormLabel("Customer Name: ");
        cardNameTextField = new FormTextField(20);
        nameErrLabel = new ErrorLabel("Name must not be blank.");
        cardEmailLabel = new FormLabel("Customer Email: ");
        cardEmailTextField = new FormTextField(20);
        emailErrLabel = new ErrorLabel("Email must not be blank.");

        createBtn = new FormButton("Add Purchase", Style.addIcon());
        clearBtn = new FormButton("Clear", Style.clearIcon());
        categoryEmptyErrLabel = new ErrorLabel("You must enter some values for at least one category.");
        purchaseErrorLabel = new ErrorLabel("PURCHASE NOT CREATED");

        /* REGISTRATION OF LISTENERS */
        FormListener handler = new FormListener();
        purchaseTypeCombo.addItemListener(handler);
        createBtn.addActionListener(handler);
        clearBtn.addActionListener(handler);
        existingCardCombo.addItemListener(handler);
        anonCardRB.addActionListener(handler);
        basicCardRB.addActionListener(handler);
        premiumCardRB.addActionListener(handler);

        /* SET UP CARD TYPE GROUP */
        cardTypeRBGroup.add(anonCardRB);
        cardTypeRBGroup.add(basicCardRB);
        cardTypeRBGroup.add(premiumCardRB);
        anonCardRB.setSelected(true);

        /* PURCHASE TYPE COMBO BOX - Create the combo box and its model */
        options.addElement("Please Choose Purchase Type Below");
        options.addElement(PurchaseType.ExistingCardPurchase.getName());
        options.addElement(PurchaseType.NewCardPurchase.getName());
        options.addElement(PurchaseType.CashPurchase.getName());

        purchaseTypeCombo.setModel(options);
        purchaseTypeCombo.setSelectedIndex(0);
        purchaseTypeCombo.setEditable(false);
        purchaseTypeCombo.setFont(Style.comboboxFont());
        add(purchaseTypeCombo, BorderLayout.NORTH);

        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                createBasePurchaseForm();
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                baseCreatePurchaseForm.setVisible(false);
                PurchaseForm.super.remove(baseCreatePurchaseForm);
            }
        });
    }

    /*==================== BASE FORM ====================*/

    private void createBasePurchaseForm() {
        // Dynamically creates purchaseCategoriesMap HashMap<FormLabel, FormTextField>
        createCategoriesListForm();
        GridBagConstraints gc = new GridBagConstraints();

        /*========== FIRST ROW - RECEIPT ID ==========*/
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 0; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        baseCreatePurchaseForm.add(receiptIDLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,0,0,10);
        receiptIDTextField.setEditable(false);
        receiptIDTextField.setText(Integer.toString(generatedReceiptID));
        baseCreatePurchaseForm.add(receiptIDTextField, gc);

        /*========== NEW ROW - CARD ID ==========*/
        labelGridConstraints(gc);
        baseCreatePurchaseForm.add(cardIDLabel, gc);
        textFieldGridConstraints(gc);
        cardIDTextField.setEditable(false);
        baseCreatePurchaseForm.add(cardIDTextField, gc);

        /*========== NEW ROW - EXISTING CARD ID ==========*/
        labelGridConstraints(gc);
        baseCreatePurchaseForm.add(existingCardLabel, gc);
        textFieldGridConstraints(gc);
        existingCardCombo.setFont(Style.textFieldFont());
        existingCardCombo.setEditable(false);
        existingCardCombo.setPreferredSize(receiptIDTextField.getPreferredSize());
        existingCardCombo.setMinimumSize(existingCardCombo.getPreferredSize());
        baseCreatePurchaseForm.add(existingCardCombo, gc);
        existingCardCombo.setVisible(false);

        /*========== NEW ROW - CARD TYPE BUTTON GROUP ==========*/
            /* LABEL FOR RADIO GROUP + RADIO BUTTON 1 */
            gc.gridy++; gc.gridx = 0; gc.weighty = 0.005;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.insets = new Insets(10,0,0,10);
            baseCreatePurchaseForm.add(cardTypeLabel, gc);

            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(anonCardRB, gc);
            anonCardRB.setVisible(false);

            /*========== NEW ROW - RADIO BUTTON 2 ==========*/
            textFieldGridConstraints(gc);
            gc.gridy++;
            baseCreatePurchaseForm.add(basicCardRB, gc);
            basicCardRB.setVisible(false);

            /*========== NEW ROW - RADIO BUTTON 3 ==========*/
            textFieldGridConstraints(gc);
            gc.gridy++;
            gc.insets = new Insets(10,0,20,0);
            baseCreatePurchaseForm.add(premiumCardRB, gc);
            premiumCardRB.setVisible(false);

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        gc.weighty = 0.1;
        gc.insets = new Insets(20,0,0,5);
        baseCreatePurchaseForm.add(cardNameLabel, gc);
        textFieldGridConstraints(gc);
        gc.insets = new Insets(20,0,0,0);
        baseCreatePurchaseForm.add(cardNameTextField, gc);

        /*========== NEW ROW - NAME ERROR LABEL ==========*/
        labelGridConstraints(gc);
        baseCreatePurchaseForm.add(new JLabel(""), gc);
        textFieldGridConstraints(gc);
        baseCreatePurchaseForm.add(nameErrLabel, gc);

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        gc.insets = new Insets(10,0,20,0);
        baseCreatePurchaseForm.add(cardEmailLabel, gc);
        textFieldGridConstraints(gc);
        gc.insets = new Insets(10,0,20,0);
        baseCreatePurchaseForm.add(cardEmailTextField, gc);

        /*========== NEW ROW - EMAIL ERROR LABEL ==========*/
        labelGridConstraints(gc);
        baseCreatePurchaseForm.add(new JLabel(""), gc);
        textFieldGridConstraints(gc);
        baseCreatePurchaseForm.add(emailErrLabel, gc);

        /*========== NEW ROW - CATEGORIES LIST ==========*/
        purchaseCategoriesMap.forEach((key, value) -> {
            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(key[0], gc);
            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(value, gc);
            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(new JLabel(""), gc);
            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(key[1], gc);
        });

        /*========== NEW ROW - EMPTY VALUES ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5,0,10,0);
        categoryEmptyErrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryEmptyErrLabel.setFont(new Font("Product Sans", Font.PLAIN, 28));
        baseCreatePurchaseForm.add(categoryEmptyErrLabel, gc);

        /*========== NEW ROW - PURCHASE ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        purchaseErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        purchaseErrorLabel.setFont(new Font("Product Sans", Font.BOLD, 32));
        purchaseErrorLabel.setBorder(Style.formBorder(""));
        baseCreatePurchaseForm.add(purchaseErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 3; gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,1);
        baseCreatePurchaseForm.add(createBtn, gc);

        gc.gridx = 1; gc.gridwidth = 1; gc.weightx = 0.5; gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,1,0,0);
        baseCreatePurchaseForm.add(clearBtn, gc);

        /* BY DEFAULT CLEAR ALL TEXT FIELDS */
        Arrays.stream(baseCreatePurchaseForm.getComponents()).filter((Component c) -> c instanceof FormTextField && ((FormTextField)c).isEditable())
                .forEach((Component comp) -> ((FormTextField) comp).setText(null));

        baseCreatePurchaseForm.setVisible(true);
        JScrollPane formScrollPane = new JScrollPane(baseCreatePurchaseForm);
        formScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.add(formScrollPane, BorderLayout.CENTER);
    }

    private void basePurchaseForm() {
        hideErrorLabels();
        hideAllFormComponents();
    }

    private void existingCardPurchase() {
        hideErrorLabels();
        hideAllFormComponents();

        receiptIDLabel.setVisible(true);
        receiptIDTextField.setVisible(true);

        existingCardCombo.setModel(existingCardModel);
        existingCardCombo.setSelectedIndex(0);
        existingCardLabel.setVisible(true);
        existingCardCombo.setVisible(true);

        setCategoriesVisible();

        createBtn.setEnabled(false);
        createBtn.setVisible(true);
        clearBtn.setVisible(true);
    }

    private void newCardPurchase() {
        hideErrorLabels();
        hideAllFormComponents();
        receiptIDLabel.setVisible(true);
        receiptIDTextField.setVisible(true);

        cardIDLabel.setVisible(true);
        cardIDTextField.setText(Generator.getNextCardID());
        cardIDTextField.setVisible(true);

        cardTypeLabel.setVisible(true);
        anonCardRB.setSelected(true);
        anonCardRB.setVisible(true);
        basicCardRB.setVisible(true);
        premiumCardRB.setVisible(true);

        enableNameAndEmail(false);

        setCategoriesVisible();
        createBtn.setVisible(true);
        createBtn.setEnabled(true);
        clearBtn.setVisible(true);
    }

    private void cashPurchase() {
        hideErrorLabels();
        hideAllFormComponents();
        receiptIDLabel.setVisible(true);
        receiptIDTextField.setVisible(true);

        cardIDLabel.setVisible(true);
        cardIDTextField.setText("Cash");
        cardIDTextField.setVisible(true);

        setCategoriesVisible();

        createBtn.setEnabled(true);
        createBtn.setVisible(true);
        clearBtn.setVisible(true);
    }

    /*==================== LISTENER CALLBACKS ====================*/
    public void setCreatePurchaseListener(PurchaseListener listener) { this.createPurchaseListener = listener; }

    /*============================== MUTATORS  ==============================*/
    private void createCategoriesListForm() {
        // This method extracts each category from the default list and places them
        // into a HashMap with an Array of FormLabel and ErrorLabel with the FormFormattedTextField.
        // It also adds a PropertyChangeListener and MouseClicked listener to each.
        categoriesList.forEach((Category cat) -> {
            JLabel[] labelArr = new JLabel[2];
            String categoryStr = cat.getName() + ": $";
            labelArr[0] = new FormLabel(categoryStr);
            labelArr[0].setName(cat.getName());
            labelArr[1] = new ErrorLabel("INVALID AMOUNT");

            NumberFormat doubleFormat = DecimalFormat.getInstance();
            doubleFormat.setMaximumFractionDigits(2);
            doubleFormat.setMinimumFractionDigits(2);
            FormFormattedTextField catValueTextField = new FormFormattedTextField(doubleFormat);
            catValueTextField.setFocusable(true);
            catValueTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);

            catValueTextField.addPropertyChangeListener(evt -> {
                if (!catValueTextField.isEditValid()) {
                    labelArr[0].setForeground(Style.redA700());
                    labelArr[1].setVisible(true);
                    catValueTextField.setForeground(Style.redA700());
                    createBtn.setEnabled(false);
                } else {
                    labelArr[0].setForeground(Color.BLACK);
                    labelArr[1].setVisible(false);
                    catValueTextField.setForeground(Color.BLACK);
                    createBtn.setEnabled(true);
                }
            });
            catValueTextField.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    catValueTextField.setValue(0.00D);
                    catValueTextField.setCaretPosition(0);
                    labelArr[0].setForeground(Color.BLACK);
                    labelArr[1].setVisible(false);
                    catValueTextField.setForeground(Color.BLACK);
                }
            });
            this.purchaseCategoriesMap.put(labelArr, catValueTextField);
        });
    }

    private void setCategoriesVisible() {
        purchaseCategoriesMap.forEach((JLabel[] k, FormFormattedTextField v) ->{
            k[0].setVisible(true);
            k[1].setVisible(false);
            v.setVisible(true); });
    }

    private void labelGridConstraints(GridBagConstraints gc) {
        gc.gridy++;
        gc.gridx = 0;
        gc.insets = new Insets(10,0,0,10);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
    }

    private void textFieldGridConstraints(GridBagConstraints gc) {
        gc.gridx = 1;
        gc.insets = new Insets(10,0,0,0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
    }

    private void hideAllFormComponents() {
        Arrays.stream(baseCreatePurchaseForm.getComponents()).forEach((c)-> c.setVisible(false));
    }

    private void enableNameAndEmail (boolean isVisible) {
        cardNameLabel.setEnabled(isVisible);
        cardNameTextField.setEditable(isVisible);
        cardEmailLabel.setEnabled(isVisible);
        cardEmailTextField.setEditable(isVisible);
        cardNameLabel.setVisible(true);
        cardNameTextField.setVisible(true);
        cardEmailLabel.setVisible(true);
        cardEmailTextField.setVisible(true);
    }

    private void hideErrorLabels() {
        Arrays.stream(baseCreatePurchaseForm.getComponents()).filter(c -> c instanceof ErrorLabel)
                .forEach((comp) -> comp.setVisible(false));
    }

    /*============================== VALIDATION ==============================*/
    private boolean validateCatValueFields(HashMap<JLabel[], FormFormattedTextField> rawCategories) {
        boolean proceed = true;
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

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getCardID() { return cardIDTextField.getText(); }
    public String getExistingCardID() { return existingCardModel.getElementAt(existingCardCombo.getSelectedIndex()); }
    public String getCardName() { return cardNameTextField.getText().trim(); }
    public String getCardEmail() { return cardEmailTextField.getText().trim(); }
    public CardType getCardType() {
        if (anonCardRB.isSelected()) return CardType.AnonCard;
        else if (basicCardRB.isSelected()) return CardType.BasicCard;
        else return CardType.PremiumCard;
    }
    public int getReceiptID() { return Integer.parseInt(receiptIDTextField.getText()); }
    public String getPurchaseType() { return (String)purchaseTypeCombo.getSelectedItem(); }

    public HashMap<Integer, Category> getCategories() {
        if (validateCatValueFields(purchaseCategoriesMap)) {
            HashMap<Integer, Category> purchaseCategories = new HashMap<>();
            purchaseCategoriesMap.forEach((k, v) -> {
                String catName = k[0].getText().substring(0, k[0].getText().indexOf(":"));
                Double catValue = ((Number)v.getValue()).doubleValue();

                categoriesList.stream().filter(c -> c.getName().equals(catName)).forEach(c -> {
                    Category cloneCategory = new Category(c);
                    cloneCategory.setAmount(catValue);
                    purchaseCategories.put(c.getId(), cloneCategory);
                });
            });
            return purchaseCategories;
        } else {
            return null;
        }
    }

    /*============================== INNER CLASS ==============================*/
    /*============================== BUILDER ==============================*/
    static class PurchaseFormBuilder {
        private final int generatedReceiptID;
        private DefaultComboBoxModel<String> existingCardModel;
        private ArrayList<Category> categoriesList;

        PurchaseFormBuilder(int generatedReceiptID) {
            this.generatedReceiptID = generatedReceiptID;
        }

        PurchaseFormBuilder existingCardModel(ArrayList<Card> cardsList) {
            DefaultComboBoxModel<String> cardModel = new DefaultComboBoxModel<>();
            cardsList.sort(Comparator.comparing(Card::getID));
            cardModel.addElement("Please Select..");
            cardsList.forEach(c->cardModel.addElement(c.getID()));
            this.existingCardModel = cardModel;
            return this;
        }

        PurchaseFormBuilder categoriesList(ArrayList<Category> categoriesList) {
            categoriesList.sort(Comparator.comparingInt(Category::getId));
            this.categoriesList = categoriesList;
            return this;
        }

        PurchaseForm build() {
            return new PurchaseForm(this);
        }
    }

    /*============================== CALLBACK HANDLER ==============================*/
    private class FormListener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createBtn) {
                hideErrorLabels();
                boolean proceed = true;

                if (purchaseTypeCombo.getSelectedItem() == PurchaseType.NewCardPurchase.getName()) {
                    if (!anonCardRB.isSelected()) {
                        if (cardNameTextField.getText() == null || cardNameTextField.getText().isEmpty()) {
                            nameErrLabel.setVisible(true);
                            proceed = false;
                        }

                        if (cardEmailTextField.getText() == null || cardEmailTextField.getText().isEmpty()) {
                            emailErrLabel.setVisible(true);
                            proceed = false;
                        }
                    }
                }

                if (getCategories().values().stream().mapToDouble(Category::getAmount).sum() <= 0) {
                    categoryEmptyErrLabel.setVisible(true);
                    proceed = false;
                }

                if (proceed && createPurchaseListener != null) createPurchaseListener.formSubmitted(PurchaseForm.this);
                else purchaseErrorLabel.setVisible(true);
            } else if (e.getSource() == clearBtn) {
                hideErrorLabels();
                Arrays.stream(baseCreatePurchaseForm.getComponents()).forEach(c -> {
                        if (c instanceof JTextField && ((JTextField) c).isEditable()) ((JTextField) c).setText("");
                        if (c instanceof FormFormattedTextField) ((FormFormattedTextField) c).setValue(0.00D);
                        if (c instanceof FormLabel) c.setForeground(Color.BLACK);
                });
            } else if (e.getSource() == anonCardRB) {
                enableNameAndEmail(false);
            } else if (e.getSource() == basicCardRB || e.getSource() == premiumCardRB) {
                enableNameAndEmail(true);
            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == purchaseTypeCombo) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals(options.getElementAt(0))) basePurchaseForm();
                    else if (e.getItem().equals(options.getElementAt(1))) existingCardPurchase();
                    else if (e.getItem().equals(options.getElementAt(2))) newCardPurchase();
                    else if (e.getItem().equals(options.getElementAt(3))) cashPurchase();
                }
            } else if (e.getSource() == existingCardCombo) {
                if (!e.getItem().equals(existingCardModel.getElementAt(0))) createBtn.setEnabled(true);
                else createBtn.setEnabled(false);
            }
        }

    }

}
