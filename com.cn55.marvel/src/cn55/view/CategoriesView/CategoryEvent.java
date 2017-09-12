package cn55.view.CategoriesView;

import cn55.view.CustomComponents.FormTextField;

import javax.swing.*;
import java.util.EventObject;

public class CategoryEvent extends EventObject {
    private FormTextField categoryNameTextField;
    private JTextArea categoryDescTextField;

    /*============================== CONSTRUCTORS  ==============================*/
    CategoryEvent(Object source, FormTextField categoryNameTextField, JTextArea categoryDescTextField) {
        super(source);
        this.categoryNameTextField = categoryNameTextField;
        this.categoryDescTextField = categoryDescTextField;
    }

    /*============================== MUTATORS  ==============================*/

    /*============================== ACCESSORS  ==============================*/
    public FormTextField getCategoryNameTextField() {
        return categoryNameTextField;
    }

    public JTextArea getCategoryDescTextField() {
        return categoryDescTextField;
    }
}
