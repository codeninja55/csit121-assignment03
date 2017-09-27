package application.view.builderFactory;

import application.view.customComponents.*;
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
    private MaterialDatePicker dateBeginPicker;
    private MaterialDatePicker dateEndPicker;
    private MaterialSlider daySlider;
    private MaterialSlider hourSlider;
    private SummaryListener listener;

    SummaryFilterForm() {
        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 900;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setVisible(false);
        setBorder(CustomBorderFactory.formBorder("Filter Form"));

        JPanel filterForm = new JPanel(new GridBagLayout());
        CancelButton cancelBtn = new CancelButton("Cancel");
        FormLabel dateFromLabel = new FormLabel("Date From:");
        dateBeginPicker = new MaterialDatePicker();
        FormLabel dateToLabel = new FormLabel("Date To:");
        dateEndPicker = new MaterialDatePicker();
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
        filterForm.add(dateFromLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1; gc.weightx = 0.6;
        gc.insets = new Insets(20, 0,0,0);
        filterForm.add(dateBeginPicker, gc);

        /*========== NEW ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.gridx = 0; gc.gridy++;
        gc.insets = new Insets(15, 0,0,10);
        filterForm.add(dateToLabel, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.insets = new Insets(10, 0,0,0);
        filterForm.add(dateEndPicker, gc);

        /*========== NEW ROW - DAY SLIDER ==========*/
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 2;
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
        filterForm.add(hourSliderLabel, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy++;
        gc.insets = new Insets(10, 0,0,0);
        filterForm.add(hourSlider, gc);

        /*========== BUTTON ROW ==========*/
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.gridy++; gc.gridx = 0; gc.weightx = 0.5; gc.weighty = 3;
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

        cancelBtn.addActionListener(e -> super.setVisible(false));

        filterBtn.addActionListener(e -> {
            if (listener != null) listener.refreshActionPerformed(SummaryFilterForm.this);
        });

        clearBtn.addActionListener(e -> {
            hourSlider.setValue(24);
            daySlider.setValue(0);
            // TODO Set the date pickers to their defaults
        });

        // TODO - Need to set the first and last date

    }

    /*============================== MUTATORS  ==============================*/
    public void setListener(SummaryListener listener) { this.listener = listener; }

    /*============================== SUMMARY VIEW METHODS ==============================*/
    public int getDaysOption() { return daySlider.getValue(); }
    public int getHoursOption() { return hourSlider.getValue(); }
    public LocalDate getDateFromOption() { return dateBeginPicker.getDate(); }
    public LocalDate getDateToOption() { return dateEndPicker.getDate(); }
}
