package application.view;

import application.model.cardModel.*;
import application.model.DataObservable;
import application.model.DataObserver;
import application.view.customComponents.Style;
import application.view.customComponents.Toolbar;
import application.view.customComponents.ToolbarButton;
import application.view.customComponents.ToolbarButtonListener;
import application.view.builderFactory.CardForm;
import application.view.builderFactory.DeleteCardForm;
import application.view.builderFactory.SearchCardForm;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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
    private final JTable cardTablePane;

    private ToolbarButtonListener createCardListener;
    private ToolbarButtonListener searchCardListener;
    private ToolbarButtonListener deleteCardListener;
    private ToolbarButtonListener viewCardListener;

    /*============================== CONSTRUCTORS ==============================*/
    CardViewPane() {
        Toolbar toolbar = new Toolbar();
        createCardBtn = new ToolbarButton("Create", Style.createIcon());
        deleteCardBtn = new ToolbarButton("Delete", Style.deleteIcon());
        searchBtn = new ToolbarButton("Search", Style.searchIcon());
        viewBtn = new ToolbarButton("View", Style.viewIcon());

        cardTableModel = new CardTableModel();
        cardTablePane = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(cardTablePane);
        tableScrollPane.setName("CardsViewTableScrollPane");
        setLayout(new BorderLayout());

        /* SORTED COMBO BOX */
        String[] sortOptions = {"Sort..",
                SortCardType.CreatedOrder.getName(),
                SortCardType.ReverseCreatedOrder.getName(),
                SortCardType.Name.getName(),
                SortCardType.Points.getName()};
        sortedCombo = new JComboBox<>(sortOptions);
        sortedCombo.setSize(searchBtn.getPreferredSize());
        sortedCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Style.blueGrey500()));
        sortedCombo.setSelectedIndex(0);

        /* TOOLBAR */
        toolbar.getLeftToolbar().add(createCardBtn);
        toolbar.getLeftToolbar().add(deleteCardBtn);
        toolbar.getLeftToolbar().add(searchBtn);
        toolbar.getRightToolbar().add(viewBtn);
        toolbar.getRightToolbar().add(sortedCombo);
        add(toolbar, BorderLayout.NORTH);

        add(tableScrollPane, BorderLayout.CENTER);

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

    private void cardTableFormatter() {
        // Formatting for the table where it renders the text.
        cardTablePane.setRowHeight(45);
        cardTablePane.getColumnModel().getColumn(0).setCellRenderer(Style.centerRenderer());
        cardTablePane.getColumnModel().getColumn(0).setPreferredWidth(1);
        cardTablePane.getColumnModel().getColumn(1).setCellRenderer(Style.centerRenderer());
        cardTablePane.getColumnModel().getColumn(1).setPreferredWidth(5);
        cardTablePane.getColumnModel().getColumn(2).setCellRenderer(Style.centerRenderer());
        cardTablePane.getColumnModel().getColumn(3).setCellRenderer(Style.centerRenderer());
        cardTablePane.getColumnModel().getColumn(4).setCellRenderer(Style.rightRenderer());
        cardTablePane.getColumnModel().getColumn(4).setPreferredWidth(5);
        cardTablePane.getColumnModel().getColumn(5).setCellRenderer(Style.rightRenderer());
        cardTablePane.getColumnModel().getColumn(5).setPreferredWidth(5);
    }

    public void setCardTableModel() {
        cardTablePane.setModel(cardTableModel);
        cardTablePane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cardTableFormatter();
        this.revalidate();
        this.repaint();
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

    public JTable getCardTablePane() {
        return cardTablePane;
    }

    /*============================== INNER CLASS ==============================*/

    /*============================ TOOLBAR LISTENER ===========================*/
    public class ToolbarListener extends MouseAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == searchBtn) {
                if (searchCardListener != null) searchCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == createCardBtn) {
                if (createCardListener != null) createCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == deleteCardBtn) {
                if (deleteCardListener != null) deleteCardListener.toolbarButtonEventOccurred();
            } else if (e.getSource() == viewBtn) {
                if (viewCardListener != null) viewCardListener.toolbarButtonEventOccurred();
            }
        }
    }

    /*============================= CardTableModel ============================*/
    class CardTableModel extends AbstractTableModel {
        private ArrayList<Card> cards;
        private final String[] cardHeaders = {"Card ID", "Card Type", "Name", "Email", "Balance", "Points"};

        void setData(ArrayList<Card> cards) {
            this.cards = cards;
        }

        public String getColumnName(int column) {
            return cardHeaders[column];
        }

        public int getRowCount() {
            return cards.size();
        }

        public int getColumnCount() {
            return cardHeaders.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Card card = cards.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return card.getID();
                case 1:
                    return card.getCardType();
                case 2:
                    if (card instanceof BasicCard) {
                        return ((BasicCard) card).getName();
                    } else if (card instanceof PremiumCard) {
                        return ((PremiumCard) card).getName();
                    } else {
                        return "";
                    }
                case 3:
                    if (card instanceof BasicCard) {
                        return ((BasicCard) card).getEmail();
                    } else if (card instanceof PremiumCard) {
                        return ((PremiumCard) card).getEmail();
                    } else {
                        return "";
                    }
                case 4:
                    if (card instanceof AdvancedCard) {
                        return Style.currencyFormat().format(((AdvancedCard) card).getBalance());
                    } else {
                        return "";
                    }

                case 5:
                    return Style.pointsFormat().format(card.getPoints());
            }
            return null;
        }
    }
}
