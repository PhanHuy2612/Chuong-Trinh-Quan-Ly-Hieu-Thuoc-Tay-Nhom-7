package gui;

import connectDB.ConnectDB;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class frmDangNhap extends JFrame {
    private JButton eyeButton;
    private ImageIcon eyeOpenIcon;  // icon mở mắt
    private ImageIcon eyeClosedIcon; // icon nhắm mắt
    private UnderlineTextField usernameField;
    private UnderlinePasswordField passwordField;
    private RoundedButton loginButton;
    private RoundedButton exitButton; // Nút thoát mới
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel descriptionLabel;
    private JCheckBox rememberCheckBox;
    private JButton forgotPasswordButton;
    private String usernamePlaceholder = "Nhập tên đăng nhập";
    private String passwordPlaceholder = "Nhập mật khẩu";

    public frmDangNhap() {
        setTitle("Đăng nhập - Hệ thống Quản lý Bán Thuốc Nhà thuốc Thiện Lương");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setDividerLocation(0.5);

        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
        setVisible(true);

        // Initialize JavaFX toolkit to avoid "Toolkit not initialized" error
        new JFXPanel();
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon("src/img/Thuoc.png");
            Image img = originalIcon.getImage();
            Image scaledImg = img.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);
            imageLabel.setIcon(scaledIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            imageLabel.setText("Hình ảnh thuốc (Thuoc.png không tìm thấy)");
            imageLabel.setFont(new Font("Arial", Font.BOLD, 14));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(173, 216, 230));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 30, 50));

        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(0, 0, 10, 0);
        gbcTop.anchor = GridBagConstraints.CENTER;

        // Icon thuốc từ file src/img/icon_Thuoc.png, thay thế hình vuông xanh
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon("src/img/icon_Thuoc.png");
            Image img = originalIcon.getImage();
            Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Scale icon phù hợp kích thước
            ImageIcon scaledIcon = new ImageIcon(scaledImg);
            iconLabel.setIcon(scaledIcon);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            iconLabel.setText("💊"); // Fallback nếu không load được
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
            iconLabel.setForeground(new Color(59, 130, 246));
        }
        try {
            ImageIcon openIcon = new ImageIcon("src/img/icon_MatMo.png");
            ImageIcon closedIcon = new ImageIcon("src/img/icon_MatDong.png");

            Image imgOpen = openIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            Image imgClosed = closedIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

            eyeOpenIcon = new ImageIcon(imgOpen);
            eyeClosedIcon = new ImageIcon(imgClosed);
        } catch (Exception ex) {
            System.err.println("Không thể tải icon mắt: " + ex.getMessage());
        }

        gbcTop.gridy = 0;
        topPanel.add(iconLabel, gbcTop);

        titleLabel = new JLabel("Nhà thuốc Thiện Lương");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
        gbcTop.gridy = 1;
        topPanel.add(titleLabel, gbcTop);

        subtitleLabel = new JLabel("Hệ thống quản lý bán thuốc");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        gbcTop.gridy = 2;
        topPanel.add(subtitleLabel, gbcTop);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        RoundedPanel formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        descriptionLabel = new JLabel("Nhập thông tin đăng nhập để truy cập hệ thống");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(107, 114, 128));
        gbc.gridy = 0;
        formPanel.add(descriptionLabel, gbc);

        JLabel userLabel = new JLabel("Tên đăng nhập");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 1;
        formPanel.add(userLabel, gbc);

        usernameField = new UnderlineTextField(30);
        usernameField.setPreferredSize(new Dimension(300, 40));
        setupPlaceholder(usernameField, usernamePlaceholder, Color.GRAY);
        gbc.gridy = 2;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 3;
        formPanel.add(passLabel, gbc);

        JPanel passwordRow = new JPanel(new BorderLayout());
        passwordRow.setBackground(Color.WHITE);

        passwordField = new UnderlinePasswordField(25);
        passwordField.setPreferredSize(new Dimension(220, 40));
        setupPasswordPlaceholder(passwordField, passwordPlaceholder, Color.GRAY);
        passwordRow.add(passwordField, BorderLayout.CENTER);

        JPanel rightPasswordPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPasswordPanel.setBackground(Color.WHITE);

        eyeButton = new JButton();
        eyeButton.setIcon(eyeOpenIcon);
        eyeButton.setPreferredSize(new Dimension(30, 40));
        eyeButton.setContentAreaFilled(false);
        eyeButton.setBorderPainted(false);
        eyeButton.setFocusPainted(false);
        eyeButton.addActionListener(e -> togglePasswordVisibility());
        rightPasswordPanel.add(eyeButton);

        rememberCheckBox = new JCheckBox("Nhớ mật khẩu");
        rememberCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberCheckBox.setBackground(Color.WHITE);
        rememberCheckBox.setMargin(new Insets(0, 5, 0, 0));
        rightPasswordPanel.add(rememberCheckBox);

        passwordRow.add(rightPasswordPanel, BorderLayout.EAST);
        gbc.gridy = 4;
        formPanel.add(passwordRow, gbc);

        forgotPasswordButton = new JButton("Quên mật khẩu?");
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordButton.setForeground(new Color(59, 130, 246));
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Chức năng quên mật khẩu sẽ được triển khai sau. Vui lòng liên hệ admin!",
                    "Quên mật khẩu", JOptionPane.INFORMATION_MESSAGE);
        });
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(forgotPasswordButton, gbc);

        // Panel chứa hai nút: Đăng nhập (trái) và Thoát (phải), ngang hàng với text field (300px)
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(300, 50)); // Giữ kích thước ngang text field

        // Nút Đăng nhập - bo tròn, xanh dương, căn trái
        loginButton = new RoundedButton("Đăng nhập");
        loginButton.setBackground(new Color(59, 130, 246));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(140, 50));
        loginButton.addActionListener(new LoginActionListener());
        buttonPanel.add(loginButton, BorderLayout.WEST);

        // Nút Thoát - bo tròn, đỏ, căn phải
        exitButton = new RoundedButton("Thoát");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setPreferredSize(new Dimension(140, 50));
        exitButton.addActionListener(e -> System.exit(0)); // Thoát ứng dụng
        buttonPanel.add(exitButton, BorderLayout.EAST);

        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    // Phương thức togglePasswordVisibility()
    private void togglePasswordVisibility() {
        if (passwordField.getEchoChar() == '*') {
            passwordField.setEchoChar((char) 0);
            eyeButton.setIcon(eyeClosedIcon); // Icon mắt đóng
        } else {
            passwordField.setEchoChar('*');
            eyeButton.setIcon(eyeOpenIcon); // Icon mắt mở
        }
    }

    private void setupPlaceholder(JTextField field, String placeholder, Color color) {
        field.setForeground(color);
        field.setText(placeholder);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(color);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setupPasswordPlaceholder(JPasswordField field, String placeholder, Color color) {
        field.setForeground(color);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).length() == 0) {
                    field.setEchoChar((char) 0);
                    field.setForeground(color);
                    field.setText(placeholder);
                }
            }
        });
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            if (username.equals(usernamePlaceholder)) username = "";
            String password = new String(passwordField.getPassword()).trim();
            if (password.equals(passwordPlaceholder)) password = "";

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frmDangNhap.this,
                        "Vui lòng nhập tên đăng nhập và mật khẩu!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection con = ConnectDB.getConnection();
                if (con == null) {
                    JOptionPane.showMessageDialog(frmDangNhap.this,
                            "Kết nối cơ sở dữ liệu thất bại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "SELECT tenDangNhap, quyenTruyCap, matKhau, trangThaiTaiKhoan, maNhanVien FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String tenDangNhap = rs.getString("tenDangNhap");
                    String quyenTruyCap = rs.getString("quyenTruyCap");
                    String matKhau = rs.getString("matKhau");
                    String trangThaiTaiKhoan = rs.getString("trangThaiTaiKhoan");
                    String maNhanVien = rs.getString("maNhanVien");

                    if (rememberCheckBox.isSelected()) {
                        JOptionPane.showMessageDialog(frmDangNhap.this,
                                "Đã lưu thông tin đăng nhập.",
                                "Nhớ mật khẩu", JOptionPane.INFORMATION_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(frmDangNhap.this,
                            "Đăng nhập thành công! Chào mừng đến với hệ thống quản lý bán thuốc.",
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    dispose(); // Đóng form đăng nhập

                    // Khởi động JavaFX Application từ Swing thread
                    Platform.runLater(() -> {
                        try {
                            new frmQLBanThuoc_NV().start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Lỗi khi mở dashboard: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(frmDangNhap.this,
                            "Tên đăng nhập hoặc mật khẩu không đúng!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frmDangNhap.this,
                        "Lỗi truy vấn cơ sở dữ liệu!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }

    private static class RoundedPanel extends JPanel {
        private int radius;
        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private int radius = 25;
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D roundRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            g2.setColor(getModel().isArmed() ? getBackground().darker() : getBackground());
            g2.fill(roundRect);
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }
    }

    // Cập nhật mới: chỉ vẽ thanh ngang phía dưới
    private static class UnderlineTextField extends JTextField {
        private boolean hasFocus = false;
        private Color underlineColor = new Color(180, 185, 190);
        private Color focusUnderlineColor = new Color(59, 130, 246);

        public UnderlineTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(null);
            // Điều chỉnh margin để văn bản không bị che bởi thanh dưới
            setMargin(new Insets(10, 10, 5, 10)); // Giảm left padding và bottom để gần thanh dưới
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    hasFocus = true;
                    underlineColor = focusUnderlineColor;
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    hasFocus = false;
                    underlineColor = new Color(180, 185, 190);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền trắng
            g2.setColor(getBackground() != null ? getBackground() : Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Vẽ thanh ngang phía dưới
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(underlineColor);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class UnderlinePasswordField extends JPasswordField {
        private boolean hasFocus = false;
        private Color underlineColor = new Color(180, 185, 190);
        private Color focusUnderlineColor = new Color(59, 130, 246);

        public UnderlinePasswordField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(null);
            setMargin(new Insets(10, 10, 5, 10)); // Giảm left padding và bottom để gần thanh dưới
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    hasFocus = true;
                    underlineColor = focusUnderlineColor;
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    hasFocus = false;
                    underlineColor = new Color(180, 185, 190);
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground() != null ? getBackground() : Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(underlineColor);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(frmDangNhap::new);
    }
}