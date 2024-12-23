import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginFrame extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    private JButton registerButton;
    JComboBox<String> roleComboBox;
    private String statusMessage;

    public String getStatusMessage() {
        return statusMessage;
    }

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400); // Perbesar ukuran frame untuk tata letak lebih nyaman

        // Center the frame
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        mainPanel.setBackground(Color.decode("#1E1E2F")); // Background utama dengan tema modern gelap

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#282A36")); // Warna header
        JLabel headerLabel = new JLabel("Employee Management App");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Font header modern
        headerLabel.setForeground(Color.decode("#FFFFFF")); // Teks header putih
        headerPanel.add(headerLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.decode("#1E1E2F")); // Latar belakang form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Tambahkan jarak antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font modern
        usernameLabel.setForeground(Color.decode("#F8F8F2")); // Warna teks terang

        usernameField = new JTextField(15); // Kolom input lebih besar
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(Color.decode("#282A36"));
        usernameField.setForeground(Color.decode("#F8F8F2"));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.decode("#6272A4")));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font modern
        passwordLabel.setForeground(Color.decode("#F8F8F2")); // Warna teks terang

        passwordField = new JPasswordField(15); // Kolom input lebih besar
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBackground(Color.decode("#282A36"));
        passwordField.setForeground(Color.decode("#F8F8F2"));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.decode("#6272A4")));

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font modern
        roleLabel.setForeground(Color.decode("#F8F8F2")); // Warna teks terang

        roleComboBox = new JComboBox<>(new String[]{"Admin", "Employee"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleComboBox.setBackground(Color.decode("#282A36"));
        roleComboBox.setForeground(Color.decode("#F8F8F2"));
        roleComboBox.setBorder(BorderFactory.createLineBorder(Color.decode("#6272A4")));
        roleComboBox.setPreferredSize(new Dimension(200, 35)); // Ukuran dropdown lebih besar

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(roleComboBox, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Tambahkan jarak antar tombol
        buttonPanel.setBackground(Color.decode("#1E1E2F")); // Warna latar belakang untuk tombol

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font tombol modern
        loginButton.setBackground(Color.decode("#6272A4")); // Warna tombol utama
        loginButton.setForeground(Color.decode("#FFFFFF")); // Teks tombol utama
        loginButton.setPreferredSize(new Dimension(150, 40)); // Ukuran tombol lebih besar

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font tombol modern
        registerButton.setBackground(Color.decode("#50FA7B")); // Warna tombol tambahan
        registerButton.setForeground(Color.decode("#1E1E2F")); // Teks tombol tambahan
        registerButton.setPreferredSize(new Dimension(150, 40)); // Ukuran tombol lebih besar

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        setVisible(true);
    }

    void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            statusMessage = "Username and Password cannot be empty!";
            JOptionPane.showMessageDialog(this, statusMessage, "Error", JOptionPane.ERROR_MESSAGE);
        } else if (role.equals("Admin") && username.equals("admin") && password.equals("admin")) {
            statusMessage = "Admin Dashboard loaded";
            new AdminDashboard();
            dispose();
        } else if (role.equals("Employee")) {
            ArrayList<Employee> employees = EmployeeDatabase.getEmployees(); // Simulasikan EmployeeDatabase
            if (EmployeeDatabase.login(username, password)) {
                statusMessage = "Employee Dashboard loaded";
                new EmployeeDashboard(username, employees);
                dispose();
            } else {
                statusMessage = "Invalid username or password!";
                JOptionPane.showMessageDialog(this, statusMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            statusMessage = "Invalid login credentials!";
            JOptionPane.showMessageDialog(this, statusMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void handleRegister() {
        new RegisterFrame();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
