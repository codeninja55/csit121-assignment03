package application.view.formbuilder.factory;

import application.view.custom.components.*;
import application.view.summary.SummaryAnalyticsPane;
import application.view.summary.SummaryViewPane;
import styles.ColorFactory;
import styles.CustomBorderFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.IntStream;

public class SummaryFilterForm extends JPanel implements FormFactory, SummaryView {
    private final MaterialDatePicker dateStartPicker;
    private final MaterialDatePicker dateEndPicker;
    private LocalDate firstPurchaseDate;
    private LocalDate lastPurchaseDate;
    private final MaterialSlider daySlider;
    private final MaterialSlider hourSlider;
    private SummaryListener listener;

    SummaryFilterForm(SummaryViewPane parent, SummaryAnalyticsPane analyticsPane) {
        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 900;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(false);
        setBorder(CustomBorderFactory.formBorder("Filter Form"));

        JPanel filterForm = new JPanel(new GridBagLayout());
        CancelButton cancelBtn = new CancelButton("Cancel");
        FormLabel dateStartLabel = new FormLabel("Date Start:");
        dateStartPicker = new MaterialDatePicker();
        FormLabel dateEndLabel = new FormLabel("Date End:");
        dateEndPicker = new MaterialDatePicker();
        ErrorLabel dateErrLabel = new ErrorLabel("Invalid Date Range. Date end before date start.");
        FormLabel daySliderLabel = new FormLabel("Filter by Day");
        daySlider = new MaterialSlider(JSlider.HORIZONTAL,0,7,0);
        FormLabel hourSliderLabel = new FormLabel("Filter by Hour");
        hourSlider = new MaterialSlider(JSlider.HORIZONTAL,0,24,24);
        FormButton filterBtn = new FormButton("Filter", IconFactory.filterIcon());
        FormButton clearBtn = new FormButton("Clear", IconFactory.clearIcon());

        /* Setup Day and Hour Sliders */
        Hashtable<Integer, JComponent> daysSliderValues = new Hashtable<>();
        daysSliderValues.put(0, new FormLabel("Any", ColorFactory.grey50(), FontFactory.sliderFont()));
        Arrays.stream(DayOfWeek.values()).forEach(d ->
                daysSliderValues.put(d.getValue(), new FormLabel(d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), ColorFactory.grey50(), FontFactory.sliderFont()))
        );
        daySlider.setPreferredSize(new Dimension(500, 100));
        daySlider.setMinimumSize(daySlider.getPreferredSize());
        daySlider.setLabelTable(daysSliderValues);
        daySlider.setValue(0);

        Hashtable<Integer, JComponent> hoursSliderValues = new Hashtable<>();
        hoursSliderValues.put(24, new FormLabel("Any", ColorFactory.grey50(), FontFactory.sliderFont()));
        IntStream.range(0,24).forEachOrdered(i -> hoursSliderValues.put(i, new FormLabel(Integer.toString(i), ColorFactory.grey50(), FontFactory.sliderFont())));
        hourSlider.setLabelTable(hoursSliderValues);

        /* FORM AREA */
        GridBagConstraints gc = new GridBagConstraints();
        Dimension size = dateEndPicker.getPreferredSize();
        size.width = 500;

        /*========== FIRST ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.4; gc.weighty = 0.1;
        gc.insets = new Insets(25, 0,0,10);
        filterForm.add(dateStartLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1; gc.weightx = 0.6;
        gc.insets = new Insets(20, 0,0,0);
        filterForm.add(dateStartPicker, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(15, 0,0,10);
        filterForm.add(dateEndLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        filterForm.add(dateEndPicker, gc);

        /*========== NEW ROW - DATE ERR LABEL ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 2;
        gc.gridx = 0; gc.gridy++; gc.weightx = 3; gc.weighty = 0.1;
        gc.insets = new Insets(15, 0,15,0);
        dateErrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        filterForm.add(dateErrLabel, gc);

        /*========== NEW ROW - DAY SLIDER ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 0; gc.gridy++; gc.weightx = 3; gc.weighty = 0.1;
        gc.insets = new Insets(20, 0,0,0);
        daySliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        filterForm.add(daySliderLabel, gc);

        gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        filterForm.add(daySlider, gc);

        /*========== NEW ROW - HOUR SLIDER==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy++;
        gc.insets = new Insets(20, 0,0,0);
        hourSliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hourSlider.setInverted(true);
        filterForm.add(hourSliderLabel, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        filterForm.add(hourSlider, gc);

        /*========== BUTTON ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy++; gc.weightx = 0.5; gc.weighty = 3;
        gc.insets = new Insets(20,0,0,5);
        filterForm.add(filterBtn, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1; gc.weightx = 0.5;
        gc.insets = new Insets(20,5,0,0);
        filterForm.add(clearBtn, gc);

        add(filterForm, BorderLayout.CENTER);
        add(cancelBtn, BorderLayout.SOUTH);

        // Set form custom components visible
        Arrays.stream(filterForm.getComponents()).filter(c -> c instanceof FormLabel || c instanceof FormButton)
                .forEach(c -> c.setVisible(true));

        cancelBtn.addActionListener(e -> {
            super.setVisible(false);
            dateErrLabel.setVisible(false);
            parent.update();
            analyticsPane.update();
        });

        filterBtn.addActionListener(e -> {
            if (getDateToOption().isBefore(getDateFromOption())) {
                dateErrLabel.setVisible(true);
                parent.update();
                analyticsPane.update();
            } else {
                dateErrLabel.setVisible(false);
                if (listener != null) listener.filterActionPerformed(SummaryFilterForm.this);
            }

        });

        clearBtn.addActionListener(e -> {
            hourSlider.setValue(24);
            daySlider.setValue(0);
            dateStartPicker.setDate(firstPurchaseDate);
            dateEndPicker.setDate(lastPurchaseDate);
            dateErrLabel.setVisible(false);
            parent.update();
            analyticsPane.update();
        });
    }

    /*============================== MUTATORS  ==============================*/
    public void setListener(SummaryListener listener) { this.listener = listener; }

    public void setPurchaseDateBounds(LocalDate firstPurchaseDate, LocalDate lastPurchaseDate) {
        this.firstPurchaseDate = firstPurchaseDate;
        dateStartPicker.setDate(firstPurchaseDate);
        this.lastPurchaseDate = lastPurchaseDate;
        dateEndPicker.setDate(lastPurchaseDate);
    }

    /*============================== SUMMARY VIEW METHODS ==============================*/
    public int getDaysOption() { return daySlider.getValue(); }
    public int getHoursOption() {
        return hourSlider.getValue();
    }
    public LocalDate getDateFromOption() { return dateStartPicker.getDate(); }
    public LocalDate getDateToOption() { return dateEndPicker.getDate(); }
}
