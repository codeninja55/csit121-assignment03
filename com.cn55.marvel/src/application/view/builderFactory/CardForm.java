package application.view.builderFactory;

import application.model.Generator;
import application.model.card.CardType;
import application.view.customComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class CardForm extends BaseForm implements FormFactory, CardFormView {
    private final DefaultComboBoxModel<String> options;
    private final JComboBox<String> cardTypeCombo;
    private final JPanel baseCreateCardForm;
    private final FormLabel cardIDLabel;
    private final FormTextField cardIDTextField;
    private final FormLabel cardNameLabel;
    private final FormTextField cardNameTextField;
    private final ErrorLabel nameErrorLabel;
    private final FormLabel cardEmailLabel;
    private final FormTextField cardEmailTextField;
    private final ErrorLabel emailErrorLabel;

    private CardListener cardListener;
    private final ErrorLabel createCardErrorLabel;
    private final FormButton createBtn;
    private final FormButton clearBtn;

    /*====================  CONSTRUCTOR for Creating Cards ====================*/
    CardForm() {
        super();
        super.setCancelBtn("Cancel New Card");
        super.setBorder("New Card");

        /* INITIALIZE ALL COMPONENTS */
        baseCreateCardForm = new JPanel(new GridBagLayout());
        cardTypeCombo = new JComboBox<>();
        options = new DefaultComboBoxModel<>();

        /* NOTE: All FormLabels and FormTextFields are hidden by default */
        cardIDLabel = new FormLabel("Card ID: ");
        cardIDTextField = new FormTextField(20);
        cardNameLabel = new FormLabel("Name: ");
        cardNameTextField = new FormTextField(20);
        nameErrorLabel = new ErrorLabel("Name Must Not Be Blank");
        cardEmailLabel = new FormLabel("Email: ");
        cardEmailTextField = new FormTextField(20);
        emailErrorLabel = new ErrorLabel("Email Must Not Be Blank");
        createCardErrorLabel = new ErrorLabel("CARD NOT CREATED");
        createBtn = new FormButton("Add Card", Style.addIcon());
        clearBtn = new FormButton("Clear", Style.clearIcon());

        /* REGISTRATION OF LISTENERS */
        FormListener handler = new FormListener();
        cardTypeCombo.addItemListener(handler);
        createBtn.addActionListener(handler);
        clearBtn.addActionListener(handler);

        /* CARD TYPE COMBO BOX - Create the model and the combobox */
        options.addElement("Please Choose Card Type Below");
        options.addElement(CardType.AnonCard.getName());
        options.addElement(CardType.BasicCard.getName());
        options.addElement(CardType.PremiumCard.getName());

        cardTypeCombo.setModel(options);
        cardTypeCombo.setSelectedIndex(0);
        cardTypeCombo.setEditable(false);
        cardTypeCombo.setFont(Style.comboboxFont());
        add(cardTypeCombo, BorderLayout.NORTH);

        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                createBaseCreateCardForm();
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                baseCreateCardForm.setVisible(false);
                CardForm.super.remove(baseCreateCardForm);
            }
        });
    }

    /*============================== BASE CREATE CARD FORM ==============================*/
    private void createBaseCreateCardForm() {
        this.add(baseCreateCardForm, BorderLayout.CENTER);
        GridBagConstraints gc = new GridBagConstraints();

        /*========== FIRST ROW - CARD ID ==========*/
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 0; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.02;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        baseCreateCardForm.add(cardIDLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,0,0,0);
        cardIDTextField.setEditable(false);
        baseCreateCardForm.add(cardIDTextField, gc);

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        baseCreateCardForm.add(cardNameLabel, gc);

        /*========== NEW ROW ==========*/
        textFieldGridConstraints(gc);
        baseCreateCardForm.add(cardNameTextField, gc);

        /*========== NEW ROW - NAME ERROR LABEL ==========*/
        labelGridConstraints(gc);
        baseCreateCardForm.add(new JLabel(""), gc);

        textFieldGridConstraints(gc);
        baseCreateCardForm.add(nameErrorLabel, gc);

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        baseCreateCardForm.add(cardEmailLabel,gc);

        /*========== NEW ROW ==========*/
        textFieldGridConstraints(gc);
        gc.gridwidth = 1;
        baseCreateCardForm.add(cardEmailTextField,gc);

        /*========== NEW ROW - EMAIL ERROR LABEL ==========*/
        labelGridConstraints(gc);
        baseCreateCardForm.add(new JLabel(""), gc);

        textFieldGridConstraints(gc);
        baseCreateCardForm.add(emailErrorLabel, gc);

        /*========== NEW ROW - CARD ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        createCardErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createCardErrorLabel.setFont(new Font("Product Sans", Font.BOLD, 32));
        createCardErrorLabel.setBorder(Style.formBorder(""));
        baseCreateCardForm.add(createCardErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 2; gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        baseCreateCardForm.add(createBtn, gc);

        gc.gridx = 1; gc.weightx = 0.5;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,10,0,0);
        baseCreateCardForm.add(clearBtn, gc);

        /* By Default Clear All Text Fields */
        Arrays.stream(baseCreateCardForm.getComponents()).filter(c -> c instanceof FormTextField)
                .forEach(c -> ((FormTextField)c).setText(null));

        cardIDTextField.setText(Generator.getNextCardID());
    }

    private void baseCardForm() {
        hideAllFormComponents(false);
        hideErrorLabels();
    }

    private void anonCardForm() {
        hideAllFormComponents(true);
        hideErrorLabels();
        enableNameAndEmail(false);
    }

    private void advancedCardForm() {
        hideAllFormComponents(true);
        hideErrorLabels();
        enableNameAndEmail(true);
    }

    /*============================== LISTENER CALLBACKS ==============================*/
    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    /*============================== MUTATORS  ==============================*/
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
        Arrays.stream(baseCreateCardForm.getComponents()).forEach(c -> c.setVisible(isVisible));
    }

    private void enableNameAndEmail (boolean isEnabled) {
        cardNameLabel.setEnabled(isEnabled);
        cardNameTextField.setEditable(isEnabled);
        cardEmailLabel.setEnabled(isEnabled);
        cardEmailTextField.setEditable(isEnabled);
        cardNameTextField.setText(null);
        cardEmailTextField.setText(null);
    }

    private void hideErrorLabels() {
            Arrays.stream(baseCreateCardForm.getComponents()).filter(c -> c instanceof ErrorLabel)
                    .forEach(c -> c.setVisible(false));
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getCardType() { return options.getElementAt(cardTypeCombo.getSelectedIndex()); }
    public String getCardID() { return cardIDTextField.getText(); }
    public String getCardName() { return cardNameTextField.getText().trim(); }
    public String getCardEmail() { return cardEmailTextField.getText().trim(); }

    /*============================== INNER CLASS ==============================*/
    private class FormListener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createBtn) {
                hideErrorLabels();
                boolean valid = true;

                if (cardTypeCombo.getSelectedIndex() != 1) {
                    if (cardNameTextField.getText() == null || cardNameTextField.getText().isEmpty()) {
                        nameErrorLabel.setVisible(true);
                        valid = false;
                    }

                    if (cardEmailTextField.getText() == null || cardEmailTextField.getText().isEmpty()) {
                        emailErrorLabel.setVisible(true);
                        valid = false;
                    }
                }

                if (valid && cardListener != null) cardListener.formActionOccurred(CardForm.this);
                else createCardErrorLabel.setVisible(true);
            } else if (e.getSource() == clearBtn) {
                Component[] formComponents = baseCreateCardForm.getComponents();
                Arrays.stream(formComponents).filter(c -> c instanceof JTextField && ((JTextField)c).isEditable())
                        .forEach(c -> ((JTextField)c).setText(null));
                Arrays.stream(formComponents).filter(c -> c instanceof ErrorLabel).forEach(c->c.setVisible(false));
                Arrays.stream(formComponents).filter(c -> c instanceof FormLabel).forEach(c->c.setForeground(Color.BLACK));
            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem().equals(options.getElementAt(0))) baseCardForm();
                else if (e.getItem().equals(options.getElementAt(1))) anonCardForm();
                else if (e.getItem().equals(options.getElementAt(2))) advancedCardForm();
                else if (e.getItem().equals(options.getElementAt(3))) advancedCardForm();
            }
        }
    }
}
