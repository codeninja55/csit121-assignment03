package application.view;

import application.model.dao.DataObservable;
import application.model.dao.DataObserver;
import application.model.category.Category;
import application.view.formbuilder.factory.CategoriesForm;
import application.view.formbuilder.factory.DeleteCategoryForm;
import styles.IconFactory;
import styles.TableFormatterFactory;
import application.view.custom.components.Toolbar;
import application.view.custom.components.ToolbarButton;
import application.view.custom.components.ToolbarButtonListener;
import application.view.table.models.CategoriesTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CategoriesViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private final CategoriesTableModel categoriesTableModel;
    private ToolbarButtonListener createCategoryListener;
    private ToolbarButtonListener deleteCategoryListener;

    /*============================== CONSTRUCTORS  ==============================*/
    CategoriesViewPane() {
        Toolbar toolbar = new Toolbar();
        ToolbarButton createCategoryBtn = new ToolbarButton("Create", IconFactory.createIcon());
        ToolbarButton deleteCategoryBtn = new ToolbarButton("Delete", IconFactory.deleteIcon());

        categoriesTableModel = new CategoriesTableModel();
        JTable categoriesTable = new JTable(categoriesTableModel);
        TableFormatterFactory.categoriesTableFormatter(categoriesTable);

        setLayout(new BorderLayout());

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createCategoryBtn);
        toolbar.getLeftToolbar().add(deleteCategoryBtn);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(categoriesTable), BorderLayout.CENTER);

        /* REGISTRATION OF TOOLBAR BUTTON LISTENERS */
        createCategoryBtn.addActionListener((ActionEvent e) -> {
            if (createCategoryListener != null)
                createCategoryListener.toolbarButtonEventOccurred();
        });

        deleteCategoryBtn.addActionListener((ActionEvent e) -> {
            if (deleteCategoryListener != null)
                deleteCategoryListener.toolbarButtonEventOccurred();
        });
    }

    /*============================== MUTATORS  ==============================*/
    public void setCreateCategoryListener(ToolbarButtonListener listener) {
        this.createCategoryListener = listener;
    }

    public void setDeleteCategoryListener(ToolbarButtonListener listener) {
        this.deleteCategoryListener = listener;
    }

    public void setCreateCategoryForm(CategoriesForm createCategoryForm) {
        this.add(createCategoryForm, BorderLayout.WEST);
        createCategoryForm.setVisible(true);
    }

    public void setDeleteCategoryForm(DeleteCategoryForm deleteCategoryForm) {
        this.add(deleteCategoryForm, BorderLayout.WEST);
        deleteCategoryForm.setVisible(true);
    }

    /* OBSERVER DESIGN PATTERN IMPLEMENTATION */
    public void setSubject(DataObservable dataObservable) {
        this.dataDAO = dataObservable;
    }

    public void update() {
        ArrayList<Category> allCategories = new ArrayList<>(dataDAO.getCategoriesUpdate(this).values());
        categoriesTableModel.setData(allCategories);
        categoriesTableModel.fireTableDataChanged();
    }
}
