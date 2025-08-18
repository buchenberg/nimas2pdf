package org.eightfoldconsulting.nimas2pdf;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Logger;

public class FrameMain extends JFrame {
    private static final Logger logger = Logger.getLogger(FrameMain.class.getName());
    
    private JTextField txtNIMASPath;
    private JTextField txtOutputPath;
    private JButton btnBrowseNIMAS;
    private JButton btnBrowseOutput;
    private JButton btnConvert;
    private JTextArea txtLog;
    
    public FrameMain() {
        initComponents();
        setupActions();
    }

    private void initComponents() {
        // Replace @Action annotations with direct method calls
        txtNIMASPath = new JTextField();
        txtOutputPath = new JTextField();
        btnBrowseNIMAS = new JButton("Browse...");
        btnBrowseOutput = new JButton("Browse...");
        btnConvert = new JButton("Convert");
        txtLog = new JTextArea();
        
        // Setup layout similar to the form designer
        // ...existing layout code...
    }

    private void setupActions() {
        btnBrowseNIMAS.addActionListener(e -> browseNIMASPath());
        btnBrowseOutput.addActionListener(e -> browseOutputPath());
        btnConvert.addActionListener(e -> convertAction());
    }

    private void browseNIMASPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtNIMASPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browseOutputPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtOutputPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void convertAction() {
        String nimasPath = txtNIMASPath.getText();
        String outputPath = txtOutputPath.getText();
        
        if (nimasPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select both input and output paths",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Existing conversion logic
        // ...existing conversion code...
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrameMain frame = new FrameMain();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}