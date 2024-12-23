import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    JTextField usernameField;
    JTextField emailField;
    JTextField contactField;
    JPasswordField passwordField;
    JButton registerButton;

    public RegisterFrame() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400); // Ukuran frame lebih besar untuk tata letak lebih nyaman

        // Center the frame
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.decode("#23424A")); // Background utama

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#57A5B8")); // Warna header
        JLabel headerLabel = new JLabel("Register New Account");
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 18)); // Ukuran font header lebih besar
        headerLabel.setForeground(Color.decode("#E1EBEC")); // Teks header
        headerPanel.add(headerLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.decode("#B4D4DC")); // Latar belakang form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Tambahkan jarak antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Ukuran font lebih besar
        usernameLabel.setForeground(Color.decode("#23424A")); // Warna teks

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14)); // Ukuran font input lebih besar

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(Color.decode("#23424A"));

        emailField = new JTextField(15);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contactLabel.setForeground(Color.decode("#23424A"));

        contactField = new JTextField(15);
        contactField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.decode("#23424A"));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(contactLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.decode("#23424A")); // Latar belakang tombol

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(Color.decode("#57A5B8")); // Warna tombol utama
        registerButton.setForeground(Color.decode("#E1EBEC")); // Warna teks tombol
        registerButton.setPreferredSize(new Dimension(150, 40)); // Ukuran tombol lebih besar

        buttonPanel.add(registerButton);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Action Listener
        registerButton.addActionListener(e -> handleRegister());

        setVisible(true);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String contact = contactField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            EmployeeDatabase.addEmployee(new Employee(username, email, contact, password, "john", "password123", "john.doe@example.com", "1234567890"));
            JOptionPane.showMessageDialog(this, "Registration successful!");
            new LoginFrame();
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
