package application.view.FormFactory;

import java.util.EventListener;

public interface DeleteListener extends EventListener {
    void deleteEventOccurred(DeleteEvent e);
}