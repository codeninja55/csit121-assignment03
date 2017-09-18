package application.view.builderFactory;

import application.view.customComponents.FormLabel;
import application.view.customComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;

public class SearchEvent extends EventObject {
    private FormLabel searchIDLabel;
    private FormTextField searchIDTextField;
    private JLabel errorLabel;
    private JLabel ruleErrLabel;

    /*============================== CONSTRUCTORS ==============================*/
    SearchEvent(Object source, FormLabel searchIDLabel, FormTextField searchIDTextField,
                JLabel errorLabel, JLabel ruleErrLabel) {
        super(source);
        this.searchIDLabel = searchIDLabel;
        this.searchIDTextField = searchIDTextField;
        this.errorLabel = errorLabel;
        this.ruleErrLabel = ruleErrLabel;
    }

    /*============================== ACCESSORS ==============================*/
    public FormLabel getSearchIDLabel() {
        return searchIDLabel;
    }

    public FormTextField getSearchIDTextField() {
        return searchIDTextField;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    public JLabel getRuleErrLabel() {
        return ruleErrLabel;
    }
}
