package application.view.builderFactory;

import application.controller.validator.*;
import application.view.customComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

public class DeleteCategoryForm extends JPanel implements FormFactory, DeleteFormView{
    private JPanel deleteCategoryForm;
    private FormLabel categoryIDLabel;
    private FormTextField categoryIDTextField;
    private FormLabel categoryNameLabel;
    private FormTextField categoryNameTextField;
    private ErrorLabel errLabel;
    private ErrorLabel idRuleErrLabel;
    private ErrorLabel othersDeleteErrLabel;
    private ErrorLabel deleteErrorLabel;
    private FormButton deleteBtn;

    private DeleteCardListener deleteCardListener;

    /*============================== CONSTRUCTORS  ==============================*/
    DeleteCategoryForm() {
        CancelButton cancelBtn = new CancelButton("Cancel Category Delete");
        JPanel rbSubPane = new JPanel(new GridLayout(1, 3));
        ButtonGroup categoryRBGroup = new ButtonGroup();
        FormLabel deleteTypeLabel = new FormLabel("Delete By: ");
        FormRadioButton idRB = new FormRadioButton("Category ID");
        FormRadioButton nameRB = new FormRadioButton("Category Name");

        categoryIDLabel = new FormLabel("Category ID");
        categoryIDTextField = new FormTextField(20);
        categoryNameLabel = new FormLabel("Category Name");
        categoryNameTextField = new FormTextField(20);

        errLabel = new ErrorLabel("CATEGORY DOES NOT EXIST");
        idRuleErrLabel = new ErrorLabel("INVALID CATEGORY ID");
        othersDeleteErrLabel = new ErrorLabel("CANNOT DELETE OTHERS CATEGORY");
        deleteErrorLabel = new ErrorLabel("CATEGORY NOT DELETED");
        deleteBtn = new FormButton("Delete Category", Style.deleteActionIcon());

        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("Delete Category"));
        setVisible(false);

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
        cancelBtn.addActionListener(e -> setVisible(false));
        cancelBtn.addActionListener((ActionEvent e) -> setVisible(false));

        deleteBtn.addActionListener((ActionEvent e) -> {
            hideErrorLabels();
            boolean proceed;
            FormValidData validData = new FormValidData();
            validData.setCategoryIDStr(getID());
            FormRule categoryIDRule = new CategoryIDRule();
            ExistsRule categoryExistsRule = new CategoryExistsRule();

            if (!categoryIDRule.validate(validData)) {
                idRuleErrLabel.setVisible(true);
                proceed = false;
            } else {
                validData.setCategoryID(Integer.parseInt(getID()));
                if (getID().equals("100")) {
                    othersDeleteErrLabel.setVisible(true);
                    proceed = false;
                } else if (!categoryExistsRule.existsValidating(validData)) {
                    errLabel.setVisible(true);
                    proceed = false;
                } else {
                    proceed = true;
                }
            }

            if (proceed && deleteCardListener != null) deleteCardListener.formSubmitted(DeleteCategoryForm.this);
            else deleteErrorLabel.setVisible(true);
        });

        idRB.addActionListener((ActionEvent e) -> deleteByIDForm());

        nameRB.addActionListener((ActionEvent e) -> {
            deleteByNameForm();
            /* TEST CODE */
            System.err.println("NAME Radio Button NOT IMPL");
            System.out.println("NAME RADIO BUTTON SEL");
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                getParent().remove(DeleteCategoryForm.this);
            }
        });

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
        deleteCategoryForm.add(errLabel, gc);

        /*========== NEW ROW - DELETE OTHERS ERROR LABEL ==========*/
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
    public void setDeleteCardListener(DeleteCardListener listener) {
        this.deleteCardListener = listener;
    }

    private void switchIDAndNameFields(boolean isVisible) {
        categoryIDLabel.setVisible(isVisible);
        categoryIDTextField.setVisible(isVisible);
        categoryNameLabel.setVisible(!isVisible);
        categoryNameTextField.setVisible(!isVisible);
    }

    private void hideErrorLabels() {
        Arrays.stream(deleteCategoryForm.getComponents()).filter(c -> c instanceof ErrorLabel)
                .forEach(c -> c.setVisible(false));
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getID() { return categoryIDTextField.getText().trim(); }
}
