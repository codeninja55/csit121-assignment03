package application.view.FormFactory;

import application.view.CustomComponents.ErrorLabel;
import application.view.CustomComponents.FormLabel;
import application.view.CustomComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;

public class DeleteEvent extends EventObject {
    private FormLabel idLabel;
    private FormTextField idTextField;
    private FormLabel nameLabel;
    private FormTextField nameTextField;
    private ErrorLabel errorLabel;
    private ErrorLabel ruleErrLabel;
    private ErrorLabel othersDeleteErrLabel;
    private ErrorLabel deleteErrorLabel;

    /*============================== CONSTRUCTORS ==============================*/
    // DELETE CONSTRUCTOR FOR DELETING CARDS
    DeleteEvent(Object source, FormLabel searchIDLabel, FormTextField searchIDTextField,
                       ErrorLabel errorLabel, ErrorLabel ruleErrLabel, ErrorLabel deleteErrorLabel) {
        super(source);
        this.idLabel = searchIDLabel;
        this.idTextField = searchIDTextField;
        this.errorLabel = errorLabel;
        this.ruleErrLabel = ruleErrLabel;
        this.deleteErrorLabel = deleteErrorLabel;
    }

    // DELETE CONSTRUCTOR FOR DELETING CATEGORY
    DeleteEvent(Object source, FormLabel categoryIDLabel, FormTextField categoryIDTextField,
                    FormLabel nameLabel, FormTextField nameTextField, ErrorLabel errorLabel,
                ErrorLabel ruleErrLabel, ErrorLabel othersDeleteErrLabel, ErrorLabel deleteErrorLabel) {
        super(source);
        this.idLabel = categoryIDLabel;
        this.idTextField = categoryIDTextField;
        this.nameLabel = nameLabel;
        this.nameTextField = nameTextField;
        this.errorLabel = errorLabel;
        this.ruleErrLabel = ruleErrLabel;
        this.othersDeleteErrLabel = othersDeleteErrLabel;
        this.deleteErrorLabel = deleteErrorLabel;
    }

    /*============================== MUTATORS ==============================*/

    /*============================== ACCESSORS ==============================*/

    public FormLabel getIdLabel() {
        return idLabel;
    }

    public FormTextField getIdTextField() {
        return idTextField;
    }

    public FormLabel getNameLabel() {
        return nameLabel;
    }

    public FormTextField getNameTextField() {
        return nameTextField;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    public JLabel getRuleErrLabel() {
        return ruleErrLabel;
    }

    public ErrorLabel getOthersDeleteErrLabel() {
        return othersDeleteErrLabel;
    }

    public ErrorLabel getDeleteErrorLabel() {
        return deleteErrorLabel;
    }
}