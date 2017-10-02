package application.view.formbuilder.factory;

import java.time.LocalDate;

public interface SummaryView {
    int getDaysOption();
    int getHoursOption();
    LocalDate getDateFromOption();
    LocalDate getDateToOption();
}
