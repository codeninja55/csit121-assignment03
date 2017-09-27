package application.view.summary;

import java.time.LocalDate;

public interface SummaryView {
    int getDaysOption();
    int getHoursOption();
    LocalDate getDateFromOption();
    LocalDate getDateToOption();
}
