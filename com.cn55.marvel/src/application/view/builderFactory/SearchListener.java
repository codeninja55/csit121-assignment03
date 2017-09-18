package application.view.builderFactory;

import java.util.EventListener;

public interface SearchListener extends EventListener {
    void searchEventOccurred(SearchEvent e);
}
