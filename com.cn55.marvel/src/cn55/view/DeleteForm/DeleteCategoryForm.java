package cn55.view.DeleteForm;

import cn55.view.ButtonListener;
import cn55.view.CustomComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteCategoryForm extends JPanel {

    private JPanel deleteCategoryForm;
    private CancelButton cancelBtn;
    private JRadioButton idRB;
    private JRadioButton nameRB;

    private FormLabel categoryIDLabel;
    private FormTextField categoryIDTextField;
    private FormLabel categoryNameLabel;
    private FormTextField categoryNameTextField;
    private ErrorLabel errLabel;
    private ErrorLabel idRuleErrLabel;
    private ErrorLabel othersDeleteErrLabel;
    private ErrorLabel deleteErrorLabel;
    private FormButton deleteBtn;

    private ButtonListener cancelListener;
    private DeleteListener deleteListener;

    /*============================== CONSTRUCTORS  ==============================*/
    public DeleteCategoryForm() {
        cancelBtn = new CancelButton("Cancel Category Delete");

        JPanel rbSubPane = new JPanel(new GridLayout(1, 3));
        ButtonGroup categoryRBGroup = new ButtonGroup();
        FormLabel deleteTypeLabel = new FormLabel("Delete By: ");
        idRB = new JRadioButton("Category ID");
        nameRB = new JRadioButton("Category Name");

        categoryIDLabel = new FormLabel("Category ID");
        categoryIDTextField = new FormTextField(20);
        categoryNameLabel = new FormLabel("Category Name");
        categoryNameTextField = new FormTextField(20);

        errLabel = new ErrorLabel("CATEGORY DOES NOT EXIST");
        idRuleErrLabel = new ErrorLabel("INVALID CATEGORY ID");
        othersDeleteErrLabel = new ErrorLabel("CANNOT DELETE OTHERS CATEGORY");
        deleteErrorLabel = new ErrorLabel("CATEGORY NOT DELETED");
        deleteBtn = new FormButton("Delete Category");

        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("Delete Category"));

        /* RADIO BUTTONS SUB PANE */
        categoryRBGroup.add(idRB);
        categoryRBGroup.add(nameRB);
        idRB.setSelected(true);
        deleteTypeLabel.setVisible(true);
        rbSubPane.add(deleteTypeLabel);
        idRB.setFont(Style.labelFont());
        rbSubPane.add(idRB);
        nameRB.setFont(Style.labelFont());
        rbSubPane.add(nameRB);
        rbSubPane.setMinimumSize(rbSubPane.getPreferredSize());
        add(rbSubPane, BorderLayout.NORTH);

        add(cancelBtn, BorderLayout.SOUTH);

        setVisible(false);

        /* REGISTRATION OF LISTENERS AND CALLBACKS */
        FormListener handler = new FormListener();
        cancelBtn.addActionListener(handler);
        deleteBtn.addActionListener(handler);
        idRB.addActionListener(handler);
        nameRB.addActionListener(handler);

        baseForm();
    }

    /*============================== BASE FORM ==============================*/
    private void baseForm() {
        deleteCategoryForm = new JPanel(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        /*========== FIRST ROW ==========*/
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(20,0,0,0);
        categoryIDLabel.setVisible(true);
        deleteCategoryForm.add(categoryIDLabel, gc);

        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        Dimension textFieldDim = categoryIDTextField.getPreferredSize();
        textFieldDim.width = 350;
        textFieldDim.height = 50;
        categoryIDTextField.setPreferredSize(textFieldDim);
        categoryIDTextField.setVisible(true);
        deleteCategoryForm.add(categoryIDTextField, gc);

        /*========== NEW ROW - ERROR LABEL ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        deleteCategoryForm.add(idRuleErrLabel, gc);

        /*========== NEW ROW ==========*/
        gc.gridy++; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_END;
        deleteCategoryForm.add(categoryNameLabel, gc);

        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        categoryNameTextField.setPreferredSize(textFieldDim);
        deleteCategoryForm.add(categoryNameTextField, gc);

        /*========== NEW ROW - ERROR LABEL ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        deleteCategoryForm.add(errLabel, gc);

        /*========== NEW ROW - DELETE OTHERS ERROR LABEL ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        deleteCategoryForm.add(othersDeleteErrLabel, gc);

        /*========== NEW ROW - DELETE ERROR LABEL ==========*/
        gc.gridy++; gc.weighty = 0.1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(20,0,20,0);
        deleteErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deleteErrorLabel.setFont(new Font("Product Sans", Font.BOLD, 32));
        deleteErrorLabel.setBorder(Style.formBorder(""));
        deleteCategoryForm.add(deleteErrorLabel, gc);

        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.weighty = 2;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,0,0);
        deleteBtn.setVisible(true);
        deleteCategoryForm.add(deleteBtn, gc);

        this.add(deleteCategoryForm, BorderLayout.CENTER);
    }

    private void deleteByIDForm() {
        hideErrorLabels();
        switchIDAndNameFields(true);
    }

    private void deleteByNameForm() {
        hideErrorLabels();
        switchIDAndNameFields(false);
    }

    /*============================== MUTATORS  ==============================*/

    public void setCancelListener(ButtonListener listener) {
        this.cancelListener = listener;
    }

    public void setDeleteListener(DeleteListener listener) {
        this.deleteListener = listener;
    }

    private void switchIDAndNameFields(boolean isVisible) {
        categoryIDLabel.setVisible(isVisible);
        categoryIDTextField.setVisible(isVisible);
        categoryNameLabel.setVisible(!isVisible);
        categoryNameTextField.setVisible(!isVisible);
    }

    private void hideErrorLabels() {
        for (Component c : deleteCategoryForm.getComponents()) {
            if (c instanceof ErrorLabel) {
                c.setVisible(false);
            }
        }
    }

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/
    /*=========================== LISTENER HANDLER ============================*/
    class FormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelBtn) {
                if (cancelListener != null) {
                    cancelListener.buttonActionOccurred();
                }
            } else if (e.getSource() == deleteBtn) {
                if (deleteListener != null) {
                    DeleteEvent event = new DeleteEvent(this, categoryIDLabel, categoryIDTextField,
                            categoryNameLabel, categoryNameTextField, errLabel,
                            idRuleErrLabel, othersDeleteErrLabel, deleteErrorLabel);
                    deleteListener.deleteEventOccurred(event);
                }
            } else if (e.getSource() == idRB) {
                deleteByIDForm();
                /* TEST CODE */
                System.err.println("INNER CLASS ID Radio Button NOT IMPL");
                System.out.println("INNER CLASS ID RADIO BUTTON SEL");
            } else if (e.getSource() == nameRB) {
                deleteByNameForm();
                /* TEST CODE */
                System.err.println("NAME Radio Button NOT IMPL");
                System.out.println("NAME RADIO BUTTON SEL");
            }
        }
    }
}
