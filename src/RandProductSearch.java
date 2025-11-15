import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;

public class RandProductSearch extends JFrame {
    private JTextField searchField;
    private JTextArea resultsArea;
    private JButton searchButton, quitButton;
    private String filePath;

    public RandProductSearch() {
        if (!selectFile()) {
            System.exit(0);
            return;
        }

        setTitle("Random Access Product Search - " + new File(filePath).getName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Product Name:"));
        searchField = new JTextField(30);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        quitButton = new JButton("Quit");
        searchPanel.add(quitButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });

        setVisible(true);
    }

    private boolean selectFile() {
        JFileChooser chooser = new JFileChooser();
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        chooser.setDialogTitle("Select Product Data File to Search");

        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();

                if (!selectedFile.exists()) {
                    JOptionPane.showMessageDialog(null,
                            "File does not exist!",
                            "File Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Sorry, you must select a file! Terminating!",
                        "No File Selected", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error selecting file: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        resultsArea.setText("");
        ArrayList<Product> matchingProducts = new ArrayList<>();

        try (RandomAccessFile randomFile = new RandomAccessFile(filePath, "r")) {
            while (randomFile.getFilePointer() < randomFile.length()) {
                try {
                    String name = randomFile.readUTF().trim();
                    String description = randomFile.readUTF().trim();
                    String id = randomFile.readUTF().trim();
                    double cost = randomFile.readDouble();

                    if (name.toLowerCase().contains(searchTerm)) {
                        Product product = new Product(name, description, id, cost);
                        matchingProducts.add(product);
                    }
                } catch (EOFException e) {
                    break;
                }
            }

            if (matchingProducts.isEmpty()) {
                resultsArea.setText("No products found matching: " + searchTerm);
            } else {
                StringBuilder results = new StringBuilder();
                results.append("Found ").append(matchingProducts.size())
                        .append(" product(s) matching: ").append(searchTerm).append("\n");
                results.append("=".repeat(80)).append("\n\n");

                for (Product product : matchingProducts) {
                    results.append(String.format("Name:        %s\n", product.getName()));
                    results.append(String.format("Description: %s\n", product.getDescription()));
                    results.append(String.format("ID:          %s\n", product.getProductID()));
                    results.append(String.format("Cost:        $%.2f\n", product.getCost()));
                    results.append("-".repeat(80)).append("\n\n");
                }

                resultsArea.setText(results.toString());
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading file: " + e.getMessage() + "\n" +
                            "Make sure you have created products using RandProductMaker first!",
                    "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductSearch();
            }
        });
    }
}