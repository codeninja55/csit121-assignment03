package application.view.builderFactory;

import application.view.jtableModels.CategoriesTableModel;

import java.time.LocalDate;

public interface SummaryView {
    CategoriesTableModel getCategoryTableModel();
    int getDaysOption();
    int getHoursOption();
    String getTableOption();
    LocalDate getDateFromOption();
    LocalDate getDateToOption();
}
