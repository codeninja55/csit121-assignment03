package cn55.view.SearchForm;

import java.util.EventListener;

public interface SearchListener extends EventListener {
    void searchEventOccurred(SearchEvent e);
}
