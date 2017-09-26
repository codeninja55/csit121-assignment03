package application.view;

import application.model.DataObservable;
import application.model.DataObserver;
import application.view.builderFactory.SummaryListener;
import application.view.builderFactory.SummaryView;
import application.view.customComponents.*;
import application.view.jtableModels.CardTableModel;
import application.view.jtableModels.CategoriesTableModel;
import application.view.jtableModels.PurchaseTableModel;
import styles.ColorFactory;
import styles.FontFactory;
import styles.IconFactory;
import styles.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.IntStream;

public class SummaryViewPane extends JPanel implements DataObserver, SummaryView {
    private DataObservable dataDAO;

    private JComboBox<String> tableViewCombo;
    private CategoriesTableModel categoriesTableModel;
    private JTable categoriesTable;
    private PurchaseTableModel purchasesTableModel;
    private JTable purchasesTable;
    private CardTableModel cardsTableModel;
    private JTable cardsTable;
    private MaterialSlider daysSlider;
    private MaterialSlider hoursSlider;
    private SummaryListener refreshListener;

    SummaryViewPane() {
        setLayout(new BorderLayout());
        Toolbar toolbar = new Toolbar();
        ToolbarButton refreshTableBtn = new ToolbarButton("Refresh", IconFactory.refreshIcon());

        categoriesTableModel = new CategoriesTableModel();
        categoriesTable = new JTable(categoriesTableModel);
        Style.categoriesTableFormatter(categoriesTable);
        purchasesTableModel = new PurchaseTableModel();
        purchasesTable = new JTable(purchasesTableModel);
        Style.purchasesTableFormatter(purchasesTable);
        cardsTableModel = new CardTableModel();
        cardsTable = new JTable(cardsTableModel);
        Style.cardTableFormatter(cardsTable);

        /* TABLE VIEW COMBO BOX */
        String[] tableOptions = {"Categories", "Purchases", "Cards"};
        tableViewCombo = new JComboBox<>(tableOptions);
        tableViewCombo.setPreferredSize(refreshTableBtn.getPreferredSize());
        tableViewCombo.setBorder(BorderFactory.createMatteBorder(2,2,2,2, ColorFactory.blueGrey500()));
        tableViewCombo.setSelectedIndex(0);

        /* TOOLBAR */
        // Date Picker FROM
        // Date Picker TO
        // Days Slider
        daysSlider = new MaterialSlider(JSlider.HORIZONTAL, 0, 7, 0);

        Hashtable<Integer, JComponent> daysSliderValues = new Hashtable<>();
        daysSliderValues.put(0, new FormLabel("Any", ColorFactory.grey50(), FontFactory.sliderFont()));
        Arrays.stream(DayOfWeek.values()).forEach(d ->
            daysSliderValues.put(d.getValue(), new FormLabel(d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), ColorFactory.grey50(), FontFactory.sliderFont()))
        );

        daysSlider.setPreferredSize(new Dimension(550, 100));
        daysSlider.setMinimumSize(daysSlider.getPreferredSize());
        daysSlider.setLabelTable(daysSliderValues);

        // Time of day slider
        hoursSlider = new MaterialSlider(JSlider.HORIZONTAL, 0, 24, 24);
        Hashtable<Integer, JComponent> hoursSliderValues = new Hashtable<>();
        hoursSliderValues.put(24, new FormLabel("Any", ColorFactory.grey50(), FontFactory.sliderFont()));
        IntStream.range(0,24).forEachOrdered(i -> hoursSliderValues.put(i, new FormLabel(Integer.toString(i), ColorFactory.grey50(), FontFactory.sliderFont())));
        hoursSlider.setLabelTable(hoursSliderValues);

        toolbar.getLeftToolbar().add(daysSlider);
        toolbar.getLeftToolbar().add(hoursSlider);

        toolbar.getRightToolbar().add(refreshTableBtn);
        toolbar.getRightToolbar().add(tableViewCombo);
        add(toolbar, BorderLayout.NORTH);

        add(new JScrollPane(categoriesTable), BorderLayout.CENTER);

        /*daysSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!daysSlider.getValueIsAdjusting()) {

            }
        });*/

        tableViewCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Arrays.stream(super.getComponents()).filter(c -> c instanceof JScrollPane).forEach(this::remove);
                if (e.getItem().equals("Categories")) {
                    super.add(new JScrollPane(categoriesTable), BorderLayout.CENTER);
                } else if (e.getItem().equals("Purchases")) {
                    super.add(new JScrollPane(purchasesTable), BorderLayout.CENTER);
                } else if (e.getItem().equals("Cards")) {
                    super.add(new JScrollPane(cardsTable), BorderLayout.CENTER);
                }
                super.revalidate();
                super.repaint();
            }
        });

        refreshTableBtn.addActionListener(e -> {
            if (refreshListener != null) refreshListener.refreshActionPerformed(SummaryViewPane.this);
        });
    }

    /*============================== MUTATORS ==============================*/
    public void setRefreshListener(SummaryListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void update() {
        categoriesTableModel.setData(new ArrayList<>(dataDAO.getCategoriesUpdate(this).values()));
        categoriesTableModel.fireTableDataChanged();
        purchasesTableModel.setData(new ArrayList<>(dataDAO.getPurchaseUpdate(this).values()));
        purchasesTableModel.fireTableDataChanged();
        cardsTableModel.setData(new ArrayList<>(dataDAO.getCardsUpdate(this).values()));
        cardsTableModel.fireTableDataChanged();
    }

    public void setSubject(DataObservable dataObservable) { this.dataDAO = dataObservable; }

    /*============================== ACCESSORS ==============================*/
    public CategoriesTableModel getCategoryTableModel() {
        return categoriesTableModel;
    }

    public int getDaysOption() {
        return daysSlider.getValue();
    }

    public int getHoursOption() { return hoursSlider.getValue(); }

    public String getTableOption() {
        return (String)tableViewCombo.getSelectedItem();
    }
}
