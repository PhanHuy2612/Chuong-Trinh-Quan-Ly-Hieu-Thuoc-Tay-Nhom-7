package connectDB;

import entity.TaiKhoan;

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
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ThienLuong; encrypt=true;trustServerCertificate=true;";
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

    public static void main(String[] args) {
        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
    }


    public TaiKhoan getCurrentUser() {
        return null;
    }

    public void setCurrentUser(TaiKhoan refreshedUser) {
    }

    public void clearSession() {

    }
}