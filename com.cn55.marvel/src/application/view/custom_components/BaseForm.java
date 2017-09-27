package application.view.custom_components;

import styles.CustomBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BaseForm extends JPanel {
    protected BaseForm() {
        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(false);

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                getParent().remove(BaseForm.this);
            }
        });
    }

    protected void setCancelBtn(String text) {
        CancelButton cancelBtn = new CancelButton(text);
        cancelBtn.addActionListener(e -> this.setVisible(false));
        this.add(cancelBtn, BorderLayout.SOUTH);
    }

    protected void setBorder(String title) {
        this.setBorder(CustomBorderFactory.formBorder(title));
    }
}
