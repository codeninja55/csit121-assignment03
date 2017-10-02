package application.view;

import application.model.dao.DataObservable;
import application.model.dao.DataObserver;
import application.model.card.Card;
import application.model.card.SortCardType;
import application.view.formbuilder.factory.CardForm;
import application.view.formbuilder.factory.DeleteCardForm;
import application.view.formbuilder.factory.SearchCardForm;
import styles.ColorFactory;
import styles.IconFactory;
import styles.TableFormatterFactory;
import application.view.custom.components.Toolbar;
import application.view.custom.components.ToolbarButton;
import application.view.custom.components.ToolbarButtonListener;
import application.view.table.models.CardTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class CardViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;
    private final ToolbarButton createCardBtn;
    private final ToolbarButton deleteCardBtn;
    private final ToolbarButton searchBtn;
    private final ToolbarButton viewBtn;
    private final CardTableModel cardTableModel;
    private final JComboBox<String> sortedCombo;
    private final JTable cardsTable;

    private ToolbarButtonListener createCardListener;
    private ToolbarButtonListener searchCardListener;
    private ToolbarButtonListener deleteCardListener;
    private ToolbarButtonListener viewCardListener;

    /*============================== CONSTRUCTORS ==============================*/
    CardViewPane() {
        Toolbar toolbar = new Toolbar();
        createCardBtn = new ToolbarButton("Create", IconFactory.createIcon());
        deleteCardBtn = new ToolbarButton("Delete", IconFactory.deleteIcon());
        searchBtn = new ToolbarButton("Search", IconFactory.searchIcon());
        viewBtn = new ToolbarButton("View", IconFactory.viewIcon());

        cardTableModel = new CardTableModel();
        cardsTable = new JTable(cardTableModel);
        TableFormatterFactory.cardTableFormatter(cardsTable);
        setLayout(new BorderLayout());

        /* SORTED COMBO BOX */
        String[] sortOptions = {"Sort..",
                SortCardType.CreatedOrder.name,
                SortCardType.ReverseCreatedOrder.name,
                SortCardType.Name.name,
                SortCardType.Points.name};
        sortedCombo = new JComboBox<>(sortOptions);
        sortedCombo.setSize(searchBtn.getPreferredSize());
        sortedCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, ColorFactory.blueGrey500()));
        sortedCombo.setSelectedIndex(0);

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createCardBtn);
        toolbar.getLeftToolbar().add(deleteCardBtn);
        toolbar.getLeftToolbar().add(searchBtn);
        toolbar.getRightToolbar().add(viewBtn);
        toolbar.getRightToolbar().add(sortedCombo);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(cardsTable), BorderLayout.CENTER);

        /* REGISTRATION OF TOOLBAR BUTTON LISTENERS */
        ToolbarListener handler = new ToolbarListener();
        createCardBtn.addActionListener(handler);
        searchBtn.addActionListener(handler);
        deleteCardBtn.addActionListener(handler);
        viewBtn.addActionListener(handler);
        createCardBtn.addMouseListener(handler);
        deleteCardBtn.addMouseListener(handler);
        searchBtn.addMouseListener(handler);
        viewBtn.addMouseListener(handler);
    }

    /*============================== MUTATORS  ==============================*/
    public void setCreateCardListener(ToolbarButtonListener listener) {
        this.createCardListener = listener;
    }

    public void setDeleteCardListener(ToolbarButtonListener listener) {
        this.deleteCardListener = listener;
    }

    public void setSearchCardListener(ToolbarButtonListener listener) { this.searchCardListener = listener; }

    public void setViewCardListener(ToolbarButtonListener listener) {
        this.viewCardListener = listener;
    }

    public void setCardForm(CardForm cardForm) {
        this.add(cardForm, BorderLayout.WEST);
        cardForm.setVisible(true);
    }

    public void setDeleteForm(DeleteCardForm deleteCardForm) {
        this.add(deleteCardForm, BorderLayout.WEST);
        deleteCardForm.setVisible(true);
    }

    public void setSearchCardForm(SearchCardForm form) {
        this.add(form, BorderLayout.WEST);
        form.setVisible(true);
    }

    public void updateTableData(ArrayList<Card> allCards) {
        cardTableModel.setData(allCards);
        cardTableModel.fireTableDataChanged();
    }

    /* OBSERVER DESIGN PATTERN IMPLEMENTATION */
    public void setSubject(DataObservable dataObservable) {
        this.dataDAO = dataObservable;
    }

    public void update() {
        ArrayList<Card> allCards = new ArrayList<>(dataDAO.getCardsUpdate(this).values());
        cardTableModel.setData(allCards);
        cardTableModel.fireTableDataChanged();
    }

    /*============================== ACCESSORS  ==============================*/
    public JComboBox<String> getSortedCombo() {
        return sortedCombo;
    }

    public JTable getCardsTable() {
        return cardsTable;
    }

    /*============================== INNER CLASS ==============================*/

    /*============================ TOOLBAR LISTENER ===========================*/
    class ToolbarListener extends MouseAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchBtn && searchCardListener != null) {
                searchCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == createCardBtn && createCardListener != null) {
                createCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == deleteCardBtn && deleteCardListener != null) {
                deleteCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == viewBtn && viewCardListener != null) {
                viewCardListener.toolbarButtonEventOccurred();
            }
        }
    }
}
