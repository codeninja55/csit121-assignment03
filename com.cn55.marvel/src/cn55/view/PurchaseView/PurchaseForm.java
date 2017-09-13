package cn55.view.PurchaseView;

import cn55.model.*;
import cn55.model.DataStoreModel;
import cn55.view.ButtonListener;
import cn55.view.CustomComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

@SuppressWarnings("SameParameterValue")
public class PurchaseForm extends JPanel {
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
    private JRadioButton anonCardRB;
    private JRadioButton basicCardRB;
    private JRadioButton premiumCardRB;

    private FormLabel cardNameLabel;
    private FormTextField cardNameTextField;
    private FormLabel cardEmailLabel;
    private FormTextField cardEmailTextField;

    private PurchaseListener createPurchaseListener;
    private ButtonListener cancelListener;
    private FormButton createBtn;
    private FormButton clearBtn;
    private ErrorLabel purchaseErrorLabel;

    /*============================== CONSTRUCTORS ==============================*/
    public PurchaseForm() {
        /* INITIALIZE ALL COMPONENTS */
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

        cardTypeLabel = new FormLabel("Card Type: ");
        ButtonGroup cardTypeRBGroup = new ButtonGroup();
        anonCardRB = new JRadioButton(CardType.AnonCard.getName());
        basicCardRB = new JRadioButton(CardType.BasicCard.getName());
        premiumCardRB = new JRadioButton(CardType.PremiumCard.getName());

        cardNameLabel = new FormLabel("Customer Name: ");
        cardNameTextField = new FormTextField(20);
        cardEmailLabel = new FormLabel("Customer Email: ");
        cardEmailTextField = new FormTextField(20);

        createBtn = new FormButton("Add Purchase");
        clearBtn = new FormButton("Clear");
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
    }

    /*==================== BASE FORM ====================*/

    public void createBasePurchaseForm() {
        /* CREATE BASE FORM WITH PURCHASE - PANEL */
        baseCreatePurchaseForm = new JPanel(new GridBagLayout());

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

        receiptIDTextField.setName("ReceiptIDTextField");
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
        for (HashMap.Entry<JLabel[], FormFormattedTextField> item : categoriesMap.entrySet()) {
            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(item.getKey()[0], gc);

            item.getValue().setFont(Style.textFieldFont());
            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(item.getValue(), gc);

            labelGridConstraints(gc);
            baseCreatePurchaseForm.add(new JLabel(""), gc);

            textFieldGridConstraints(gc);
            baseCreatePurchaseForm.add(item.getKey()[1], gc);
        }

        /*========== NEW ROW - PURCHASE ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        purchaseErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        purchaseErrorLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        purchaseErrorLabel.setBorder(Style.formBorder(""));
        baseCreatePurchaseForm.add(purchaseErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 3; gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        baseCreatePurchaseForm.add(createBtn, gc);

        gc.gridx = 1; gc.weightx = 0.5;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,10,0,0);
        baseCreatePurchaseForm.add(clearBtn, gc);

        /* BY DEFAULT CLEAR ALL TEXT FIELDS */
        Arrays.stream(baseCreatePurchaseForm.getComponents()).forEach((Component comp) ->{
            if (comp instanceof FormTextField)  ((FormTextField) comp).setText(null);
        });

        receiptIDTextField.setText(Integer.toString(generatedReceiptID));

        baseCreatePurchaseForm.setVisible(true);
        JScrollPane formScrollPane = new JScrollPane(baseCreatePurchaseForm);
        formScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(2,2,2,2));
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
        receiptIDTextField.setText(Integer.toString(generatedReceiptID));
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
        cardIDTextField.setText(DataStoreModel.getNextCardID());
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
    public void setCancelPurchaseListener(ButtonListener listener) { this.cancelListener = listener; }

    public void setCreatePurchaseListener(PurchaseListener listener) { this.createPurchaseListener = listener; }

    /*============================== MUTATORS  ==============================*/
    public void setGeneratedReceiptID(int generatedReceiptID) { this.generatedReceiptID = generatedReceiptID; }

    public void setCardModel(DefaultComboBoxModel<String> cardModel) { this.existingCardModel = cardModel; }

    public void setCategoriesList(ArrayList<Category> categoriesList) {
        categoriesList.sort(Comparator.comparingInt(Category::getId));
        this.categoriesList = categoriesList;
    }

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

    /*============================== ACCESSORS  ==============================*/
    public JComboBox<String> getPurchaseTypeCombo() {
        return purchaseTypeCombo;
    }

    public JPanel getBaseCreatePurchaseForm() {
        return baseCreatePurchaseForm;
    }

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/
    /*============================== CALLBACK HANDLER ==============================*/
    private class FormListener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelBtn) {
                if (cancelListener != null) {
                    cancelListener.buttonActionOccurred();
                }
            } else if (e.getSource() == createBtn) {
                PurchaseEvent event = new PurchaseEvent(this, purchaseTypeCombo, categoriesMap, receiptIDTextField,
                        cardIDLabel, cardIDTextField, cardIDErrorLabel, existingCardCombo, anonCardRB, basicCardRB,
                        premiumCardRB, cardNameLabel, cardNameTextField, cardEmailLabel, cardEmailTextField, purchaseErrorLabel);

                if (createPurchaseListener != null)
                    createPurchaseListener.formActionOccurred(event);
            } else if (e.getSource() == clearBtn) {
                for (Component c : baseCreatePurchaseForm.getComponents()) {
                    if (c instanceof JTextField && ((JTextField) c).isEditable())
                        ((JTextField) c).setText("");
                    else if (c instanceof FormFormattedTextField)
                        ((FormFormattedTextField) c).setValue(0.00D);
                    else if (c instanceof ErrorLabel)
                        c.setVisible(false);
                    else if (c instanceof FormLabel)
                        c.setForeground(Color.BLACK);
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
                    if (e.getItem().equals(options.getElementAt(0))) {
                        basePurchaseForm();
                    } else if (e.getItem().equals(options.getElementAt(1))) {
                        existingCardPurchase();
                    } else if (e.getItem().equals(options.getElementAt(2))) {
                        newCardPurchase();
                    } else if (e.getItem().equals(options.getElementAt(3))) {
                        cashPurchase();
                    }
                }
            } else if (e.getSource() == existingCardCombo) {
                if (!e.getItem().equals(existingCardModel.getElementAt(0))) {
                    createBtn.setEnabled(true);
                } else {
                    createBtn.setEnabled(false);
                }
            }
        }

    }

}
