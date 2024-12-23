import javax.swing.*;

public class EmployeeManagementApp {
    public static void main(String[] args) {

        // Gunakan SwingUtilities untuk memastikan GUI berjalan di thread yang benar
        SwingUtilities.invokeLater(() -> {
            // Jalankan LoginFrame sebagai titik awal aplikasi
            new LoginFrame();
        });
    }
}
