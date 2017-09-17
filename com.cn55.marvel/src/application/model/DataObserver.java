package application.model;

public interface DataObserver {
    // Method to getCategoriesUpdate the observer, used by the observable object
    void update();
    // Attach with observable object to observer
    void setSubject(DataObservable dataObservable);
}
