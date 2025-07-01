import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TemperatureConverterGUI extends JFrame implements ActionListener {

    private JTextField inputField;
    private JComboBox<String> unitSelector;
    private JLabel resultLabel1, resultLabel2, iconLabel;
    private JButton convertButton, resetButton;

    public TemperatureConverterGUI() {
        setTitle("Temperature Converter");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().setBackground(new Color(245, 245, 245)); // Light background

        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font resultFont = new Font("Arial", Font.BOLD, 16);

        // Title
        JLabel titleLabel = new JLabel("Temperature Converter");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(titleLabel, gbc);

        // Image Icon
        ImageIcon tempIcon = new ImageIcon("temperature.png"); // Place an image named 'temperature.png' in your project directory
        iconLabel = new JLabel(tempIcon);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(iconLabel, gbc);

        // Input label
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel inputLabel = new JLabel("Enter Temperature:");
        inputLabel.setFont(labelFont);
        add(inputLabel, gbc);

        // Input field
        inputField = new JTextField(10);
        inputField.setFont(labelFont);
        gbc.gridx = 1;
        add(inputField, gbc);

        // Input validation (only numbers)
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validateInput(); }
            public void removeUpdate(DocumentEvent e) { validateInput(); }
            public void changedUpdate(DocumentEvent e) { validateInput(); }

            private void validateInput() {
                String text = inputField.getText();
                if (!text.matches("-?\\d*(\\.\\d*)?")) {
                    inputField.setText(text.replaceAll("[^\\d.-]", ""));
                }
            }
        });

        // Unit selector label
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel unitLabel = new JLabel("Select Unit:");
        unitLabel.setFont(labelFont);
        add(unitLabel, gbc);

        // Unit selector
        String[] units = {"Celsius", "Fahrenheit", "Kelvin"};
        unitSelector = new JComboBox<>(units);
        unitSelector.setFont(labelFont);
        gbc.gridx = 1;
        add(unitSelector, gbc);

        // Convert button
        convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(52, 152, 219));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setFont(new Font("Arial", Font.BOLD, 16));
        convertButton.addActionListener(this);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(convertButton, gbc);

        // Reset button
        resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(231, 76, 60));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(e -> resetFields());

        gbc.gridx = 1;
        add(resetButton, gbc);

        // Result labels
        resultLabel1 = new JLabel("");
        resultLabel1.setFont(resultFont);
        resultLabel1.setForeground(new Color(39, 174, 96));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(resultLabel1, gbc);

        resultLabel2 = new JLabel("");
        resultLabel2.setFont(resultFont);
        resultLabel2.setForeground(new Color(39, 174, 96));
        gbc.gridy = 6;
        add(resultLabel2, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double inputTemp = Double.parseDouble(inputField.getText());
            String selectedUnit = (String) unitSelector.getSelectedItem();

            if (selectedUnit.equals("Celsius")) {
                double fahrenheit = (inputTemp * 9 / 5) + 32;
                double kelvin = inputTemp + 273.15;
                resultLabel1.setText(String.format("Fahrenheit: %.2f °F", fahrenheit));
                resultLabel2.setText(String.format("Kelvin: %.2f K", kelvin));
            } else if (selectedUnit.equals("Fahrenheit")) {
                double celsius = (inputTemp - 32) * 5 / 9;
                double kelvin = (inputTemp - 32) * 5 / 9 + 273.15;
                resultLabel1.setText(String.format("Celsius: %.2f °C", celsius));
                resultLabel2.setText(String.format("Kelvin: %.2f K", kelvin));
            } else if (selectedUnit.equals("Kelvin")) {
                double celsius = inputTemp - 273.15;
                double fahrenheit = (inputTemp - 273.15) * 9 / 5 + 32;
                resultLabel1.setText(String.format("Celsius: %.2f °C", celsius));
                resultLabel2.setText(String.format("Fahrenheit: %.2f °F", fahrenheit));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        inputField.setText("");
        resultLabel1.setText("");
        resultLabel2.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TemperatureConverterGUI());
    }
}
