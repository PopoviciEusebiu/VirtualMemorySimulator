import javax.swing.*;
import javax.swing.SpringLayout;
import java.awt.*;
import java.awt.event.*;

class StartGUI extends JFrame {
    private JTextField pageSizeTextField, tlbSizeTextField, memorySizeTextField;

    public StartGUI() {
        setTitle("Virtual Memory Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 240);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);

        JLabel welcomeLabel = new JLabel("Welcome to Virtual Memory Simulator");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(welcomeLabel);

        JLabel instructionLabel = new JLabel("Please enter the following details:");
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(instructionLabel);

        JLabel pageSizeLabel = new JLabel("Page Size (in bits, power of 2):");
        pageSizeTextField = new JTextField(15);
        mainPanel.add(pageSizeLabel);
        mainPanel.add(pageSizeTextField);

        JLabel tlbSizeLabel = new JLabel("TLB Size:");
        tlbSizeTextField = new JTextField(15);
        mainPanel.add(tlbSizeLabel);
        mainPanel.add(tlbSizeTextField);

        JLabel memorySizeLabel = new JLabel("Memory Size (power of 2):");
        memorySizeTextField = new JTextField(15);
        mainPanel.add(memorySizeLabel);
        mainPanel.add(memorySizeTextField);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());
        mainPanel.add(startButton);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, welcomeLabel, 10, SpringLayout.NORTH, mainPanel);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, instructionLabel, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, instructionLabel, 10, SpringLayout.SOUTH, welcomeLabel);

        layout.putConstraint(SpringLayout.WEST, pageSizeLabel, 10, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, pageSizeLabel, 10, SpringLayout.SOUTH, instructionLabel);

        layout.putConstraint(SpringLayout.WEST, pageSizeTextField, 10, SpringLayout.EAST, pageSizeLabel);
        layout.putConstraint(SpringLayout.NORTH, pageSizeTextField, 0, SpringLayout.NORTH, pageSizeLabel);

        layout.putConstraint(SpringLayout.WEST, tlbSizeLabel, 0, SpringLayout.WEST, pageSizeLabel);
        layout.putConstraint(SpringLayout.NORTH, tlbSizeLabel, 10, SpringLayout.SOUTH, pageSizeLabel);

        layout.putConstraint(SpringLayout.WEST, tlbSizeTextField, 0, SpringLayout.WEST, pageSizeTextField);
        layout.putConstraint(SpringLayout.NORTH, tlbSizeTextField, 0, SpringLayout.NORTH, tlbSizeLabel);

        layout.putConstraint(SpringLayout.WEST, memorySizeLabel, 0, SpringLayout.WEST, pageSizeLabel);
        layout.putConstraint(SpringLayout.NORTH, memorySizeLabel, 10, SpringLayout.SOUTH, tlbSizeLabel);

        layout.putConstraint(SpringLayout.WEST, memorySizeTextField, 0, SpringLayout.WEST, pageSizeTextField);
        layout.putConstraint(SpringLayout.NORTH, memorySizeTextField, 0, SpringLayout.NORTH, memorySizeLabel);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, startButton, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);
        layout.putConstraint(SpringLayout.SOUTH, startButton, -5, SpringLayout.SOUTH, mainPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                int pageSize = Integer.parseInt(pageSizeTextField.getText());
                int tlbSize = Integer.parseInt(tlbSizeTextField.getText());
                int memorySize = Integer.parseInt(memorySizeTextField.getText());

                if (!isPowerOfTwo(pageSize) || !isPowerOfTwo(memorySize)) {
                    throw new IllegalArgumentException("Invalid input. Please enter powers of 2.");
                }

                VirtualMemoryGUI simulationFrame = new VirtualMemoryGUI(pageSize, tlbSize, memorySize);
                simulationFrame.getMainFrame().setVisible(true);
                dispose();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(StartGUI.this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean isPowerOfTwo(int number) {
            return (number & (number - 1)) == 0;
        }
    }
}
