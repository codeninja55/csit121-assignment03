package cn55.view.CardView;

import cn55.model.CardType;
import cn55.model.Database;
import cn55.view.ButtonListener;
import cn55.view.CustomComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CardForm extends JPanel {
    private JComboBox<String> cardTypeCombo;
    private DefaultComboBoxModel<String> options;
    private CancelButton cancelBtn;

    private JPanel baseCreateCardForm;
    private FormLabel cardIDLabel;
    private FormTextField cardIDTextField;
    private FormLabel cardNameLabel;
    private FormTextField cardNameTextField;
    private FormLabel cardEmailLabel;
    private FormTextField cardEmailTextField;

    private CardListener cardListener;
    private ButtonListener cancelListener;
    private FormButton createBtn;
    private FormButton clearBtn;

    /*====================  CONSTRUCTOR for Creating Cards ====================*/
    public CardForm() {
        /* INITIALIZE ALL COMPONENTS */
        cardTypeCombo = new JComboBox<>();
        options = new DefaultComboBoxModel<>();
        cancelBtn = new CancelButton("Cancel New Card");

        /* NOTE: All FormLabels and FormTextFields are hidden by default */
        cardIDLabel = new FormLabel("Card ID: ");
        cardIDTextField = new FormTextField(20);
        cardNameLabel = new FormLabel("Name: ");
        cardNameTextField = new FormTextField(20);
        cardEmailLabel = new FormLabel("Email: ");
        cardEmailTextField = new FormTextField(20);

        createBtn = new FormButton("Add Card");
        clearBtn = new FormButton("Clear");

        /* INITIALIZE THIS PANEL */
        setName("CardForm");
        setLayout(new BorderLayout());
        /* SIZING - Make sure the form is at least always 800 pixels */
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("New Card"));
        setVisible(false);

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

        add(cancelBtn, BorderLayout.SOUTH);

        /* REGISTRATION OF LISTENERS AND CALLBACKS */
        FormListener handler = new FormListener();
        cardTypeCombo.addItemListener(handler);
        cancelBtn.addActionListener(handler);
        createBtn.addActionListener(handler);
        clearBtn.addActionListener(handler);
    }

    /*============================== BASE CREATE CARD FORM ==============================*/
    public void createBaseCreateCardForm() {
        /* CREATE BASE FORM - PANEL */
        baseCreateCardForm = new JPanel(new GridBagLayout());

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

        /*========== NEW ROW ==========*/
        labelGridConstraints(gc);
        baseCreateCardForm.add(cardEmailLabel,gc);

        /*========== NEW ROW ==========*/
        textFieldGridConstraints(gc);
        gc.gridwidth = 1;
        cardEmailTextField.setEditable(false);
        baseCreateCardForm.add(cardEmailTextField,gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 2; gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        baseCreateCardForm.add(createBtn, gc);

        gc.gridx = 1; gc.weightx = 0.5;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,10,0,0);
        baseCreateCardForm.add(clearBtn, gc);

        /* BY DEFAULT CLEAR ALL TEXT FIELDS */
        for (Component item : baseCreateCardForm.getComponents())
            if (item instanceof FormTextField) ((FormTextField) item).setText("");

        cardIDTextField.setText(Database.getNextCardID());

    }

    private void baseCardForm() {
        hideErrorLabels();
        hideAllFormComponents(false);
    }

    private void anonCardForm() {
        hideErrorLabels();
        hideAllFormComponents(true);
        enableNameAndEmail(false);
    }

    private void advancedCardForm() {
        hideErrorLabels();
        hideAllFormComponents(true);
        enableNameAndEmail(true);
    }

    /*============================== LISTENER CALLBACKS ==============================*/

    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    public void setCancelListener(ButtonListener listener) { this.cancelListener = listener; }

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
        for (Component item : baseCreateCardForm.getComponents())
            item.setVisible(isVisible);
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
        for (Component comp : baseCreateCardForm.getComponents()) {
            if (comp instanceof ErrorLabel)
                comp.setVisible(false);
        }
    }

    /*============================== ACCESSORS  ==============================*/

    public JPanel getBaseCreateCardForm() {
        return baseCreateCardForm;
    }

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/
    /*=========================== LISTENER HANDLER ============================*/
    private class FormListener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelBtn) {
                if (cancelListener != null)
                    cancelListener.buttonActionOccurred();
            } else if ( e.getSource() == createBtn) {
                CardEvent event = new CardEvent(this);
                event.setCardTypeCombo(cardTypeCombo);
                event.setCardIDLabel(cardIDLabel);
                event.setCardIDTextField(cardIDTextField);
                event.setCardNameLabel(cardNameLabel);
                event.setCardNameTextField(cardNameTextField);
                event.setCardEmailLabel(cardEmailLabel);
                event.setCardEmailTextField(cardEmailTextField);

                if (cardListener != null)
                    cardListener.formActionOccurred(event);
            } else if (e.getSource() == clearBtn) {
                for (Component c : baseCreateCardForm.getComponents()) {
                    if (c instanceof JTextField && ((JTextField) c).isEditable())
                        ((JTextField) c).setText(null);

                    if (c instanceof ErrorLabel)
                        c.setVisible(false);

                    if (c instanceof FormLabel)
                        c.setForeground(Color.BLACK);
                }
            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals(options.getElementAt(0))) {
                        baseCardForm();
                    } else if (e.getItem().equals(options.getElementAt(1))) {
                        anonCardForm();
                    } else if (e.getItem().equals(options.getElementAt(2))) {
                        advancedCardForm();
                    } else if (e.getItem().equals(options.getElementAt(3))) {
                        advancedCardForm();
                    }
                }
            }
        }
    }
}
