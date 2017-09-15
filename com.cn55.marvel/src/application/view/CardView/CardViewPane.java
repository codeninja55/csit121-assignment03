package application.view.CardView;

import application.model.CardModel.*;
import application.model.DataObservable;
import application.model.DataObserver;
import application.view.CustomComponents.ResultsPane;
import application.view.CustomComponents.Style;
import application.view.CustomComponents.Toolbar;
import application.view.CustomComponents.ToolbarButton;
import application.view.DeleteForm.DeleteCardForm;
import application.view.SearchForm.SearchForm;
import application.view.ToolbarButtonListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CardViewPane extends JPanel implements DataObserver {
    private DataObservable dataDAO;

    private ToolbarButton createCardBtn;
    private ToolbarButton deleteCardBtn;
    private ToolbarButton searchBtn;
    private ToolbarButton viewBtn;

    private CardTableModel cardTableModel;
    private JComboBox<String> sortedCombo;
    private JTable cardTablePane;
    private ResultsPane resultsPane;

    private SearchForm searchForm;
    private CardForm cardForm;
    private DeleteCardForm deleteForm;

    private ToolbarButtonListener createCardListener;
    private ToolbarButtonListener searchCardListener;
    private ToolbarButtonListener deleteCardListener;
    private ToolbarButtonListener viewCardListener;

    /*============================== CONSTRUCTORS ==============================*/
    public CardViewPane() {
        Toolbar toolbar = new Toolbar();
        createCardBtn = new ToolbarButton("Create");
        deleteCardBtn = new ToolbarButton("Delete");
        searchBtn = new ToolbarButton("Search");
        viewBtn = new ToolbarButton("View");

        cardTableModel = new CardTableModel();
        cardTablePane = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(cardTablePane);
        tableScrollPane.setName("CardsViewTableScrollPane");

        resultsPane = new ResultsPane("CardViewResultsPane");

        setName("CardsViewPane");
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

        add(resultsPane, BorderLayout.EAST);

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
        this.cardForm = cardForm;
    }

    public void setDeleteForm(DeleteCardForm deleteForm) {
        this.deleteForm = deleteForm;
    }

    public void setSearchForm(SearchForm searchForm) {
        this.searchForm = searchForm;
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

    public SearchForm getSearchForm() {
        return searchForm;
    }

    public CardForm getCardForm() {
        return cardForm;
    }

    public DeleteCardForm getDeleteForm() {
        return deleteForm;
    }

    public JTable getCardTablePane() {
        return cardTablePane;
    }

    public CardTableModel getCardTableModel() {
        return cardTableModel;
    }

    public ResultsPane getResultsPane() {
        return resultsPane;
    }

    /*=========================================================================*/
    /*============================== INNER CLASS ==============================*/
    /*=========================================================================*/

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

        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == createCardBtn) Style.hoverEffect(createCardBtn, true);
            else if (e.getSource() == deleteCardBtn) Style.hoverEffect(deleteCardBtn, true);
            else if (e.getSource() == viewBtn) Style.hoverEffect(viewBtn, true);
            else if (e.getSource() == searchBtn) Style.hoverEffect(searchBtn, true);
        }

        public void mouseExited(MouseEvent e) {
            if (e.getSource() == createCardBtn) Style.hoverEffect(createCardBtn, false);
            else if (e.getSource() == deleteCardBtn) Style.hoverEffect(deleteCardBtn, false);
            else if (e.getSource() == viewBtn) Style.hoverEffect(viewBtn, false);
            else if (e.getSource() == searchBtn) Style.hoverEffect(searchBtn, false);
        }


    }

    /*============================= CardTableModel ============================*/
    class CardTableModel extends AbstractTableModel {
        private ArrayList<Card> cards;
        private String[] cardHeaders = {"Card ID", "Card Type", "Name", "Email", "Balance", "Points"};

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
