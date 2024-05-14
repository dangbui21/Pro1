package data_interaction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import dataMining.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CsvReader {
	public static void main(String[] args) throws CsvValidationException {
        String filePath = "D:/Workspace/Java/Pro1/data/1/links.csv";
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
    
    public void theanh_find(String filePath,List <Article> article) {
      	 //   List<Article> article = new ArrayList<>();
       	System.out.println("theanh_find path " + filePath);
       	String line;
      	    try {
      	    	BufferedReader reader = new BufferedReader(new FileReader(filePath));

      	        while ((line = reader.readLine()) != null) {
      	            String[] parts = line.split(",");
      	            String id = parts[0];
      	            String articleLink = parts[1];
      	            String websiteSource = parts[2];
      	            String articleType = parts[3];
      	            String articleSummary = parts[4];
      	            String articleTitle = parts[5];
      	            String content = parts[6];
      	            String date = parts[7];
      	            String tagHash = parts[8];
      	            String author = parts[9];
      	            String category = parts[10];
      	            

      	            
      	            article.add (new Article( id,  articleLink,  websiteSource,  articleType,
      	                       articleSummary,  articleTitle,  content,  date,
      	                        tagHash,  author, category));
      	        }
      	   	                	                
      	            
      	      reader.close();
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
