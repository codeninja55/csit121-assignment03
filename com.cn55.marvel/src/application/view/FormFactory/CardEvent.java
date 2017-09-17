package application.view.FormFactory;

import application.view.CustomComponents.FormLabel;
import application.view.CustomComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;

public class CardEvent extends EventObject {
    private JComboBox<String> cardTypeCombo;
    private FormTextField cardIDTextField;
    private FormTextField cardNameTextField;
    private FormTextField cardEmailTextField;

    /*============================== CONSTRUCTORS ==============================*/
    CardEvent(Object source) {
        super(source);
    }

    /*============================== MUTATORS ==============================*/
    void setCardTypeCombo(JComboBox<String> cardTypeCombo) {
        this.cardTypeCombo = cardTypeCombo;
    }
    void setCardIDLabel(FormLabel cardIDLabel) {
        FormLabel cardIDLabel1 = cardIDLabel;
    }
    void setCardIDTextField(FormTextField cardIDTextField) {
        this.cardIDTextField = cardIDTextField;
    }
    void setCardNameLabel(FormLabel cardNameLabel) {
        FormLabel cardNameLabel1 = cardNameLabel;
    }
    void setCardNameTextField(FormTextField cardNameTextField) {
        this.cardNameTextField = cardNameTextField;
    }
    void setCardEmailLabel(FormLabel cardEmailLabel) {
        FormLabel cardEmailLabel1 = cardEmailLabel;
    }
    void setCardEmailTextField(FormTextField cardEmailTextField) {
        this.cardEmailTextField = cardEmailTextField;
    }

    /*============================== ACCESSORS ==============================*/
    public JComboBox<String> getCardTypeCombo() {
        return cardTypeCombo;
    }
    public FormTextField getCardIDTextField() {
        return cardIDTextField;
    }
    public FormTextField getCardNameTextField() {
        return cardNameTextField;
    }
    public FormTextField getCardEmailTextField() {
        return cardEmailTextField;
    }
}
