import java.util.ArrayList;

public class EmployeeDatabase {
    private static final ArrayList<Employee> employees = new ArrayList<>();
    public static Employee getEmployeeByUsername(String username) {
        return employees.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    static {
        // Tambahkan 10 data karyawan langsung ke dalam database
        employees.add(new Employee("John Doe", "Manager", "HR", "8000", "john", "password123", "john.doe@example.com", "1234567890"));
        employees.add(new Employee("Jane Smith", "Developer", "IT", "7000", "jane", "devpass", "jane.smith@example.com", "0987654321"));
        employees.add(new Employee("Emily Johnson", "Accountant", "Finance", "6000", "emily", "finpass", "emily.johnson@example.com", "1122334455"));
        employees.add(new Employee("Natasha Romanoff", "Security", "Operations", "6000", "natasha", "securepass", "natasha.romanoff@example.com", "9988776655"));
        employees.add(new Employee("Bruce Banner", "Scientist", "R&D", "10000", "bruce", "hulkpass", "bruce.banner@example.com", "4455667788"));
    }

    // Tambahkan karyawan baru
    public static void addEmployee(Employee employee) {
        employees.add(employee);
    }

    // Hapus karyawan berdasarkan indeks
    public static void removeEmployee(int index) {
        if (index >= 0 && index < employees.size()) {
            employees.remove(index);
        }
    }

    // Dapatkan daftar semua karyawan
    public static ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employees); // Kembalikan salinan untuk menghindari manipulasi langsung
    }

    // Cari karyawan berdasarkan nama (atau kriteria lainnya)
    public static ArrayList<Employee> searchEmployee(String query) {
        ArrayList<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getName().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getRole().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getDepartment().toLowerCase().contains(query.toLowerCase())) {
                result.add(employee);
            }
        }
        return result;
    }

    // Perbarui data karyawan
    public static void updateEmployee(int index, Employee updatedEmployee) {
        if (index >= 0 && index < employees.size()) {
            employees.set(index, updatedEmployee);
        }
    }

    // Tambahkan metode login
    public static boolean login(String username, String password) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
