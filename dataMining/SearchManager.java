package dataMining;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SearchManager {
    public static DefaultTableModel search(DefaultTableModel originalCsvData, String fieldName, String searchText) {
        DefaultTableModel searchData = new DefaultTableModel();
        for (int col = 0; col < originalCsvData.getColumnCount(); col++) {
            searchData.addColumn(originalCsvData.getColumnName(col));
        }
        int columnIndex = getColumnIndex(originalCsvData, fieldName); // Lấy chỉ mục cột của trường cần tìm kiếm
        if (columnIndex == -1) {
            // Nếu không tìm thấy tên cột, hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(null, "Không tìm thấy tên cột: " + fieldName);
            return null; // Trả về null nếu không tìm thấy cột
        }

        // Kiểm tra nếu trường là "id", thực hiện tìm kiếm chính xác
        if (fieldName.equals("id")) {
            return searchExactID(originalCsvData, searchText);
        }

        // Tạo một danh sách để lưu các hàng phù hợp
        List<Integer> matchedRows = new ArrayList<>();

        // Thực hiện tìm kiếm mờ trên dữ liệu
        for (int row = 0; row < originalCsvData.getRowCount(); row++) {
            String fieldValue = originalCsvData.getValueAt(row, columnIndex).toString().toLowerCase();
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
            for (int col = 0; col < originalCsvData.getColumnCount(); col++) {
                rowData.add(originalCsvData.getValueAt(row, col));
            }
            searchData.addRow(rowData);
        }

        if (searchData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả chứa '" + searchText + "' trong trường '" + fieldName + "'");
        } else {
            displaySearchResult(searchData);
        }
        return searchData;
    }

    // Phương thức để tìm kiếm chính xác cho trường "ID"
    private static DefaultTableModel searchExactID(DefaultTableModel originalCsvData, String searchText) {
        DefaultTableModel searchData = new DefaultTableModel();
        for (int col = 0; col < originalCsvData.getColumnCount(); col++) {
            searchData.addColumn(originalCsvData.getColumnName(col));
        }
        int idColumnIndex = getColumnIndex(originalCsvData, "id");
        if (idColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy cột 'id'.");
            return null;
        }
        // Tìm kiếm chính xác theo giá trị "ID"
        for (int row = 0; row < originalCsvData.getRowCount(); row++) {
            String idValue = originalCsvData.getValueAt(row, idColumnIndex).toString();
            if (idValue.equals(searchText)) {
                Vector rowData = new Vector();
                for (int col = 0; col < originalCsvData.getColumnCount(); col++) {
                    rowData.add(originalCsvData.getValueAt(row, col));
                }
                searchData.addRow(rowData);
                break; // Dừng sau khi tìm thấy kết quả đầu tiên
            }
        }
        if (searchData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả chứa id: " + searchText);
        } else {
            displaySearchResult(searchData);
        }
        return searchData;
    }

    // Phương thức để hiển thị kết quả tìm kiếm
    private static void displaySearchResult(DefaultTableModel searchData) {
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

    // Phương thức để lấy chỉ mục của cột dựa trên tên cột
    private static int getColumnIndex(DefaultTableModel model, String columnName) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}
