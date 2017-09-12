package cn55.view.CustomComponents;

import javax.swing.*;
import java.awt.*;

/* NOTE:
*  To make this appear and hide dynamically, run these steps in Controller on Event Occurred
*  1. Create the JPanel first and set it to visible
*  2. Create the ResultsTextPane by invoking setResultsTextPane()
*  3. Populate the ResultsTextPane with all the data
*  4. Create the JScrollPane by invoking setScrollPane() with the ResultsTextPane component added
*  5. Add the JScrollPane to the ResultsPane
*  6. Invoke grabFocus() and setCaretPosition(0) on the ResultsTextPane
*
*  DO NOT FORGET TO REMOVE BOTH JScrollPane and ResultsTextPane after finishing */

public class ResultsPane extends JPanel {

    private ResultsTextPane resultsTextPane;
    private JScrollPane scrollPane;

    public ResultsPane(String paneName) {
        setLayout(new BorderLayout());

        Dimension resultsDim = getPreferredSize();
        resultsDim.width = 800;
        setPreferredSize(resultsDim);
        setMinimumSize(getPreferredSize());

        setName(paneName);
        setVisible(false);
    }

    /*============================== MUTATORS ==============================*/

    public void setResultsTextPane() {
        this.resultsTextPane = new ResultsTextPane();
    }

    public void setScrollPane(ResultsTextPane resultsTextPane) {
        this.scrollPane = new JScrollPane(resultsTextPane);
    }

    /*============================== ACCESSORS  ==============================*/
    public ResultsTextPane getResultsTextPane() {
        return resultsTextPane;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /*============================== INNER CLASS ==============================*/
    public class ResultsTextPane extends JTextPane {

        ResultsTextPane() {

            Dimension textPaneDim = getPreferredSize();
            textPaneDim.width = 800;
            setPreferredSize(textPaneDim);
            setMinimumSize(getPreferredSize());

            setBorder(Style.resultsPaneBorder());
            setFont(Style.textPaneFont());
            setBackground(Style.blueGrey400());
            setForeground(Style.grey50());
            setText(null);

            setVisible(true);
            ResultsPane.this.revalidate();
            ResultsPane.this.repaint();
        }

    }
}
