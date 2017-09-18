package application.view.builderFactory;

import application.model.cardModel.Card;
import application.model.cardModel.CardType;
import application.model.categoryModel.Category;
import application.model.Generator;
import application.model.purchaseModel.PurchaseType;
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

public class PurchaseForm extends JPanel implements FormFactory {
    private int generatedReceiptID;
    private DefaultComboBoxModel<String> existingCardModel;
    private ArrayList<Category> categoriesList;
    private HashMap<JLabel[], FormFormattedTextField> categoriesMap;

    private JComboBox<String> purchaseTypeCombo;
    private DefaultComboBoxModel<String> options;
    private CancelButton cancelBtn;

    private JPanel baseCreatePurchaseForm;
    private FormLabel receiptIDLabel;
    private FormTextField receiptIDTextField;
    private FormLabel cardIDLabel;
    private FormTextField cardIDTextField;
    private ErrorLabel cardIDErrorLabel;
    private FormLabel existingCardLabel;
    private JComboBox<String> existingCardCombo;

    private FormLabel cardTypeLabel;
    private FormRadioButton anonCardRB;
    private FormRadioButton basicCardRB;
    private FormRadioButton premiumCardRB;

    private FormLabel cardNameLabel;
    private FormTextField cardNameTextField;
    private FormLabel cardEmailLabel;
    private FormTextField cardEmailTextField;

    private PurchaseListener createPurchaseListener;
    private FormButton createBtn;
    private FormButton clearBtn;
    private ErrorLabel purchaseErrorLabel;

    /*============================== CONSTRUCTORS ==============================*/
    private PurchaseForm(PurchaseFormBuilder builder) {
        /* INITIALIZE ALL COMPONENTS */
        baseCreatePurchaseForm = new JPanel(new GridBagLayout());
        purchaseTypeCombo = new JComboBox<>();
        options = new DefaultComboBoxModel<>();
        cancelBtn = new CancelButton("Cancel New Purchase");

        /* NOTE: All FormLabels and FormTextField are hidden by default */
        receiptIDLabel = new FormLabel("Receipt ID: ");
        receiptIDTextField = new FormTextField(20);
        cardIDLabel = new FormLabel("Card ID: ");
        cardIDTextField = new FormTextField(20);
        cardIDErrorLabel = new ErrorLabel("INVALID CARD ID");
        existingCardLabel = new FormLabel("Select Existing Card: ");
        existingCardCombo = new JComboBox<>();
        existingCardModel = builder.existingCardModel;
        categoriesList = builder.categoriesList;
        generatedReceiptID = builder.generatedReceiptID;

        cardTypeLabel = new FormLabel("Card Type: ");
        ButtonGroup cardTypeRBGroup = new ButtonGroup();
        anonCardRB = new FormRadioButton(CardType.AnonCard.getName());
        basicCardRB = new FormRadioButton(CardType.BasicCard.getName());
        premiumCardRB = new FormRadioButton(CardType.PremiumCard.getName());

        cardNameLabel = new FormLabel("Customer Name: ");
        cardNameTextField = new FormTextField(20);
        cardEmailLabel = new FormLabel("Customer Email: ");
        cardEmailTextField = new FormTextField(20);

        createBtn = new FormButton("Add Purchase", Style.addIcon());
        clearBtn = new FormButton("Clear", Style.clearIcon());
        purchaseErrorLabel = new ErrorLabel("PURCHASE NOT ADDED");

        /* INITIALIZE THIS PANEL */
        setLayout(new BorderLayout());
        /* SIZING - Make sure the form is at least always 800 pixels */
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("New Purchase"));
        setVisible(false);

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

        add(cancelBtn, BorderLayout.SOUTH);

        /* REGISTRATION OF LISTENERS */
        FormListener handler = new FormListener();
        purchaseTypeCombo.addItemListener(handler);
        cancelBtn.addActionListener(handler);
        createBtn.addActionListener(handler);
        clearBtn.addActionListener(handler);
        existingCardCombo.addItemListener(handler);
        anonCardRB.addActionListener(handler);
        basicCardRB.addActionListener(handler);
        premiumCardRB.addActionListener(handler);

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
                getParent().remove(PurchaseForm.this);
            }
        });
    }

    /*==================== BASE FORM ====================*/

    private void createBasePurchaseForm() {
        // Dynamically creates categoriesMap HashMap<FormLabel, FormTextField>
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

        /*========== NEW ROW - CARD ID ERROR LABEL ==========*/
        labelGridConstraints(gc);
        baseCreatePurchaseForm.add(new JLabel(""), gc);

        textFieldGridConstraints(gc);
        baseCreatePurchaseForm.add(cardIDErrorLabel, gc);

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

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        gc.insets = new Insets(10,0,20,0);
        baseCreatePurchaseForm.add(cardEmailLabel, gc);

        textFieldGridConstraints(gc);
        gc.insets = new Insets(10,0,20,0);
        baseCreatePurchaseForm.add(cardEmailTextField, gc);

        /*========== NEW ROW - CATEGORIES LIST ==========*/
        categoriesMap.forEach((key,value) -> {
            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(key[0], gc);

            value.setFont(Style.textFieldFont());
            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(value, gc);

            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(new JLabel(""), gc);

            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(key[1], gc);
        });

        /*========== NEW ROW - PURCHASE ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        purchaseErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        purchaseErrorLabel.setFont(new Font("Product Sans", Font.BOLD, 32));
        purchaseErrorLabel.setBorder(Style.formBorder(""));
        baseCreatePurchaseForm.add(purchaseErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 3; gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,1);
        baseCreatePurchaseForm.add(createBtn, gc);

        gc.gridx = 1; gc.gridwidth = 1; gc.weightx = 0.5; gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,1,0,0);
        baseCreatePurchaseForm.add(clearBtn, gc);

        /* BY DEFAULT CLEAR ALL TEXT FIELDS */
        Arrays.stream(baseCreatePurchaseForm.getComponents()).filter(c -> c instanceof FormTextField)
                .filter(c -> ((FormTextField)c).isEditable())
                .forEach((Component comp) -> ((FormTextField) comp).setText(null));

        baseCreatePurchaseForm.setVisible(true);
        JScrollPane formScrollPane = new JScrollPane(baseCreatePurchaseForm);
        formScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.add(formScrollPane, BorderLayout.CENTER);
    }

    private void basePurchaseForm() {
        hideErrorLabels();
        hideAllFormComponents(false);
    }

    private void existingCardPurchase() {
        hideErrorLabels();
        hideAllFormComponents(false);

        receiptIDLabel.setVisible(true);
        receiptIDTextField.setVisible(true);

        existingCardCombo.setModel(existingCardModel);
        existingCardCombo.setSelectedIndex(0);
        existingCardLabel.setVisible(true);
        existingCardCombo.setVisible(true);

        setCategoriesVisible(true);

        createBtn.setEnabled(false);
        createBtn.setVisible(true);
        clearBtn.setVisible(true);
    }

    private void newCardPurchase() {
        hideErrorLabels();
        hideAllFormComponents(false);
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

        setCategoriesVisible(true);
        createBtn.setVisible(true);
        createBtn.setEnabled(true);
        clearBtn.setVisible(true);
    }

    private void cashPurchase() {
        hideErrorLabels();
        hideAllFormComponents(false);
        receiptIDLabel.setVisible(true);
        receiptIDTextField.setVisible(true);

        cardIDLabel.setVisible(true);
        cardIDTextField.setText("Cash");
        cardIDTextField.setVisible(true);

        setCategoriesVisible(true);

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
        HashMap<JLabel[], FormFormattedTextField> categoriesMap = new HashMap<>();
        categoriesList.forEach((cat) -> {
            JLabel[] labelArr = new JLabel[2];
            String categoryStr = cat.getName() + ": $";
            labelArr[0] = new FormLabel(categoryStr);
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
                } else {
                    labelArr[0].setForeground(Color.BLACK);
                    labelArr[1].setVisible(false);
                    catValueTextField.setForeground(Color.BLACK);
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
            categoriesMap.put(labelArr, catValueTextField);
        });
        this.categoriesMap = categoriesMap;
    }

    private void setCategoriesVisible(boolean isVisible) {
        categoriesMap.forEach((JLabel[] k, FormFormattedTextField v) ->{
            k[0].setVisible(isVisible);
            k[1].setVisible(!isVisible);
            v.setVisible(isVisible); });
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

    private void hideAllFormComponents(boolean isVisible) {
        Arrays.stream(baseCreatePurchaseForm.getComponents()).forEach((c)-> c.setVisible(isVisible));
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
        Arrays.stream(baseCreatePurchaseForm.getComponents()).forEach((comp) -> {
            if (comp instanceof ErrorLabel) comp.setVisible(false);
        });
    }

    /*============================== INNER CLASS ==============================*/
    /*============================== BUILDER ==============================*/
    public static class PurchaseFormBuilder {
        private int generatedReceiptID;
        private DefaultComboBoxModel<String> existingCardModel;
        private ArrayList<Category> categoriesList;

        public PurchaseFormBuilder(int generatedReceiptID) {
            this.generatedReceiptID = generatedReceiptID;
        }

        public PurchaseFormBuilder existingCardModel(ArrayList<Card> cardsList) {
            DefaultComboBoxModel<String> cardModel = new DefaultComboBoxModel<>();
            cardsList.sort(Comparator.comparing(Card::getID));
            cardModel.addElement("Please Select..");
            cardsList.forEach(c->cardModel.addElement(c.getID()));
            this.existingCardModel = cardModel;
            return this;
        }

        public PurchaseFormBuilder categoriesList(ArrayList<Category> categoriesList) {
            categoriesList.sort(Comparator.comparingInt(Category::getId));
            this.categoriesList = categoriesList;
            return this;
        }

        public PurchaseForm build() {
            return new PurchaseForm(this);
        }

    }

    /*============================== CALLBACK HANDLER ==============================*/
    private class FormListener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelBtn) {
                setVisible(false);
                getParent().remove(PurchaseForm.this);
            } else if (e.getSource() == createBtn) {
                PurchaseEvent event = new PurchaseEvent(this, purchaseTypeCombo, categoriesMap, receiptIDTextField,
                        cardIDLabel, cardIDTextField, cardIDErrorLabel, existingCardCombo, anonCardRB, basicCardRB,
                        premiumCardRB, cardNameLabel, cardNameTextField, cardEmailLabel, cardEmailTextField, purchaseErrorLabel);

                if (createPurchaseListener != null) createPurchaseListener.formActionOccurred(event);
            } else if (e.getSource() == clearBtn) {
                for (Component c : baseCreatePurchaseForm.getComponents()) {
                    if (c instanceof JTextField && ((JTextField) c).isEditable()) ((JTextField) c).setText("");
                    else if (c instanceof FormFormattedTextField) ((FormFormattedTextField) c).setValue(0.00D);
                    else if (c instanceof ErrorLabel) c.setVisible(false);
                    else if (c instanceof FormLabel) c.setForeground(Color.BLACK);
                }
            } else if (e.getSource() == anonCardRB) {
                enableNameAndEmail(false);
            } else if (e.getSource() == basicCardRB) {
                enableNameAndEmail(true);
            } else if (e.getSource() == premiumCardRB) {
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
