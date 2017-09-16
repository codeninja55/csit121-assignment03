package application.view;

import application.model.CategoryModel.Category;
import application.model.DataObservable;
import application.model.DataObserver;
import application.view.CustomComponents.Style;
import application.view.CustomComponents.Toolbar;
import application.view.CustomComponents.ToolbarButton;
import application.view.CustomComponents.ToolbarButtonListener;
import application.view.FormFactory.CategoriesForm;
import application.view.FormFactory.DeleteCategoryForm;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoriesViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;

    private ToolbarButton createCategoryBtn;
    private ToolbarButton deleteCategoryBtn;

    private CategoriesTableModel categoriesTableModel;
    private JTable categoriesTablePane;

    private ToolbarButtonListener createCategoryListener;
    private ToolbarButtonListener deleteCategoryListener;

    /*============================== CONSTRUCTORS  ==============================*/
    public CategoriesViewPane() {
        Toolbar toolbar = new Toolbar();
        createCategoryBtn = new ToolbarButton("Create");
        deleteCategoryBtn = new ToolbarButton("Delete");

        categoriesTableModel = new CategoriesTableModel();
        categoriesTablePane = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(categoriesTablePane);

        setLayout(new BorderLayout());

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createCategoryBtn);
        toolbar.getLeftToolbar().add(deleteCategoryBtn);
        add(toolbar, BorderLayout.NORTH);

        add(tableScrollPane, BorderLayout.CENTER);

        /* REGISTRATION OF TOOLBAR BUTTON LISTENERS */
        ToolbarListener handler = new ToolbarListener();
        createCategoryBtn.addActionListener(handler);
        deleteCategoryBtn.addActionListener(handler);
        createCategoryBtn.addMouseListener(handler);
        deleteCategoryBtn.addMouseListener(handler);
    }

    /*============================== MUTATORS  ==============================*/
    public void setCreateCategoryListener(ToolbarButtonListener listener) {
        this.createCategoryListener = listener;
    }

    public void setDeleteCategoryListener(ToolbarButtonListener listener) {
        this.deleteCategoryListener = listener;
    }

    private void categoriesTableFormatter() {
        // Formatting for the table where it renders the text.
        categoriesTablePane.setRowHeight(45);
        categoriesTablePane.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        categoriesTablePane.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        categoriesTablePane.getColumnModel().getColumn(2).setCellRenderer(Style.leftRenderer());
        categoriesTablePane.getColumnModel().getColumn(3).setCellRenderer(Style.rightRenderer());
    }

    public void setCategoriesTableModel() {
        categoriesTablePane.setModel(categoriesTableModel);
        categoriesTablePane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTableFormatter();
        this.revalidate();
        this.repaint();
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
        ArrayList<Category> allCategories = new ArrayList<>();
        allCategories.addAll(dataDAO.getCategoriesUpdate(this).values());
        categoriesTableModel.setData(allCategories);
        categoriesTableModel.fireTableDataChanged();
    }

    /*============================== ACCESSORS  ==============================*/

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/

    /*=========================== TOOLBAR LISTENER ============+===============*/
    class ToolbarListener extends MouseAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createCategoryBtn) {
                if (createCategoryListener != null) {
                    createCategoryListener.toolbarButtonEventOccurred();
                }
            } else if (e.getSource() == deleteCategoryBtn) {
                if (deleteCategoryListener != null) {
                    deleteCategoryListener.toolbarButtonEventOccurred();
                }
            }
        }
    }

    /*========================== CategoriesTableModel =========================*/
    public class CategoriesTableModel extends AbstractTableModel {

        private ArrayList<Category> categories;
        private String[] tableHeaders = {"ID", "Name", "Description", "Total Amount"};

        void setData(ArrayList<Category> categories) {
            Collections.sort(categories);
            categories.sort(Comparator.comparingInt(Category::getId));
            this.categories = categories;
        }

        public String getColumnName(int column) { return tableHeaders[column]; }

        public int getRowCount() {
            return categories.size();
        }

        public int getColumnCount() {
            return tableHeaders.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Category category = categories.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return category.getId();
                case 1:
                    return category.getName();
                case 2:
                    return category.getDescription();
                case 3:
                    return Style.currencyFormat().format(category.getTotalAmount());
            }
            return null;
        }
    }

}
