package application.view.builderFactory;

import application.model.Generator;
import application.view.customComponents.*;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class CategoriesForm extends BaseForm implements FormFactory, CategoryFormView {
    private final JPanel createCategoriesForm;
    private final FormTextField categoryNameTextField;
    private final JTextArea categoryDescTextField;
    private CategoryListener createCategoryListener;

    CategoriesForm() {
        super();
        super.setBorder("New Category");
        super.setCancelBtn("Cancel Create Category");

        createCategoriesForm = new JPanel(new GridBagLayout());
        FormLabel categoryIDLabel = new FormLabel("Category ID");
        FormTextField categoryIDTextField = new FormTextField(20);
        FormLabel categoryNameLabel = new FormLabel("Category Name");
        categoryNameTextField = new FormTextField(35);
        FormLabel categoryDescLabel = new FormLabel("Category Description");
        categoryDescTextField = new JTextArea(15,35);
        FormButton createBtn = new FormButton("Create Category", IconFactory.addIcon());
        FormButton clearBtn = new FormButton("Clear", IconFactory.clearIcon());

        /* FORM AREA */
        GridBagConstraints gc = new GridBagConstraints();
        /*========== FIRST ROW ==========*/
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 0.1; gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(20,0,0,0);
        createCategoriesForm.add(categoryIDLabel, gc);
        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        categoryIDTextField.setEditable(false);
        Dimension textFieldDim = getPreferredSize();
        textFieldDim.width = 350;
        textFieldDim.height = 50;
        categoryIDTextField.setMinimumSize(textFieldDim);
        categoryIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
        createCategoriesForm.add(categoryIDTextField, gc);
        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        createCategoriesForm.add(categoryNameLabel, gc);
        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        categoryNameTextField.setPreferredSize(textFieldDim);
        categoryNameTextField.setMinimumSize(textFieldDim);
        createCategoriesForm.add(categoryNameTextField, gc);
        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        createCategoriesForm.add(categoryDescLabel, gc);
        /*========== NEW ROW ==========*/
        gc.gridy++;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,10,0);
        Dimension descTextFieldDim = getPreferredSize();
        descTextFieldDim.width = 350;
        descTextFieldDim.height = 250;
        categoryDescTextField.setPreferredSize(descTextFieldDim);
        categoryDescTextField.setMinimumSize(descTextFieldDim);
        categoryDescTextField.setFont(FontFactory.textAreaFont());
        createCategoriesForm.add(categoryDescTextField, gc);
        categoryDescTextField.setVisible(true);
        /*========== BUTTON ROW ==========*/
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 3; gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(20,0,0,10);
        createCategoriesForm.add(createBtn, gc);

        gc.gridx = 1; gc.gridwidth = 1; gc.weightx = 0.5;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,10,0,0);
        createCategoriesForm.add(clearBtn, gc);
        add(createCategoriesForm, BorderLayout.CENTER);

        /* SET FORM CUSTOM COMPONENTS VISIBLE */
        Arrays.stream(createCategoriesForm.getComponents())
            .filter(c -> c instanceof FormLabel || c instanceof FormTextField || c instanceof FormButton)
            .forEach(c -> c.setVisible(true));

        categoryIDTextField.setText(Integer.toString(Generator.getNextCategoryID()));

        createBtn.addActionListener((ActionEvent e) -> {
            if (createCategoryListener != null)
                createCategoryListener.createCategoryEventOccurred(CategoriesForm.this);
        });

        clearBtn.addActionListener((ActionEvent e) -> Arrays.stream(createCategoriesForm.getComponents()).forEach(c -> {
            if (c instanceof JTextField && ((JTextField) c).isEditable()) ((JTextField) c).setText("");
            if (c instanceof JTextArea) ((JTextArea) c).setText("");
            if (c instanceof FormLabel) c.setForeground(Color.BLACK);
        }));
    }

    /*============================== MUTATORS  ==============================*/
    public void setCreateCategoryListener(CategoryListener listener) {
        this.createCategoryListener = listener;
    }

    /*============================== VIEW ONLY IMPLEMENTATIONS ==============================*/
    public String getCategoryName() { return categoryNameTextField.getText().trim(); }
    public String getDescription() { return categoryDescTextField.getText().trim(); }
}
