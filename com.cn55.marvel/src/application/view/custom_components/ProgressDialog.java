package application.view.custom_components;

import styles.ColorFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {
    private JProgressBar progressBar;
    private CancelButton cancelBtn;

    public ProgressDialog(Window parent, String title, String initialString) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setLayout(new FlowLayout());

        progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
        cancelBtn = new CancelButton("Cancel");
        cancelBtn.setVisible(true);

        Dimension size = cancelBtn.getPreferredSize();
        size.width = 400;
        progressBar.setPreferredSize(size);
        progressBar.setMinimumSize(size);
        progressBar.setStringPainted(true);
        progressBar.setFont(FontFactory.sliderFont());
        progressBar.setForeground(ColorFactory.redA700());
        progressBar.setBackground(ColorFactory.red200());
        progressBar.setIndeterminate(false);
        progressBar.setString(initialString);

        add(progressBar);
        add(cancelBtn);
        pack();
        setLocationRelativeTo(parent);
    }

    public void setMaximum(int value) { progressBar.setMaximum(value); }

    public void setValue(int value) {
        int progress = 100*value/progressBar.getMaximum();
        progressBar.setString(String.format("%d%%", progress));
        progressBar.setValue(value);
    }

    public void setString(String text) { progressBar.setString(text); }

    public CancelButton getCancelBtn() { return cancelBtn; }

    @Override
    public void setVisible(boolean isVisible) {
        SwingUtilities.invokeLater(() -> {
            if (!isVisible) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                progressBar.setValue(0);
            }
            ProgressDialog.super.setVisible(isVisible);
        });
    }
}
