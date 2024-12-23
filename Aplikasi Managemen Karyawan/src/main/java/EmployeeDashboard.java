import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Image;
import javax.swing.ImageIcon;

public class EmployeeDashboard extends JFrame {
    private String loggedInUsername;
    private ArrayList<Employee> employeesData;

    public EmployeeDashboard(String username, ArrayList<Employee> employees) {
        this.loggedInUsername = username;
        this.employeesData = employees;

        setTitle("Employee Dashboard - Welcome " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450); // Ukuran ditingkatkan untuk memberikan ruang pada header
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#57A5B8")); // Warna header
        JLabel headerLabel = new JLabel("Employee Dashboard");
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        headerLabel.setForeground(Color.decode("#E1EBEC")); // Warna teks header
        headerPanel.add(headerLabel);

        // Pengaturan GUI utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.setBackground(Color.decode("#23424A")); // Warna latar belakang utama
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Buat tombol dengan tema warna baru
        JButton profileButton = createStyledButton("Profile");
        JButton leaveButton = createStyledButton("Leave Requests");
        JButton attendanceButton = createStyledButton("Attendance & Schedule");
        JButton salaryButton = createStyledButton("Salary & Payslips");

        // Tambahkan action listeners ke tombol
        profileButton.addActionListener(e -> openProfile());
        leaveButton.addActionListener(e -> openLeaveRequests());
        attendanceButton.addActionListener(e -> openAttendanceSchedule());
        salaryButton.addActionListener(e -> openSalaryPayslips());

        // Tambahkan tombol ke panel utama
        mainPanel.add(profileButton);
        mainPanel.add(leaveButton);
        mainPanel.add(attendanceButton);
        mainPanel.add(salaryButton);

        // Tambahkan header panel dan main panel ke frame
        setLayout(new BorderLayout()); // Menggunakan BorderLayout untuk tata letak
        add(headerPanel, BorderLayout.NORTH); // Header di bagian atas
        add(mainPanel, BorderLayout.CENTER); // Panel utama di tengah

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Ukuran font tombol
        button.setBackground(Color.decode("#57A5B8")); // Warna latar belakang tombol
        button.setForeground(Color.decode("#E1EBEC")); // Warna teks tombol
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding pada tombol
        return button;
    }


    private void openProfile() {
        // Cari employee berdasarkan loggedInUsername
        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No logged-in user found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee currentEmployee = employeesData.stream()
                .filter(emp -> emp.getUsername().equalsIgnoreCase(loggedInUsername))
                .findFirst()
                .orElse(null);

        // Jika tidak ditemukan, tampilkan pesan error
        if (currentEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee data not found for username: " + loggedInUsername, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat frame untuk profil
        JFrame profileFrame = new JFrame("Profile");
        profileFrame.setSize(700, 600); // Perbesar ukuran frame
        profileFrame.setLocationRelativeTo(null); // Center the frame
        profileFrame.setLayout(new BorderLayout(10, 10)); // BorderLayout untuk tata letak fleksibel

        // Panel untuk gambar
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        imagePanel.setBackground(Color.decode("#B4D4DC"));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton chooseImageButton = new JButton("Choose Image");
        if (currentEmployee.getImagePath() != null && !currentEmployee.getImagePath().isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(currentEmployee.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
            imageLabel.setIcon(imageIcon);
        }

        chooseImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
                imageLabel.setIcon(imageIcon);
                currentEmployee.setImagePath(filePath); // Assume Employee class has a field for storing image path
            }
        });

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(chooseImageButton, BorderLayout.SOUTH);

        // Panel untuk Form Input
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Padding untuk form
        formPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang form panel

        JLabel nameLabel = createStyledLabel("Name:");
        JTextField nameField = createStyledTextField(currentEmployee.getName());
        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField(currentEmployee.getEmail());
        JLabel roleLabel = createStyledLabel("Role:");
        JTextField roleField = createStyledTextField(currentEmployee.getRole());
        roleField.setEditable(false); // Tidak bisa diubah
        JLabel departmentLabel = createStyledLabel("Department:");
        JTextField departmentField = createStyledTextField(currentEmployee.getDepartment());
        departmentField.setEditable(false); // Tidak bisa diubah
        JLabel contactLabel = createStyledLabel("Contact:");
        JTextField contactField = createStyledTextField(currentEmployee.getContact());

        // Set ukuran textField dan label agar seragam
        Dimension fieldDimension = new Dimension(300, 30);
        Dimension labelDimension = new Dimension(100, 30);

        nameField.setPreferredSize(fieldDimension);
        emailField.setPreferredSize(fieldDimension);
        roleField.setPreferredSize(fieldDimension);
        departmentField.setPreferredSize(fieldDimension);
        contactField.setPreferredSize(fieldDimension);

        nameLabel.setPreferredSize(labelDimension);
        emailLabel.setPreferredSize(labelDimension);
        roleLabel.setPreferredSize(labelDimension);
        departmentLabel.setPreferredSize(labelDimension);
        contactLabel.setPreferredSize(labelDimension);

        // Tambahkan field ke panel form
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(roleLabel);
        formPanel.add(roleField);
        formPanel.add(departmentLabel);
        formPanel.add(departmentField);
        formPanel.add(contactLabel);
        formPanel.add(contactField);

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.decode("#23424A")); // Warna latar belakang panel tombol
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        // Tambahkan logika tombol
        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newContact = contactField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newContact.isEmpty()) {
                JOptionPane.showMessageDialog(profileFrame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi format email
            if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                JOptionPane.showMessageDialog(profileFrame, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Perbarui data karyawan
            currentEmployee.setName(newName);
            currentEmployee.setEmail(newEmail);
            currentEmployee.setContact(newContact);

            JOptionPane.showMessageDialog(profileFrame, "Profile updated successfully!");
            profileFrame.dispose();
        });

        cancelButton.addActionListener(e -> profileFrame.dispose());

        // Tambahkan tombol ke panel
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Tambahkan panel ke frame
        profileFrame.add(imagePanel, BorderLayout.NORTH);
        profileFrame.add(formPanel, BorderLayout.CENTER);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        profileFrame.getContentPane().setBackground(Color.decode("#E1EBEC")); // Warna latar belakang frame
        profileFrame.setVisible(true);
    }


    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.decode("#23424A")); // Warna teks label
        return label;
    }

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang text field
        textField.setForeground(Color.decode("#23424A")); // Warna teks text field
        return textField;
    }



    private void openLeaveRequests() {
        JFrame leaveFrame = new JFrame("Leave Requests");
        leaveFrame.setSize(600, 550); // Tambahkan ruang untuk tombol Back
        leaveFrame.setLocationRelativeTo(null); // Center the frame
        leaveFrame.setLayout(new BorderLayout());
        leaveFrame.getContentPane().setBackground(Color.decode("#E1EBEC")); // Warna latar belakang utama

        // Panel Formulir Pengajuan
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Submit Leave Request"));
        formPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang form panel

        JLabel nameLabel = createStyledLabel("Employee Name:");
        JTextField nameField = createStyledTextField(loggedInUsername); // Autofill name
        nameField.setEditable(false);

        JLabel leaveTypeLabel = createStyledLabel("Leave Type:");
        JComboBox<String> leaveTypeComboBox = new JComboBox<>(new String[]{"Annual Leave", "Sick Leave", "Emergency Leave"});
        leaveTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        leaveTypeComboBox.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang combo box
        leaveTypeComboBox.setForeground(Color.decode("#23424A")); // Warna teks combo box

        JLabel startDateLabel = createStyledLabel("Start Date:");
        JTextField startDateField = createStyledTextField("");

        JLabel endDateLabel = createStyledLabel("End Date:");
        JTextField endDateField = createStyledTextField("");

        JLabel reasonLabel = createStyledLabel("Reason:");
        JTextField reasonField = createStyledTextField("");

        JButton uploadButton = createStyledButton("Upload File");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(leaveTypeLabel);
        formPanel.add(leaveTypeComboBox);
        formPanel.add(startDateLabel);
        formPanel.add(startDateField);
        formPanel.add(endDateLabel);
        formPanel.add(endDateField);
        formPanel.add(reasonLabel);
        formPanel.add(reasonField);
        formPanel.add(createStyledLabel("Attachment:"));
        formPanel.add(uploadButton);

        // Panel Riwayat Pengajuan
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Leave Request History"));
        historyPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang history panel

        JTextArea historyArea = new JTextArea("ID\tDate\tType\tStatus\tRemarks\n"); // Header untuk riwayat
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 12));
        historyArea.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang text area
        historyArea.setForeground(Color.decode("#23424A")); // Warna teks text area

        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        // Panel Tombol Submit dan Back
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#23424A")); // Warna latar belakang panel tombol
        JButton submitButton = createStyledButton("Submit");
        JButton backButton = createStyledButton("Back");

        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        // Logika tombol upload file
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(leaveFrame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(leaveFrame, "File uploaded: " + fileChooser.getSelectedFile().getName());
            }
        });

        // Logika tombol submit
        submitButton.addActionListener(e -> {
            String leaveType = leaveTypeComboBox.getSelectedItem().toString();
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            String reason = reasonField.getText();

            if (startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty()) {
                JOptionPane.showMessageDialog(leaveFrame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simulasi penyimpanan pengajuan ke database
            String requestId = "REQ" + (int) (Math.random() * 10000);
            String historyEntry = String.format("%s\t%s\t%s\tPending\t-\n", requestId, startDate, leaveType);
            historyArea.append(historyEntry);

            JOptionPane.showMessageDialog(leaveFrame, "Leave request submitted successfully with ID: " + requestId);
        });

        // Logika tombol back
        backButton.addActionListener(e -> leaveFrame.dispose());

        // Tambahkan panel ke frame
        leaveFrame.add(formPanel, BorderLayout.NORTH);
        leaveFrame.add(historyPanel, BorderLayout.CENTER);
        leaveFrame.add(buttonPanel, BorderLayout.SOUTH);

        leaveFrame.setVisible(true);
    }






    private void openAttendanceSchedule() {
        JFrame attendanceFrame = new JFrame("Attendance & Schedule");
        attendanceFrame.setSize(700, 500);
        attendanceFrame.setLocationRelativeTo(null); // Center the frame
        attendanceFrame.setLayout(new BorderLayout());
        attendanceFrame.getContentPane().setBackground(Color.decode("#E1EBEC")); // Warna latar belakang utama

        // Tabbed Pane untuk dua fitur
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang tabbed pane
        tabbedPane.setForeground(Color.decode("#23424A")); // Warna teks tab

        // Panel untuk Lihat Jadwal Kerja
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.setBorder(BorderFactory.createTitledBorder("Work Schedule"));
        schedulePanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel

        String[] scheduleColumns = {"Date", "Shift", "Start Time", "End Time", "Location"};
        Object[][] scheduleData = {
                {"01/01/2023", "Morning", "8:00 AM", "4:00 PM", "Office"},
                {"02/01/2023", "Evening", "4:00 PM", "12:00 AM", "Office"},
                {"03/01/2023", "Night", "12:00 AM", "8:00 AM", "Factory"}
        };

        JTable scheduleTable = new JTable(scheduleData, scheduleColumns);
        scheduleTable.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang tabel
        scheduleTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 12)); // Font tabel
        scheduleTable.getTableHeader().setBackground(Color.decode("#57A5B8")); // Warna latar header tabel
        scheduleTable.getTableHeader().setForeground(Color.decode("#E1EBEC")); // Warna teks header tabel

        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTable);

        JButton syncButton = createStyledButton("Sync with Calendar");

        schedulePanel.add(scheduleScrollPane, BorderLayout.CENTER);
        schedulePanel.add(syncButton, BorderLayout.SOUTH);

        syncButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(attendanceFrame, "Work schedule synced with your calendar!");
        });

        // Panel untuk Lihat Rekap Kehadiran
        JPanel attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBorder(BorderFactory.createTitledBorder("Attendance Summary"));
        attendancePanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel

        String[] attendanceColumns = {"Date", "Check-In Time", "Check-Out Time", "Status", "Remarks"};
        Object[][] attendanceData = {
                {"01/01/2023", "8:00 AM", "4:00 PM", "On Time", "-"},
                {"02/01/2023", "8:30 AM", "4:00 PM", "Late", "Traffic Jam"},
                {"03/01/2023", "Absent", "Absent", "Absent", "Sick Leave"}
        };

        JTable attendanceTable = new JTable(attendanceData, attendanceColumns);
        attendanceTable.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang tabel
        attendanceTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 12)); // Font tabel
        attendanceTable.getTableHeader().setBackground(Color.decode("#57A5B8")); // Warna latar header tabel
        attendanceTable.getTableHeader().setForeground(Color.decode("#E1EBEC")); // Warna teks header tabel

        JScrollPane attendanceScrollPane = new JScrollPane(attendanceTable);

        JButton exportButton = createStyledButton("Export Attendance Report");

        attendancePanel.add(attendanceScrollPane, BorderLayout.CENTER);
        attendancePanel.add(exportButton, BorderLayout.SOUTH);

        // Logika Ekspor PDF
        exportButton.addActionListener(e -> {
            try {
                exportAttendanceToPDF(attendanceData, attendanceColumns);
                JOptionPane.showMessageDialog(attendanceFrame, "Attendance report exported successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(attendanceFrame, "Error exporting attendance report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tombol Back
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(Color.decode("#23424A")); // Warna latar belakang panel tombol
        JButton backButton = createStyledButton("Back");
        backPanel.add(backButton);

        backButton.addActionListener(e -> attendanceFrame.dispose());

        // Tambahkan panel ke TabbedPane
        tabbedPane.addTab("View Work Schedule", schedulePanel);
        tabbedPane.addTab("View Attendance Summary", attendancePanel);

        attendanceFrame.add(tabbedPane, BorderLayout.CENTER);
        attendanceFrame.add(backPanel, BorderLayout.SOUTH);

        attendanceFrame.setVisible(true);
    }


    private void exportAttendanceToPDF(Object[][] data, String[] columns) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("AttendanceReport.pdf"));
        document.open();

        // Judul
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Attendance Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Tabel
        PdfPTable table = new PdfPTable(columns.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Header
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Data
        com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Object[] row : data) {
            for (Object value : row) {
                PdfPCell cell = new PdfPCell(new Phrase(value.toString(), dataFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        document.add(table);
        document.close();
    }


    private void openSalaryPayslips() {
        JFrame salaryFrame = new JFrame("Salary & Payslips");
        salaryFrame.setSize(600, 400);
        salaryFrame.setLocationRelativeTo(null); // Center the frame
        salaryFrame.setLayout(new BorderLayout());
        salaryFrame.getContentPane().setBackground(Color.decode("#E1EBEC")); // Latar belakang utama frame

        // Panel untuk Tabel Riwayat Gaji
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Salary History"));
        historyPanel.setBackground(Color.decode("#B4D4DC")); // Latar belakang panel tabel

        String[] salaryColumns = {"Month", "Basic Salary", "Bonus", "Allowance", "Deductions", "Net Salary"};
        Object[][] salaryData = {
                {"January 2023", "$5000", "$300", "$200", "$100", "$5400"},
                {"February 2023", "$5000", "$400", "$200", "$150", "$5450"},
                {"March 2023", "$5000", "$250", "$200", "$200", "$5250"}
        };

        JTable salaryTable = new JTable(salaryData, salaryColumns);
        salaryTable.setBackground(Color.decode("#E1EBEC")); // Latar belakang tabel
        salaryTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        salaryTable.setFont(new Font("Arial", Font.PLAIN, 12)); // Font teks tabel
        salaryTable.getTableHeader().setBackground(Color.decode("#57A5B8")); // Latar belakang header tabel
        salaryTable.getTableHeader().setForeground(Color.decode("#E1EBEC")); // Warna teks header tabel
        salaryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12)); // Font header tabel

        JScrollPane salaryScrollPane = new JScrollPane(salaryTable);
        historyPanel.add(salaryScrollPane, BorderLayout.CENTER);

        // Panel untuk Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Latar belakang panel tombol

        JButton downloadButton = createStyledButton("Download Payslip");
        JButton backButton = createStyledButton("Back");

        buttonPanel.add(downloadButton);
        buttonPanel.add(backButton);

        // Logika Tombol Unduh Slip Gaji
        downloadButton.addActionListener(e -> {
            int selectedRow = salaryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(salaryFrame, "Please select a month to download the payslip.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String month = salaryTable.getValueAt(selectedRow, 0).toString();
                String basicSalary = salaryTable.getValueAt(selectedRow, 1).toString();
                String bonus = salaryTable.getValueAt(selectedRow, 2).toString();
                String allowance = salaryTable.getValueAt(selectedRow, 3).toString();
                String deductions = salaryTable.getValueAt(selectedRow, 4).toString();
                String netSalary = salaryTable.getValueAt(selectedRow, 5).toString();

                try {
                    exportPayslipToPDF(month, basicSalary, bonus, allowance, deductions, netSalary);
                    JOptionPane.showMessageDialog(salaryFrame, "Payslip for " + month + " downloaded successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(salaryFrame, "Error downloading payslip: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Logika Tombol Back
        backButton.addActionListener(e -> salaryFrame.dispose());

        // Tambahkan Panel ke Frame
        salaryFrame.add(historyPanel, BorderLayout.CENTER);
        salaryFrame.add(buttonPanel, BorderLayout.SOUTH);

        salaryFrame.setVisible(true);
    }



    private void exportPayslipToPDF(String month, String basicSalary, String bonus, String allowance, String deductions, String netSalary) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Payslip_" + month.replace(" ", "_") + ".pdf"));
        document.open();

        // Judul
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Payslip - " + month, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Informasi Slip Gaji
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        com.itextpdf.text.Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        com.itextpdf.text.Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        table.addCell(new PdfPCell(new Phrase("Basic Salary:", labelFont)));
        table.addCell(new PdfPCell(new Phrase(basicSalary, valueFont)));

        table.addCell(new PdfPCell(new Phrase("Bonus:", labelFont)));
        table.addCell(new PdfPCell(new Phrase(bonus, valueFont)));

        table.addCell(new PdfPCell(new Phrase("Allowance:", labelFont)));
        table.addCell(new PdfPCell(new Phrase(allowance, valueFont)));

        table.addCell(new PdfPCell(new Phrase("Deductions:", labelFont)));
        table.addCell(new PdfPCell(new Phrase(deductions, valueFont)));

        table.addCell(new PdfPCell(new Phrase("Net Salary:", labelFont)));
        table.addCell(new PdfPCell(new Phrase(netSalary, valueFont)));

        document.add(table);

        // Penutup
        document.add(new Paragraph("This payslip is auto-generated by the system.", FontFactory.getFont(FontFactory.HELVETICA, 10)));
        document.close();
    }


    public static void main(String[] args) {
        // Konversi HashMap<String, Employee> ke ArrayList<Employee>
        HashMap<String, Employee> employees = new HashMap<>();

// Panggil EmployeeDashboard dengan ArrayList<Employee>
        SwingUtilities.invokeLater(() -> new EmployeeDashboard("", new ArrayList<>(employees.values())));

    }
}

