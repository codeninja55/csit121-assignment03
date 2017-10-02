package application.view;

import application.view.custom.components.*;
import styles.ColorFactory;
import styles.CustomBorderFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public class StartViewPane extends JPanel {
    private final JTabbedPane tabbedPane;
    private final JPanel loginPane;
    private final JCheckBox newUserOption;
    private final FormLabel usernameLabel;
    private final FormTextField usernameTextField;
    private final FormLabel passwordLabel;
    private final JPasswordField passwordTextField;
    private final FormButton loginBtn;
    private final FormButton logoutBtn;
    private LoginListener listener;

    StartViewPane(JTabbedPane tabbedPane) {
        setLayout(new BorderLayout());

        this.tabbedPane = tabbedPane;
        Toolbar toolbar = new Toolbar();
        loginPane = new JPanel(new FlowLayout());
        newUserOption = new JCheckBox("New User", IconFactory.checkBoxIcon());
        usernameLabel = new FormLabel("Username: ");
        usernameTextField = new FormTextField(10);
        passwordLabel = new FormLabel("Password: ");
        passwordTextField = new JPasswordField(10);
        loginBtn = new FormButton("Login", IconFactory.loginWhiteIcon());
        logoutBtn = new FormButton("Logout", IconFactory.logoutWhiteIcon());
        toolbar.setBorder(CustomBorderFactory.toolbarBorder(""));

        JLabel copyrightLabel = new JLabel(" codeninja55  |  Dinh Che  |  5721970", IconFactory.copyrightIcon(), SwingConstants.RIGHT);
        copyrightLabel.setFont(FontFactory.toolbarButtonFont());
        copyrightLabel.setForeground(ColorFactory.red500());
        copyrightLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        toolbar.getLeftToolbar().add(setDateTimePanel());
        toolbar.getRightToolbar().add(setLoginPanel());

        add(toolbar, BorderLayout.NORTH);
        add(createWelcomePane(), BorderLayout.CENTER);
        add(copyrightLabel, BorderLayout.SOUTH);
    }

    private JPanel createWelcomePane() {
        JPanel welcomePane = new JPanel(new GridBagLayout());

        URL marvelURL = getClass().getResource("/img/Marvel-Logo-3-25pc.png");
        ImageIcon marvelImage = new ImageIcon(marvelURL);
        JLabel welcomeLabel = new JLabel("WELCOME TO");
        JLabel imageLabel = new JLabel("", marvelImage, SwingConstants.CENTER);
        JLabel rewardsCardLabel = new JLabel("REWARDS CARDS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Product Sans", Font.BOLD,80));
        welcomeLabel.setForeground(ColorFactory.red500());
        rewardsCardLabel.setFont(new Font("Product Sans", Font.BOLD,72));
        rewardsCardLabel.setForeground(ColorFactory.red500());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(300,0,0,0);
        welcomePane.add(welcomeLabel, gc);

        gc.gridy++; gc.insets = new Insets(0,0,0,0);
        welcomePane.add(imageLabel, gc);

        gc.gridy++;  gc.weighty = 2;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(40,0,0,0);
        welcomePane.add(rewardsCardLabel, gc);

        return welcomePane;
    }

    private JPanel setLoginPanel() {
        loginPane.add(new JLabel(new ImageIcon("com.cn55.marvel/src/img/supervisor_account_black_48.png")));
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        FormLabel adminLabel = new FormLabel("Administrator Login    ");
        adminLabel.setBorder(BorderFactory.createMatteBorder(0,0,0,4, ColorFactory.blueGrey800()));
        loginPane.add(adminLabel);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        loginPane.add(usernameLabel);
        Dimension size = usernameTextField.getPreferredSize();
        size.height = 45;
        usernameTextField.setPreferredSize(size);
        usernameTextField.setMinimumSize(size);
        loginPane.add(usernameTextField);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        loginPane.add(passwordLabel);
        passwordTextField.setFont(FontFactory.textFieldFont());
        passwordTextField.setPreferredSize(usernameTextField.getPreferredSize());
        loginPane.add(passwordTextField);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        newUserOption.setSelectedIcon(IconFactory.checkBoxIconChecked());
        newUserOption.setFont(FontFactory.labelFont());
        newUserOption.setBackground(ColorFactory.blueGrey200());
        newUserOption.setBorderPainted(false);
        newUserOption.setBorderPaintedFlat(false);
        newUserOption.setEnabled(false);
        loginPane.add(newUserOption);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        loginBtn.setIconTextGap(15);
        loginPane.add(loginBtn);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        logoutBtn.setIconTextGap(15);
        Dimension btnSize = logoutBtn.getPreferredSize();
        btnSize.height = 50;
        loginBtn.setPreferredSize(btnSize);
        logoutBtn.setPreferredSize(btnSize);
        loginPane.add(logoutBtn);
        loginPane.add(new JSeparator(JSeparator.HORIZONTAL));

        Arrays.stream(loginPane.getComponents())
                .filter(c -> c instanceof FormButton || c instanceof FormLabel || c instanceof FormTextField).forEach(c -> c.setVisible(true));
        logoutBtn.setEnabled(false);


        loginBtn.addActionListener(e -> {
            String username = usernameTextField.getText().trim();
            char[] password = passwordTextField.getPassword();
            if (listener != null) listener.loginDetailsSet(username, password, newUserOption.isSelected());
        });

        logoutBtn.addActionListener(e -> {
            logoutBtn.setEnabled(false);
            newUserOption.setEnabled(false);
            tabbedPane.setEnabledAt(4,false);
            setDefaults();
        });

        return loginPane;
    }

    private JPanel setDateTimePanel() {
        JPanel dateTimePanel = new JPanel(new FlowLayout());
        ZonedDateTime dateTime = ZonedDateTime.now();
        FormLabel dateLabel = new FormLabel(dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd YYYY", Locale.getDefault())));
        dateLabel.setVisible(true);
        dateLabel.setFont(FontFactory.toolbarButtonFont());
        dateLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        dateTimePanel.add(new JLabel(new ImageIcon("com.cn55.marvel/src/img/watch_later_black_48.png")));
        dateTimePanel.add(new JSeparator(JSeparator.HORIZONTAL));
        dateTimePanel.add(dateLabel);
        dateTimePanel.add(new JSeparator(JSeparator.HORIZONTAL));
        dateTimePanel.add(new Clock(dateTime));

        return dateTimePanel;
    }

    class Clock extends JLabel {
        Clock(ZonedDateTime dateTime) {
            setFont(FontFactory.toolbarButtonFont());
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh : mm : ss a  z O", Locale.getDefault());
            setText(formatter.format(dateTime));
            Timer timer = new Timer(1000, e -> setText(formatter.format(ZonedDateTime.now())));
            timer.start();
        }
    }

    public void setListener(LoginListener listener) { this.listener = listener; }

    public void setDefaults() {
        usernameTextField.setText(null);
        passwordTextField.setText(null);
        newUserOption.setSelected(false);
    }

    public JCheckBox getNewUserOption() { return newUserOption; }

    public FormButton getLogoutBtn() { return logoutBtn; }
}
