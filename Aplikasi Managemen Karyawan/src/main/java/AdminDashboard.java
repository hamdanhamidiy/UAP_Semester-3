import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AdminDashboard extends JFrame {
    private ArrayList<Employee> employees;
    private ArrayList<String> payrollHistory = new ArrayList<>();
    private DefaultTableModel employeeTableModel;

    public AdminDashboard() {
        employees = EmployeeDatabase.getEmployees();

        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang utama

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#57A5B8")); // Warna header
        JLabel headerLabel = new JLabel("Admin Dashboard");
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        headerLabel.setForeground(Color.decode("#E1EBEC")); // Warna teks header
        headerPanel.add(headerLabel);

        // Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel opsi
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton manageEmployeesButton = createStyledButton("Manage Employees");
        JButton payrollButton = createStyledButton("Payroll");
        JButton scheduleButton = createStyledButton("Schedule & Attendance");
        JButton reportsButton = createStyledButton("Reports & Statistics");

        // Tambahkan tombol ke panel opsi
        optionsPanel.add(manageEmployeesButton);
        optionsPanel.add(payrollButton);
        optionsPanel.add(scheduleButton);
        optionsPanel.add(reportsButton);

        // Tambahkan action listener untuk tombol
        manageEmployeesButton.addActionListener(e -> openManageEmployees());
        payrollButton.addActionListener(e -> openPayroll());
        scheduleButton.addActionListener(e -> openSchedule());
        reportsButton.addActionListener(e -> openReports());

        // Tambahkan panel ke panel utama
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Tambahkan panel utama ke frame
        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Ukuran font yang lebih besar
        button.setBackground(Color.decode("#23424A")); // Warna latar belakang tombol
        button.setForeground(Color.decode("#E1EBEC")); // Warna teks tombol
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Padding tombol
        return button;
    }


    private void openPayroll() {
        JFrame payrollFrame = new JFrame("Payroll Management");
        payrollFrame.setSize(600, 400); // Ukuran frame yang sesuai
        payrollFrame.setLocationRelativeTo(null);
        payrollFrame.setLayout(new BorderLayout());

        // Model tabel untuk menampilkan riwayat penggajian
        String[] columnNames = {"Employee Name", "Base Salary", "Bonus", "Deductions", "Net Salary"};
        DefaultTableModel payrollTableModel = new DefaultTableModel(columnNames, 0);
        JTable payrollTable = new JTable(payrollTableModel);
        JScrollPane tableScrollPane = new JScrollPane(payrollTable);

        // Warna tabel
        payrollTable.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang tabel
        payrollTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        payrollTable.setGridColor(Color.decode("#57A5B8")); // Warna grid tabel

        // Tombol untuk fitur
        JButton calculateButton = createStyledButton("Calculate Payroll");
        JButton exportButton = createStyledButton("Export to PDF");
        JButton backButton = createStyledButton("Back");

        // Set ukuran tombol agar konsisten
        Dimension buttonSize = new Dimension(150, 30);
        calculateButton.setPreferredSize(buttonSize);
        exportButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        // Logika tombol Calculate Payroll
        calculateButton.addActionListener(e -> {
            payrollTableModel.setRowCount(0); // Hapus data tabel sebelumnya

            for (Employee employee : employees) {
                try {
                    // Ambil nilai salary dan validasi
                    String salaryStr = employee.getSalary();
                    double baseSalary = (salaryStr != null && !salaryStr.trim().isEmpty()) ? Double.parseDouble(salaryStr) : 0.0;

                    double bonus = 500; // Contoh nilai bonus tetap
                    double deductions = 100; // Contoh potongan tetap
                    double netSalary = baseSalary + bonus - deductions;

                    // Tambahkan data ke tabel
                    payrollTableModel.addRow(new Object[]{
                            employee.getName(),
                            baseSalary,
                            bonus,
                            deductions,
                            netSalary
                    });

                    // Simpan riwayat ke daftar payrollHistory
                    String details = String.format(
                            "Employee: %s, Base Salary: %.2f, Bonus: %.2f, Deductions: %.2f, Net Salary: %.2f",
                            employee.getName(), baseSalary, bonus, deductions, netSalary
                    );
                    payrollHistory.add(details);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(payrollFrame,
                            "Error calculating payroll for employee: " + employee.getName() + ". Invalid salary format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(payrollFrame, "Payroll calculated successfully!");
        });

        // Logika tombol Export to PDF
        exportButton.addActionListener(e -> {
            try {
                // Buat dokumen PDF menggunakan iText library
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream("PayrollReport.pdf"));

                document.open();
                document.add(new com.itextpdf.text.Paragraph("Payroll Report"));
                document.add(new com.itextpdf.text.Paragraph(" "));

                // Tambahkan data tabel ke PDF
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(columnNames.length);
                for (String columnName : columnNames) {
                    pdfTable.addCell(new com.itextpdf.text.Phrase(columnName));
                }
                for (int i = 0; i < payrollTableModel.getRowCount(); i++) {
                    for (int j = 0; j < payrollTableModel.getColumnCount(); j++) {
                        pdfTable.addCell(payrollTableModel.getValueAt(i, j).toString());
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(payrollFrame, "Payroll report exported to PDF successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(payrollFrame, "Error exporting to PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logika tombol Back
        backButton.addActionListener(e -> payrollFrame.dispose());

        // Panel tombol menggunakan FlowLayout untuk tata letak yang rapi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol
        buttonPanel.add(calculateButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(backButton);

        // Atur warna panel
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang panel history
        historyPanel.add(tableScrollPane, BorderLayout.CENTER);

        payrollFrame.add(historyPanel, BorderLayout.CENTER); // Tabel di tengah
        payrollFrame.add(buttonPanel, BorderLayout.SOUTH); // Tombol di bawah
        payrollFrame.setVisible(true);
    }


    private void openSchedule() {
        JFrame scheduleFrame = new JFrame("Schedule & Attendance Management");
        scheduleFrame.setSize(600, 400); // Ukuran frame yang lebih kecil
        scheduleFrame.setLocationRelativeTo(null);
        scheduleFrame.setLayout(new BorderLayout());
        scheduleFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang utama

        // Model Tabel untuk Jadwal dan Kehadiran
        String[] columnNames = {"Employee Name", "Date", "Shift", "Attendance Status"};
        DefaultTableModel scheduleTableModel = new DefaultTableModel(columnNames, 0);
        JTable scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setBackground(Color.decode("#E1EBEC")); // Warna latar tabel
        scheduleTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        scheduleTable.setGridColor(Color.decode("#57A5B8")); // Warna grid tabel
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);

        // Panel tombol untuk mengelola jadwal dan kehadiran
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5)); // Layout 2x2 untuk tombol
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol
        JButton addScheduleButton = createStyledButton("Add Schedule");
        JButton addAttendanceButton = createStyledButton("Add Attendance");
        JButton generateReportButton = createStyledButton("Generate Report");
        JButton backButton = createStyledButton("Back");

        // Atur ukuran tetap untuk tombol
        Dimension buttonSize = new Dimension(120, 30); // Ukuran tombol yang lebih kecil
        addScheduleButton.setPreferredSize(buttonSize);
        addAttendanceButton.setPreferredSize(buttonSize);
        generateReportButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        // Tambahkan tombol ke panel
        buttonPanel.add(addScheduleButton);
        buttonPanel.add(addAttendanceButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(backButton);

        // Logika tombol Add Schedule
        addScheduleButton.addActionListener(e -> {
            JFrame addScheduleFrame = new JFrame("Add Schedule");
            addScheduleFrame.setSize(400, 300);
            addScheduleFrame.setLocationRelativeTo(scheduleFrame);
            addScheduleFrame.setLayout(new GridLayout(5, 2, 10, 10));
            addScheduleFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang frame

            JTextField employeeNameField = new JTextField();
            JTextField dateField = new JTextField();
            JComboBox<String> shiftComboBox = new JComboBox<>(new String[]{"Morning", "Afternoon", "Night"});
            JButton saveButton = createStyledButton("Save");
            JButton cancelButton = createStyledButton("Cancel");

            addScheduleFrame.add(new JLabel("Employee Name:"));
            addScheduleFrame.add(employeeNameField);
            addScheduleFrame.add(new JLabel("Date (YYYY-MM-DD):"));
            addScheduleFrame.add(dateField);
            addScheduleFrame.add(new JLabel("Shift:"));
            addScheduleFrame.add(shiftComboBox);
            addScheduleFrame.add(cancelButton);
            addScheduleFrame.add(saveButton);

            saveButton.addActionListener(event -> {
                String employeeName = employeeNameField.getText();
                String date = dateField.getText();
                String shift = shiftComboBox.getSelectedItem().toString();

                if (!employeeName.isEmpty() && !date.isEmpty()) {
                    scheduleTableModel.addRow(new Object[]{employeeName, date, shift, "Not Marked"});
                    JOptionPane.showMessageDialog(addScheduleFrame, "Schedule added successfully!");
                    addScheduleFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(addScheduleFrame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(event -> addScheduleFrame.dispose());

            addScheduleFrame.setVisible(true);
        });

        // Logika tombol Add Attendance
        addAttendanceButton.addActionListener(e -> {
            int selectedRow = scheduleTable.getSelectedRow();
            if (selectedRow >= 0) {
                String employeeName = scheduleTableModel.getValueAt(selectedRow, 0).toString();
                String date = scheduleTableModel.getValueAt(selectedRow, 1).toString();
                String shift = scheduleTableModel.getValueAt(selectedRow, 2).toString();

                String attendanceStatus = JOptionPane.showInputDialog(scheduleFrame,
                        "Enter Attendance Status for " + employeeName + " on " + date + " (" + shift + "):\n(Present/Absent)");

                if (attendanceStatus != null && !attendanceStatus.isEmpty()) {
                    scheduleTableModel.setValueAt(attendanceStatus, selectedRow, 3);
                    JOptionPane.showMessageDialog(scheduleFrame, "Attendance updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(scheduleFrame, "Attendance status cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(scheduleFrame, "Please select a schedule to mark attendance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logika tombol Generate Report
        generateReportButton.addActionListener(e -> {
            StringBuilder reportBuilder = new StringBuilder("Attendance Report:\n");
            for (int i = 0; i < scheduleTableModel.getRowCount(); i++) {
                String employeeName = scheduleTableModel.getValueAt(i, 0).toString();
                String date = scheduleTableModel.getValueAt(i, 1).toString();
                String shift = scheduleTableModel.getValueAt(i, 2).toString();
                String attendanceStatus = scheduleTableModel.getValueAt(i, 3).toString();

                reportBuilder.append(String.format("Employee: %s, Date: %s, Shift: %s, Status: %s\n",
                        employeeName, date, shift, attendanceStatus));
            }

            // Tampilkan laporan dalam dialog
            JTextArea reportArea = new JTextArea(reportBuilder.toString());
            reportArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(reportArea);

            JFrame reportFrame = new JFrame("Attendance Report");
            reportFrame.setSize(500, 400);
            reportFrame.setLocationRelativeTo(scheduleFrame);
            reportFrame.add(scrollPane);
            reportFrame.setVisible(true);
        });

        // Logika tombol Back
        backButton.addActionListener(e -> scheduleFrame.dispose());

        // Tambahkan komponen ke frame utama
        scheduleFrame.add(tableScrollPane, BorderLayout.CENTER);
        scheduleFrame.add(buttonPanel, BorderLayout.SOUTH);
        scheduleFrame.setVisible(true);
    }



    private void openReports() {
        JFrame reportsFrame = new JFrame("Reports & Statistics");
        reportsFrame.setSize(700, 500); // Ukuran frame untuk tabel dan tombol
        reportsFrame.setLocationRelativeTo(null);
        reportsFrame.setLayout(new BorderLayout());
        reportsFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang utama

        // Kolom untuk tabel laporan kinerja
        String[] columnNames = {"Employee Name", "Department", "Days Worked", "Late Days", "Leave Days", "Performance"};
        DefaultTableModel reportTableModel = new DefaultTableModel(columnNames, 0);

        // Tambahkan data ke tabel
        for (Employee employee : employees) {
            int daysWorked = (int) (Math.random() * 20) + 10; // Simulasi jumlah hari kerja (10-30)
            int lateDays = (int) (Math.random() * 5); // Simulasi jumlah hari terlambat (0-5)
            int leaveDays = (int) (Math.random() * 5); // Simulasi jumlah hari cuti (0-5)
            String performance = (lateDays <= 1 && leaveDays <= 1) ? "Excellent" : "Good";

            reportTableModel.addRow(new Object[]{
                    employee.getName(),
                    employee.getDepartment(),
                    daysWorked,
                    lateDays,
                    leaveDays,
                    performance
            });
        }

        JTable reportTable = new JTable(reportTableModel);
        reportTable.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang tabel
        reportTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        reportTable.setGridColor(Color.decode("#57A5B8")); // Warna grid tabel
        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        tableScrollPane.getViewport().setBackground(Color.decode("#E1EBEC")); // Warna latar belakang scroll pane

        // Tombol untuk fitur
        JButton exportButton = createStyledButton("Export to PDF");
        JButton backButton = createStyledButton("Back");

        // Logika tombol Export to PDF
        exportButton.addActionListener(e -> {
            try {
                // Gunakan Document dari iText untuk membuat PDF
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                PdfWriter.getInstance(document, new FileOutputStream("ReportsAndStatistics.pdf"));
                document.open();

                // Tambahkan judul laporan
                document.add(new Paragraph("Employee Reports & Statistics", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Generated Report:"));
                document.add(new Paragraph(" "));

                // Tambahkan data tabel ke PDF
                PdfPTable pdfTable = new PdfPTable(columnNames.length);
                for (String columnName : columnNames) {
                    pdfTable.addCell(new Phrase(columnName, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                }
                for (int i = 0; i < reportTableModel.getRowCount(); i++) {
                    for (int j = 0; j < reportTableModel.getColumnCount(); j++) {
                        pdfTable.addCell(reportTableModel.getValueAt(i, j).toString());
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(reportsFrame, "Report exported to PDF successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(reportsFrame, "Error exporting to PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logika tombol Back
        backButton.addActionListener(e -> reportsFrame.dispose());

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol
        buttonPanel.add(exportButton);
        buttonPanel.add(backButton);

        // Tambahkan komponen ke frame
        reportsFrame.add(tableScrollPane, BorderLayout.CENTER);
        reportsFrame.add(buttonPanel, BorderLayout.SOUTH);
        reportsFrame.setVisible(true);
    }





    private void openManageEmployees() {
        JFrame manageFrame = new JFrame("Manage Employees");
        manageFrame.setSize(600, 450); // Ukuran frame lebih kecil dan proporsional
        manageFrame.setLocationRelativeTo(null);
        manageFrame.setLayout(new BorderLayout());
        manageFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang utama

        // Panel kontrol untuk tombol
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang kontrol panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding antar tombol

        JButton addButton = createStyledButton("Add Employee");
        addButton.setPreferredSize(new Dimension(150, 30)); // Ukuran tombol seragam
        JButton editButton = createStyledButton("Edit Employee");
        editButton.setPreferredSize(new Dimension(150, 30));
        JButton deleteButton = createStyledButton("Delete Employee");
        deleteButton.setPreferredSize(new Dimension(150, 30));
        JTextField searchField = new JTextField(15);
        searchField.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang input
        searchField.setForeground(Color.decode("#23424A")); // Warna teks input
        JButton searchButton = createStyledButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));

        // Tambahkan komponen ke panel kontrol
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(addButton, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        controlPanel.add(editButton, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        controlPanel.add(deleteButton, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        controlPanel.add(searchField, gbc);
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(searchButton, gbc);

        // Tombol kembali
        JButton backButton = createStyledButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> manageFrame.dispose());

        // Tabel untuk data karyawan
        String[] columnNames = {"Name", "Role", "Department", "Salary"};
        employeeTableModel = new DefaultTableModel(columnNames, 0);
        JTable employeeTable = new JTable(employeeTableModel);
        employeeTable.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang tabel
        employeeTable.setForeground(Color.decode("#23424A")); // Warna teks tabel
        employeeTable.setGridColor(Color.decode("#57A5B8")); // Warna grid tabel
        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.getViewport().setBackground(Color.decode("#E1EBEC")); // Warna latar belakang scroll pane

        // Panel bawah untuk tombol kembali
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol
        buttonPanel.add(backButton);

        // Tambahkan panel ke frame
        manageFrame.add(controlPanel, BorderLayout.NORTH);
        manageFrame.add(tableScroll, BorderLayout.CENTER);
        manageFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Tambahkan fungsi tombol
        addButton.addActionListener(e -> addEmployee());
        editButton.addActionListener(e -> editEmployee(employeeTable));
        deleteButton.addActionListener(e -> deleteEmployee(employeeTable));
        searchButton.addActionListener(e -> searchEmployee(searchField.getText(), employeeTable));

        // Perbarui tabel
        updateEmployeeTable();
        manageFrame.setVisible(true);
    }


    private void addEmployee() {
        JFrame addFrame = new JFrame("Add Employee");
        addFrame.setSize(450, 350);
        addFrame.setLocationRelativeTo(null);
        addFrame.setLayout(new BorderLayout(10, 10));
        addFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang utama

        // Panel untuk Form Input
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding untuk panel
        formPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang form panel

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.decode("#23424A")); // Warna teks label
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang input
        nameField.setForeground(Color.decode("#23424A")); // Warna teks input

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(Color.decode("#23424A"));
        JTextField roleField = new JTextField();
        roleField.setFont(new Font("Arial", Font.PLAIN, 14));
        roleField.setBackground(Color.decode("#E1EBEC"));
        roleField.setForeground(Color.decode("#23424A"));

        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        departmentLabel.setForeground(Color.decode("#23424A"));
        JTextField departmentField = new JTextField();
        departmentField.setFont(new Font("Arial", Font.PLAIN, 14));
        departmentField.setBackground(Color.decode("#E1EBEC"));
        departmentField.setForeground(Color.decode("#23424A"));

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        salaryLabel.setForeground(Color.decode("#23424A"));
        JTextField salaryField = new JTextField();
        salaryField.setFont(new Font("Arial", Font.PLAIN, 14));
        salaryField.setBackground(Color.decode("#E1EBEC"));
        salaryField.setForeground(Color.decode("#23424A"));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(roleLabel);
        formPanel.add(roleField);
        formPanel.add(departmentLabel);
        formPanel.add(departmentField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryField);

        // Panel untuk Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // FlowLayout untuk tombol
        buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol
        JButton saveButton = createStyledButton("Save");
        JButton backButton = createStyledButton("Back");

        backButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setPreferredSize(new Dimension(100, 30));

        backButton.addActionListener(e -> addFrame.dispose());

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String role = roleField.getText();
            String department = departmentField.getText();
            String salary = salaryField.getText();

            if (!name.isEmpty() && !role.isEmpty() && !department.isEmpty() && !salary.isEmpty()) {
                try {
                    // Validasi salary agar hanya menerima angka valid
                    Double.parseDouble(salary);

                    Employee newEmployee = new Employee(name, role, department, salary, "john", "password123", "john.doe@example.com", "1234567890");
                    employees.add(newEmployee);
                    updateEmployeeTable();
                    JOptionPane.showMessageDialog(addFrame, "Employee added successfully!");
                    addFrame.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addFrame, "Salary must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(addFrame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        // Tambahkan panel ke frame
        addFrame.add(formPanel, BorderLayout.CENTER);
        addFrame.add(buttonPanel, BorderLayout.SOUTH);

        addFrame.setVisible(true);
    }




    private void editEmployee(JTable employeeTable) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            Employee employee = employees.get(selectedRow);

            JFrame editFrame = new JFrame("Edit Employee");
            editFrame.setSize(450, 300);
            editFrame.setLocationRelativeTo(null);
            editFrame.setLayout(new BorderLayout(10, 10));
            editFrame.getContentPane().setBackground(Color.decode("#B4D4DC")); // Warna latar belakang utama

            // Panel untuk Form Input
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding untuk panel
            formPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel form

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setForeground(Color.decode("#23424A")); // Warna teks label
            JTextField nameField = new JTextField(employee.getName());
            nameField.setFont(new Font("Arial", Font.PLAIN, 14));
            nameField.setBackground(Color.decode("#E1EBEC")); // Warna latar belakang input
            nameField.setForeground(Color.decode("#23424A")); // Warna teks input

            JLabel roleLabel = new JLabel("Role:");
            roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            roleLabel.setForeground(Color.decode("#23424A"));
            JTextField roleField = new JTextField(employee.getRole());
            roleField.setFont(new Font("Arial", Font.PLAIN, 14));
            roleField.setBackground(Color.decode("#E1EBEC"));
            roleField.setForeground(Color.decode("#23424A"));

            JLabel departmentLabel = new JLabel("Department:");
            departmentLabel.setFont(new Font("Arial", Font.BOLD, 14));
            departmentLabel.setForeground(Color.decode("#23424A"));
            JTextField departmentField = new JTextField(employee.getDepartment());
            departmentField.setFont(new Font("Arial", Font.PLAIN, 14));
            departmentField.setBackground(Color.decode("#E1EBEC"));
            departmentField.setForeground(Color.decode("#23424A"));

            JLabel salaryLabel = new JLabel("Salary:");
            salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            salaryLabel.setForeground(Color.decode("#23424A"));
            JTextField salaryField = new JTextField(employee.getSalary());
            salaryField.setFont(new Font("Arial", Font.PLAIN, 14));
            salaryField.setBackground(Color.decode("#E1EBEC"));
            salaryField.setForeground(Color.decode("#23424A"));

            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(roleLabel);
            formPanel.add(roleField);
            formPanel.add(departmentLabel);
            formPanel.add(departmentField);
            formPanel.add(salaryLabel);
            formPanel.add(salaryField);

            // Panel untuk Tombol
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setBackground(Color.decode("#B4D4DC")); // Warna latar belakang panel tombol

            JButton saveButton = createStyledButton("Save");
            JButton backButton = createStyledButton("Back");

            saveButton.addActionListener(e -> {
                employee.setName(nameField.getText());
                employee.setRole(roleField.getText());
                employee.setDepartment(departmentField.getText());
                employee.setSalary(salaryField.getText());
                updateEmployeeTable();
                JOptionPane.showMessageDialog(editFrame, "Employee updated successfully!");
                editFrame.dispose();
            });

            backButton.addActionListener(e -> editFrame.dispose());

            buttonPanel.add(backButton);
            buttonPanel.add(saveButton);

            // Tambahkan panel ke frame
            editFrame.add(formPanel, BorderLayout.CENTER);
            editFrame.add(buttonPanel, BorderLayout.SOUTH);

            editFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee(JTable employeeTable) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Konfirmasi sebelum menghapus
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Hapus karyawan dari daftar
                employees.remove(selectedRow);
                updateEmployeeTable(); // Perbarui tabel
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void searchEmployee(String query, JTable employeeTable) {
        // Buat model tabel baru untuk menampilkan hasil pencarian
        DefaultTableModel searchModel = new DefaultTableModel(new String[]{"Name", "Role", "Department", "Salary"}, 0);

        // Cari karyawan berdasarkan query
        for (Employee employee : employees) {
            if (employee.getName().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getRole().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getDepartment().toLowerCase().contains(query.toLowerCase())) {
                // Tambahkan data yang cocok ke model pencarian
                searchModel.addRow(new Object[]{employee.getName(), employee.getRole(), employee.getDepartment(), employee.getSalary()});
            }
        }

        // Jika hasil pencarian kosong, tampilkan pesan
        if (searchModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No employees found matching the search query.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }

        // Perbarui model tabel dengan hasil pencarian
        employeeTable.setModel(searchModel);
    }


    private void updateEmployeeTable() {
        employeeTableModel.setRowCount(0);
        for (Employee employee : employees) {
            employeeTableModel.addRow(new Object[]{employee.getName(), employee.getRole(), employee.getDepartment(), employee.getSalary()});
        }
    }
}
