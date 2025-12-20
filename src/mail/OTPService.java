package mail;

import java.util.HashMap;
import java.util.Map;

public class OTPService {
    private static final Map<String, OTPData> store = new HashMap<>();
    private static final long EXPIRY_MS = 5 * 60 * 1000; // 5 phút

    public static String generate(String username) {
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));
        store.put(username.toLowerCase(), new OTPData(otp, System.currentTimeMillis()));
        return otp;
    }

    public static boolean verify(String username, String inputOTP) {
        OTPData data = store.get(username.toLowerCase());
        if (data == null) return false;

        if (System.currentTimeMillis() - data.timestamp > EXPIRY_MS) {
            store.remove(username.toLowerCase());
            return false;
        }

        boolean valid = data.otp.equals(inputOTP);
        if (valid) store.remove(username.toLowerCase()); // dùng 1 lần
        return valid;
    }

    private static class OTPData {
        String otp;
        long timestamp;
        OTPData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
}