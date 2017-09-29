package application.view.form_builder_factory;

import application.model.Generator;
import application.model.card.CardType;
import application.view.custom_components.*;
import styles.CustomBorderFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class CardForm extends BaseForm implements FormFactory, CardFormView {
    private final MaterialRadioButton anonRB;
    private final MaterialRadioButton basicRB;
    private final MaterialRadioButton premRB;
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

        /* NOTE: All FormLabels and FormTextFields are hidden by default */
        JPanel cardRBPane = new JPanel(new GridLayout(1,3));
        ButtonGroup cardButtonGroup = new ButtonGroup();
        anonRB = new MaterialRadioButton("AnonCard");
        basicRB = new MaterialRadioButton("BasicCard");
        premRB = new MaterialRadioButton("PremiumCard");
        cardIDLabel = new FormLabel("Card ID: ");
        cardIDTextField = new FormTextField(20);
        cardNameLabel = new FormLabel("Name: ");
        cardNameTextField = new FormTextField(20);
        nameErrorLabel = new ErrorLabel("Name Must Not Be Blank");
        cardEmailLabel = new FormLabel("Email: ");
        cardEmailTextField = new FormTextField(20);
        emailErrorLabel = new ErrorLabel("Email Must Not Be Blank");
        createCardErrorLabel = new ErrorLabel("CARD NOT CREATED");
        createBtn = new FormButton("Add Card", IconFactory.addIcon());
        clearBtn = new FormButton("Clear", IconFactory.clearIcon());

        /* REGISTRATION OF LISTENERS */
        FormListener handler = new FormListener();
        createBtn.addActionListener(handler);
        clearBtn.addActionListener(handler);

        cardButtonGroup.add(anonRB);
        cardButtonGroup.add(basicRB);
        cardButtonGroup.add(premRB);
        anonRB.setSelected(true);

        cardRBPane.add(anonRB);
        cardRBPane.add(basicRB);
        cardRBPane.add(premRB);

        add(cardRBPane, BorderLayout.NORTH);

        anonRB.addActionListener(e -> anonCardForm());
        basicRB.addActionListener(e -> advancedCardForm());
        premRB.addActionListener(e -> advancedCardForm());

        createBaseCreateCardForm();
        anonCardForm();
        add(baseCreateCardForm, BorderLayout.CENTER);

        /*this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                baseCreateCardForm.setVisible(false);
            }
        });*/

        Arrays.stream(baseCreateCardForm.getComponents()).filter(c -> !(c instanceof ErrorLabel)).forEach(c -> c.setVisible(true));
    }

    /*============================== BASE CREATE CARD FORM ==============================*/
    private void createBaseCreateCardForm() {
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
        createCardErrorLabel.setBorder(CustomBorderFactory.formBorder(""));
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

    private void anonCardForm() {
        hideErrorLabels();
        enableNameAndEmail(false);
    }

    private void advancedCardForm() {
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

    private void enableNameAndEmail (boolean isEnabled) {
        cardNameLabel.setEnabled(isEnabled);
        cardNameTextField.setEditable(isEnabled);
        cardEmailLabel.setEnabled(isEnabled);
        cardEmailTextField.setEditable(isEnabled);
        cardNameTextField.setText(null);
        cardEmailTextField.setText(null);
    }

    private void hideErrorLabels() {
            Arrays.stream(baseCreateCardForm.getComponents()).filter(c -> c instanceof ErrorLabel).forEach(c -> c.setVisible(false));
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getCardType() {
        if (anonRB.isSelected())
            return CardType.AnonCard.name;
        else if (basicRB.isSelected())
            return CardType.BasicCard.name;
        else
            return CardType.PremiumCard.name;
    }
    public String getCardID() { return cardIDTextField.getText(); }
    public String getCardName() { return cardNameTextField.getText().trim(); }
    public String getCardEmail() { return cardEmailTextField.getText().trim(); }

    /*============================== INNER CLASS ==============================*/
    private class FormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createBtn) {
                hideErrorLabels();
                boolean valid = true;

                if (basicRB.isSelected() || premRB.isSelected()) {
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
    }
}
