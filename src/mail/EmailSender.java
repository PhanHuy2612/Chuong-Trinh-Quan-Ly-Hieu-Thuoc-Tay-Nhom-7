package mail;

import javax.mail.PasswordAuthentication;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    private static final String FROM_EMAIL = "trongtan123tan@gmail.com";
    private static final String APP_PASSWORD = "qius aupb bdhn azqg";

    public static boolean sendOTP(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP Khôi phục mật khẩu - Nhà thuốc Thiện Lương");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; text-align: center;">
                    <h2 style="color: #3B82F6;">Khôi phục mật khẩu</h2>
                    <p>Mã OTP của bạn là:</p>
                    <h1 style="font-size: 42px; letter-spacing: 10px; color: #1E40AF;"><b>%s</b></h1>
                    <p>Mã có hiệu lực trong <b>5 phút</b>.</p>
                    <p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>
                </div>
                """.formatted(otp);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Gửi OTP thành công tới: " + toEmail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}