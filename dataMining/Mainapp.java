package dataMining;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Mainapp {
    private static DefaultTableModel originalCsvData;
    private static JFrame frame;

    public static void main(String args[]) {
        // Create the main JFrame
        frame = new JFrame("Java Swing Table");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create panels for various components
        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(functionPanel, BorderLayout.SOUTH);

        // Buttons for search, trend detection, and cancel search
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> showSearchDialog());
        functionPanel.add(searchButton);

        JButton trendsButton = new JButton("Phát hiện xu hướng");
        trendsButton.addActionListener(e -> detectTrends());
        functionPanel.add(trendsButton);

        JButton cancelSearchButton = new JButton("Hủy tìm kiếm");
        cancelSearchButton.addActionListener(e -> resetTable());
        functionPanel.add(cancelSearchButton);

        // Menu bar with open file option
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    // Method to show file chooser dialog and load CSV data
    private static void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                originalCsvData = CsvFileManager.loadCsvData(selectedFile);
                if (originalCsvData != null) {
                    displayTable(originalCsvData);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chỉ chấp nhận các tệp CSV.");
            }
        }
    }

    // Method to display the table
    private static void displayTable(DefaultTableModel data) {
        JTable jTable1 = new JTable(data);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.getViewport().add(jTable1);
        JPanel tablePanel = (JPanel) frame.getContentPane().getComponent(0).getComponentAt(0, 0);
        tablePanel.removeAll();
        tablePanel.add(jScrollPane2, BorderLayout.CENTER);
        frame.validate();
    }

    // Method to show the search dialog
    private static void showSearchDialog() {
        if (originalCsvData == null) {
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu được tải.");
            return;
        }
        String[] fields = getColumnNames(originalCsvData);
        String selectedField = (String) JOptionPane.showInputDialog(null, "Chọn trường để tìm kiếm:", "Chọn trường",
                JOptionPane.QUESTION_MESSAGE, null, fields, fields.length > 0 ? fields[0] : null);
        if (selectedField != null && !selectedField.isEmpty()) {
            String searchText = JOptionPane.showInputDialog(null, "Nhập giá trị cần tìm:");
            if (searchText != null && !searchText.isEmpty()) {
                DefaultTableModel searchData = SearchManager.search(originalCsvData, selectedField, searchText);
                if (searchData != null) {
                    displayTable(searchData);
                }
            }
        }
    }

    // Method to get column names from the table model
    private static String[] getColumnNames(DefaultTableModel model) {
        String[] columnNames = new String[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnNames[i] = model.getColumnName(i);
        }
        return columnNames;
    }

    // Method to reset the table to its original state
    private static void resetTable() {
        if (originalCsvData == null) {
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu để đặt lại.");
            return;
        }
        displayTable(originalCsvData);
    }

    // Method to detect trends in the data
    private static void detectTrends() {
        if (originalCsvData == null) {
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu để phân tích xu hướng.");
            return;
        }
        TrendAnalyzer.detectTrends(originalCsvData);
    }
}
