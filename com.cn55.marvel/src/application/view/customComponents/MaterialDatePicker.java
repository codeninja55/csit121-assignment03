package application.view.customComponents;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import styles.ColorFactory;
import styles.FontFactory;
import styles.IconFactory;

import javax.swing.*;
import java.time.DayOfWeek;

/*
* Title: LGoodDatePicker
* Author: LGoodDatePicker
* Version: 10.3.1
* Availability: https://github.com/codeninja55/LGoodDatePicker
* Demo: https://github.com/LGoodDatePicker/LGoodDatePicker/blob/master/Project/src/main/java/com/github/lgooddatepicker/demo/FullDemo.java
* */

public class MaterialDatePicker extends DatePicker {
    public MaterialDatePicker() {
        super();
        setMinimumSize(getPreferredSize());

        // Custom icon for toggle calendar button
        JButton pickerBtn = getComponentToggleCalendarButton();
        pickerBtn.setText("");
        pickerBtn.setIcon(IconFactory.datePickerIcon());
        pickerBtn.setBackground(ColorFactory.redA700());

        // Custom changes to text field. Set not editable
        JTextField pickerTextField = getComponentDateTextField();
        pickerTextField.setEditable(false);
        pickerTextField.setBackground(ColorFactory.red100());

        // DatePickerSettings allows all changes below
        DatePickerSettings settings = new DatePickerSettings();
        int newHeight = (int) (settings.getSizeDatePanelMinimumHeight() * 2.5);
        int newWidth = (int) (settings.getSizeDatePanelMinimumWidth() * 2.8);
        settings.setSizeDatePanelMinimumHeight(newHeight);
        settings.setSizeDatePanelMinimumWidth(newWidth);
        settings.setFontMonthAndYearMenuLabels(FontFactory.sliderFont());
        settings.setFontTodayLabel(FontFactory.sliderFont());
        settings.setFontClearLabel(FontFactory.sliderFont());
        settings.setFontCalendarDateLabels(FontFactory.sliderFont());
        settings.setFontCalendarWeekdayLabels(FontFactory.sliderFont());
        settings.setFontCalendarWeekNumberLabels(FontFactory.sliderFont());

        settings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, ColorFactory.grey50());
        settings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, ColorFactory.redA700());
        settings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, ColorFactory.redA700());
        settings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, ColorFactory.grey50());
        settings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, ColorFactory.grey50());
        settings.setColorBackgroundWeekdayLabels(ColorFactory.blueGrey500(),false);
        settings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate, ColorFactory.red500());
        settings.setColor(DatePickerSettings.DateArea.TextTodayLabel, ColorFactory.redA700());
        settings.setColor(DatePickerSettings.DateArea.TextClearLabel, ColorFactory.redA700());

        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForDatesBeforeCommonEra("uuuu-MM-dd");
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        settings.setAllowKeyboardEditing(false);

        setSettings(settings);
    }
}
