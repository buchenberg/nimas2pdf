package org.eightfoldconsulting.nimas2pdf.web.util;

import org.springframework.mock.web.MockMultipartFile;

/**
 * Factory class for creating test data objects.
 */
public class TestDataFactory {

    /**
     * Creates a mock XML file for testing.
     */
    public static MockMultipartFile createMockXmlFile() {
        return new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
    }

    /**
     * Creates a mock XML file with custom content.
     */
    public static MockMultipartFile createMockXmlFile(String filename, String content) {
        return new MockMultipartFile(
            "file",
            filename,
            "application/xml",
            content.getBytes()
        );
    }

    /**
     * Creates a mock ZIP file for testing.
     */
    public static MockMultipartFile createMockZipFile() {
        return new MockMultipartFile(
            "file",
            "test.zip",
            "application/zip",
            "PK\003\004".getBytes() // Minimal ZIP header
        );
    }

    /**
     * Creates sample XML content for testing.
     */
    public static String createSampleXmlContent() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <dtbook xmlns="http://www.daisy.org/z3986/2005/dtbook/" version="2005-3" xml:lang="en">
                <book>
                    <frontmatter>
                        <doctitle>Test Book</doctitle>
                    </frontmatter>
                    <bodymatter>
                        <level1>
                            <h1>Chapter 1</h1>
                            <p>This is a test paragraph.</p>
                        </level1>
                    </bodymatter>
                </book>
            </dtbook>
            """;
    }

    /**
     * Creates sample properties JSON for testing.
     */
    public static String createSamplePropertiesJson() {
        return """
            {
                "dpi": 300,
                "pageSize": "A4",
                "margins": "1in",
                "fontSize": "12pt",
                "lineHeight": "1.5"
            }
            """;
    }
}
