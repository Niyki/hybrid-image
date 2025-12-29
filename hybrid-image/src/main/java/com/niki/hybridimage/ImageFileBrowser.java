package com.niki.hybridimage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;


public class ImageFileBrowser extends JFrame {
    private JTextField lowPassField;
    private JTextField highPassField;
    private final JButton createHybridButton;
    
    public ImageFileBrowser() {
        setTitle("Hybrid Image");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(700, 200);
        
        // UI background colour
        getContentPane().setBackground(new Color(12, 12, 12));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Low Pass row
        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lowPassLabel = new JLabel("Low Pass:");
        lowPassLabel.setForeground(new Color(240, 240, 240));
        lowPassLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(lowPassLabel, gbc);
        
        // Textbox
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lowPassField = new JTextField();
        lowPassField.setEditable(false);
        lowPassField.setPreferredSize(new Dimension(300, 30));
        lowPassField.setBackground(new Color(25, 25, 25));
        lowPassField.setForeground(new Color(150, 150, 150));
        lowPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        add(lowPassField, gbc);
        
        // Button
        gbc.gridx = 2;
        gbc.weightx = 0;
        JButton lowPassButton = new JButton("Browse");
        lowPassButton.setBackground(new Color(70, 130, 180));
        lowPassButton.setForeground(Color.WHITE);
        lowPassButton.setFocusPainted(false);
        lowPassButton.setFont(new Font("Arial", Font.BOLD, 16));
        lowPassButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        lowPassButton.addActionListener(e -> browseForImage(lowPassField));
        add(lowPassButton, gbc);
        
        // High Pass row
        // Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel highPassLabel = new JLabel("High Pass:");
        highPassLabel.setForeground(new Color(240, 240, 240));
        highPassLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(highPassLabel, gbc);
        
        // Textbox
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        highPassField = new JTextField();
        highPassField.setEditable(false);
        highPassField.setPreferredSize(new Dimension(300, 30));
        highPassField.setBackground(new Color(25, 25, 25));
        highPassField.setForeground(new Color(150, 150, 150));
        highPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        add(highPassField, gbc);
        
        // Button
        gbc.gridx = 2;
        gbc.weightx = 0;
        JButton highPassButton = new JButton("Browse");
        highPassButton.setBackground(new Color(70, 130, 180));
        highPassButton.setForeground(Color.WHITE);
        highPassButton.setFocusPainted(false);
        highPassButton.setFont(new Font("Arial", Font.BOLD, 16));
        highPassButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        highPassButton.addActionListener(e -> browseForImage(highPassField));
        add(highPassButton, gbc);
        
        // Create Hybrid Button row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 10, 5, 10);
        createHybridButton = new JButton("Create Hybrid Image");
        createHybridButton.setBackground(new Color(34, 139, 34));
        createHybridButton.setForeground(Color.WHITE);
        createHybridButton.setFocusPainted(false);
        createHybridButton.setFont(new Font("Arial", Font.BOLD, 16));
        createHybridButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        createHybridButton.addActionListener(e -> createHybridImage());
        add(createHybridButton, gbc);
        
        setLocationRelativeTo(null);
    }
    
    // Open file dialog
    private void browseForImage(JTextField targetField) {
        System.setProperty("apple.awt.fileDialogForDirectories", "false");
        
        FileDialog fileDialog = new FileDialog(this, "Select Image File", FileDialog.LOAD);
        
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            fileDialog.setFile("*.jpg;*.jpeg;*.png;*.gif;*.bmp;*.tif;*.tiff");
        } else {
            fileDialog.setFilenameFilter((dir, name) -> {
                String lowerName = name.toLowerCase();
                return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") ||
                       lowerName.endsWith(".png") || lowerName.endsWith(".gif") ||
                       lowerName.endsWith(".bmp") || lowerName.endsWith(".tif") ||
                       lowerName.endsWith(".tiff");
            });
        }
        
        fileDialog.setVisible(true);
        
        String fileName = fileDialog.getFile();
        String directory = fileDialog.getDirectory();
        
        if (fileName != null && directory != null) {
            File selectedFile = new File(directory, fileName);
            targetField.setText(selectedFile.getAbsolutePath());
        }
    }
    
    // Create hybrid image from selected files
    private void createHybridImage() {
        String lowPassPath = lowPassField.getText();
        String highPassPath = highPassField.getText();
        
        // Validate that both images are selected
        if (lowPassPath.isEmpty() || highPassPath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select both low-pass and high-pass images.",
                "Missing Images",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Load the images
            File lowImageFile = new File(lowPassPath);
            MBFImage lowImage = ImageUtilities.readMBF(lowImageFile);
            DisplayUtilities.display(lowImage, "Low-Pass Image");
            
            File highImageFile = new File(highPassPath);
            MBFImage highImage = ImageUtilities.readMBF(highImageFile);
            DisplayUtilities.display(highImage, "High-Pass Image");
            
            // Create and display hybrid image
            MBFImage hybridImage = MyHybridImages.makeHybrid(lowImage, 2f, highImage, 2.5f);
            DisplayUtilities.display(hybridImage, "Hybrid Image");
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading images: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error creating hybrid image: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageFileBrowser frame = new ImageFileBrowser();
            frame.setVisible(true);
        });
    }
}