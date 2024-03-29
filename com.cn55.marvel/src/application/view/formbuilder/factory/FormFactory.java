package application.view.formbuilder.factory;

/* FACTORY DESIGN PATTERN */

import application.model.Generator;
import application.model.card.Card;
import application.model.category.Category;
import application.view.summary.SummaryAnalyticsPane;
import application.view.summary.SummaryViewPane;

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
        return new PurchaseForm.PurchaseFormBuilder(Generator.getReceiptID())
                .existingCardModel(cards).categoriesList(categories).build();
    }

    static SummaryFilterForm createSummaryFilterForm(SummaryViewPane parent, SummaryAnalyticsPane analyticsPane) {
        return new SummaryFilterForm(parent, analyticsPane);
    }
}
