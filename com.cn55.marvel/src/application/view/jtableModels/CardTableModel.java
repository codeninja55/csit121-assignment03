package application.view.jtableModels;

import application.model.card.AdvancedCard;
import application.model.card.BasicCard;
import application.model.card.Card;
import application.model.card.PremiumCard;
import styles.Style;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CardTableModel extends AbstractTableModel {
    private ArrayList<Card> cards;
    private final String[] cardHeaders = {"Card ID", "Card Type", "Name", "Email", "Balance", "Points"};

    public void setData(ArrayList<Card> cards) {
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