package gui_dialog;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import mail.EmailSender;
import mail.OTPService;

public class QuenMatKhauDialog extends Dialog<Boolean> {

    public QuenMatKhauDialog() {
        setTitle("Quên mật khẩu");
        setHeaderText("Khôi phục mật khẩu qua Email");

        ButtonType btnOK = new ButtonType("Đổi mật khẩu", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(btnOK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Tên đăng nhập");

        PasswordField txtOTP = new PasswordField();
        txtOTP.setPromptText("Mã OTP (6 số)");

        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("Mật khẩu mới");

        PasswordField txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Nhập lại mật khẩu");

        Button btnSendOTP = new Button("Gửi mã OTP");
        btnSendOTP.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold;");

        grid.add(new Label("Tên đăng nhập:"), 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(btnSendOTP, 2, 0);

        grid.add(new Label("Mã OTP:"), 0, 1);
        grid.add(txtOTP, 1, 1);

        grid.add(new Label("Mật khẩu mới:"), 0, 2);
        grid.add(txtNewPass, 1, 2);

        grid.add(new Label("Xác nhận:"), 0, 3);
        grid.add(txtConfirm, 1, 3);

        // Ẩn các ô chưa cần
        txtOTP.setVisible(false);
        txtNewPass.setVisible(false);
        txtConfirm.setVisible(false);

        getDialogPane().setContent(grid);

        // Gửi OTP
        btnSendOTP.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            if (username.isEmpty()) {
                alert("Vui lòng nhập tên đăng nhập!");
                return;
            }

            TaiKhoan tk = new TaiKhoan_DAO().timTheoTenDangNhap(username);
            if (tk == null) {
                alert("Tên đăng nhập không tồn tại!");
                return;
            }
            if (tk.getEmail() == null || tk.getEmail().isBlank()) {
                alert("Tài khoản chưa liên kết email!\nLiên hệ quản trị viên.");
                return;
            }

            String otp = OTPService.generate(username);
            if (EmailSender.sendOTP(tk.getEmail(), otp)) {
                info("Đã gửi mã OTP đến:\n" + tk.getEmail());
                txtOTP.setVisible(true);
                txtNewPass.setVisible(true);
                txtConfirm.setVisible(true);
                grid.getChildren().remove(btnSendOTP);
            } else {
                alert("Gửi email thất bại! Kiểm tra kết nối mạng.");
            }
        });

        setResultConverter(btn -> {
            if (btn == btnOK) {
                String user = txtUsername.getText().trim();
                String otp = txtOTP.getText();
                String pass1 = txtNewPass.getText();
                String pass2 = txtConfirm.getText();

                if (!OTPService.verify(user, otp)) {
                    alert("Mã OTP không đúng hoặc đã hết hạn!");
                    return null;
                }
                if (pass1.isEmpty() || pass2.isEmpty()) {
                    alert("Vui lòng nhập đầy đủ mật khẩu mới!");
                    return null;
                }
                if (!pass1.equals(pass2)) {
                    alert("Mật khẩu xác nhận không khớp!");
                    return null;
                }
                if (pass1.length() < 6) {
                    alert("Mật khẩu phải từ 6 ký tự trở lên!");
                    return null;
                }

                boolean success = new TaiKhoan_DAO().doiMatKhau(user, pass1);
                if (success) {
                    info("Đổi mật khẩu thành công!\nBạn có thể đăng nhập ngay bây giờ.");
                } else {
                    alert("Đổi mật khẩu thất bại!");
                }
                return success;
            }
            return null;
        });
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}