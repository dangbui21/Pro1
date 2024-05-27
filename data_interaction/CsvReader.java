package data_interaction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CsvReader implements DataReader {
	
    public static int countLines(String filePath) {
        try {
            Reader reader = new FileReader(filePath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            int lineCount = 0;
            for (CSVRecord record : csvParser) {
                lineCount++;
            }
            csvParser.close();
            return lineCount;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
        }
    }
	@Override
    public void read(String filePath, List<String> sttList, List<String> linkList, List<String> selectedList) throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            reader.skip(1); // Bỏ qua dòng đầu tiên (tiêu đề cột)
            while ((nextLine = reader.readNext()) != null) {
                String stt = nextLine[0];
                String link = nextLine[1];
                String selected = nextLine[2];

                // Thêm dữ liệu vào từng danh sách
                sttList.add(stt);
                linkList.add(link);
                selectedList.add(selected);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    public static DefaultTableModel loadCsvData(File csvFile) {
        DefaultTableModel originalCsvData = new DefaultTableModel();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFile));
            CSVParser csvParser = CSVFormat.DEFAULT.parse(inputStreamReader);
            int start = 0;
            for (CSVRecord csvRecord : csvParser) {
                if (start == 0) {
                    start = 1;
                    for (int i = 0; i < csvRecord.size(); i++) {
                        originalCsvData.addColumn(csvRecord.get(i));
                    }
                } else {
                    Vector row = new Vector();
                    for (int i = 0; i < csvRecord.size(); i++) {
                        row.add(csvRecord.get(i));
                    }
                    originalCsvData.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi đọc tệp CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return originalCsvData;
    }







    
    
} 
