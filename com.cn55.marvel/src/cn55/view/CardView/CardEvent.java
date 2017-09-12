package cn55.view.CardView;

import cn55.view.CustomComponents.FormLabel;
import cn55.view.CustomComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;

public class CardEvent extends EventObject {

    private JComboBox<String> cardTypeCombo;
    private FormLabel cardIDLabel;
    private FormTextField cardIDTextField;
    private FormLabel cardNameLabel;
    private FormTextField cardNameTextField;
    private FormLabel cardEmailLabel;
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
        this.cardIDLabel = cardIDLabel;
    }

    void setCardIDTextField(FormTextField cardIDTextField) {
        this.cardIDTextField = cardIDTextField;
    }

    void setCardNameLabel(FormLabel cardNameLabel) {
        this.cardNameLabel = cardNameLabel;
    }

    void setCardNameTextField(FormTextField cardNameTextField) {
        this.cardNameTextField = cardNameTextField;
    }

    void setCardEmailLabel(FormLabel cardEmailLabel) {
        this.cardEmailLabel = cardEmailLabel;
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
