package crawl_data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import data_interaction.CsvReader;
import data_interaction.CsvWriter;

public class Crawl_link{
	 public static void main(String[] args) {      	        
	        // Khởi tạo một đối tượng Crawl_2 với constructor mặc định
	        Crawler_3 cr2 = new Crawler_3("D:/Workspace/Java/Pro1/data/3/data.csv" , "D:/Workspace/Java/Pro1/data/3/links.csv");      
	        cr2.setKey();
	
	        //lúc lấy thì duyệt web lấy về thẻ html sau đó lưu lại từ lần sau test thì đọc file ra 
	        
	        // Thiết lập ChromeOptions
	        ChromeOptions options = new ChromeOptions();
	        options.setBinary(cr2.chromePath); 
	        WebDriver driver = new ChromeDriver(options);
	        // Mở trang web
	        driver.get(cr2.key_websiteSource);  
	        // Wait để đảm bảo trang web được tải hoàn toàn
	        try {
	            Thread.sleep(10000); 
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        
	        //load them bai viet
	        cr2.clickLoadMoreButton(driver);

	        
	        // Lấy HTML của trang web đã được tải hoàn toàn
	        String html = driver.getPageSource();       

	        // Sử dụng Jsoup để phân tích HTML
	        Document document = Jsoup.parse(html);
//	         Lưu HTML vào file
	        String fileName = "output3.txt";
	        try {
	            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	            writer.write(document.outerHtml());
	            writer.close();
	            System.out.println("HTML đã được lưu vào file '" + fileName + "' thành công.");
	        } catch (IOException e) {
	            System.out.println("Đã xảy ra lỗi khi ghi vào file: " + e.getMessage());
	        }
	        
//	       
//	       // Đường dẫn tới file HTML
//	          String filePath = "output3.txt";
//	          // Phân tích HTML từ file đã lưu đỡ phải duyệt web khi test
//	          Document document = null;
//	          try {
//	              File input = new File(filePath);
//	              document = Jsoup.parse(input, "UTF-8");
//	          } catch (IOException e) {
//	              System.out.println("Đã xảy ra lỗi khi đọc file: " + e.getMessage());
//	          }
	                  	                 
	        //lưu lại các đường link
	        CsvWriter writer = new CsvWriter(cr2.linkPath);
	        writer.appendLink(cr2.linkPath, cr2.selectLinks(document));
	        	   
	        
	    }
	
}
