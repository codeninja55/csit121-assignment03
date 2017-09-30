package application.view;

import application.controller.Utils;
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

public class MainFrame extends JFrame {
    private final JTabbedPane tabPane;
    private final StartViewPane startViewPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;
    private final SummaryViewPane summaryViewPane;
    private ActionListener saveListener;
    private ActionListener exitListener;
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
        this.startViewPane = new StartViewPane(tabPane);
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
        tabPane.addTab(" Start ", IconFactory.homeIcon(), startViewPane);
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
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu dataMenu = new JMenu("Data");
        JMenuItem login = new JMenuItem("Login..", IconFactory.loginRed500Icon());
        JMenuItem logout = new JMenuItem("Logout..", IconFactory.logoutIcon());
        JMenuItem exit = new JMenuItem("Exit", IconFactory.exitIcon());
        JMenuItem save = new JMenuItem("Save Data", IconFactory.saveIcon());
        JMenuItem importData = new JMenuItem("Import Data..", IconFactory.importIcon());
        JMenuItem exportData = new JMenuItem("Export Data..", IconFactory.exportIcon());

        fileMenu.setFont(FontFactory.toolbarButtonFont());
        dataMenu.setFont(FontFactory.toolbarButtonFont());
        login.setFont(FontFactory.labelFont());
        logout.setFont(FontFactory.labelFont());
        exit.setFont(FontFactory.labelFont());
        save.setFont(FontFactory.labelFont());
        importData.setFont(FontFactory.labelFont());
        exportData.setFont(FontFactory.labelFont());

        fileMenu.setForeground(ColorFactory.redA700());
        dataMenu.setForeground(ColorFactory.redA700());
        login.setForeground(ColorFactory.redA700());
        logout.setForeground(ColorFactory.redA700());
        exit.setForeground(ColorFactory.redA700());
        save.setForeground(ColorFactory.redA700());
        importData.setForeground(ColorFactory.redA700());
        exportData.setForeground(ColorFactory.redA700());

        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        login.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));

        fileMenu.add(login);
        fileMenu.add(logout);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        dataMenu.add(save);
        dataMenu.addSeparator();
        dataMenu.add(importData);
        dataMenu.add(exportData);
        menu.add(fileMenu);
        menu.add(dataMenu);

        fileChooser.addChoosableFileFilter(new CSVFileFilter());

        login.addActionListener(e -> tabPane.setSelectedIndex(0));

        logout.addActionListener(e -> {
            setSummaryViewPaneEnabled(false);
            startViewPane.getLogoutBtn().setEnabled(false);
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

    public void setSaveListener(ActionListener saveListener) { this.saveListener = saveListener; }

    public void setExitListener(ActionListener exitListener) { this.exitListener = exitListener; }

    public void setSummaryViewPaneEnabled(boolean isEnabled) { tabPane.setEnabledAt(4,isEnabled); }

    /*============================== ACCESSORS  ==============================*/
    public JTabbedPane getTabPane() { return tabPane; }

    public StartViewPane getStartViewPane() { return startViewPane; }

    public CardViewPane getCardViewPane() { return cardViewPane; }

    public PurchaseViewPane getPurchaseViewPane() { return purchaseViewPane; }

    public CategoriesViewPane getCategoriesViewPane() {
        return categoriesViewPane;
    }

    public SummaryViewPane getSummaryViewPane() { return summaryViewPane; }

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
