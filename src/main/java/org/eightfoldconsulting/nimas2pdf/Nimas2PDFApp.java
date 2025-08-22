package org.eightfoldconsulting.nimas2pdf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eightfoldconsulting.nimas2pdf.config.ApplicationProperties;
import org.eightfoldconsulting.nimas2pdf.OPFReader;
import org.eightfoldconsulting.nimas2pdf.XMLConverter;

import java.io.File;
import java.util.logging.Logger;

public class Nimas2PDFApp extends Application {
    private static final Logger logger = Logger.getLogger(Nimas2PDFApp.class.getName());
    
    // UI Components
    private TextField txtNIMASPath;
    private TextField txtOutputPath;
    private Button btnBrowseNIMAS;
    private Button btnBrowseOutput;
    private Button btnConvert;
    private Button btnProperties;
    private TextArea txtLog;
    
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Set up the main window
        primaryStage.setTitle("NIMAS to PDF Converter");
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(600);
        
        // Create the main scene
        Scene scene = new Scene(createMainLayout());
        primaryStage.setScene(scene);
        
        // Show the window
        primaryStage.show();
        
        // Center the window
        primaryStage.centerOnScreen();
    }

    private VBox createMainLayout() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        
        // Create menu bar
        MenuBar menuBar = createMenuBar();
        
        // Create main content
        VBox contentBox = createContentBox();
        
        // Add components to main layout
        mainLayout.getChildren().addAll(menuBar, contentBox);
        
        return mainLayout;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // File Menu
        Menu menuFile = new Menu("File");
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(e -> Platform.exit());
        menuFile.getItems().add(menuItemExit);
        
        // Tools Menu
        Menu menuTools = new Menu("Tools");
        MenuItem menuItemProperties = new MenuItem("Properties...");
        menuItemProperties.setOnAction(e -> showProperties());
        menuTools.getItems().add(menuItemProperties);
        
        // Help Menu
        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("About");
        menuItemAbout.setOnAction(e -> showAbout());
        menuHelp.getItems().add(menuItemAbout);
        
        menuBar.getMenus().addAll(menuFile, menuTools, menuHelp);
        
        return menuBar;
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox(15);
        
        // Input section
        VBox inputSection = createInputSection();
        
        // Convert button section
        HBox buttonSection = createButtonSection();
        
        // Log section
        VBox logSection = createLogSection();
        
        // Add sections to content box
        contentBox.getChildren().addAll(inputSection, buttonSection, logSection);
        
        // Make log section expand to fill available space
        VBox.setVgrow(logSection, Priority.ALWAYS);
        
        return contentBox;
    }

    private VBox createInputSection() {
        VBox inputSection = new VBox(10);
        
        // NIMAS Path section
        HBox nimasSection = new HBox(10);
        nimasSection.setAlignment(Pos.CENTER_LEFT);
        
        Label lblNIMAS = new Label("NIMAS File/Path:");
        lblNIMAS.setMinWidth(120);
        txtNIMASPath = new TextField();
        txtNIMASPath.setPromptText("Select NIMAS file or directory");
        HBox.setHgrow(txtNIMASPath, Priority.ALWAYS);
        btnBrowseNIMAS = new Button("Browse...");
        btnBrowseNIMAS.setOnAction(e -> browseNIMASPath());
        
        nimasSection.getChildren().addAll(lblNIMAS, txtNIMASPath, btnBrowseNIMAS);
        
        // Output Path section
        HBox outputSection = new HBox(10);
        outputSection.setAlignment(Pos.CENTER_LEFT);
        
        Label lblOutput = new Label("Output Directory:");
        lblOutput.setMinWidth(120);
        txtOutputPath = new TextField();
        txtOutputPath.setPromptText("Select output directory");
        HBox.setHgrow(txtOutputPath, Priority.ALWAYS);
        btnBrowseOutput = new Button("Browse...");
        btnBrowseOutput.setOnAction(e -> browseOutputPath());
        
        outputSection.getChildren().addAll(lblOutput, txtOutputPath, btnBrowseOutput);
        
        inputSection.getChildren().addAll(nimasSection, outputSection);
        
        return inputSection;
    }

    private HBox createButtonSection() {
        HBox buttonSection = new HBox(15);
        buttonSection.setAlignment(Pos.CENTER);
        
        btnConvert = new Button("Convert");
        btnConvert.setPrefWidth(120);
        btnConvert.setOnAction(e -> convertAction());
        
        btnProperties = new Button("Properties...");
        btnProperties.setPrefWidth(120);
        btnProperties.setOnAction(e -> showProperties());
        
        buttonSection.getChildren().addAll(btnConvert, btnProperties);
        
        return buttonSection;
    }

    private VBox createLogSection() {
        VBox logSection = new VBox(5);
        
        Label lblLog = new Label("Conversion Log:");
        
        txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.setWrapText(true);
        txtLog.setPromptText("Conversion progress will appear here...");
        
        // Make text area expand to fill available space
        VBox.setVgrow(txtLog, Priority.ALWAYS);
        
        logSection.getChildren().addAll(lblLog, txtLog);
        
        return logSection;
    }

    private void browseNIMASPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select NIMAS File or Directory");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("OPF Files", "*.opf"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            txtNIMASPath.setText(selectedFile.getAbsolutePath());
        }
    }

    private void browseOutputPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory");
        
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            txtOutputPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void convertAction() {
        String nimasPath = txtNIMASPath.getText();
        String outputPath = txtOutputPath.getText();
        
        if (nimasPath.isEmpty() || outputPath.isEmpty()) {
            showAlert("Input Required", "Please select both input and output paths", Alert.AlertType.WARNING);
            return;
        }

        // Disable convert button during processing
        btnConvert.setDisable(true);
        btnConvert.setText("Converting...");
        
        // Run conversion in background thread
        new Thread(() -> {
            try {
                performConversion(nimasPath, outputPath);
                Platform.runLater(() -> {
                    showAlert("Conversion Complete", 
                        "Conversion completed successfully!", 
                        Alert.AlertType.INFORMATION);
                });
            } catch (Exception e) {
                logger.severe("Conversion failed: " + e.getMessage());
                Platform.runLater(() -> {
                    showAlert("Conversion Error", 
                        "Conversion failed: " + e.getMessage(), 
                        Alert.AlertType.ERROR);
                });
            } finally {
                // Re-enable convert button
                Platform.runLater(() -> {
                    btnConvert.setDisable(false);
                    btnConvert.setText("Convert");
                });
            }
        }).start();
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

    private void showProperties() {
        PropertiesDialog propertiesDialog = new PropertiesDialog(primaryStage);
        if (propertiesDialog.showDialog()) {
            // Properties were updated, we could refresh the UI or log here
            logMessage("Properties dialog was approved");
        }
    }

    private void showAbout() {
        showAlert("About NIMAS2PDF", 
            "NIMAS to PDF Converter\nVersion 1.0\n\nConverts NIMAS files to accessible PDF format.", 
            Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void logMessage(String message) {
        Platform.runLater(() -> {
            txtLog.appendText(message + "\n");
            // Auto-scroll to bottom
            txtLog.setScrollTop(Double.MAX_VALUE);
        });
        logger.info(message);
    }

    public static void main(String[] args) {
        try {
            // Check if JavaFX modules are available
            Class.forName("javafx.application.Application");
            launch(args);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JavaFX runtime components are missing.");
            System.err.println("Please ensure you have JavaFX installed or use the following command:");
            System.err.println("java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar target/nimas2pdf-1.0-SNAPSHOT.jar");
            System.err.println("");
            System.err.println("Alternatively, you can use Maven to run the application:");
            System.err.println("mvn javafx:run");
            System.exit(1);
        }
    }
}
