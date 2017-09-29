package application.view.custom_components;

public interface LoginListener {
    void loginDetailsSet(String username, char[] password, boolean signup);
}
