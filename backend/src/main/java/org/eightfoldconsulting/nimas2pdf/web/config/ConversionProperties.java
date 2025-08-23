package org.eightfoldconsulting.nimas2pdf.web.config;

/**
 * Configuration properties for NIMAS to PDF conversion.
 * These properties are stored per NIMAS package and can be customized by users.
 */
public class ConversionProperties {

    // Font settings
    private String baseFontSize = "18pt";
    private String tableFontSize = "18pt";
    private String lineHeight = "1.5em";
    private String baseFontFamily = "DejaVu Sans";
    private String headerFontFamily = "DejaVu Serif";

    // Page settings
    private String pageOrientation = "portrait";
    private String pageWidth = "8.5in";
    private String pageHeight = "11in";

    // Bookmark settings
    private boolean bookmarkHeaders = false;
    private boolean bookmarkTables = false;

    // Default constructor
    public ConversionProperties() {
    }

    // Copy constructor
    public ConversionProperties(ConversionProperties other) {
        if (other != null) {
            this.baseFontSize = other.baseFontSize;
            this.tableFontSize = other.tableFontSize;
            this.lineHeight = other.lineHeight;
            this.baseFontFamily = other.baseFontFamily;
            this.headerFontFamily = other.headerFontFamily;
            this.pageOrientation = other.pageOrientation;
            this.pageWidth = other.pageWidth;
            this.pageHeight = other.pageHeight;
            this.bookmarkHeaders = other.bookmarkHeaders;
            this.bookmarkTables = other.bookmarkTables;
        }
    }

    // Getters and setters
    public String getBaseFontSize() {
        return baseFontSize;
    }

    public void setBaseFontSize(String baseFontSize) {
        this.baseFontSize = baseFontSize;
    }

    public String getTableFontSize() {
        return tableFontSize;
    }

    public void setTableFontSize(String tableFontSize) {
        this.tableFontSize = tableFontSize;
    }

    public String getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(String lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getBaseFontFamily() {
        return baseFontFamily;
    }

    public void setBaseFontFamily(String baseFontFamily) {
        this.baseFontFamily = baseFontFamily;
    }

    public String getHeaderFontFamily() {
        return headerFontFamily;
    }

    public void setHeaderFontFamily(String headerFontFamily) {
        this.headerFontFamily = headerFontFamily;
    }

    public String getPageOrientation() {
        return pageOrientation;
    }

    public void setPageOrientation(String pageOrientation) {
        this.pageOrientation = pageOrientation;
    }

    public String getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }

    public boolean isBookmarkHeaders() {
        return bookmarkHeaders;
    }

    public void setBookmarkHeaders(boolean bookmarkHeaders) {
        this.bookmarkHeaders = bookmarkHeaders;
    }

    public boolean isBookmarkTables() {
        return bookmarkTables;
    }

    public void setBookmarkTables(boolean bookmarkTables) {
        this.bookmarkTables = bookmarkTables;
    }

    @Override
    public String toString() {
        return "ConversionProperties{" +
                "baseFontSize='" + baseFontSize + '\'' +
                ", tableFontSize='" + tableFontSize + '\'' +
                ", lineHeight='" + lineHeight + '\'' +
                ", baseFontFamily='" + baseFontFamily + '\'' +
                ", headerFontFamily='" + headerFontFamily + '\'' +
                ", pageOrientation='" + pageOrientation + '\'' +
                ", pageWidth='" + pageWidth + '\'' +
                ", pageHeight='" + pageHeight + '\'' +
                ", bookmarkHeaders=" + bookmarkHeaders +
                ", bookmarkTables=" + bookmarkTables +
                '}';
    }
}
