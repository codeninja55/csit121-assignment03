package application.view.builderFactory;

import application.view.jtableModels.CategoriesTableModel;

import java.time.LocalDate;

public interface SummaryView {
    int getDaysOption();
    int getHoursOption();
    LocalDate getDateFromOption();
    LocalDate getDateToOption();
}
