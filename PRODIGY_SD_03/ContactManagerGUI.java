import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ContactManagerGUI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> contactJList;
    private java.util.List<String[]> contacts;
    private final String FILE_NAME = "contacts.txt";

    public ContactManagerGUI() {
        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contacts = new ArrayList<>();
        listModel = new DefaultListModel<>();
        contactJList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(contactJList);

        loadContactsFromFile();

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addContact());
        editButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());
    }

    private void addContact() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] message = {
            "Name:", nameField,
            "Phone:", phoneField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Contact", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                String[] contact = {name, phone, email};
                contacts.add(contact);
                listModel.addElement(formatListEntry(contact));
                saveContactsToFile();
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required!");
            }
        }
    }

    private void editContact() {
        int index = contactJList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a contact to edit.");
            return;
        }

        String[] contact = contacts.get(index);

        JTextField nameField = new JTextField(contact[0]);
        JTextField phoneField = new JTextField(contact[1]);
        JTextField emailField = new JTextField(contact[2]);

        Object[] message = {
            "Name:", nameField,
            "Phone:", phoneField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Contact", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            contact[0] = nameField.getText().trim();
            contact[1] = phoneField.getText().trim();
            contact[2] = emailField.getText().trim();

            listModel.set(index, formatListEntry(contact));
            saveContactsToFile();
        }
    }

    private void deleteContact() {
        int index = contactJList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a contact to delete.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this contact?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            contacts.remove(index);
            listModel.remove(index);
            saveContactsToFile();
        }
    }

    private void loadContactsFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                contacts.clear();
                listModel.clear();
                boolean skipHeader = true;

                while ((line = br.readLine()) != null) {
                    if (skipHeader || line.trim().startsWith("-")) {
                        skipHeader = false;
                        continue; // skip headers
                    }

                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        String name = parts[0].trim();
                        String phone = parts[1].trim();
                        String email = parts[2].trim();
                        String[] contact = {name, phone, email};
                        contacts.add(contact);
                        listModel.addElement(formatListEntry(contact));
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading contacts.");
            }
        }
    }

    private void saveContactsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.printf("%-20s | %-15s | %-30s%n", "Name", "Phone", "Email");
            pw.println("---------------------+-----------------+------------------------------");
            for (String[] contact : contacts) {
                pw.printf("%-20s | %-15s | %-30s%n", contact[0], contact[1], contact[2]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving contacts.");
        }
    }

    private String formatListEntry(String[] contact) {
        return contact[0] + " | " + contact[1] + " | " + contact[2];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ContactManagerGUI().setVisible(true);
        });
    }
}
