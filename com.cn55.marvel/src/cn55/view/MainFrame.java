package cn55.view;

import cn55.view.CardView.CardViewPane;
import cn55.view.CategoriesView.CategoriesViewPane;
import cn55.view.CustomComponents.Style;
import cn55.view.PurchaseView.PurchaseViewPane;

import javax.swing.*;
import java.awt.*;
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        // Change the default Java icon to this
        URL iconURL = getClass().getResource("/cn55/img/favicon.png");
        ImageIcon mainIcon = new ImageIcon(iconURL);
        setIconImage(mainIcon.getImage());

        this.tabPane = new JTabbedPane();

        // Initialize panels for tabs
        JPanel welcomePane = new JPanel();
        welcomePane.setLayout(new GridBagLayout());
        URL marvelURL = getClass().getResource("/cn55/img/Marvel-Logo-3-25pc.png");
        ImageIcon marvelImage = new ImageIcon(marvelURL);
        JLabel welcomeLabel = new JLabel("WELCOME");
        JLabel imageLabel = new JLabel("", marvelImage, SwingConstants.CENTER);
        JLabel rewardsCardLabel = new JLabel("REWARDS CARDS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Verdana", Font.BOLD,80));
        welcomeLabel.setForeground(Style.red500());
        rewardsCardLabel.setFont(new Font("Verdana", Font.BOLD,72));
        rewardsCardLabel.setForeground(Style.red500());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 0.5;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(200,0,80,0);

        welcomePane.add(welcomeLabel, gc);

        gc.gridy++; gc.insets = new Insets(0,0,0,0);
        welcomePane.add(imageLabel, gc);

        gc.gridy++;  gc.weighty = 2;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,0,0);
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
