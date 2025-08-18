package org.eightfoldconsulting.nimas2pdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class FrameImageResize extends JDialog {
    private static final Logger logger = Logger.getLogger(FrameImageResize.class.getName());
    
    private JSpinner spinnerDPI;
    private JSpinner spinnerMaxWidth;
    private JSpinner spinnerMaxHeight;
    private JButton btnOK;
    private JButton btnCancel;
    
    private boolean approved = false;

    public FrameImageResize(Frame parent) {
        super(parent, true);
        initComponents();
        setupActions();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        spinnerDPI = new JSpinner(new SpinnerNumberModel(300, 72, 1200, 1));
        spinnerMaxWidth = new JSpinner(new SpinnerNumberModel(800, 100, 5000, 10));
        spinnerMaxHeight = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 10));
        btnOK = new JButton("OK");
        btnCancel = new JButton("Cancel");

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add components using GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("DPI:"), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(spinnerDPI, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(new JLabel("Max Width:"), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(spinnerMaxWidth, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Max Height:"), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(spinnerMaxHeight, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
    }

    private void setupActions() {
        btnOK.addActionListener(e -> {
            approved = true;
            dispose();
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                approved = false;
                dispose();
            }
        });
    }

    public boolean showDialog() {
        setVisible(true);
        return approved;
    }

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