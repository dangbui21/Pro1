package data_interaction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import crawl_data.Article;


public class CsvWriter implements DataWriter {
    private String filePath;
    
    //truyền đường dẫn tới file lưu
    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public void appendData(String id, String articleLink, String websiteSource, String articleType,
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
    
    @Override
    public void appendData(Article article) {
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
    
    @Override
    public void appendLink(String linkPath, ArrayList<String> links) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(linkPath, true))) { // Chú ý thêm đối số true để mở chế độ ghi thêm
            // Ghi dữ liệu mới vào cuối file CSV
        	CsvReader re = new CsvReader();
        	int x = re.countLines(linkPath)-1;
            for (int i = x ; i < x + links.size(); i++) {
                String[] data = {String.valueOf(i + 1), links.get(i-x), "0"}; // Mặc định selected là 0
                writer.writeNext(data);           
            }
            System.out.println("Links have been appended to " + linkPath);
        } catch (IOException e) {
            System.err.println("Error appending links to CSV file: " + e.getMessage());
        }
    }

    @Override
    public void updateLink(List<String> sttList, List<String> linkList, List<String> selectedList) {  
        // update lại dữ liệu 
    	try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("stt,link,selected\n"); // Viết tiêu đề cột vào file CSV
            for (int i = 0; i < sttList.size(); i++) {
                writer.write("\"" + sttList.get(i) + "\",\"" + linkList.get(i) + "\",\"" + selectedList.get(i) + "\"\n");
            }
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving data to CSV file: " + e.getMessage());
        }
    }
    


    
}
