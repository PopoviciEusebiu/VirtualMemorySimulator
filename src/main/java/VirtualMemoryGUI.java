import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class VirtualMemoryGUI {
    private JFrame mainFrame;
    private MemoryManager memoryManager;
    private JTextArea tlbTextArea, pageTableTextArea, physicalMemoryTextArea, historyTextArea;
    private JLabel capacityLabel, hitMissLabel; // Adaugă hitMissLabel
    private JTextField capacityTextField;

    public VirtualMemoryGUI(int pageSize, int tlbSize, int memorySize) {
        memoryManager = new MemoryManager(pageSize, tlbSize, memorySize);

        mainFrame = new JFrame();
        mainFrame.setTitle("Virtual Memory Simulator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 800);
        mainFrame.setLayout(new BorderLayout());

        JPanel memoryPanelsPanel = new JPanel(new GridLayout(1, 3));

        // Adăugăm titluri pentru JTextArea-uri
        JLabel tlbLabel = new JLabel("TLB");
        JLabel pageTableLabel = new JLabel("Page Table");
        JLabel physicalMemoryLabel = new JLabel("Physical Memory");
        JLabel historyLabel = new JLabel("History");

        tlbTextArea = new JTextArea();
        pageTableTextArea = new JTextArea();
        physicalMemoryTextArea = new JTextArea();
        historyTextArea = new JTextArea();

        tlbTextArea.setEditable(false);
        pageTableTextArea.setEditable(false);
        physicalMemoryTextArea.setEditable(false);
        historyTextArea.setEditable(false);

        JScrollPane tlbScrollPane = new JScrollPane(tlbTextArea);
        JScrollPane pageTableScrollPane = new JScrollPane(pageTableTextArea);
        JScrollPane physicalMemoryScrollPane = new JScrollPane(physicalMemoryTextArea);
        JScrollPane historyScrollPane = new JScrollPane(historyTextArea);

        JPanel tlbPanel = createPanelWithLabelAndTextArea(tlbLabel, tlbScrollPane);
        JPanel pageTablePanel = createPanelWithLabelAndTextArea(pageTableLabel, pageTableScrollPane);
        JPanel physicalMemoryPanel = createPanelWithLabelAndTextArea(physicalMemoryLabel, physicalMemoryScrollPane);
        JPanel historyPanel = createPanelWithLabelAndTextArea(historyLabel, historyScrollPane);

        memoryPanelsPanel.add(tlbPanel);
        memoryPanelsPanel.add(pageTablePanel);
        memoryPanelsPanel.add(physicalMemoryPanel);
        memoryPanelsPanel.add(historyPanel);

        mainFrame.add(memoryPanelsPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());

        JLabel inputLabel = new JLabel("Enter Virtual Address:");
        JTextField inputTextField = new JTextField(10);

        JButton readButton = new JButton("Read");
        JButton writeButton = new JButton("Write");

        capacityLabel = new JLabel("Capacity:");
        capacityTextField = new JTextField("0/" + memoryManager.getMemorySize());
        capacityTextField.setEditable(false);
        capacityTextField.setPreferredSize(new Dimension(100, 30));

        hitMissLabel = new JLabel("Hit/Miss:");

        readButton.addActionListener(e -> {
            try {
                long virtualAddress = Long.parseLong(inputTextField.getText());
                long data = memoryManager.readFromMemory(virtualAddress);
                historyTextArea.append("Read from Virtual Address " + virtualAddress + ": " + data + "\n");
                updateMemoryInfo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        writeButton.addActionListener(e -> {
            try {
                long virtualAddress = Long.parseLong(inputTextField.getText());
                if (!memoryManager.isAddressWritten(virtualAddress)) {
                    memoryManager.addPageToMemory(virtualAddress, 0);
                    capacityTextField.setBorder(BorderFactory.createEmptyBorder());
                    capacityTextField.setText(memoryManager.getUsedMemory() + "/" + memoryManager.getMemorySize());
                }
                long data = (long) (Math.random() * 100);
                memoryManager.writeToMemory(virtualAddress, data);
                historyTextArea.append("Write to Virtual Address " + virtualAddress + ": " + data + "\n");
                updateMemoryInfo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        controlPanel.add(inputLabel);
        controlPanel.add(inputTextField);
        controlPanel.add(readButton);
        controlPanel.add(writeButton);
        controlPanel.add(capacityLabel);
        controlPanel.add(capacityTextField);
        controlPanel.add(hitMissLabel);

        mainFrame.add(controlPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private JPanel createPanelWithLabelAndTextArea(JLabel label, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void closePreviousFrame() {
        mainFrame.dispose();
    }

    private void updateMemoryInfo() {
        tlbTextArea.setText(memoryManager.getTlbInfo());
        pageTableTextArea.setText(memoryManager.getPageTableInfo());
        physicalMemoryTextArea.setText(memoryManager.getPhysicalMemoryInfo());

        Set<Long> uniquePages = new LinkedHashSet<>(memoryManager.getFifoQueue());
        StringBuilder fifoInfo = new StringBuilder("Queue: ");
        for (Long entry : uniquePages) {
            VirtualPage virtualPage = memoryManager.getPageTable().get(entry);
            if (virtualPage != null) {
                fifoInfo.append(virtualPage.getId()).append(" | ");
            }
        }
        tlbTextArea.append("\n" + fifoInfo.toString() + "\n");

        hitMissLabel.setText("Hit/Miss: " + memoryManager.getLastOperationResult());
    }
}
