package application.view.form_builder_factory;

import application.controller.validator.*;
import application.view.custom_components.*;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SearchCardForm extends BaseForm implements FormFactory, SearchFormView {
    private final JPanel searchForm;
    private final FormTextField searchIDTextField;
    private SearchListener searchListener;

    SearchCardForm() {
        super();
        super.setCancelBtn("Cancel Card Search");
        super.setBorder("Search Cards");

        searchForm = new JPanel(new GridBagLayout());
        FormLabel searchLabel = new FormLabel("Search by Card ID");
        searchIDTextField = new FormTextField(20);
        ErrorLabel errorLabel = new ErrorLabel("CARD DOES NOT EXIST");
        ErrorLabel ruleErrLabel = new ErrorLabel("INVALID CARD ID NUMBER");
        FormButton searchBtn = new FormButton("Search", IconFactory.searchIcon());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 0.2;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(20,0,10,0);
        searchForm.add(searchLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,10,0);
        Dimension textFieldDim = getPreferredSize();
        textFieldDim.width = 350;
        textFieldDim.height = 50;
        searchIDTextField.setPreferredSize(textFieldDim);
        searchForm.add(searchIDTextField, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        searchForm.add(errorLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        searchForm.add(ruleErrLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 3;
        gc.gridwidth = 1; gc.gridheight = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10,0,10,0);
        searchForm.add(searchBtn, gc);

        /* Set Components Visible */
        Arrays.stream(searchForm.getComponents())
                .filter(c -> c instanceof FormButton || c instanceof FormTextField || c instanceof FormLabel)
                .forEach(c -> c.setVisible(true));

        searchBtn.addActionListener(e -> {
            hideErrorLabel();
            boolean proceed = true;
            FormValidData validData = new FormValidData();
            validData.setCardID(getID());
            ExistsRule cardExists = new CardExistsRule();
            FormRule cardIDRule = new CardIDRule();

            if (!cardExists.existsValidating(validData)) {
                errorLabel.setVisible(true);
                proceed = false;
            } else if (!cardIDRule.validate(validData)) {
                ruleErrLabel.setVisible(true);
                proceed = false;
            }

            if (proceed && searchListener != null)
                searchListener.searchFormSubmitted(SearchCardForm.this);
        });

        add(searchForm, BorderLayout.CENTER);
    }

    /*============================== MUTATORS ==============================*/
    public void setSearchListener(SearchListener listener) { this.searchListener = listener; }

    private void hideErrorLabel() {
        Arrays.stream(searchForm.getComponents()).filter(c -> c instanceof ErrorLabel).forEach(c -> c.setVisible(false));
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getID() { return searchIDTextField.getText().toUpperCase(); }
}
