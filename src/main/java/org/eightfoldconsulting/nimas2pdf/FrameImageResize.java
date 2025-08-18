package org.eightfoldconsulting.nimas2pdf;

import javax.swing.*;
import java.awt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameImageResize extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(FrameImageResize.class);
    
    private JSpinner spinnerDPI;
    private JSpinner spinnerMaxWidth;
    private JSpinner spinnerMaxHeight;
    private JButton btnOK;
    private JButton btnCancel;
    private boolean approved = false;

    public FrameImageResize(Frame parent) {
        super(parent, "Image Settings", true);
        initComponents();
        setupActions();
        setLocationRelativeTo(parent);
    }

    // ...existing code...

    public boolean showDialog() {
        setVisible(true);
        return approved;
    }

    // Getter methods
    public int getDPI() {
        return (Integer) spinnerDPI.getValue();
    }

    public int getMaxWidth() {
        return (Integer) spinnerMaxWidth.getValue();
    }

    public int getMaxHeight() {
        return (Integer) spinnerMaxHeight.getValue();
    }
}