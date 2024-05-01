package data_interaction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
	public static void main(String[] args) throws CsvValidationException {
        String filePath = "D:/Workspace/Java/Pro1/data/link.csv";
        int lineCount = dang_countLines(filePath);
        System.out.println("Number of lines in the CSV file: " + lineCount);
        
     // Khởi tạo danh sách để lưu dữ liệu từ file CSV
        List<String> sttList = new ArrayList<>();
        List<String> linkList = new ArrayList<>();
        List<String> selectedList = new ArrayList<>();
  

        // Tạo một đối tượng CsvReader và gọi phương thức để đọc dữ liệu từ file CSV
        CsvReader csvReader = new CsvReader();
        csvReader.readCsvFile(filePath, sttList, linkList, selectedList);

        // In dữ liệu từ danh sách
        for (int i = 0; i < sttList.size(); i++) {
            System.out.println("Row " + (i + 1) + ": ");
            System.out.println("stt: " + sttList.get(i));
            System.out.println("link: " + linkList.get(i));
            System.out.println("selected: " + selectedList.get(i));
            System.out.println();
        }
	}
    

    public static int dang_countLines(String filePath) {
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
    
    public void readCsvFile(String filePath, List<String> sttList, List<String> linkList, List<String> selectedList) throws CsvValidationException {
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
} 
