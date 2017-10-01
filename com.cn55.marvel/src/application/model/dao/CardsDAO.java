package application.model.dao;

import application.model.card.Card;

import java.util.HashMap;

@SuppressWarnings("unused")
public interface CardsDAO {
    void createCard(Card card);
    Card getCard(String id);
    HashMap<String, Card> getOrigCardsMap();
    void deleteCard(String id);
}
