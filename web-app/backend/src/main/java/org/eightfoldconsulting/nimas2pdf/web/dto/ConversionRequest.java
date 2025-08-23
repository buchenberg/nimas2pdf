package org.eightfoldconsulting.nimas2pdf.web.dto;

/**
 * DTO for conversion requests.
 */
public class ConversionRequest {
    private String inputFile;
    private String outputName;
    private String properties;

    public ConversionRequest() {}

    public ConversionRequest(String inputFile, String outputName) {
        this.inputFile = inputFile;
        this.outputName = outputName;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
