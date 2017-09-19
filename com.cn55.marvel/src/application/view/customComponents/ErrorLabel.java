package application.view.customComponents;
import javax.swing.*;
public class ErrorLabel extends JLabel {
    public ErrorLabel(String text) {
        super(text);
        setFont(Style.errorFont());
        setForeground(Style.redA700());
        setVisible(false);
    }
}