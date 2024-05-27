package data_mining;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TrendAnalyzer {
    public static void detectTrends(DefaultTableModel originalCsvData) {
        List<String> stopWords = readStopWordsFromFile("src\\stopwords.txt");
        Map<String, Integer> wordCount = new HashMap<>();
        int contentColumnIndex = getColumnIndex(originalCsvData, "Content") - 1;
        if (contentColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy cột 'Content'.");
            return;
        }
        for (int row = 0; row < originalCsvData.getRowCount(); row++) {
            String content = originalCsvData.getValueAt(row, contentColumnIndex).toString().toLowerCase();
            String[] words = content.split("\\s+");
            for (String word : words) {
                word = word.replaceAll("[^a-zA-Z]", "").trim();
                if (!word.isEmpty() && !stopWords.contains(word)) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }
        List<Map.Entry<String, Integer>> sortedWordCount = wordCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
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

    private static List<String> readStopWordsFromFile(String fileName) {
        List<String> stopWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    private static int getColumnIndex(DefaultTableModel model, String columnName) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }
}
