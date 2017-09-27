package application.view.form_builder_factory;

import application.controller.validator.*;
import application.view.custom_components.*;
import styles.CustomBorderFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DeleteCardForm extends BaseForm implements FormFactory, DeleteFormView {
    private final JPanel deleteForm;
    private final FormTextField cardIDTextField;
    private DeleteCardListener deleteCardListener;

    /*============================== CONSTRUCTORS ==============================*/
    DeleteCardForm() {
        super();
        super.setCancelBtn("Cancel Card Delete");
        super.setBorder("Delete Card");

        deleteForm = new JPanel(new GridBagLayout());
        FormLabel cardIDLabel = new FormLabel("Delete by Card ID");
        cardIDTextField = new FormTextField(20);
        ErrorLabel existsErrorLabel = new ErrorLabel("CARD DOES NOT EXIST");
        ErrorLabel ruleErrLabel = new ErrorLabel("INVALID CARD ID NUMBER");
        ErrorLabel deleteErrorLabel = new ErrorLabel("CARD NOT DELETED");
        FormButton deleteBtn = new FormButton("Delete Card", IconFactory.deleteActionIcon());

        GridBagConstraints gc = new GridBagConstraints();
        /*========== FIRST ROW ==========*/
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 0.2;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(20,0,0,0);
        deleteForm.add(cardIDLabel, gc);

        /*========== NEW ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,10,0);
        Dimension textFieldDim = getPreferredSize();
        textFieldDim.width = 350;
        textFieldDim.height = 50;
        cardIDTextField.setPreferredSize(textFieldDim);
        deleteForm.add(cardIDTextField, gc);

        /*========== NEW ROW - ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        deleteForm.add(existsErrorLabel, gc);

        /*========== NEW ROW - ERROR LABEL ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        deleteForm.add(ruleErrLabel, gc);

        /*========== NEW ROW - DELETE ERROR LABEL ==========*/
        gc.gridy++; gc.weighty = 0.1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        deleteErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deleteErrorLabel.setFont(new Font("Product Sans", Font.BOLD, 32));
        deleteErrorLabel.setBorder(CustomBorderFactory.formBorder(""));
        deleteForm.add(deleteErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 3;
        gc.gridwidth = 1; gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10,0,10,0);
        deleteForm.add(deleteBtn, gc);

        /* SET FORM CUSTOM COMPONENTS VISIBLE */
        Arrays.stream(deleteForm.getComponents()).filter(c -> c instanceof FormLabel || c instanceof FormTextField || c instanceof FormButton)
                .forEach(c -> c.setVisible(true));

        deleteBtn.addActionListener(e -> {
            hideErrorLabels();
            boolean proceed = true;
            FormValidData validData = new FormValidData();
            validData.setCardID(getID());
            ExistsRule cardExists = new CardExistsRule();
            FormRule cardIDRule = new CardIDRule();

            if (!cardIDRule.validate(validData)) {
                ruleErrLabel.setVisible(true);
                proceed = false;
            } else if (!cardExists.existsValidating(validData)) {
                existsErrorLabel.setVisible(true);
                proceed = false;
            }

            if (proceed && deleteCardListener != null) deleteCardListener.formSubmitted(DeleteCardForm.this);
            else deleteErrorLabel.setVisible(true);
        });

        add(deleteForm, BorderLayout.CENTER);
    }

    /*============================== MUTATORS ==============================*/
    public void setDeleteCardListener(DeleteCardListener listener) {
        this.deleteCardListener = listener;
    }

    private void hideErrorLabels() {
        Arrays.stream(deleteForm.getComponents()).filter(c -> c instanceof ErrorLabel).forEach(c -> c.setVisible(false));
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getID() { return cardIDTextField.getText().toUpperCase(); }
}
