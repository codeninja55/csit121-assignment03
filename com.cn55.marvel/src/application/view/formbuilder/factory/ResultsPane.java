package application.view.formbuilder.factory;

import styles.ColorFactory;
import styles.CustomBorderFactory;
import styles.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/* BUILDER DESIGN PATTERN - Build a text pane with the appropriate sequential order */

public class ResultsPane extends JPanel {
    private final ResultsTextPane resultsTextPane; // required

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
                JScrollPane scrollPane = new JScrollPane(resultsTextPane);
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
        private final ResultsTextPane resultsTextPane;

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
            setBorder(CustomBorderFactory.resultsPaneBorder());
            setFont(FontFactory.textPaneFont());
            setBackground(ColorFactory.blueGrey400());
            setForeground(ColorFactory.grey50());
            setVisible(true);
            setText(resultsText);
        }

    }
}
