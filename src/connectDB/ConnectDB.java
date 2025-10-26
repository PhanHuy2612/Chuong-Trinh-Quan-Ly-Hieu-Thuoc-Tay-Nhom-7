package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection con = null;
    private static final ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }

    public static Connection getConnection() {
        return con;
    }

    public void connect() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ThienLuong;trustServerCertificate=true";
        String user = "sa";
        String password = "sapassword";
        con = DriverManager.getConnection(url, user, password);
    }

    public void disconnect() throws SQLException {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    /* Mo phuong thuc nay de kiem tra ket noi CSDL
    public static void main(String[] args) {
        ConnectDB db = ConnectDB.getInstance();
        try {
            // 1. Thực hiện kết nối
            db.connect();

            // 2. Kiểm tra trạng thái kết nối
            if (ConnectDB.getConnection() != null && !ConnectDB.getConnection().isClosed()) {
                System.out.println("Kết nối database THÀNH CÔNG!");
            } else {
                System.out.println("Kết nối database THẤT BẠI.");
            }

        } catch (SQLException e) {
            System.err.println("LỖI KẾT NỐI XẢY RA:");
            System.err.println("Mã lỗi: " + e.getErrorCode());
            System.err.println("Thông báo: " + e.getMessage());
            // In stack trace chi tiết nếu cần
            // e.printStackTrace();
        } finally {
            // 3. Ngắt kết nối (Rất quan trọng)
            try {
                db.disconnect();
                System.out.println("Đã ngắt kết nối.");
            } catch (SQLException e) {
                System.err.println("Lỗi khi ngắt kết nối: " + e.getMessage());
            }
        }
    }
     */
}