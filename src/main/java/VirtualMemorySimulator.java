import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VirtualMemorySimulator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartGUI inputFrame = new StartGUI();
            inputFrame.setVisible(true);
        });
    }
}