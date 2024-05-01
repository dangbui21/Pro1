package crawl_Data;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Thread;
import data_interaction.CsvReader;
import data_interaction.CsvWriter;

public class Crawl {
	
    public static void main(String[] args) {
		String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";   
		String webLink = "https://blockchain.news";
		String articleLink;
		String dataPath = "D:/Workspace/Java/Pro1/data/data.csv";
        String linkPath = "D:/Workspace/Java/Pro1/data/link.csv";
        // Thiết lập ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setBinary(chromePath); // Đặt đường dẫn tới chrome.exe
        // Khởi tạo WebDriver với ChromeOptions
        WebDriver driver = new ChromeDriver(options);
        // Mở trang web
        driver.get(webLink);  
        // Wait để đảm bảo trang web được tải hoàn toàn
        try {
            Thread.sleep(4000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            
        
        //Load them bai viet
        Random random = new Random();
//        for(int i=0 ; i<3 ; i++) {
//        	// Tìm phần tử chứa nút "Load More Posts"
//            WebElement loadMoreButton = driver.findElement(By.id("btnLoadMore"));
//            // Thực hiện click vào nút "Load More Posts"
//            loadMoreButton.click();
//            
//            // ngủ từ 2 đến 4 giây
//            int randomDelay = random.nextInt(4) + 3;
//            try {
//                Thread.sleep(randomDelay * 1000);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt(); 
//            }
//        }
        
        // Lấy HTML của trang web đã được tải hoàn toàn
        String html = driver.getPageSource();       
        // Sử dụng Jsoup để phân tích HTML
        Document document = Jsoup.parse(html);
        // Tìm tất cả các phần tử có class là "h5.entry-title"
        Elements entryTitles = document.select("h4.card-title");
        
        //Lưu lại các link bài viết
        ArrayList<String> links = new ArrayList<>();
        for (Element entryTitle : entryTitles) {
            Element linkElement = entryTitle.select("a").first();
            String link = linkElement.attr("href");
            articleLink = webLink + link;
            links.add(articleLink);
    
        CsvWriter writer = new CsvWriter(dataPath);
        writer.dang_saveLink(linkPath, links);
        
        // Duyệt qua các phần tử và lấy văn bản trong đó 
        int i = CsvReader.dang_countLines(dataPath);//số hàng đang có trong file csv
        for (Element entry : entryTitles) {     
        	Element elink = entry.select("a").first();
        	String slink = elink.attr("href");
        	Crawl_in_one crOne = new Crawl_in_one();
        	articleLink = webLink + link;
        	System.out.println("articleLink : " + articleLink);
        	//xử lý khi duyệt bị lỗi
        	try {
                driver.get(articleLink); 
                String html1 = driver.getPageSource();               
                // ngủ từ 3 đến 6 giây
                int randomDelay = random.nextInt(4) + 3;
                try {
                    Thread.sleep(randomDelay * 1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); 
                }
                
                crOne.dang_Crawl_one(i, chromePath, dataPath, webLink, articleLink, html1);
                
            } catch (Exception e) {
                // Xử lý lỗi khi duyệt web
                System.err.println("Error occurred while crawling web: " + e.getMessage());
                // Tiếp tục vòng lặp với phần tử tiếp theo
                continue;
            }
            i++;          
        }
    }
}
}
