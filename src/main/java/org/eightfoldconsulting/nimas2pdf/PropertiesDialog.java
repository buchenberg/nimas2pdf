package org.eightfoldconsulting.nimas2pdf;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eightfoldconsulting.nimas2pdf.config.ApplicationProperties;

import java.util.logging.Logger;

public class PropertiesDialog {
    private static final Logger logger = Logger.getLogger(PropertiesDialog.class.getName());
    
    // Font Settings
    private TextField txtBaseFontFamily;
    private TextField txtHeaderFontFamily;
    private TextField txtBaseFontSize;
    private TextField txtTableFontSize;
    private TextField txtLineHeight;
    
    // Bookmark Settings
    private CheckBox chkBookmarkHeaders;
    private CheckBox chkBookmarkTables;
    
    // Page Settings
    private ComboBox<String> cmbPageOrientation;
    private TextField txtPageWidth;
    private TextField txtPageHeight;
    
    // PDF Settings
    private CheckBox chkPDFA;
    private CheckBox chkAccessiblePDF;
    
    // Image Settings
    private Spinner<Integer> spinnerDPI;
    private Spinner<Integer> spinnerMaxWidth;
    private Spinner<Integer> spinnerMaxHeight;
    
    private Stage dialogStage;
    private boolean approved = false;

    public PropertiesDialog(Stage owner) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setTitle("Properties");
        dialogStage.setResizable(false);
        
        // Create the dialog content
        Scene scene = new Scene(createDialogContent());
        dialogStage.setScene(scene);
        
        // Load current properties
        loadCurrentProperties();
        
        // Center the dialog
        dialogStage.centerOnScreen();
    }

    private VBox createDialogContent() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        
        // Create tabbed pane
        TabPane tabPane = new TabPane();
        
        // Font Settings Tab
        tabPane.getTabs().add(createFontsTab());
        
        // Page Settings Tab
        tabPane.getTabs().add(createPageTab());
        
        // PDF Settings Tab
        tabPane.getTabs().add(createPDFTab());
        
        // Image Settings Tab
        tabPane.getTabs().add(createImageTab());
        
        // Button Panel
        HBox buttonPanel = createButtonPanel();
        
        mainLayout.getChildren().addAll(tabPane, buttonPanel);
        
        return mainLayout;
    }

    private Tab createFontsTab() {
        Tab tab = new Tab("Fonts");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Base Font Family
        HBox baseFontRow = new HBox(10);
        baseFontRow.setAlignment(Pos.CENTER_LEFT);
        Label lblBaseFont = new Label("Base Font Family:");
        lblBaseFont.setMinWidth(120);
        txtBaseFontFamily = new TextField();
        HBox.setHgrow(txtBaseFontFamily, Priority.ALWAYS);
        baseFontRow.getChildren().addAll(lblBaseFont, txtBaseFontFamily);
        
        // Header Font Family
        HBox headerFontRow = new HBox(10);
        headerFontRow.setAlignment(Pos.CENTER_LEFT);
        Label lblHeaderFont = new Label("Header Font Family:");
        lblHeaderFont.setMinWidth(120);
        txtHeaderFontFamily = new TextField();
        HBox.setHgrow(txtHeaderFontFamily, Priority.ALWAYS);
        headerFontRow.getChildren().addAll(lblHeaderFont, txtHeaderFontFamily);
        
        // Base Font Size
        HBox baseSizeRow = new HBox(10);
        baseSizeRow.setAlignment(Pos.CENTER_LEFT);
        Label lblBaseSize = new Label("Base Font Size:");
        lblBaseSize.setMinWidth(120);
        txtBaseFontSize = new TextField();
        HBox.setHgrow(txtBaseFontSize, Priority.ALWAYS);
        baseSizeRow.getChildren().addAll(lblBaseSize, txtBaseFontSize);
        
        // Table Font Size
        HBox tableSizeRow = new HBox(10);
        tableSizeRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTableSize = new Label("Table Font Size:");
        lblTableSize.setMinWidth(120);
        txtTableFontSize = new TextField();
        HBox.setHgrow(txtTableFontSize, Priority.ALWAYS);
        tableSizeRow.getChildren().addAll(lblTableSize, txtTableFontSize);
        
        // Line Height
        HBox lineHeightRow = new HBox(10);
        lineHeightRow.setAlignment(Pos.CENTER_LEFT);
        Label lblLineHeight = new Label("Line Height:");
        lblLineHeight.setMinWidth(120);
        txtLineHeight = new TextField();
        HBox.setHgrow(txtLineHeight, Priority.ALWAYS);
        lineHeightRow.getChildren().addAll(lblLineHeight, txtLineHeight);
        
        content.getChildren().addAll(baseFontRow, headerFontRow, baseSizeRow, tableSizeRow, lineHeightRow);
        tab.setContent(content);
        
        return tab;
    }

    private Tab createPageTab() {
        Tab tab = new Tab("Page Layout");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Page Orientation
        HBox orientationRow = new HBox(10);
        orientationRow.setAlignment(Pos.CENTER_LEFT);
        Label lblOrientation = new Label("Page Orientation:");
        lblOrientation.setMinWidth(120);
        cmbPageOrientation = new ComboBox<>();
        cmbPageOrientation.getItems().addAll("portrait", "landscape");
        cmbPageOrientation.setValue("portrait");
        HBox.setHgrow(cmbPageOrientation, Priority.ALWAYS);
        orientationRow.getChildren().addAll(lblOrientation, cmbPageOrientation);
        
        // Page Width
        HBox widthRow = new HBox(10);
        widthRow.setAlignment(Pos.CENTER_LEFT);
        Label lblWidth = new Label("Page Width:");
        lblWidth.setMinWidth(120);
        txtPageWidth = new TextField();
        HBox.setHgrow(txtPageWidth, Priority.ALWAYS);
        widthRow.getChildren().addAll(lblWidth, txtPageWidth);
        
        // Page Height
        HBox heightRow = new HBox(10);
        heightRow.setAlignment(Pos.CENTER_LEFT);
        Label lblHeight = new Label("Page Height:");
        lblHeight.setMinWidth(120);
        txtPageHeight = new TextField();
        HBox.setHgrow(txtPageHeight, Priority.ALWAYS);
        heightRow.getChildren().addAll(lblHeight, txtPageHeight);
        
        content.getChildren().addAll(orientationRow, widthRow, heightRow);
        tab.setContent(content);
        
        return tab;
    }

    private Tab createPDFTab() {
        Tab tab = new Tab("PDF Options");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // PDF/A
        HBox pdfaRow = new HBox(10);
        pdfaRow.setAlignment(Pos.CENTER_LEFT);
        chkPDFA = new CheckBox();
        Label lblPDFA = new Label("Generate PDF/A compliant document");
        pdfaRow.getChildren().addAll(chkPDFA, lblPDFA);
        
        // Accessible PDF
        HBox accessibleRow = new HBox(10);
        accessibleRow.setAlignment(Pos.CENTER_LEFT);
        chkAccessiblePDF = new CheckBox();
        Label lblAccessible = new Label("Generate accessible PDF");
        accessibleRow.getChildren().addAll(chkAccessiblePDF, lblAccessible);
        
        // Bookmark Headers
        HBox headerBookmarkRow = new HBox(10);
        headerBookmarkRow.setAlignment(Pos.CENTER_LEFT);
        chkBookmarkHeaders = new CheckBox();
        Label lblHeaderBookmark = new Label("Create bookmarks for headers");
        headerBookmarkRow.getChildren().addAll(chkBookmarkHeaders, lblHeaderBookmark);
        
        // Bookmark Tables
        HBox tableBookmarkRow = new HBox(10);
        tableBookmarkRow.setAlignment(Pos.CENTER_LEFT);
        chkBookmarkTables = new CheckBox();
        Label lblTableBookmark = new Label("Create bookmarks for tables");
        tableBookmarkRow.getChildren().addAll(chkBookmarkTables, lblTableBookmark);
        
        content.getChildren().addAll(pdfaRow, accessibleRow, headerBookmarkRow, tableBookmarkRow);
        tab.setContent(content);
        
        return tab;
    }

    private Tab createImageTab() {
        Tab tab = new Tab("Images");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // DPI
        HBox dpiRow = new HBox(10);
        dpiRow.setAlignment(Pos.CENTER_LEFT);
        Label lblDPI = new Label("Image DPI:");
        lblDPI.setMinWidth(120);
        spinnerDPI = new Spinner<>(72, 1200, 300, 1);
        HBox.setHgrow(spinnerDPI, Priority.ALWAYS);
        dpiRow.getChildren().addAll(lblDPI, spinnerDPI);
        
        // Max Width
        HBox maxWidthRow = new HBox(10);
        maxWidthRow.setAlignment(Pos.CENTER_LEFT);
        Label lblMaxWidth = new Label("Max Width (px):");
        lblMaxWidth.setMinWidth(120);
        spinnerMaxWidth = new Spinner<>(100, 5000, 800, 10);
        HBox.setHgrow(spinnerMaxWidth, Priority.ALWAYS);
        maxWidthRow.getChildren().addAll(lblMaxWidth, spinnerMaxWidth);
        
        // Max Height
        HBox maxHeightRow = new HBox(10);
        maxHeightRow.setAlignment(Pos.CENTER_LEFT);
        Label lblMaxHeight = new Label("Max Height (px):");
        lblMaxHeight.setMinWidth(120);
        spinnerMaxHeight = new Spinner<>(100, 5000, 1000, 10);
        HBox.setHgrow(spinnerMaxHeight, Priority.ALWAYS);
        maxHeightRow.getChildren().addAll(lblMaxHeight, spinnerMaxHeight);
        
        content.getChildren().addAll(dpiRow, maxWidthRow, maxHeightRow);
        tab.setContent(content);
        
        return tab;
    }

    private HBox createButtonPanel() {
        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnReset = new Button("Reset to Defaults");
        btnReset.setOnAction(e -> resetToDefaults());
        
        Button btnOK = new Button("OK");
        btnOK.setOnAction(e -> {
            saveProperties();
            approved = true;
            dialogStage.close();
        });
        
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> dialogStage.close());
        
        buttonPanel.getChildren().addAll(btnReset, btnOK, btnCancel);
        
        return buttonPanel;
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
        
        cmbPageOrientation.setValue(ApplicationProperties.getProperty("pageOrientation", "portrait"));
        txtPageWidth.setText(ApplicationProperties.getProperty("pageWidth", "8.5in"));
        txtPageHeight.setText(ApplicationProperties.getProperty("pageHeight", "11in"));
        
        chkPDFA.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("pdfA", "false")));
        chkAccessiblePDF.setSelected(Boolean.parseBoolean(ApplicationProperties.getProperty("accessiblePDF", "true")));
        
        spinnerDPI.getValueFactory().setValue(Integer.parseInt(ApplicationProperties.getProperty("image.dpi", "300")));
        spinnerMaxWidth.getValueFactory().setValue(Integer.parseInt(ApplicationProperties.getProperty("image.maxWidth", "800")));
        spinnerMaxHeight.getValueFactory().setValue(Integer.parseInt(ApplicationProperties.getProperty("image.maxHeight", "1000")));
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
        logger.info("Page Orientation: " + cmbPageOrientation.getValue());
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
        
        cmbPageOrientation.setValue("portrait");
        txtPageWidth.setText("8.5in");
        txtPageHeight.setText("11in");
        
        chkPDFA.setSelected(false);
        chkAccessiblePDF.setSelected(true);
        
        spinnerDPI.getValueFactory().setValue(300);
        spinnerMaxWidth.getValueFactory().setValue(800);
        spinnerMaxHeight.getValueFactory().setValue(1000);
    }

    public boolean showDialog() {
        dialogStage.showAndWait();
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
    public String getPageOrientation() { return cmbPageOrientation.getValue(); }
    public String getPageWidth() { return txtPageWidth.getText(); }
    public String getPageHeight() { return txtPageHeight.getText(); }
    public boolean isPDFA() { return chkPDFA.isSelected(); }
    public boolean isAccessiblePDF() { return chkAccessiblePDF.isSelected(); }
    public int getDPI() { return spinnerDPI.getValue(); }
    public int getMaxWidth() { return spinnerMaxWidth.getValue(); }
    public int getMaxHeight() { return spinnerMaxHeight.getValue(); }
}
