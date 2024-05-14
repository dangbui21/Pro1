import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class Main {
    private static DefaultTableModel originalCsvData;
    private static DefaultTableModel searchData;

    public static void main(String args[]) {
        JFrame frame = new JFrame("Java Swing Table");
        frame.setSize(500, 500);

        // Tạo JMenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hiển thị hộp thoại chọn tệp
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Kiểm tra xem tệp được chọn có phải là CSV không
                    if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                        // Nếu là tệp CSV, tải dữ liệu từ tệp đó
                        loadCsvData(selectedFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Chỉ chấp nhận các tệp CSV.");
                    }
                }
            }
        });
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JPanel jPanel = new JPanel();
        BoxLayout verticalLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(verticalLayout);
        frame.add(jPanel);

        originalCsvData = new DefaultTableModel();
        JTable jTable1 = new JTable();
        jTable1.setModel(originalCsvData);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.getViewport().add(jTable1);
        jPanel.add(jScrollPane2);

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> {
            String[] fields = getColumnNames();
            // Hiển thị hộp thoại để chọn trường cần tìm kiếm
            String selectedField = (String) JOptionPane.showInputDialog(null, "Chọn trường để tìm kiếm:", "Chọn trường",
                    JOptionPane.QUESTION_MESSAGE, null, fields, fields.length > 0 ? fields[0] : null);

            if (selectedField != null && !selectedField.isEmpty()) {
                // Nếu có trường được chọn, hiển thị hộp thoại để nhập giá trị cần tìm
                String searchText = JOptionPane.showInputDialog(null, "Nhập giá trị cần tìm:");
                if (searchText != null && !searchText.isEmpty()) {
                    // Nếu có giá trị tìm kiếm, thực hiện tìm kiếm
                    search(selectedField, searchText);
                }
            }
        });
        jPanel.add(searchButton);

        JButton trendsButton = new JButton("Phát hiện xu hướng");
        trendsButton.addActionListener(e -> detectTrends());
        jPanel.add(trendsButton);

        JButton cancelSearchButton = new JButton("Hủy tìm kiếm");
        cancelSearchButton.addActionListener(e -> resetTable());
        jPanel.add(cancelSearchButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Phương thức để tải dữ liệu từ tệp CSV vào bảng
    private static void loadCsvData(File csvFile) {
        originalCsvData.setRowCount(0); // Xóa dữ liệu hiện tại trước khi tải dữ liệu mới
        try {
            // Đọc tệp CSV và thêm dữ liệu vào bảng
            int start = 0;
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFile));
            CSVParser csvParser = CSVFormat.DEFAULT.parse(inputStreamReader);
            for (CSVRecord csvRecord : csvParser) {
                if (start == 0) {
                    start = 1;
                    // Thêm cột từ dòng tiêu đề của tệp CSV
                    for (int i = 0; i < csvRecord.size(); i++) {
                        originalCsvData.addColumn(csvRecord.get(i));
                    }
                } else {
                    // Thêm dòng dữ liệu từ các dòng tiếp theo của tệp CSV
                    Vector row = new Vector();
                    for (int i = 0; i < csvRecord.size(); i++) {
                        row.add(csvRecord.get(i));
                    }
                    originalCsvData.addRow(row);
                }
            }
        } catch (Exception e) {
            // Hiển thị thông báo lỗi nếu có lỗi khi đọc tệp CSV
            JOptionPane.showMessageDialog(null, "Lỗi khi đọc tệp CSV: " + e.getMessage());
            e.printStackTrace(); // In ra stack trace của lỗi
        }
    }

    // Phương thức để lấy danh sách tên cột từ bảng
    private static String[] getColumnNames() {
        String[] columnNames = new String[originalCsvData.getColumnCount()];
        for (int i = 0; i < originalCsvData.getColumnCount(); i++) {
            columnNames[i] = originalCsvData.getColumnName(i);
        }
        return columnNames;
    }

    // Phương thức để tìm kiếm dữ liệu trong bảng
    private static void search(String fieldName, String searchText) {
        DefaultTableModel model = (DefaultTableModel) originalCsvData;
        searchData = new DefaultTableModel();
        for (int col = 0; col < model.getColumnCount(); col++) {
            searchData.addColumn(model.getColumnName(col));
        }
        int columnIndex = getColumnIndex(fieldName); // Lấy chỉ mục cột của trường cần tìm kiếm
        if (columnIndex == -1) {
            // Nếu không tìm thấy tên cột, hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(null, "Không tìm thấy tên cột: " + fieldName);
            return; // Thoát khỏi phương thức
        }
        for (int row = 0; row < model.getRowCount(); row++) {
            String fieldValue = model.getValueAt(row, columnIndex).toString();
            // Thay đổi điều kiện kiểm tra để kiểm tra xem fieldValue có chứa searchText không
            if (fieldValue.toLowerCase().contains(searchText.toLowerCase())) {
                Vector rowData = new Vector();
                for (int col = 0; col < model.getColumnCount(); col++) {
                    rowData.add(model.getValueAt(row, col));
                }
                searchData.addRow(rowData);
            }
        }

        if (searchData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả chứa '" + searchText + "' trong trường '" + fieldName + "'");
        } else {
            JTable jTable2 = new JTable(searchData);
            JScrollPane jScrollPane3 = new JScrollPane();
            jScrollPane3.getViewport().add(jTable2);
            JFrame searchFrame = new JFrame("Kết quả tìm kiếm");
            searchFrame.add(jScrollPane3);
            searchFrame.pack();
            searchFrame.setVisible(true);
        }
    }

    // Phương thức để lấy chỉ mục của cột từ tên cột
    private static int getColumnIndex(String columnName) {
        for (int i = 0; i < originalCsvData.getColumnCount(); i++) {
            if (originalCsvData.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    // Phương thức để đặt lại bảng về trạng thái ban đầu
    private static void resetTable() {
        searchData = null;
    }

    // Phương thức để phát hiện xu hướng trong dữ liệu
    private static void detectTrends() {
        // Kiểm tra nếu dữ liệu CSV là null hoặc không có hàng
        if (originalCsvData == null || originalCsvData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không có dữ liệu để phân tích xu hướng.");
            return;
        }

        // Tìm chỉ mục của cột "Title"
        int titleColumnIndex = getColumnIndex("Content") - 1;
        if (titleColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy cột 'Title'.");
            return;
        }

        // Gather all article titles
        List<String> titles = new ArrayList<>();
        for (int row = 0; row < originalCsvData.getRowCount(); row++) {
            Object titleObject = originalCsvData.getValueAt(row, titleColumnIndex);
            if (titleObject != null) {
                String title = titleObject.toString();
                titles.add(title);
            }
        }

        if (titles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không có tiêu đề nào để phân tích xu hướng.");
            return;
        }

        // Count word occurrences in titles
        Map<String, Integer> wordCount = new HashMap<>();
        for (String title : titles) {
            String[] words = title.split("\\s+");
            for (String word : words) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        // Sort word occurrences by frequency
        List<Map.Entry<String, Integer>> sortedWordCount = wordCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Display top 5 trending words
        StringBuilder trends = new StringBuilder("Xu hướng: ");
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedWordCount) {
            trends.append(entry.getKey()).append("(").append(entry.getValue()).append(")").append(", ");
            count++;
            if (count == 5) {
                break;
            }
        }

        JOptionPane.showMessageDialog(null, trends.toString());
    }
}
