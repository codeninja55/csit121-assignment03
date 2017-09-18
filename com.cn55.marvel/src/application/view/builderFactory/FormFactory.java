package application.view.builderFactory;

/* FACTORY DESIGN PATTERN */

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

    static SearchForm searchCardForm() {
        return new SearchForm();
    }

    static DeleteCardForm deleteCardForm() {
        return new DeleteCardForm();
    }


}
