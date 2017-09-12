package cn55.view.PurchaseView;

import cn55.view.CustomComponents.ErrorLabel;
import cn55.view.CustomComponents.FormFormattedTextField;
import cn55.view.CustomComponents.FormLabel;
import cn55.view.CustomComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;
import java.util.HashMap;

public class PurchaseEvent extends EventObject {

    private JComboBox<String> purchaseTypeCombo;

    private HashMap<JLabel[], FormFormattedTextField> categoriesMap;

    private FormTextField receiptIDTextField;
    private FormLabel cardIDLabel;
    private FormTextField cardIDTextField;
    private ErrorLabel cardIDErrorLabel;
    private JComboBox<String> existingCardCombo;

    private JRadioButton anonCardRB;
    private JRadioButton basicCardRB;
    private JRadioButton premiumCardRB;

    private FormLabel cardNameLabel;
    private FormTextField cardNameTextField;
    private FormLabel cardEmailLabel;
    private FormTextField cardEmailTextField;

    private ErrorLabel purchaseErrorLabel;

    /*============================== CONSTRUCTORS ==============================*/
    PurchaseEvent(Object source, JComboBox<String> purchaseTypeCombo, HashMap<JLabel[], FormFormattedTextField> categoriesMap,
                  FormTextField receiptIDTextField, FormLabel cardIDLabel, FormTextField cardIDTextField, ErrorLabel cardIDErrorLabel,
                  JComboBox<String> existingCardCombo, JRadioButton anonCardRB, JRadioButton basicCardRB, JRadioButton premiumCardRB,
                  FormLabel cardNameLabel, FormTextField cardNameTextField, FormLabel cardEmailLabel, FormTextField cardEmailTextField,
                  ErrorLabel purchaseErrorLabel) {

        super(source);
        this.purchaseTypeCombo = purchaseTypeCombo;
        this.categoriesMap = categoriesMap;
        this.receiptIDTextField = receiptIDTextField;
        this.cardIDLabel = cardIDLabel;
        this.cardIDTextField = cardIDTextField;
        this.cardIDErrorLabel = cardIDErrorLabel;
        this.existingCardCombo = existingCardCombo;
        this.anonCardRB = anonCardRB;
        this.basicCardRB = basicCardRB;
        this.premiumCardRB = premiumCardRB;
        this.cardNameLabel = cardNameLabel;
        this.cardNameTextField = cardNameTextField;
        this.cardEmailLabel = cardEmailLabel;
        this.cardEmailTextField = cardEmailTextField;
        this.purchaseErrorLabel = purchaseErrorLabel;
    }

    /*============================== ACCESSORS ==============================*/
    public JComboBox<String> getPurchaseTypeCombo() {
        return purchaseTypeCombo;
    }

    public HashMap<JLabel[], FormFormattedTextField> getCategoriesMap() {
        return categoriesMap;
    }

    public FormTextField getReceiptIDTextField() {
        return receiptIDTextField;
    }

    public FormLabel getCardIDLabel() {
        return cardIDLabel;
    }

    public FormTextField getCardIDTextField() {
        return cardIDTextField;
    }

    public ErrorLabel getCardIDErrorLabel() {
        return cardIDErrorLabel;
    }

    public JComboBox<String> getExistingCardCombo() {
        return existingCardCombo;
    }

    public JRadioButton getAnonCardRB() {
        return anonCardRB;
    }

    public JRadioButton getBasicCardRB() {
        return basicCardRB;
    }

    public JRadioButton getPremiumCardRB() {
        return premiumCardRB;
    }

    public FormTextField getCardNameTextField() {
        return cardNameTextField;
    }

    public FormTextField getCardEmailTextField() {
        return cardEmailTextField;
    }

    public ErrorLabel getPurchaseErrorLabel() {
        return purchaseErrorLabel;
    }
}
