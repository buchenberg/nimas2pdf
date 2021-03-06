// Copyright (C) 2009 Eightfold Consulting LLC
//
// This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with this program;
// if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package org.eightfoldconsulting.nimas2pdf;

//Java
import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

//logging
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

//Swing
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingWorker;

//JAXP
import javax.xml.transform.TransformerException;


//FOP
import org.apache.fop.apps.FOPException;
import org.jdesktop.application.Action;

/**
 *
 * @author Gregory Buchenberger
 */
public class FrameMain extends javax.swing.JFrame {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private static String jarDirPath;

    /**
     *
     */
    public static Properties applicationProps;
    /**
     *
     */
    public static String applicationPropertiesFilePath;

    /** Creates new form MainFrame */
    public FrameMain() {
        initComponents();
        initLogging();
        try {
            File jarFile = new File(FrameMain.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString());
            String jarDir = jarFile.getParent().toString();
            jarDirPath = URLDecoder.decode(jarDir, "UTF-8");
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
        try {
            initProperties();
        } catch (FileNotFoundException ex) {
            logger.severe(ex.getMessage());
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourceFormatButtonGroup = new javax.swing.ButtonGroup();
        renderFormatbuttonGroup = new javax.swing.ButtonGroup();
        jPanelMain = new javax.swing.JPanel();
        sourceXMLLabel = new javax.swing.JLabel();
        sourceXMLTextField = new javax.swing.JTextField();
        resultScrollPane = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();
        resultLabel = new javax.swing.JLabel();
        chooseSourceXMLButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        transformButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jMenuBarMain = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        fileExitMenuItem = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItemShrinkImages = new javax.swing.JMenuItem();
        jMenuOptions = new javax.swing.JMenu();
        optionsSourceFormatMenu = new javax.swing.JMenu();
        sourceNIMASRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        sourceXSLFORadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        optionsRenderFormatMenu = new javax.swing.JMenu();
        renderFormatPDFRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        renderFormatXSLFORadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NIMAS2PDF");
        setName("frameMain"); // NOI18N

        sourceXMLLabel.setText("Package File");

        resultTextArea.setColumns(20);
        resultTextArea.setRows(5);
        resultScrollPane.setViewportView(resultTextArea);

        resultLabel.setText("Result:");

        chooseSourceXMLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/drive_go.png"))); // NOI18N
        chooseSourceXMLButton.setToolTipText("Browse for source XML document");
        chooseSourceXMLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseSourceXMLButtonActionPerformed(evt);
            }
        });

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/cancel.png"))); // NOI18N
        exitButton.setText("Exit");
        exitButton.setToolTipText("Exit NIMAS2PDF");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        transformButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_go.png"))); // NOI18N
        transformButton.setText("Transform");
        transformButton.setToolTipText("Perform transformation");
        transformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transformButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(resultScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 463, Short.MAX_VALUE)
                        .addComponent(exitButton)
                        .addGap(18, 18, 18)
                        .addComponent(transformButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelMainLayout.createSequentialGroup()
                        .addComponent(sourceXMLLabel)
                        .addGap(18, 18, 18)
                        .addComponent(sourceXMLTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(chooseSourceXMLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(resultLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceXMLLabel)
                    .addComponent(sourceXMLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseSourceXMLButton))
                .addGap(18, 18, 18)
                .addComponent(resultLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resultScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exitButton)
                        .addComponent(transformButton))
                    .addComponent(statusLabel))
                .addContainerGap())
        );

        jMenuFile.setText("File");
        jMenuFile.setToolTipText("File menu");

        fileExitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/cancel.png"))); // NOI18N
        fileExitMenuItem.setText("Exit");
        fileExitMenuItem.setToolTipText("Exit NIMAS2PDF");
        fileExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileExitMenuItemActionPerformed(evt);
            }
        });
        jMenuFile.add(fileExitMenuItem);

        jMenuBarMain.add(jMenuFile);

        jMenuTools.setText("Tools");

        jMenuItemShrinkImages.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_picture.png"))); // NOI18N
        jMenuItemShrinkImages.setText("Shrink images");
        jMenuItemShrinkImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShrinkImagesActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemShrinkImages);

        jMenuBarMain.add(jMenuTools);

        jMenuOptions.setText("Options");
        jMenuOptions.setToolTipText("Options menu");

        optionsSourceFormatMenu.setText("Source Format");
        optionsSourceFormatMenu.setToolTipText("Choose source format");

        sourceFormatButtonGroup.add(sourceNIMASRadioButtonMenuItem);
        sourceNIMASRadioButtonMenuItem.setSelected(true);
        sourceNIMASRadioButtonMenuItem.setText("NIMAS");
        sourceNIMASRadioButtonMenuItem.setToolTipText("Select source XML as Digital Talking Book / NIMAS (.odf) format");
        sourceNIMASRadioButtonMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_code.png"))); // NOI18N
        sourceNIMASRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceNIMASRadioButtonMenuItemActionPerformed(evt);
            }
        });
        optionsSourceFormatMenu.add(sourceNIMASRadioButtonMenuItem);

        sourceFormatButtonGroup.add(sourceXSLFORadioButtonMenuItem);
        sourceXSLFORadioButtonMenuItem.setText("XSL-FO");
        sourceXSLFORadioButtonMenuItem.setToolTipText("Select source XML as Formating Objects (.fo) format");
        sourceXSLFORadioButtonMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_code_red.png"))); // NOI18N
        sourceXSLFORadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceXSLFORadioButtonMenuItemActionPerformed(evt);
            }
        });
        optionsSourceFormatMenu.add(sourceXSLFORadioButtonMenuItem);

        jMenuOptions.add(optionsSourceFormatMenu);

        optionsRenderFormatMenu.setText("Render Format");
        optionsRenderFormatMenu.setToolTipText("Choose render format");
        optionsRenderFormatMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
                optionsRenderFormatMenuMenuDeselected(evt);
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
            }
        });

        renderFormatbuttonGroup.add(renderFormatPDFRadioButtonMenuItem);
        renderFormatPDFRadioButtonMenuItem.setSelected(true);
        renderFormatPDFRadioButtonMenuItem.setText("PDF");
        renderFormatPDFRadioButtonMenuItem.setToolTipText("Select to render a PDF document from the source XML");
        renderFormatPDFRadioButtonMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_acrobat.png"))); // NOI18N
        renderFormatPDFRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderFormatPDFRadioButtonMenuItemActionPerformed(evt);
            }
        });
        optionsRenderFormatMenu.add(renderFormatPDFRadioButtonMenuItem);
        renderFormatPDFRadioButtonMenuItem.getAccessibleContext().setAccessibleDescription("A radio button that when selected renders PDF format");
        renderFormatPDFRadioButtonMenuItem.getAccessibleContext().setAccessibleParent(optionsRenderFormatMenu);

        renderFormatbuttonGroup.add(renderFormatXSLFORadioButtonMenuItem);
        renderFormatXSLFORadioButtonMenuItem.setText("XSL-FO");
        renderFormatXSLFORadioButtonMenuItem.setToolTipText("Select to render an XSL-FO document from the source XML");
        renderFormatXSLFORadioButtonMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/eightfoldconsulting/nimas2pdf/resources/page_white_code_red.png"))); // NOI18N
        renderFormatXSLFORadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderFormatXSLFORadioButtonMenuItemActionPerformed(evt);
            }
        });
        optionsRenderFormatMenu.add(renderFormatXSLFORadioButtonMenuItem);

        jMenuOptions.add(optionsRenderFormatMenu);

        jMenuItem1.setText("Global Options");
        jMenuItem1.setToolTipText("Set Global persistent options");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuOptions.add(jMenuItem1);

        jMenuBarMain.add(jMenuOptions);

        setJMenuBar(jMenuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initLogging() {
        ////set up logging
        LoggingOutputStream los;
        //capture stdout to log
        Logger.getLogger("stdout");
        los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);
        System.setOut(new PrintStream(los, true));
        //capture stderr to log
        Logger.getLogger("stderr");
        los = new LoggingOutputStream(logger, StdOutErrLevel.STDERR);
        System.setErr(new PrintStream(los, true));
        //hook custom handler
        TextAreaHandler.setTextArea(resultTextArea);
        //load logging config
        setupJdkLoggerHandler();
    }

    private void initProperties() throws FileNotFoundException, IOException {
        // load default properties from classpath
        Properties defaultProps = new Properties();
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream in = loader.getResourceAsStream ("org/eightfoldconsulting/nimas2pdf/resources/default.properties");
        defaultProps.load(in);
        in.close();
        // create application properties with default
        applicationProps = new Properties(defaultProps);
        // now load application  properties from file system

       File applicationPropertiesFile = new File(System.getProperty("user.home")+File.separator+".nimas2pdf"+File.separator+ "application.properties");
       //File applicationPropertiesFile = new File(jarDirPath, File.separator + "conf" + File.separator + "application.properties");
       applicationPropertiesFilePath = applicationPropertiesFile.getCanonicalPath().toString();
        in = loader.getResourceAsStream ("org/eightfoldconsulting/nimas2pdf/resources/application.properties");
        FileInputStream fin = new FileInputStream(applicationPropertiesFile);
        applicationProps.load(fin);
        fin.close();
    }

    //background thread
    private class TransformTask extends SwingWorker<Void, String> {

        @Override
        protected Void doInBackground() {
            File sourceFile;
            File sourceDir;
            File xsltfile = new File(jarDirPath, "xml/xslt/"+applicationProps.getProperty("stylesheet"));
            String source = sourceXMLTextField.getText();
            if (source.length() != 0) {
                sourceFile = new File(sourceXMLTextField.getText());
                sourceDir = new File(sourceFile.getParent());
                publish(String.format("transforming %s", sourceFile.getName()));
            } else {
                JOptionPane.showMessageDialog(null, "Please select a NIMAS package file.", "Oops!", JOptionPane.ERROR_MESSAGE);
                transformButton.setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return null;
            }

            //DTBook to XSL-FO
            if (sourceNIMASRadioButtonMenuItem.isSelected() && renderFormatXSLFORadioButtonMenuItem.isSelected()) {
                //parse OPF
                OPFReader opfReader = new OPFReader(sourceFile);
                String nimasID = opfReader.getIdentifier();
                File xmlfile = new File(sourceDir, opfReader.getDaisyXML());
                File fofile = new File(sourceDir, nimasID + ".fo");
                XMLConverter xc = new XMLConverter(xmlfile, xsltfile);
                publish("Transforming. Please wait...");
                try {
                    xc.convertNIMAS2FO(fofile);
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                } catch (TransformerException e) {
                    logger.severe(e.getMessage());
                } finally {
                    publish("Transformation complete.");
                    transformButton.setEnabled(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return null;
                }
                //DTBook to PDF
            } else if (sourceNIMASRadioButtonMenuItem.isSelected() && renderFormatPDFRadioButtonMenuItem.isSelected()) {
                //parse OPF
                OPFReader opfReader = new OPFReader(sourceFile);
                String nimasID = opfReader.getIdentifier();
                File xmlfile = new File(sourceDir, opfReader.getDaisyXML());
                XMLConverter xc = new XMLConverter(xmlfile, xsltfile);
                File pdffile = new File(sourceDir, nimasID + ".pdf");
                //do FOP transformation
                publish("Transforming. Please wait...");
                try {
                    xc.convertXML2PDF(opfReader, pdffile);
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                } catch (TransformerException e) {
                    logger.severe(e.getMessage());
                } catch (FOPException e) {
                    logger.severe(e.getMessage());
                } finally {
                    publish("Transformation complete.");
                    transformButton.setEnabled(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return null;
                }
            //XSL-FO to PDF
            } else if (sourceXSLFORadioButtonMenuItem.isSelected() && renderFormatPDFRadioButtonMenuItem.isSelected()) {
                //parse OPF
                OPFReader opfReader = new OPFReader(sourceFile);
                String nimasID = opfReader.getIdentifier();
                File xmlfile = new File(sourceDir, nimasID + ".fo");
                XMLConverter xc = new XMLConverter(xmlfile, xsltfile);
                File fofile = new File(sourceDir, nimasID + ".pdf");
                //do FOP transformation
                publish("Transforming. Please wait...");
                try {
                    xc.convertFO2PDF(opfReader, fofile);
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                } catch (FOPException e) {
                    logger.severe(e.getMessage());
                } finally {
                    publish("Transformation complete.");
                    transformButton.setEnabled(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return null;
                }
            }
            transformButton.setEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return null;
        }

        @Override
        protected void process(List<String> messages) {
            String status = messages.get(messages.size() - 1);
            statusLabel.setText(status);
        }
    }

    private void chooseSourceXMLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseSourceXMLButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        FilterOPF opff = new FilterOPF();
        jfc.setFileFilter(opff);
        jfc.setAcceptAllFileFilterUsed(false);
        int result = jfc.showOpenDialog(jPanelMain);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            sourceXMLTextField.setText(file.getAbsolutePath());
        } else {
            return;
        }
    }//GEN-LAST:event_chooseSourceXMLButtonActionPerformed

    @SuppressWarnings({"static-access", "static-access"})
    private void transformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transformButtonActionPerformed
        resultTextArea.setText("");
        transformButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TransformTask transformation = new TransformTask();
        transformation.execute();
    }//GEN-LAST:event_transformButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        dispose();
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void fileExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileExitMenuItemActionPerformed
        dispose();
        System.exit(0);
    }//GEN-LAST:event_fileExitMenuItemActionPerformed

    private void optionsRenderFormatMenuMenuDeselected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_optionsRenderFormatMenuMenuDeselected
    }//GEN-LAST:event_optionsRenderFormatMenuMenuDeselected

    private void sourceNIMASRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceNIMASRadioButtonMenuItemActionPerformed

        renderFormatPDFRadioButtonMenuItem.setSelected(true);
        renderFormatXSLFORadioButtonMenuItem.setEnabled(true);
    }//GEN-LAST:event_sourceNIMASRadioButtonMenuItemActionPerformed

    private void sourceXSLFORadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceXSLFORadioButtonMenuItemActionPerformed

        renderFormatPDFRadioButtonMenuItem.setSelected(true);
        renderFormatXSLFORadioButtonMenuItem.setSelected(false);
        renderFormatXSLFORadioButtonMenuItem.setEnabled(false);
    }//GEN-LAST:event_sourceXSLFORadioButtonMenuItemActionPerformed
    private void renderFormatXSLFORadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renderFormatXSLFORadioButtonMenuItemActionPerformed

    }//GEN-LAST:event_renderFormatXSLFORadioButtonMenuItemActionPerformed
    private void renderFormatPDFRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renderFormatPDFRadioButtonMenuItemActionPerformed

    }//GEN-LAST:event_renderFormatPDFRadioButtonMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrameOptions of = new FrameOptions();
        of.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemShrinkImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShrinkImagesActionPerformed
        FrameImageResize ir= new FrameImageResize();
        ir.setVisible(true);
    }//GEN-LAST:event_jMenuItemShrinkImagesActionPerformed

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMain().setVisible(true);
            }
        });
    }

    /**
     *
     */
    protected static void setupJdkLoggerHandler() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("handlers = java.util.logging.FileHandler, org.eightfoldconsulting.nimas2pdf.TextAreaHandler");
        buffer.append("\n");
        buffer.append("java.util.logging.FileHandler.pattern = %h/.nimas2pdf/logs/nimas2pdf_log_%g.xml");
        buffer.append("\n");
        buffer.append("java.util.logging.FileHandler.limit = 200000");
        buffer.append("\n");
        buffer.append("java.util.logging.FileHandler.count = 10");
        buffer.append("\n");
        buffer.append("java.util.logging.FileHandler.formatter = java.util.logging.XMLFormatter");
        buffer.append("\n");
        buffer.append(".level= INFO");
        buffer.append("\n");
        buffer.append("FOP.level = INFO");
        buffer.append("\n");
        buffer.append("org.apache.fop.level = INFO");
        buffer.append("\n");
        buffer.append("org.apache.fop.render.level = INFO");
        buffer.append("\n");


        try {
            java.util.logging.LogManager.getLogManager().readConfiguration(
                    new ByteArrayInputStream(buffer.toString().getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseSourceXMLButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem fileExitMenuItem;
    private javax.swing.JMenuBar jMenuBarMain;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemShrinkImages;
    private javax.swing.JMenu jMenuOptions;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JMenu optionsRenderFormatMenu;
    private javax.swing.JMenu optionsSourceFormatMenu;
    private javax.swing.JRadioButtonMenuItem renderFormatPDFRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem renderFormatXSLFORadioButtonMenuItem;
    private javax.swing.ButtonGroup renderFormatbuttonGroup;
    private javax.swing.JLabel resultLabel;
    private javax.swing.JScrollPane resultScrollPane;
    private javax.swing.JTextArea resultTextArea;
    private javax.swing.ButtonGroup sourceFormatButtonGroup;
    private javax.swing.JRadioButtonMenuItem sourceNIMASRadioButtonMenuItem;
    private javax.swing.JLabel sourceXMLLabel;
    private javax.swing.JTextField sourceXMLTextField;
    private javax.swing.JRadioButtonMenuItem sourceXSLFORadioButtonMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton transformButton;
    // End of variables declaration//GEN-END:variables

}
