package application.view.customComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/* BUILDER DESIGN PATTERN - Build a text pane with the appropriate sequential order */

public class ResultsPane extends JPanel {
    private ResultsTextPane resultsTextPane; // required
    private JScrollPane scrollPane; // required but only initialized after this pane is shown

    private ResultsPane(ResultsPaneBuilder builder) {
        this.resultsTextPane = builder.resultsTextPane;
        setLayout(new BorderLayout());
        Dimension resultsDim = getPreferredSize();
        resultsDim.width = 800;
        setPreferredSize(resultsDim);
        setMinimumSize(getPreferredSize());
        setVisible(false);

        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                scrollPane = new JScrollPane(resultsTextPane);
                ResultsPane.super.add(scrollPane);

                getParent().addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        setVisible(false);
                    }
                });

                JScrollPane table = (JScrollPane) Arrays.stream(getParent().getComponents())
                        .filter(comp -> comp instanceof JScrollPane)
                        .findAny().orElse(null);

                if (table != null) {
                    table.getViewport().getView().addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            setVisible(false);
                        }
                    });
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                getParent().remove(ResultsPane.this);
            }
        });
    }

    /*============================== BUILDER CLASS ==============================*/
    public static class ResultsPaneBuilder {
        private ResultsTextPane resultsTextPane;

        public ResultsPaneBuilder(String resultsText) {
            this.resultsTextPane = new ResultsTextPane(resultsText);
        }

        public ResultsPane build() {
            return new ResultsPane(this);
        }
    }

    /*============================== INNER CLASS ==============================*/
    private static class ResultsTextPane extends JTextPane {
        ResultsTextPane(String resultsText) {
            Dimension textPaneDim = getPreferredSize();
            textPaneDim.width = 800;
            setPreferredSize(textPaneDim);
            setMinimumSize(getPreferredSize());
            setBorder(Style.resultsPaneBorder());
            setFont(Style.textPaneFont());
            setBackground(Style.blueGrey400());
            setForeground(Style.grey50());
            setVisible(true);
            setText(resultsText);
        }

    }
}
