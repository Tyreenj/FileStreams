import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class RandProductMaker extends JFrame {
    private JTextField nameField, descriptionField, idField, costField, recordCountField;
    private JButton addButton, quitButton;
    private int recordCount = 0;
    private RandomAccessFile randomFile;
    private String filePath;

    public RandProductMaker() {
        if (!selectFile()) {
            System.exit(0);
            return;
        }

        setTitle("Random Access Product Maker - " + new File(filePath).getName());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Product Name (max 35 chars):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField(35);
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Description (max 75 chars):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        descriptionField = new JTextField(35);
        mainPanel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Product ID (6 chars):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        idField = new JTextField(35);
        mainPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Cost:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        costField = new JTextField(35);
        mainPanel.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Record Count:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        recordCountField = new JTextField("0", 35);
        recordCountField.setEditable(false);
        recordCountField.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(recordCountField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Product");
        quitButton = new JButton("Quit");
        buttonPanel.add(addButton);
        buttonPanel.add(quitButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAndExit();
            }
        });

        setVisible(true);
    }

    private boolean selectFile() {
        JFileChooser chooser = new JFileChooser();
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        chooser.setDialogTitle("Select or Create Product Data File");
        chooser.setSelectedFile(new File("ProductData.dat"));

        try {
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".dat")) {
                    filePath += ".dat";
                }

                randomFile = new RandomAccessFile(filePath, "rw");
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Sorry, you must select a file! Terminating!",
                        "No File Selected", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error opening file: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    private void addProduct() {
        if (nameField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                idField.getText().trim().isEmpty() ||
                costField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double cost;
        try {
            cost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cost must be a valid number!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String id = idField.getText().trim();

            Product product = new Product(name, description, id, cost);

            randomFile.writeUTF(product.getNameForRandom());
            randomFile.writeUTF(product.getDescriptionForRandom());
            randomFile.writeUTF(product.getProductIDForRandom());
            randomFile.writeDouble(product.getCost());

            recordCount++;
            recordCountField.setText(String.valueOf(recordCount));

            nameField.setText("");
            descriptionField.setText("");
            idField.setText("");
            costField.setText("");
            nameField.requestFocus();

            JOptionPane.showMessageDialog(this, "Product added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeAndExit() {
        try {
            if (randomFile != null) {
                randomFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductMaker();
            }
        });
    }
}