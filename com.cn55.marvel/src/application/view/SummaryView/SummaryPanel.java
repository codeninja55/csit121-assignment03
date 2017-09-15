package application.view.SummaryView;
import javax.swing.*;
import java.awt.*;

class SummaryPanel extends JPanel {
    private JPanel summaryPanel;

    public SummaryPanel() {
        summaryPanel = new JPanel();

        setLayout(new BorderLayout());

        add(summaryPanel, BorderLayout.CENTER);

    }
}
