package application.view;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("ALL")
class SummaryViewPane extends JPanel {
    private final JPanel summaryPanel;

    public SummaryViewPane() {
        summaryPanel = new JPanel();
        setLayout(new BorderLayout());
        add(summaryPanel, BorderLayout.CENTER);
    }
}
