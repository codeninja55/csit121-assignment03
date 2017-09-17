package application.model.CardModel;

import java.util.HashMap;

public interface CardsDAO {
    void createCard(Card card);
    Card getCard(String id);
    HashMap<String, Card> getAllCards();
    void deleteCard(String id);
}
