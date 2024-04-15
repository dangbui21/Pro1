package data_interaction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class CsvReader {
	public static void main(String[] args) {
        String filePath = "D:/Workspace/Java/Pro1/data/data.csv";
        int lineCount = dang_countLines(filePath);
        System.out.println("Number of lines in the CSV file: " + lineCount);
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
} 
