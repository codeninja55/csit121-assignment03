package application.view;

import application.controller.Utils;
import application.view.custom.components.LoginDialog;
import application.view.summary.SummaryViewPane;
import styles.ColorFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

public class MainFrame extends JFrame {
    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;
    private final SummaryViewPane summaryViewPane;
    private ActionListener saveListener;
    private ActionListener exitListener;
    private LoginDialog loginDialog;
    private JFileChooser fileChooser;

    public MainFrame() {
        super("Marvel Rewards Cards");
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1080, 720));
        setUndecorated(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setJMenuBar(createMenu());

        // Change the default Java icon to this
        ImageIcon mainIcon = new ImageIcon("com.cn55.marvel/src/img/favicon.png");
        setIconImage(mainIcon.getImage());

        this.tabPane = new JTabbedPane();
        this.cardViewPane = new CardViewPane();
        this.purchaseViewPane = new PurchaseViewPane();
        this.categoriesViewPane = new CategoriesViewPane();
        this.summaryViewPane = new SummaryViewPane();

        // Add panels, toolbars, and panes to main Frame
        tabPane.setBackground(ColorFactory.blueGrey500());
        tabPane.setForeground(ColorFactory.btnTextColor());
        tabPane.setFont(FontFactory.tabPaneFont());

        add(tabPane, BorderLayout.CENTER);

        // Add tabs to tabPane group
        tabPane.addTab(" Start ", IconFactory.homeIcon(), createWelcomePanel());
        tabPane.addTab(" Cards ", IconFactory.cardIcon(),cardViewPane);
        tabPane.addTab(" Purchases ", IconFactory.purchaseIcon(), purchaseViewPane);
        tabPane.addTab(" Categories ", IconFactory.categoryIcon(), categoriesViewPane);
        tabPane.addTab(" Summary ", IconFactory.summaryViewPaneIcon(), summaryViewPane);

        // DEFAULT PANE BEGIN AT
        tabPane.setSelectedIndex(0);
        tabPane.setDisabledIconAt(4, IconFactory.summaryViewPaneIconDisbaled());
        tabPane.setEnabledAt(4, false);
    }

    /*============================== MUTATORS ==============================*/
    private JMenuBar createMenu() {
        fileChooser = new JFileChooser();
        loginDialog = new LoginDialog(this);
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu adminSubMenu = new JMenu("Administrator");
        JMenu dataMenu = new JMenu("Data");
        JMenuItem login = new JMenuItem("Login..", IconFactory.loginRed500Icon());
        JMenuItem logout = new JMenuItem("Logout..", IconFactory.logoutIcon());
        JMenuItem exit = new JMenuItem("Exit", IconFactory.exitIcon());
        JMenuItem save = new JMenuItem("Save Data", IconFactory.saveIcon());
        JMenuItem importData = new JMenuItem("Import Data..", IconFactory.importIcon());
        JMenuItem exportData = new JMenuItem("Export Data..", IconFactory.exportIcon());

        fileMenu.setFont(FontFactory.toolbarButtonFont());
        dataMenu.setFont(FontFactory.toolbarButtonFont());
        adminSubMenu.setFont(FontFactory.labelFont());
        login.setFont(FontFactory.labelFont());
        logout.setFont(FontFactory.labelFont());
        exit.setFont(FontFactory.labelFont());
        save.setFont(FontFactory.labelFont());
        importData.setFont(FontFactory.labelFont());
        exportData.setFont(FontFactory.labelFont());

        fileMenu.setForeground(ColorFactory.redA700());
        dataMenu.setForeground(ColorFactory.redA700());
        adminSubMenu.setForeground(ColorFactory.redA700());
        login.setForeground(ColorFactory.redA700());
        logout.setForeground(ColorFactory.redA700());
        exit.setForeground(ColorFactory.redA700());
        save.setForeground(ColorFactory.redA700());
        importData.setForeground(ColorFactory.redA700());
        exportData.setForeground(ColorFactory.redA700());

        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        login.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));

        adminSubMenu.add(login);
        adminSubMenu.add(logout);
        fileMenu.add(adminSubMenu);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        dataMenu.add(save);
        dataMenu.addSeparator();
        dataMenu.add(importData);
        dataMenu.add(exportData);
        menu.add(fileMenu);
        menu.add(dataMenu);

        fileChooser.addChoosableFileFilter(new CSVFileFilter());

        login.addActionListener(e -> loginDialog.setVisible(true));

        logout.addActionListener(e -> {
            setSummaryViewPaneEnabled(false);
            tabPane.setSelectedIndex(0);
        });

        save.addActionListener(e -> { if (saveListener != null) saveListener.actionPerformed(e); });

        exit.addActionListener(e -> { if (exitListener != null) exitListener.actionPerformed(e); });

        // TODO - Add the handler for importing and exporting files
        importData.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                System.out.println(fileChooser.getSelectedFile());
        });

        exportData.addActionListener(e -> {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
                System.out.println(fileChooser.getSelectedFile());
        });

        return menu;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePane = new JPanel();
        welcomePane.setLayout(new GridBagLayout());
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

    public void setSaveListener(ActionListener saveListener) { this.saveListener = saveListener; }

    public void setExitListener(ActionListener exitListener) { this.exitListener = exitListener; }

    public void setSummaryViewPaneEnabled(boolean isEnabled) { tabPane.setEnabledAt(4,isEnabled); }

    /*============================== ACCESSORS  ==============================*/
    public JTabbedPane getTabPane() { return tabPane; }

    public CardViewPane getCardViewPane() { return cardViewPane; }

    public PurchaseViewPane getPurchaseViewPane() { return purchaseViewPane; }

    public CategoriesViewPane getCategoriesViewPane() {
        return categoriesViewPane;
    }

    public SummaryViewPane getSummaryViewPane() { return summaryViewPane; }

    public LoginDialog getLoginDialog() { return loginDialog; }

    class CSVFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String name = f.getName();
            String extension = Utils.getFileExtensions(name);
            return extension != null && extension.equals("csv");
        }

        public String getDescription() {
            return "Comma Separated File (*.csv)";
        }
    }
}
