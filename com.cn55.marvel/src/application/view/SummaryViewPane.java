package application.view;
import javax.swing.*;
import java.awt.*;

class SummaryViewPane extends JPanel {
    private JPanel summaryPanel;

    public SummaryViewPane() {
        summaryPanel = new JPanel();
        setLayout(new BorderLayout());
        add(summaryPanel, BorderLayout.CENTER);
    }
}
