package crawl_Data;

import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dataMining.Article;
import data_interaction.CsvReader;
import data_interaction.CsvWriter;

public class Crawler_1 extends Crawler {
	
	// Default constructor
    public Crawler_1() {
        super();
        String webLink = "https://www.blockchain.com/blog";
    }
	 
//    public Crawler_1(String articleLink, String websiteSource, String articleType,
//                   String articleSummary, String articleTitle, String content,
//                   String date, String tagHash, String author, String category) {
//        super(articleLink, websiteSource, articleType, articleSummary, articleTitle, content, date, tagHash, author, category);
//    }
    
    // Hàm setKey để gán giá trị cho tất cả các thuộc tính kế thừa từ lớp Crawler
    public void setKey(String articleLink) {
        setKey_articleLink(articleLink);
        setKey_websiteSource("https://www.blockchain.com/blog");
        setKey_articleType("Blog");
        setKey_articleSummary("p.sc-1f19948b-6.gTWyeq");
        setKey_articleTitle("h4.sc-1f19948b-5.fOSzTG");
        setKey_content("div.sc-1f19948b-7.fWlASx");
        setKey_date("p.sc-cba31b2d-8.bEKtyJ");
        setKey_tagHash("unkown");
        setKey_author("span.sc-cba31b2d-7.gPdnYn");
        setKey_category("unkown");
    }

    
    public static void main(String[] args) {   
        String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";   
        String articleLink = "https://www.blockchain.com/blog/posts/pay-metamask";
        String dataPath = "D:/Workspace/Java/Pro1/data/data.csv";
               
        // Thiết lập ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setBinary(chromePath); // Đặt đường dẫn tới chrome.exe
        // Khởi tạo WebDriver với ChromeOptions
        WebDriver driver = new ChromeDriver(options);
        // Mở trang web
        driver.get(articleLink);  
        // Wait để đảm bảo trang web được tải hoàn toàn
        try {
            Thread.sleep(4000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
      //Load them bai viet
        Random random = new Random();
        for(int i=0 ; i<100 ; i++) {
        	// Tìm phần tử chứa nút "Load More Posts"
            WebElement loadMoreButton = driver.findElement(By.id("btnLoadMore"));
            // Thực hiện click vào nút "Load More Posts"
            loadMoreButton.click();
            
            // ngủ từ 4 đến 8 giây
            int randomDelay = random.nextInt(4) + 4;
            try {
                Thread.sleep(randomDelay * 1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); 
            }
        }
        
        // Lấy HTML của trang web đã được tải hoàn toàn
        String html = driver.getPageSource();       

        // Sử dụng Jsoup để phân tích HTML
        Document document = Jsoup.parse(html);
       
               
        Article ar = new Article();
        CsvWriter csvWriter = new CsvWriter(dataPath);
        // Khởi tạo một đối tượng Crawl_1 với constructor mặc định
        Crawler_1 cr1 = new Crawler_1();
        cr1.setKey(articleLink);
        
        // Duyệt qua các phần tử và lấy văn bản trong đó 
        int i = CsvReader.dang_countLines(dataPath);//số hàng đang có trong file csv
        
        ar.setId(i);
        ar.setArticleLink(cr1.dang_select_ArticleLink()); 
        ar.setArticleTitle(cr1.dang_select_Title(document));
        ar.setArticleSummary(cr1.dang_select_Summary(document));
        ar.setArticleType(cr1.dang_select_ArticleType());
        ar.setAuthor(cr1.dang_select_Author(document));
        ar.setCategory(cr1.dang_select_Category(document));
        ar.setContent(cr1.dang_select_Content(document));
        ar.setDate(cr1.dang_select_Date(document));
        ar.setTagHash(cr1.dang_select_Tags(document));
        ar.setWebsiteSource(cr1.dang_select_WebsiteSource());
        
        csvWriter.dang_AppendData(ar);
        
    }
    
    
	

}
