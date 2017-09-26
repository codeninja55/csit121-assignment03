package application.view.builderFactory;

import application.view.jtableModels.CategoriesTableModel;

public interface SummaryView {
    CategoriesTableModel getCategoryTableModel();
    int getDaysOption();
    int getHoursOption();
    String getTableOption();
}
