package application.view.FormFactory;

import application.view.CustomComponents.ButtonListener;
import application.view.CustomComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteCardForm extends JPanel {

    private JPanel deleteForm;
    private FormLabel cardIDLabel;
    private FormTextField cardIDTextField;
    private ErrorLabel errorLabel;
    private ErrorLabel ruleErrLabel;
    private ErrorLabel deleteErrorLabel;
    @SuppressWarnings("FieldCanBeLocal")
    private FormButton deleteBtn;

    private ButtonListener cancelListener;
    private DeleteListener deleteListener;

    /*============================== CONSTRUCTORS ==============================*/
    public DeleteCardForm() {

        deleteForm = new JPanel(new GridBagLayout());
        CancelButton cancelBtn = new CancelButton("Cancel Card Delete");
        cardIDLabel = new FormLabel("Delete by Card ID");
        cardIDTextField = new FormTextField(20);
        errorLabel = new ErrorLabel("CARD DOES NOT EXIST");
        ruleErrLabel = new ErrorLabel("INVALID CARD ID NUMBER");
        deleteErrorLabel = new ErrorLabel("CARD NOT DELETED");
        deleteBtn = new FormButton("Delete Card");

        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("Delete Card"));

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
        deleteForm.add(errorLabel, gc);

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
        deleteErrorLabel.setBorder(Style.formBorder(""));
        deleteForm.add(deleteErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 3;
        gc.gridwidth = 1; gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10,0,10,0);
        deleteForm.add(deleteBtn, gc);

        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteEvent event = new DeleteEvent(this,
                        cardIDLabel, cardIDTextField, errorLabel, ruleErrLabel, deleteErrorLabel);

                if (deleteListener != null) {
                    deleteListener.deleteEventOccurred(event);
                }
            }
        });

        add(deleteForm, BorderLayout.CENTER);
        add(cancelBtn, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> {
            if (cancelListener != null)
                cancelListener.buttonActionOccurred();
        });

        /* SET FORM CUSTOM COMPONENTS VISIBLE */
        for (Component c : deleteForm.getComponents()) {
            if (c instanceof FormLabel || c instanceof FormTextField || c instanceof FormButton) {
                c.setVisible(true);
            }
        }
        setVisible(false);
    }

    /*============================== MUTATORS ==============================*/
    public void setDeleteListener(DeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setCancelListener(ButtonListener listener) {
        this.cancelListener = listener;
    }

    /*============================== ACCESSORS ==============================*/

}
