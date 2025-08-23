package org.eightfoldconsulting.nimas2pdf.web.dto;

/**
 * Response DTO for conversion requests.
 */
public class ConversionResponse {
    private String conversionId;
    private String message;
    private boolean success;

    public ConversionResponse() {}

    public ConversionResponse(String conversionId, String message) {
        this.conversionId = conversionId;
        this.message = message;
        this.success = conversionId != null && !conversionId.trim().isEmpty();
    }

    // Getters and Setters
    public String getConversionId() {
        return conversionId;
    }

    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
