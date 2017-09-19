package application.view.builderFactory;

/* FACTORY DESIGN PATTERN */

import application.model.Generator;
import application.model.cardModel.Card;
import application.model.categoryModel.Category;

import java.util.ArrayList;

public interface FormFactory {
    static CategoriesForm createCategoryForm() {
        return new CategoriesForm();
    }

    static DeleteCategoryForm deleteCategoryForm() {
        return new DeleteCategoryForm();
    }

    static CardForm createCardForm() {
        return new CardForm();
    }

    static SearchCardForm searchCardForm() {
        return new SearchCardForm();
    }

    static DeleteCardForm deleteCardForm() {
        return new DeleteCardForm();
    }

    static PurchaseForm createPurchaseForm(ArrayList<Card> cards, ArrayList<Category> categories) {
        return new PurchaseForm.PurchaseFormBuilder(Generator.setReceiptID())
                .existingCardModel(cards).categoriesList(categories).build();
    }
}
