package application.view.custom_components;

import styles.ColorFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

public class LoginDialog extends JDialog {
    private final JPanel loginForm;
    private final JCheckBox signupOption;
    private final FormLabel usernameLabel;
    private final FormTextField usernameTextField;
    private final FormLabel passwordLabel;
    private final JPasswordField passwordTextField;
    private final FormButton loginBtn;
    private final FormButton cancelBtn;
    private LoginListener listener;

    public LoginDialog(JFrame parent) {
        super(parent, "Administrator Login", false);
        setSize(600, 300);

        loginForm = new JPanel(new GridBagLayout());
        signupOption = new JCheckBox("Sign Up", IconFactory.checkBoxIcon());
        usernameLabel = new FormLabel("USERNAME: ");
        usernameTextField = new FormTextField(10);
        passwordLabel = new FormLabel("PASSWORD: ");
        passwordTextField = new JPasswordField(10);
        loginBtn = new FormButton("Login", IconFactory.loginWhiteIcon());
        cancelBtn = new FormButton("Cancel", IconFactory.cancel24Icon());

        createLoginForm();
        setDefaults();
        add(loginForm);

        Arrays.stream(loginForm.getComponents())
                .filter(c -> c instanceof FormButton || c instanceof FormLabel || c instanceof FormTextField)
                .forEach(c -> c.setVisible(true));

        setLocationRelativeTo(parent);

        cancelBtn.addActionListener(e -> setVisible(false));

        loginBtn.addActionListener(e -> {
            String username = usernameTextField.getText().trim();
            char[] password = passwordTextField.getPassword();
            if (listener != null) listener.loginDetailsSet(username, password, signupOption.isSelected());
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                setDefaults();
            }
        });
    }

    private void createLoginForm() {
        GridBagConstraints gc = new GridBagConstraints();
        /*========== FIRST ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.5; gc.weighty = 0.2;
        gc.insets = new Insets(20,30,0,5);
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        loginForm.add(usernameLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1; gc.gridy = 0;
        gc.insets = new Insets(20,5,0,30);
        usernameTextField.setFocusable(true);
        loginForm.add(usernameTextField, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(15,30,0,5);
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        loginForm.add(passwordLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(15,5,0,30);
        passwordTextField.setFont(FontFactory.textFieldFont());
        passwordTextField.setPreferredSize(usernameTextField.getPreferredSize());
        loginForm.add(passwordTextField, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(15,30,0,5);
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        loginForm.add(passwordLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(15,5,0,30);
        passwordTextField.setFont(FontFactory.textFieldFont());
        passwordTextField.setPreferredSize(usernameTextField.getPreferredSize());
        loginForm.add(passwordTextField,gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1; gc.gridy++;
        gc.insets = new Insets(5,0,0,0);
        signupOption.setSelectedIcon(IconFactory.checkBoxIconChecked());
        signupOption.setFont(FontFactory.labelFont());
        signupOption.setBackground(ColorFactory.blueGrey200());
        signupOption.setBorderPainted(false);
        signupOption.setBorderPaintedFlat(false);
        signupOption.setHorizontalAlignment(SwingConstants.LEFT);
        loginForm.add(signupOption, gc);

        signupOption.setEnabled(false);

        /*========== BUTTON ROW ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy++; gc.weightx = 2; gc.weighty = 2;
        gc.insets = new Insets(15,30,0,15);
        loginBtn.setPreferredSize(cancelBtn.getPreferredSize());
        loginBtn.setMinimumSize(loginBtn.getPreferredSize());
        loginForm.add(loginBtn, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridx = 1; gc.weightx = 2;
        gc.insets = new Insets(15,15,0,30);
        loginForm.add(cancelBtn, gc);
    }

    public void setListener(LoginListener listener) { this.listener = listener; }

    private void setDefaults() {
        usernameTextField.setText(null);
        passwordTextField.setText(null);
        signupOption.setSelected(false);
    }

}
