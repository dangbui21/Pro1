package crawl_Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dataMining.Article;
import data_interaction.CsvReader;
import data_interaction.CsvWriter;

public class Crawler_3 extends Crawler {
	// Default constructor
    public Crawler_3() {
        super();
       
    }
    
    public Crawler_3(String dataPath, String linkPath) {
    	super(dataPath, linkPath);
    }
    
 // Hàm setKey để gán giá trị cho tất cả các thuộc tính kế thừa từ lớp Crawler
    public void setKey() {
        setKey_articleLink("a.article__content_link");
        setKey_websiteSource("https://www.ibm.com/blog/category/blockchain");
        setKey_articleType("Blog");
        setKey_articleSummary("unkown");
        setKey_articleTitle("h1.breadcrumbs__page_title");
        setKey_content("main.post__content.category");
        setKey_date("span.date");
        setKey_tagHash("div.post__tags a[rel=tag]");
        setKey_author("a.bylink");
        setKey_category("span.name_category");
    }
    
    public static void clickLoadMoreButton(WebDriver driver) {
	       Random random = new Random();
	       int x =0;
	       for (int i = 0; i < 50; i++) {
	           try {
	        	   scrollDownToLoadMore(driver);
	        	   // ngủ từ 4 đến 8 giây
	               int randomDelay = random.nextInt(6) + 6;
	               Thread.sleep(randomDelay * 1000);
	               	             
	        	   // Tìm phần tử chứa nút "Load More Posts"
	               WebElement loadMoreButton = driver.findElement(By.xpath("//button[@aria-label='Load more Category items ']"));
	               // Thực hiện click vào nút "Load More Posts"	             	               
	           
	               loadMoreButton.click();
	               x =0;
	              
	           } catch (Exception e) {
	               // Xử lý lỗi (ví dụ: in ra thông báo)
	               System.out.println("Đã xảy ra lỗi khi thực hiện click vào nút Load More Posts: " + e.getMessage());
	               x++;
	               // Break ra khỏi vòng lặp nếu có lỗi
	               if(x>=3) break;
	           }
	       }
	   }

    public static void scrollDownToLoadMore(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    
  public static void main(String[] args) {   
  String articleLink = "https://www.ibm.com/blog/the-orion-blockchain-database-empowering-multi-party-data-governance/";
	// Khởi tạo một đối tượng Crawl_2 với constructor mặc định
   Crawler_3 cr2 = new Crawler_3("D:/Workspace/Java/Pro1/data/3/data.csv" ,"D:/Workspace/Java/Pro1/data/3/links.csv"); 
   cr2.setKey();
        
//   // Thiết lập ChromeOptions
//   ChromeOptions options = new ChromeOptions();
//   options.setBinary(cr2.chromePath); // Đặt đường dẫn tới chrome.exe
//   // Khởi tạo WebDriver với ChromeOptions
//   WebDriver driver = new ChromeDriver(options);
//   // Mở trang web
//  
//	   driver.get(articleLink);  
//      // Wait để đảm bảo trang web được tải hoàn toàn
//      try {
//          Thread.sleep(6000); 
//      } catch (InterruptedException e) {
//          e.printStackTrace();
//      }
//      
//      // Lấy HTML của trang web đã được tải hoàn toàn
//      String html = driver.getPageSource();       
//
//      // Sử dụng Jsoup để phân tích HTML
//      Document document = Jsoup.parse(html);
//      // Lưu HTML vào file
//        String fileName = "output.txt";
//      try {
//          BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//          writer.write(document.outerHtml());
//          writer.close();
//          System.out.println("HTML đã được lưu vào file '" + fileName + "' thành công.");
//      } catch (IOException e) {
//          System.out.println("Đã xảy ra lỗi khi ghi vào file: " + e.getMessage());
//      }
      
     // Đường dẫn tới file HTML
        String filePath = "output.txt";

        // Phân tích HTML từ file
        Document document = null;
        try {
            File input = new File(filePath);
            document = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi khi đọc file: " + e.getMessage());
        }
                
               
      
		
      
      Article ar = new Article();
      CsvWriter csvWriter = new CsvWriter(cr2.dataPath);
      
      // Duyệt qua các phần tử và lấy văn bản trong đó 
      int i = CsvReader.dang_countLines(cr2.dataPath);//số hàng đang có trong file csv
      
      ar.setId(i);
      ar.setArticleLink(cr2.dang_select_ArticleLink()); 
      ar.setArticleTitle(cr2.dang_select_Title(document));
      ar.setArticleSummary(cr2.dang_select_Summary(document));
      ar.setArticleType(cr2.dang_select_ArticleType());
      ar.setAuthor(cr2.dang_select_Author(document));
      ar.setCategory(cr2.dang_select_Category(document));
      ar.setContent(cr2.dang_select_Content(document));
      ar.setDate(cr2.dang_select_Date(document));
      ar.setTagHash(cr2.dang_select_Tags(document));
      ar.setWebsiteSource(cr2.dang_select_WebsiteSource());
      
//      csvWriter.dang_AppendData(ar);
  }
        

    
}
