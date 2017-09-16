package application.view.FormFactory;

/* FACTORY DESIGN PATTERN */

public interface FormFactory {
    static CategoriesForm createCategoryForm() {
        return new CategoriesForm();
    }

    static DeleteCategoryForm deleteCategoryForm() {
        return new DeleteCategoryForm();
    }

    static PurchaseForm createPurchaseForm() {
        return new PurchaseForm();
    }

    static CardForm createCardForm() {
        return new CardForm();
    }

    static SearchForm searchCardForm() {
        return new SearchForm();
    }

    static DeleteCardForm deleteCardForm() {
        return new DeleteCardForm();
    }
}
