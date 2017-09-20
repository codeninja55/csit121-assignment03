package application.view.builderFactory;
import application.model.cardModel.CardType;
import application.model.categoryModel.Category;
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
