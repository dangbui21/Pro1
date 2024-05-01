package crawl_Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.opencsv.exceptions.CsvValidationException;

import dataMining.Article;
import data_interaction.CsvReader;
import data_interaction.CsvWriter;

public class Crawler_2 extends Crawler {
	
	// Default constructor
    public Crawler_2() {
        super();
       
    }
	 
//    public Crawler_2(String articleLink, String websiteSource, String articleType,
//                   String articleSummary, String articleTitle, String content,
//                   String date, String tagHash, String author, String category) {
//        super(articleLink, websiteSource, articleType, articleSummary, articleTitle, content, date, tagHash, author, category);
//    }
    
    // Hàm setKey để gán giá trị cho tất cả các thuộc tính kế thừa từ lớp Crawler
    public void setKey() {
        setKey_articleLink("a.Card-title");
        setKey_websiteSource("https://www.cnbc.com/blockchain");
        setKey_articleType("News");
        setKey_articleSummary("div.group");
        setKey_articleTitle("h1.ArticleHeader-headline");
        setKey_content("div.ArticleBody-articleBody");
        setKey_date("time[datetime]");
        setKey_tagHash("unkown");
        setKey_author("a.Author-authorName");
        setKey_category("a.ArticleHeader-eyebrow");
    }
    
    
   @Override	
   public String dang_select_Date(Document document) {
	   if("unknown".equals(key_date)) return "unknown";
	   Element timeElement = document.selectFirst(key_date);
       String datetime = timeElement.attr("datetime");
      // Chuyển đổi định dạng ngày tháng
       String dateString = parseDateTime(datetime);   
       System.out.println("Data : " + dateString);
       return dateString;
	}
   
   public static void clickLoadMoreButton(WebDriver driver) {
       Random random = new Random();
       for (int i = 0; i < 30; i++) {
           try {
               // Tìm phần tử chứa nút "Load More Posts"
               WebElement loadMoreButton = driver.findElement(By.className("LoadMoreButton-loadMore"));
               // Thực hiện click vào nút "Load More Posts"
               loadMoreButton.click();

               // ngủ từ 4 đến 8 giây
               int randomDelay = random.nextInt(5) + 4;
               Thread.sleep(randomDelay * 1000);
           } catch (Exception e) {
               // Xử lý lỗi (ví dụ: in ra thông báo)
               System.out.println("Đã xảy ra lỗi khi thực hiện click vào nút Load More Posts: " + e.getMessage());
               // Break ra khỏi vòng lặp nếu có lỗi
               break;
           }
       }
   }
    
    public static void main(String[] args) throws CsvValidationException {   
        // Khởi tạo một đối tượng Crawl_2 với constructor mặc định
        Crawler_2 cr2 = new Crawler_2(); 
        cr2.setKey();
        //lấy danh sách đường link đã thu thập
        // Khởi tạo danh sách để lưu dữ liệu từ file CSV
        List<String> sttList = new ArrayList<>();
        List<String> linkList = new ArrayList<>();
        List<String> selectedList = new ArrayList<>();
  
        // Tạo một đối tượng CsvReader và gọi phương thức để đọc dữ liệu từ file CSV
        CsvReader csvReader = new CsvReader();
        csvReader.readCsvFile(cr2.linkPath, sttList, linkList, selectedList);
        
        // Thiết lập ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setBinary(cr2.chromePath); // Đặt đường dẫn tới chrome.exe
        // Khởi tạo WebDriver với ChromeOptions
        WebDriver driver = new ChromeDriver(options);
        int x =0;
        for(String link : linkList) {		   
		   if (selectedList.get(x).equals("0")) {
			   cr2.setKey_articleLink(link);
			   try {
			        driver.get(link);  
			        // Wait để đảm bảo trang web được tải hoàn toàn
					Random random = new Random();
					int randomDelay = random.nextInt(10) + 6;
					try {
					    Thread.sleep(randomDelay * 1000); 
					} catch (InterruptedException e) {
					    e.printStackTrace();
					}				
					// Lấy HTML của trang web đã được tải hoàn toàn
					String html = driver.getPageSource();       				
					// Sử dụng Jsoup để phân tích HTML
					Document document = Jsoup.parse(html);	
					
					// Khởi tạo một đối tượng Crawl_2 với constructor mặc định
//					Crawler_2 cr1 = new Crawler_2();  				
					Article ar = new Article();
					CsvWriter csvWriter = new CsvWriter(cr2.dataPath);				
					// Duyệt qua các phần tử và lấy văn bản trong đó 
					int i = CsvReader.dang_countLines(cr2.dataPath); //số hàng đang có trong file csv
				
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
				
				    csvWriter.dang_AppendData(ar);
				    
				    selectedList.set(x, "1");
				    
					} catch (Exception e) {
					    System.err.println("Error processing link: " + link + " - " + e.getMessage());
					}
		   }
		   
		   x++;
        }
        CsvWriter w = new CsvWriter(cr2.linkPath);
        w.dang_update(sttList, linkList, selectedList);
        
          
        
    }
    
 //main này để test 
    
//    public static void main(String[] args) {   
//       String articleLink = "https://www.cnbc.com/blockchain";
//    	// Khởi tạo một đối tượng Crawl_2 với constructor mặc định
//        Crawler_2 cr2 = new Crawler_2(); 
//        cr2.setKey();
//             
//        // Thiết lập ChromeOptions
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary(cr2.chromePath); // Đặt đường dẫn tới chrome.exe
//        // Khởi tạo WebDriver với ChromeOptions
//        WebDriver driver = new ChromeDriver(options);
//        // Mở trang web
//       
//    	   driver.get(articleLink);  
//           // Wait để đảm bảo trang web được tải hoàn toàn
//           try {
//               Thread.sleep(6000); 
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//           
//           // Lấy HTML của trang web đã được tải hoàn toàn
//           String html = driver.getPageSource();       
//
//           // Sử dụng Jsoup để phân tích HTML
//           Document document = Jsoup.parse(html);
//           // Lưu HTML vào file
//             String fileName = "output.txt";
//           try {
//               BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//               writer.write(document.outerHtml());
//               writer.close();
//               System.out.println("HTML đã được lưu vào file '" + fileName + "' thành công.");
//           } catch (IOException e) {
//               System.out.println("Đã xảy ra lỗi khi ghi vào file: " + e.getMessage());
//           }
//           
////          // Đường dẫn tới file HTML
////             String filePath = "output.txt";
//   //
////             // Phân tích HTML từ file
////             Document document = null;
////             try {
////                 File input = new File(filePath);
////                 document = Jsoup.parse(input, "UTF-8");
////             } catch (IOException e) {
////                 System.out.println("Đã xảy ra lỗi khi đọc file: " + e.getMessage());
////             }
//                     
//                    
//           
//   		// Khởi tạo một đối tượng Crawl_2 với constructor mặc định
//   		Crawler_2 cr1 = new Crawler_2();  
//           
//           Article ar = new Article();
//           CsvWriter csvWriter = new CsvWriter(cr2.dataPath);
//           
//           // Duyệt qua các phần tử và lấy văn bản trong đó 
//           int i = CsvReader.dang_countLines(cr2.dataPath);//số hàng đang có trong file csv
//           
//           ar.setId(i);
//           ar.setArticleLink(cr1.dang_select_ArticleLink()); 
//           ar.setArticleTitle(cr1.dang_select_Title(document));
//           ar.setArticleSummary(cr1.dang_select_Summary(document));
//           ar.setArticleType(cr1.dang_select_ArticleType());
//           ar.setAuthor(cr1.dang_select_Author(document));
//           ar.setCategory(cr1.dang_select_Category(document));
//           ar.setContent(cr1.dang_select_Content(document));
//           ar.setDate(cr1.dang_select_Date(document));
//           ar.setTagHash(cr1.dang_select_Tags(document));
//           ar.setWebsiteSource(cr1.dang_select_WebsiteSource());
//           
//           csvWriter.dang_AppendData(ar);
//       }
//             
//    }
    
	

}
