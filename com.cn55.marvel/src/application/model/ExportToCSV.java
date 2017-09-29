package application.model;
import application.view.custom_components.ProgressDialog;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ExportToCSV {
    void exportData(DataDAO db, BufferedWriter writer) throws IOException;
    void closeFile();
}
