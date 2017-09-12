package cn55.view.DeleteForm;

import java.util.EventListener;

public interface DeleteListener extends EventListener {
    void deleteEventOccurred(DeleteEvent e);
}