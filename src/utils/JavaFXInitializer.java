package utils;

import javafx.application.Platform;
import javax.swing.SwingUtilities;

public class JavaFXInitializer {
    private static boolean initialized = false;

    public static void initialize() {
        if (!initialized) {
            Platform.setImplicitExit(false);
            SwingUtilities.invokeLater(() -> {
                try {
                    new javafx.embed.swing.JFXPanel();
                    initialized = true;
                    System.out.println("✓ JavaFX initialized");
                } catch (Exception e) {
                    System.err.println("✗ Failed to initialize JavaFX: " + e.getMessage());
                }
            });
        }
    }
}