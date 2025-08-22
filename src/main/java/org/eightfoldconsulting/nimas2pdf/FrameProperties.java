package org.eightfoldconsulting.nimas2pdf;

import org.eightfoldconsulting.nimas2pdf.config.ApplicationProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class FrameProperties extends JDialog {
    private static final Logger logger = Logger.getLogger(FrameProperties.class.getName());
    
    // Font Settings
    private JTextField txtBaseFontFamily;
    private JTextField txtHeaderFontFamily;
    private JTextField txtBaseFontSize;
    private JTextField txtTableFontSize;
    private JTextField txtLineHeight;
    
    // Bookmark Settings
    private JCheckBox chkBookmarkHeaders;
    private JCheckBox chkBookmarkTables;
    
    // Page Settings
    private JComboBox<String> cmbPageOrientation;
    private JTextField txtPageWidth;
    private JTextField txtPageHeight;
    
    // PDF Settings
    private JCheckBox chkPDFA;
    private JCheckBox chkAccessiblePDF;
    
    // Image Settings
    private JSpinner spinnerDPI;
    private JSpinner spinnerMaxWidth;
    private JSpinner spinnerMaxHeight;
    
    // Buttons
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnReset;
    
    private boolean approved = false;

    public FrameProperties(Frame parent) {
        super(parent, "Properties", true);
        initComponents();
        loadCurrentProperties();
        setupActions();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        // Create components
        txtBaseFontFamily = new JTextField(20);
        txtHeaderFontFamily = new JTextField(20);
        txtBaseFontSize = new JTextField(10);
        txtTableFontSize = new JTextField(10);
        txtLineHeight = new JTextField(10);
        
        chkBookmarkHeaders = new JCheckBox();
        chkBookmarkTables = new JCheckBox();
        
        cmbPageOrientation = new JComboBox<>(new String[]{"portrait", "landscape"});
        txtPageWidth = new JTextField(10);
        txtPageHeight = new JTextField(10);
        
        chkPDFA = new JCheckBox();
        chkAccessiblePDF = new JCheckBox();
        
        spinnerDPI = new JSpinner(new SpinnerNumberModel(300, 72, 1200, 1));
        spinnerMaxWidth = new JSpinner(new SpinnerNumberModel(800, 100, 5000, 10));
        spinnerMaxHeight = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 10));
        
        btnOK = new JButton("OK");
        btnCancel = new JButton("Cancel");
        btnReset = new JButton("Reset to Defaults");
        
        // Create tabbed pane for organization
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Font Settings Tab
        tabbedPane.addTab("Fonts", createFontsPanel());
        
        // Page Settings Tab
        tabbedPane.addTab("Page Layout", createPagePanel());
        
        // PDF Settings Tab
        tabbedPane.addTab("PDF Options", createPDFPanel());
        
        // Image Settings Tab
        tabbedPane.addTab("Images", createImagePanel());
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnReset);
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        
        // Main layout
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(500, 600);
        setResizable(false);
    }

    private JPanel createFontsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Base Font Family
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Base Font Family:"), gbc);
        gbc.gridx = 1;
        panel.add(txtBaseFontFamily, gbc);
        
        // Header Font Family
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Header Font Family:"), gbc);
        gbc.gridx = 1;
        panel.add(txtHeaderFontFamily, gbc);
        
        // Base Font Size
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Base Font Size:"), gbc);
        gbc.gridx = 1;
        panel.add(txtBaseFontSize, gbc);
        
        // Table Font Size
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Table Font Size:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTableFontSize, gbc);
        
        // Line Height
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Line Height:"), gbc);
        gbc.gridx = 1;
        panel.add(txtLineHeight, gbc);
        
        return panel;
    }

    private JPanel createPagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Page Orientation
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Page Orientation:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbPageOrientation, gbc);
        
        // Page Width
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Page Width:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPageWidth, gbc);
        
        // Page Height
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Page Height:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPageHeight, gbc);
        
        return panel;
    }

    private JPanel createPDFPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // PDF/A
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(chkPDFA, gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Generate PDF/A compliant document"), gbc);
        
        // Accessible PDF
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(chkAccessiblePDF, gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Generate accessible PDF"), gbc);
        
        // Bookmark Headers
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(chkBookmarkHeaders, gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Create bookmarks for headers"), gbc);
        
        // Bookmark Tables
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(chkBookmarkTables, gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Create bookmarks for tables"), gbc);
        
        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // DPI
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Image DPI:"), gbc);
        gbc.gridx = 1;
        panel.add(spinnerDPI, gbc);
        
        // Max Width
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Max Width (px):"), gbc);
        gbc.gridx = 1;
        panel.add(spinnerMaxWidth, gbc);
        
        // Max Height
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Max Height (px):"), gbc);
        gbc.gridx = 1;
        panel.add(spinnerMaxHeight, gbc);
        
        return panel;
    }

    private void loadCurrentProperties() {
        // Load current values from ApplicationProperties
        txtBaseFontFamily.setText(ApplicationProperties.getProperty("baseFontFamily", "Times New Roman"));
        txtHeaderFontFamily.setText(ApplicationProperties.getProperty("headerFontFamily", "Arial"));
        txtBaseFontSize.setText(ApplicationProperties.getProperty("baseFontSize", "12pt"));
        txtTableFontSize.setText(ApplicationProperties.getProperty("tableFontSize", "10pt"));
        txtLineHeight.setText(ApplicationProperties.getProperty("lineHeight", "1.5em"));
        
        chkBookmarkHeaders.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("bookmarkHeaders", "true")));
        chkBookmarkTables.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("bookmarkTables", "true")));
        
        cmbPageOrientation.setSelectedItem(ApplicationProperties.getProperty("pageOrientation", "portrait"));
        txtPageWidth.setText(ApplicationProperties.getProperty("pageWidth", "8.5in"));
        txtPageHeight.setText(ApplicationProperties.getProperty("pageHeight", "11in"));
        
        chkPDFA.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("pdfA", "false")));
        chkAccessiblePDF.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("accessiblePDF", "true")));
        
        spinnerDPI.setValue(Integer.parseInt(ApplicationProperties.getProperty("image.dpi", "300")));
        spinnerMaxWidth.setValue(Integer.parseInt(ApplicationProperties.getProperty("image.maxWidth", "800")));
        spinnerMaxHeight.setValue(Integer.parseInt(ApplicationProperties.getProperty("image.maxHeight", "1000")));
    }

    private void setupActions() {
        btnOK.addActionListener(e -> {
            saveProperties();
            approved = true;
            dispose();
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        btnReset.addActionListener(e -> resetToDefaults());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                approved = false;
                dispose();
            }
        });
    }

    private void saveProperties() {
        // Save all properties to ApplicationProperties
        // Note: This would need to be implemented to actually persist the changes
        // For now, we'll just log the values
        logger.info("Properties updated:");
        logger.info("Base Font Family: " + txtBaseFontFamily.getText());
        logger.info("Header Font Family: " + txtHeaderFontFamily.getText());
        logger.info("Base Font Size: " + txtBaseFontSize.getText());
        logger.info("Table Font Size: " + txtTableFontSize.getText());
        logger.info("Line Height: " + txtLineHeight.getText());
        logger.info("Bookmark Headers: " + chkBookmarkHeaders.isSelected());
        logger.info("Bookmark Tables: " + chkBookmarkTables.isSelected());
        logger.info("Page Orientation: " + cmbPageOrientation.getSelectedItem());
        logger.info("Page Width: " + txtPageWidth.getText());
        logger.info("Page Height: " + txtPageHeight.getText());
        logger.info("PDF/A: " + chkPDFA.isSelected());
        logger.info("Accessible PDF: " + chkAccessiblePDF.isSelected());
        logger.info("Image DPI: " + spinnerDPI.getValue());
        logger.info("Max Width: " + spinnerMaxWidth.getValue());
        logger.info("Max Height: " + spinnerMaxHeight.getValue());
    }

    private void resetToDefaults() {
        txtBaseFontFamily.setText("Times New Roman");
        txtHeaderFontFamily.setText("Arial");
        txtBaseFontSize.setText("12pt");
        txtTableFontSize.setText("10pt");
        txtLineHeight.setText("1.5em");
        
        chkBookmarkHeaders.setSelected(true);
        chkBookmarkTables.setSelected(true);
        
        cmbPageOrientation.setSelectedItem("portrait");
        txtPageWidth.setText("8.5in");
        txtPageHeight.setText("11in");
        
        chkPDFA.setSelected(false);
        chkAccessiblePDF.setSelected(true);
        
        spinnerDPI.setValue(300);
        spinnerMaxWidth.setValue(800);
        spinnerMaxHeight.setValue(1000);
    }

    public boolean showDialog() {
        setVisible(true);
        return approved;
    }

    // Getters for accessing the current values
    public String getBaseFontFamily() { return txtBaseFontFamily.getText(); }
    public String getHeaderFontFamily() { return txtHeaderFontFamily.getText(); }
    public String getBaseFontSize() { return txtBaseFontSize.getText(); }
    public String getTableFontSize() { return txtTableFontSize.getText(); }
    public String getLineHeight() { return txtLineHeight.getText(); }
    public boolean isBookmarkHeaders() { return chkBookmarkHeaders.isSelected(); }
    public boolean isBookmarkTables() { return chkBookmarkTables.isSelected(); }
    public String getPageOrientation() { return (String) cmbPageOrientation.getSelectedItem(); }
    public String getPageWidth() { return txtPageWidth.getText(); }
    public String getPageHeight() { return txtPageHeight.getText(); }
    public boolean isPDFA() { return chkPDFA.isSelected(); }
    public boolean isAccessiblePDF() { return chkAccessiblePDF.isSelected(); }
    public int getDPI() { return (Integer) spinnerDPI.getValue(); }
    public int getMaxWidth() { return (Integer) spinnerMaxWidth.getValue(); }
    public int getMaxHeight() { return (Integer) spinnerMaxHeight.getValue(); }
}
