package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection con = null;
    private static final ConnectDB instance = new ConnectDB();

    private final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLThuoc;encrypt=true;trustServerCertificate=true;";
    private final String USER = "sa";
    private final String PASSWORD = "sapassword";

    // 🔹 Singleton pattern
    public static ConnectDB getInstance() {
        return instance;
    }

    // 🔹 Trả về connection hiện tại
    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi mở lại kết nối: " + e.getMessage());
            try {
                ConnectDB.getInstance().connect();
            } catch (SQLException ex) {
                throw new RuntimeException("Không thể mở lại kết nối SQL Server", ex);
            }
        }
        return con;
    }


    // 🔹 Kết nối SQL Server (tự động mở lại nếu bị đóng)
    public void connect() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi kết nối SQL Server: " + e.getMessage());
            throw e;
        }
    }

    // 🔹 Đóng kết nối (chỉ gọi khi thoát chương trình)
    public void disconnect() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
                System.out.println("🔌 Đã ngắt kết nối SQL Server.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi ngắt kết nối: " + e.getMessage());
        }
    }
}
