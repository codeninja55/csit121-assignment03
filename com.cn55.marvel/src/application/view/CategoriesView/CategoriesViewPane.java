package application.view.CategoriesView;
import application.model.*;
import application.model.CategoryModel.Category;
import application.model.DataStoreModel;
import application.view.CustomComponents.Style;
import application.view.CustomComponents.Toolbar;
import application.view.CustomComponents.ToolbarButton;
import application.view.DeleteForm.DeleteCategoryForm;
import application.view.ToolbarButtonListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoriesViewPane extends JPanel implements Observer {
    private Subject database;

    private ToolbarButton createCategoryBtn;
    private ToolbarButton deleteCategoryBtn;

    private CategoriesTableModel categoriesTableModel;
    private JTable categoriesTablePane;

    private CategoriesForm createCategoryForm;
    private DeleteCategoryForm deleteCategoryForm;

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
        this.createCategoryForm = createCategoryForm;
    }

    public void setDeleteCategoryForm(DeleteCategoryForm deleteCategoryForm) {
        this.deleteCategoryForm = deleteCategoryForm;
    }

    /* OBSERVER DESIGN PATTERN IMPLEMENTATION */
    @Override
    public void setSubject(Subject subject) {
        this.database = subject;
    }

    @Override
    public void update() {
        categoriesTableModel.setData(database.getCategoriesUpdate(this));
        categoriesTableModel.fireTableDataChanged();
    }

    /*============================== ACCESSORS  ==============================*/
    public CategoriesForm getCreateCategoryForm() {
        return createCategoryForm;
    }

    public DeleteCategoryForm getDeleteCategoryForm() {
        return deleteCategoryForm;
    }

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

        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == createCategoryBtn)
                Style.hoverEffect(createCategoryBtn, true);
            else if (e.getSource() == deleteCategoryBtn)
                Style.hoverEffect(deleteCategoryBtn, true);
        }

        public void mouseExited(MouseEvent e) {
            if (e.getSource() == createCategoryBtn)
                Style.hoverEffect(createCategoryBtn, false);
            else if (e.getSource() == deleteCategoryBtn)
                Style.hoverEffect(deleteCategoryBtn, false);
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
                    return DataStoreModel.getCategoriesTotalMap().get(category.getId());
            }
            return null;
        }
    }

}
