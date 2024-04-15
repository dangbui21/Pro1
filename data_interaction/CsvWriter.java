package data_interaction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataMining.Article;
import com.opencsv.CSVWriter;


public class CsvWriter {
    private String filePath;
    
    //truyền đường dẫn tới file lưu
    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void dang_AppendData(String id, String articleLink, String websiteSource, String articleType,
                           String articleSummary, String articleTitle, String content, String date,
                           String tagHash, String author, String category) {
        try {
            FileWriter writer = new FileWriter(filePath, true);

            StringBuilder data = new StringBuilder();
            data.append("\"").append(id).append("\",");
            data.append("\"").append(articleLink).append("\",");
            data.append("\"").append(websiteSource).append("\",");
            data.append("\"").append(articleType).append("\",");
            data.append("\"").append(articleSummary).append("\",");
            data.append("\"").append(articleTitle).append("\",");
            data.append("\"").append(content).append("\",");
            data.append("\"").append(date).append("\",");
            data.append("\"").append(tagHash).append("\",");
            data.append("\"").append(author).append("\",");
            data.append("\"").append(category).append("\"");

            writer.append(data.toString());
            writer.append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public void dang_AppendData(Article article) {
        try {
            // FileWriter để ghi vào file CSV
            FileWriter writer = new FileWriter(filePath, true);

            CSVWriter csvWriter = new CSVWriter(writer);

            String[] data = {
                    article.getId(),
                    article.getArticleLink(),
                    article.getWebsiteSource(),
                    article.getArticleType(),
                    article.getArticleSummary(),
                    article.getArticleTitle(),
                    article.getContent(),
                    article.getDate(),
                    article.getTagHash(),
                    article.getAuthor(),
                    article.getCategory()
            };
            // Ghi dữ liệu vào file CSV
            csvWriter.writeNext(data);
            // Đóng luồng ghi
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
   
    public void dang_saveLink(String linkPath , ArrayList<String> links) {
    	try (CSVWriter writer = new CSVWriter(new FileWriter(linkPath))) {
            for (String link : links) {
                String[] data = {link};
                writer.writeNext(data);
            }
            System.out.println("Links have been saved to " + linkPath);
        } catch (IOException e) {
            System.err.println("Error writing links to CSV file: " + e.getMessage());
        }
	}
    
}
