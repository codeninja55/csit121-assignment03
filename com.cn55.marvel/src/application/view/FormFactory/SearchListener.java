package application.view.FormFactory;

import java.util.EventListener;

public interface SearchListener extends EventListener {
    void searchEventOccurred(SearchEvent e);
}
