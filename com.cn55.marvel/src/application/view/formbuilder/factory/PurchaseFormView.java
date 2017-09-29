package application.view.formbuilder.factory;
import application.model.card.CardType;
import application.model.category.Category;
import java.util.Map;
public interface PurchaseFormView {
    String getCardID();
    String getExistingCardID();
    String getCardName();
    String getCardEmail();
    CardType getCardType();
    int getReceiptID();
    String getPurchaseType();
    Map<Integer, Category> getCategories();
}
