public class Employee {
    private String name; // Nama karyawan
    private String role; // Jabatan
    private String department; // Departemen
    private String salary; // Gaji
    private String username; // Username untuk login
    private String password; // Password untuk login
    private String email; // Email
    private String contact; // Nomor telepon
    private String imagePath; // Tambahkan atribut imagePath

    // Constructor lengkap
    public Employee(String name, String role, String department, String salary, String username, String password, String email, String contact, String imagePath) {
        this.name = name;
        this.role = role;
        this.department = department;
        this.salary = salary;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.imagePath = imagePath; // Inisialisasi imagePath
    }

    // Constructor lengkap
    public Employee(String name, String role, String department, String salary, String username, String password, String email, String contact) {
        this.name = name;
        this.role = role;
        this.department = department;
        this.salary = salary;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getter dan Setter untuk semua atribut
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Role: " + role + ", Department: " + department + ", Salary: " + salary;
    }
}
