package application.view.custom_components;

import javax.swing.*;
import java.awt.*;

// TODO - This implementation does not work. Need to use multithreading with SwingWorker
public class ProgressDialog extends JDialog {
    private JProgressBar progressBar;


    public ProgressDialog(Window parent) {
        super(parent, "Saving data...", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout());

        progressBar = new JProgressBar(0,100);
        progressBar.setVisible(true);
        CancelButton cancelBtn = new CancelButton("Cancel");
        cancelBtn.setVisible(true);

        Dimension size = cancelBtn.getPreferredSize();
        size.width = 400;
        progressBar.setPreferredSize(size);
        progressBar.setMinimumSize(size);
        progressBar.setValue(0);
        progressBar.setIndeterminate(false);

        add(progressBar);
        add(cancelBtn);
        pack();
    }

    public void setMaximum(int value) { progressBar.setMaximum(value); }

    public void setValue(int value) {
        progressBar.setValue(value);
    }

    @Override
    public void setVisible(boolean isVisible) {
        SwingUtilities.invokeLater(() -> {
            if (!isVisible) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ProgressDialog.super.setVisible(isVisible);
        });
    }
}
