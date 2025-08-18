package org.eightfoldconsulting.nimas2pdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class FrameOptions extends JDialog {
    private static final Logger logger = Logger.getLogger(FrameOptions.class.getName());
    
    private JCheckBox chkProcessImages;
    private JButton btnImageSettings;
    private JButton btnOK;
    private JButton btnCancel;
    
    private boolean approved = false;
    private final FrameImageResize imageResize;

    public FrameOptions(Frame parent) {
        super(parent, true);
        imageResize = new FrameImageResize(parent);
        initComponents();
        setupActions();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        chkProcessImages = new JCheckBox("Process Images");
        btnImageSettings = new JButton("Image Settings...");
        btnOK = new JButton("OK");
        btnCancel = new JButton("Cancel");

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(chkProcessImages, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(btnImageSettings, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
    }

    private void setupActions() {
        btnImageSettings.addActionListener(e -> imageResize.showDialog());
        
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

    public boolean isProcessImages() {
        return chkProcessImages.isSelected();
    }

    public FrameImageResize getImageResize() {
        return imageResize;
    }
}