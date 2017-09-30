package application.model.dao;

import application.model.card.Card;
import application.model.category.Category;
import application.model.purchase.Purchase;

import java.util.HashMap;

abstract class DAO {
    protected HashMap<String,Card> cards;
    protected HashMap<Integer,Purchase> purchases;
    protected HashMap<Integer,Category> categories;

}
