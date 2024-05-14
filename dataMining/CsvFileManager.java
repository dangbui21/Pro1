package dataMining;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvFileManager {
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
