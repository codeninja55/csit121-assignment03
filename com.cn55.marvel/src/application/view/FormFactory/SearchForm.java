package application.view.FormFactory;

import application.view.CustomComponents.ButtonListener;
import application.view.CustomComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchForm extends JPanel implements FormFactory {

    private JPanel searchForm;
    private FormLabel searchLabel;
    private FormTextField searchIDTextField;
    private ErrorLabel errorLabel;
    private ErrorLabel ruleErrLabel;
    private FormButton searchBtn;
    private SearchListener searchListener;
    private ButtonListener cancelListener;

    SearchForm() {
        /* NOTE: searchForm is the form Container within*/
        searchForm = new JPanel(new GridBagLayout());
        CancelButton cancelBtn = new CancelButton("Cancel Search");
        searchLabel = new FormLabel("Search by Card ID");
        searchLabel.setVisible(true);
        searchIDTextField = new FormTextField(20);
        searchIDTextField.setVisible(true);
        errorLabel = new ErrorLabel("CARD DOES NOT EXIST");
        ruleErrLabel = new ErrorLabel("INVALID CARD ID NUMBER");
        searchBtn = new FormButton("Search", Style.searchIcon());
        searchBtn.setVisible(true);

        setName("SearchForm");
        setLayout(new BorderLayout());
        Dimension dim = getPreferredSize();
        dim.width = 800;
        setPreferredSize(dim);
        setMinimumSize(getPreferredSize());
        setBorder(Style.formBorder("Search Cards"));

        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 0.2;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(20,0,10,0);
        searchForm.add(searchLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(20,0,10,0);
        Dimension textFieldDim = getPreferredSize();
        textFieldDim.width = 350;
        textFieldDim.height = 50;
        searchIDTextField.setPreferredSize(textFieldDim);
        searchForm.add(searchIDTextField, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        searchForm.add(errorLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0,0,0,0);
        searchForm.add(ruleErrLabel, gc);

        gc.gridy++; gc.gridx = 0; gc.weightx = 1; gc.weighty = 3;
        gc.gridwidth = 1; gc.gridheight = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10,0,10,0);
        searchForm.add(searchBtn, gc);

        /* Register listener, handle the event, and raise an Event object
        * to pass the source to that SearchEvent object that then passes
        *  that event to Program controller to handle */
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchEvent event = new SearchEvent(this, searchLabel, searchIDTextField,
                        errorLabel, ruleErrLabel);

                if (searchListener != null)
                    searchListener.searchEventOccurred(event);
            }
        });

        add(searchForm, BorderLayout.CENTER);
        add(cancelBtn, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> {
            if (cancelListener != null) {
                cancelListener.buttonActionOccurred();
            }
        });

        setVisible(false);
    }

    /*============================== MUTATORS ==============================*/
    public void setSearchListener(SearchListener listener) { this.searchListener = listener; }

    public void setCancelListener(ButtonListener cancelListener) {
        this.cancelListener = cancelListener;
    }

}
