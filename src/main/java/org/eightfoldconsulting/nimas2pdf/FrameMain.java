package org.eightfoldconsulting.nimas2pdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Logger;
import org.eightfoldconsulting.nimas2pdf.OPFReader;
import org.eightfoldconsulting.nimas2pdf.XMLConverter;

public class FrameMain extends JFrame {
    private static final Logger logger = Logger.getLogger(FrameMain.class.getName());
    
    private JTextField txtNIMASPath;
    private JTextField txtOutputPath;
    private JButton btnBrowseNIMAS;
    private JButton btnBrowseOutput;
    private JButton btnConvert;
    private JButton btnProperties;
    private JTextArea txtLog;
    
    public FrameMain() {
        initComponents();
        setupActions();
    }

    private void initComponents() {
        // Create components
        txtNIMASPath = new JTextField(30);
        txtOutputPath = new JTextField(30);
        btnBrowseNIMAS = new JButton("Browse...");
        btnBrowseOutput = new JButton("Browse...");
        btnConvert = new JButton("Convert");
        btnProperties = new JButton("Properties...");
        txtLog = new JTextArea(15, 50);
        txtLog.setEditable(false);
        
        // Create scroll pane for log
        JScrollPane logScrollPane = new JScrollPane(txtLog);
        
        // Setup main content panel with GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // NIMAS Path section
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        contentPanel.add(new JLabel("NIMAS File/Path:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(txtNIMASPath, gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        contentPanel.add(btnBrowseNIMAS, gbc);
        
        // Output Path section
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(new JLabel("Output Directory:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(txtOutputPath, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        contentPanel.add(btnBrowseOutput, gbc);
        
        // Convert button and Properties button section
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(btnConvert, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(btnProperties, gbc);
        
        // Log section
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPanel.add(new JLabel("Conversion Log:"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPanel.add(logScrollPane, gbc);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuTools = new JMenu("Tools");
        JMenu menuHelp = new JMenu("Help");
        
        JMenuItem menuItemProperties = new JMenuItem("Properties...");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        JMenuItem menuItemAbout = new JMenuItem("About");
        
        menuItemProperties.addActionListener(e -> showProperties());
        menuItemExit.addActionListener(e -> System.exit(0));
        
        menuFile.add(menuItemExit);
        menuTools.add(menuItemProperties);
        menuHelp.add(menuItemAbout);
        
        menuBar.add(menuFile);
        menuBar.add(menuTools);
        menuBar.add(menuHelp);
        
        // Set up the main frame layout
        setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        add(contentPanel, BorderLayout.CENTER);
        
        // Set frame properties
        setTitle("NIMAS to PDF Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
    }

    private void setupActions() {
        btnBrowseNIMAS.addActionListener(e -> browseNIMASPath());
        btnBrowseOutput.addActionListener(e -> browseOutputPath());
        btnConvert.addActionListener(e -> convertAction());
        btnProperties.addActionListener(e -> showProperties());
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

        // Disable convert button during processing
        btnConvert.setEnabled(false);
        btnConvert.setText("Converting...");
        
        // Run conversion in background thread to avoid blocking UI
        SwingUtilities.invokeLater(() -> {
            try {
                performConversion(nimasPath, outputPath);
            } catch (Exception e) {
                logger.severe("Conversion failed: " + e.getMessage());
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Conversion failed: " + e.getMessage(),
                        "Conversion Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                // Re-enable convert button
                SwingUtilities.invokeLater(() -> {
                    btnConvert.setEnabled(true);
                    btnConvert.setText("Convert");
                });
            }
        });
    }
    
    private void performConversion(String nimasPath, String outputPath) throws Exception {
        File nimasFile = new File(nimasPath);
        File outputDir = new File(outputPath);
        
        if (!nimasFile.exists()) {
            throw new IllegalArgumentException("NIMAS file/directory does not exist: " + nimasPath);
        }
        
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                throw new IllegalArgumentException("Could not create output directory: " + outputPath);
            }
        }
        
        // Log start of conversion
        logMessage("Starting conversion...");
        logMessage("Input: " + nimasPath);
        logMessage("Output: " + outputPath);
        
        // Find the OPF file
        File opfFile = findOPFFile(nimasFile);
        if (opfFile == null) {
            throw new IllegalArgumentException("No OPF file found in NIMAS directory");
        }
        
        logMessage("Found OPF file: " + opfFile.getName());
        
        // Read OPF metadata
        OPFReader opfReader = new OPFReader(opfFile);
        logMessage("Title: " + opfReader.getTitle());
        logMessage("Identifier: " + opfReader.getIdentifier());
        
        // Find the main XML file
        String daisyXMLPath = opfReader.getDaisyXML();
        if (daisyXMLPath == null) {
            throw new IllegalArgumentException("No main XML file found in OPF");
        }
        
        File xmlFile = new File(opfFile.getParentFile(), daisyXMLPath);
        if (!xmlFile.exists()) {
            throw new IllegalArgumentException("Main XML file not found: " + xmlFile.getAbsolutePath());
        }
        
        logMessage("Processing XML file: " + xmlFile.getName());
        
        // Find XSLT file
        File xsltFile = findXSLTFile();
        if (xsltFile == null) {
            throw new IllegalArgumentException("XSLT file not found");
        }
        
        logMessage("Using XSLT: " + xsltFile.getName());
        
        // Create XMLConverter and perform conversion
        XMLConverter converter = new XMLConverter(xmlFile, xsltFile);
        
        // Generate output filename
        String outputFilename = opfReader.getIdentifier() + ".pdf";
        if (outputFilename.isEmpty()) {
            outputFilename = "output.pdf";
        }
        File outputFile = new File(outputDir, outputFilename);
        
        logMessage("Converting to PDF...");
        converter.convertXML2PDF(opfReader, outputFile);
        
        logMessage("Conversion completed successfully!");
        logMessage("Output file: " + outputFile.getAbsolutePath());
        
        // Show success message
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                "Conversion completed successfully!\nOutput: " + outputFile.getAbsolutePath(),
                "Conversion Complete",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private File findOPFFile(File nimasPath) {
        if (nimasPath.isFile()) {
            // If it's a file, check if it's an OPF file
            if (nimasPath.getName().toLowerCase().endsWith(".opf")) {
                return nimasPath;
            }
            // If it's not an OPF file, look in the same directory
            nimasPath = nimasPath.getParentFile();
        }
        
        if (nimasPath != null && nimasPath.isDirectory()) {
            File[] files = nimasPath.listFiles((dir, name) -> name.toLowerCase().endsWith(".opf"));
            if (files != null && files.length > 0) {
                return files[0]; // Return first OPF file found
            }
        }
        
        return null;
    }
    
    private File findXSLTFile() {
        // Look for XSLT file in resources
        try {
            java.net.URL xsltUrl = getClass().getResource("/xml/xslt/dtbook2fo.xsl");
            if (xsltUrl != null) {
                return new File(xsltUrl.toURI());
            }
        } catch (Exception e) {
            logger.warning("Could not load XSLT from resources: " + e.getMessage());
        }
        
        // Fallback: look in current directory
        File xsltFile = new File("src/main/resources/xml/xslt/dtbook2fo.xsl");
        if (xsltFile.exists()) {
            return xsltFile;
        }
        
        return null;
    }
    
    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append(message + "\n");
            // Auto-scroll to bottom
            txtLog.setCaretPosition(txtLog.getDocument().getLength());
        });
        logger.info(message);
    }
    
    private void showProperties() {
        FrameProperties propertiesDialog = new FrameProperties(this);
        if (propertiesDialog.showDialog()) {
            // Properties were updated, we could refresh the UI or log here
            logger.info("Properties dialog was approved");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrameMain frame = new FrameMain();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}