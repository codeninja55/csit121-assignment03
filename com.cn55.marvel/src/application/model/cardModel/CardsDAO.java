package application.model.cardModel;

import java.util.HashMap;

@SuppressWarnings("unused")
public interface CardsDAO {
    void createCard(Card card);
    Card getCard(String id);
    HashMap<String, Card> getAllCards();
    void deleteCard(String id);
}
