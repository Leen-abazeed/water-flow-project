import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run UI on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> new WaterFlowUI());
    }
}
