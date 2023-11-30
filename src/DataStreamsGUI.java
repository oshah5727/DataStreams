import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalFileTA;
    private JTextArea filteredFileTA;
    private JTextField searchString;
    private JButton loadFileBtn;
    private JButton searchFileBtn;
    private JButton quitBtn;

    public DataStreamsGUI() {
        originalFileTA = new JTextArea();
        filteredFileTA = new JTextArea();
        searchString = new JTextField();
        loadFileBtn = new JButton("Load File");
        searchFileBtn = new JButton("Search");
        quitBtn = new JButton("Quit");

        loadFileBtn.addActionListener(this::loadFile);
        searchFileBtn.addActionListener(this::searchFile);
        quitBtn.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(loadFileBtn);
        buttonPanel.add(searchFileBtn);
        buttonPanel.add(quitBtn);

        panel.add(searchString, BorderLayout.NORTH);
        panel.add(new JScrollPane(originalFileTA), BorderLayout.WEST);
        panel.add(new JScrollPane(filteredFileTA), BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadFile(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path filePath = fileChooser.getSelectedFile().toPath();
                String fileContent = Files.readString(filePath);
                originalFileTA.setText(fileContent);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile(ActionEvent event) {
        String searchStringPhrase = searchString.getText();
        if (searchStringPhrase.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search string.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String originalText = originalFileTA.getText();
            Stream<String> lines = originalText.lines();

            String filteredText = lines.filter(line -> line.contains(searchStringPhrase))
                    .collect(Collectors.joining(System.lineSeparator()));

            filteredFileTA.setText(filteredText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
