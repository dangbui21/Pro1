import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class TesterChucNang3 {
    private static DefaultTableModel originalCsvData;
    private static DefaultTableModel searchData;

    public static void main(String args[]) {
        JFrame frame = new JFrame("Java Swing Table");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel chứa bảng dữ liệu và các chức năng
        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        // Panel chứa bảng dữ liệu
        
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.CENTER);

       
        originalCsvData = new DefaultTableModel();
        JTable jTable1 = new JTable();
        jTable1.setModel(originalCsvData);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.getViewport().add(jTable1);
        tablePanel.add(jScrollPane2, BorderLayout.CENTER);

        // Panel chứa các chức năng
        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(functionPanel, BorderLayout.SOUTH);

        
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> showSearchDialog());
        functionPanel.add(searchButton);

        JButton trendsButton = new JButton("Phát hiện xu hướng");
        trendsButton.addActionListener(e -> detectTrends());
        functionPanel.add(trendsButton);

        JButton cancelSearchButton = new JButton("Hủy tìm kiếm");
        cancelSearchButton.addActionListener(e -> resetTable());
        functionPanel.add(cancelSearchButton);

        // Menu Bar
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

    // Phương thức để hiển thị hộp thoại chọn tệp
    private static void showFileChooser() {
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

    // Phương thức để hiển thị hộp thoại tìm kiếm
    private static void showSearchDialog() {
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
    }

    // Phương thức để lấy danh sách tên cột từ bảng
    private static String[] getColumnNames() {
        String[] columnNames = new String[originalCsvData.getColumnCount()];
        for (int i = 0; i < originalCsvData.getColumnCount(); i++) {
            columnNames[i] = originalCsvData.getColumnName(i);
        }
        return columnNames;
    }





 // Phương thức để tìm kiếm dữ liệu trong bảng với tùy chọn tìm kiếm mờ (fuzzy search) và contains
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
        
        // Kiểm tra nếu trường là "id", thực hiện tìm kiếm chính xác
        if (fieldName.equals("id")) {
            searchExactID(searchText);
            return; // Kết thúc phương thức sau khi tìm kiếm chính xác
        }
        
        // Tạo một danh sách để lưu các hàng phù hợp
        List<Integer> matchedRows = new ArrayList<>();
        
        // Thực hiện tìm kiếm mờ trên dữ liệu
        for (int row = 0; row < model.getRowCount(); row++) {
            String fieldValue = model.getValueAt(row, columnIndex).toString().toLowerCase();
            // Thực hiện so sánh fuzzy
            if (isFuzzyMatch(fieldValue, searchText.toLowerCase())) {
                matchedRows.add(row);
            } else if (fieldValue.contains(searchText.toLowerCase())) {
                matchedRows.add(row);
            }
        }
        
        // Thêm các hàng phù hợp vào bảng kết quả tìm kiếm
        for (Integer row : matchedRows) {
            Vector rowData = new Vector();
            for (int col = 0; col < model.getColumnCount(); col++) {
                rowData.add(model.getValueAt(row, col));
            }
            searchData.addRow(rowData);
        }
        
        if (searchData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả chứa '" + searchText + "' trong trường '" + fieldName + "'");
        } else {
            displaySearchResult();
        }
    }

    // Phương thức để tìm kiếm chính xác cho trường "ID"
    private static void searchExactID(String searchText) {
        DefaultTableModel model = (DefaultTableModel) originalCsvData;
        int idColumnIndex = getColumnIndex("id");
        if (idColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy cột 'id'.");
            return;
        }
        // Tìm kiếm chính xác theo giá trị "ID"
        for (int row = 0; row < model.getRowCount(); row++) {
            String idValue = model.getValueAt(row, idColumnIndex).toString();
            if (idValue.equals(searchText)) {
                Vector rowData = new Vector();
                for (int col = 0; col < model.getColumnCount(); col++) {
                    rowData.add(model.getValueAt(row, col));
                }
                searchData.addRow(rowData);
                break; // Dừng sau khi tìm thấy kết quả đầu tiên
            }
        }
        if (searchData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả chứa id: " + searchText);
        } else {
            displaySearchResult();
        }
    }


        

       

    // Phương thức để kiểm tra xem hai chuỗi có là một tìm kiếm mờ không (fuzzy search)
    private static boolean isFuzzyMatch(String s1, String s2) {
        // Độ lệch tối đa cho phép
        int maxDistance = 2;
        // Tính độ lệch Levenshtein
        int distance = levenshteinDistance(s1, s2);
        // Trả về true nếu độ lệch nhỏ hơn hoặc bằng độ lệch tối đa cho phép
        return distance <= maxDistance;
    }

    // Phương thức tính độ lệch Levenshtein giữa hai chuỗi
    private static int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }


    // Phương thức để hiển thị kết quả tìm kiếm
    private static void displaySearchResult() {
        JTable jTable2 = new JTable(searchData);
        JScrollPane jScrollPane3 = new JScrollPane();
        jScrollPane3.getViewport().add(jTable2);
        JFrame searchFrame = new JFrame("Kết quả tìm kiếm");
        searchFrame.add(jScrollPane3);
        searchFrame.pack();
        searchFrame.setVisible(true);
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

        // Tạo một danh sách chứa các từ dừng
        List<String> stopWords = Arrays.asList("a", "an", "the", "and", "but", "or", "for", "nor", "on", "at", "to", "by", "with", "from","of","in","m");

        // Tạo một HashMap để lưu tần suất xuất hiện của từ
        Map<String, Integer> wordCount = new HashMap<>();

        // Tìm chỉ mục của cột "Content"
        int contentColumnIndex = getColumnIndex("Content")-1;
        if (contentColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy cột 'Content'.");
            return;
        }

        // Đếm tần suất xuất hiện của các từ trong tiêu đề
        for (int row = 0; row < originalCsvData.getRowCount(); row++) {
            String content = originalCsvData.getValueAt(row, contentColumnIndex).toString().toLowerCase();
            String[] words = content.split("\\s+");
            for (String word : words) {
                // Loại bỏ các từ dừng và các ký tự đặc biệt
                word = word.replaceAll("[^a-zA-Z]", "").trim();
                if (!word.isEmpty() && !stopWords.contains(word)) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Sắp xếp các từ theo tần suất xuất hiện giảm dần
        List<Map.Entry<String, Integer>> sortedWordCount = wordCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Hiển thị top 5 từ xu hướng
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