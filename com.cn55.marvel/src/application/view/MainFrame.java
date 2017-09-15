package application.view;

import application.view.CardView.CardViewPane;
import application.view.CategoriesView.CategoriesViewPane;
import application.view.CustomComponents.Style;
import application.view.PurchaseView.PurchaseViewPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

@SuppressWarnings("Convert2Lambda")
public class MainFrame extends JFrame {

    private final JTabbedPane tabPane;
    private final CardViewPane cardViewPane;
    private final PurchaseViewPane purchaseViewPane;
    private final CategoriesViewPane categoriesViewPane;
    //private SummaryPanel summaryPanel;

    public MainFrame() {
        super("Marvel Rewards Cards");

        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Change the default Java icon to this
        URL iconURL = getClass().getResource("/img/favicon.png");
        ImageIcon mainIcon = new ImageIcon(iconURL);
        setIconImage(mainIcon.getImage());

        this.tabPane = new JTabbedPane();

        // Initialize panels for tabs
        JPanel welcomePane = new JPanel();
        welcomePane.setLayout(new GridBagLayout());
        URL marvelURL = getClass().getResource("/img/Marvel-Logo-3-25pc.png");
        ImageIcon marvelImage = new ImageIcon(marvelURL);
        JLabel welcomeLabel = new JLabel("WELCOME TO");
        JLabel imageLabel = new JLabel("", marvelImage, SwingConstants.CENTER);
        JLabel rewardsCardLabel = new JLabel("REWARDS CARDS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Product Sans", Font.BOLD,80));
        welcomeLabel.setForeground(Style.red500());
        rewardsCardLabel.setFont(new Font("Product Sans", Font.BOLD,72));
        rewardsCardLabel.setForeground(Style.red500());

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

        this.cardViewPane = new CardViewPane();
        this.purchaseViewPane = new PurchaseViewPane();
        this.categoriesViewPane = new CategoriesViewPane();

        // Add panels, toolbars, and panes to main Frame
        tabPane.setBackground(Style.blueGrey500());
        tabPane.setForeground(Style.btnTextColor());
        tabPane.setFont(Style.tabPaneFont());

        add(tabPane, BorderLayout.CENTER);

        // Add tabs to tabPane group
        tabPane.addTab("Start", welcomePane);
        tabPane.addTab("Cards", cardViewPane);
        tabPane.addTab("Purchases", purchaseViewPane);
        tabPane.addTab("Categories", categoriesViewPane);

        // DEFAULT PANE BEGIN AT
        tabPane.setSelectedIndex(0);
    }

    /*============================== ACCESSORS  ==============================*/
    public JTabbedPane getTabPane() { return tabPane; }

    public CardViewPane getCardViewPane() { return cardViewPane; }

    public PurchaseViewPane getPurchaseViewPane() { return purchaseViewPane; }

    public CategoriesViewPane getCategoriesViewPane() {
        return categoriesViewPane;
    }
}
