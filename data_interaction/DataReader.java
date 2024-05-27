package data_interaction;

import java.util.List;

import com.opencsv.exceptions.CsvValidationException;


public interface DataReader {
	public abstract void read(String filePath, List<String> sttList, List<String> linkList, List<String> selectedList) throws CsvValidationException;
	
}
