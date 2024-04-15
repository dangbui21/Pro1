package crawl_Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.lang.Thread;
import dataMining.Article;


import data_interaction.CsvWriter;

public class Crawl_in_one {	
	public Crawl_in_one() {
		
	}
	
	public void dang_Crawl_one(int i, String chromePath, String dataPath , String webLink ,String articleLink , String html){

        // Sử dụng Jsoup để phân tích HTML
        Document document = Jsoup.parse(html);
               
        Article ar = new Article();
        CsvWriter csvWriter = new CsvWriter(dataPath);
        
        //tạo articleType theo hiểu biết về trang lấy dữ liệu
        String articleType = "unknown" ;
        if(webLink == "https://blockchain.news") {
        	articleType = "News Article";
        }
        System.out.println("articleType : " + articleType);
        
        //select title
        Element title = document.selectFirst("h2.title");
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        
        //select summary
        Element summary = document.selectFirst("p.text-size-big");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        
        //select content
        Element div = document.selectFirst("div.textbody");
        String content = div.text();
        System.out.println("Nội dung: " + content);
        
        //select date
        Element timeElement = document.selectFirst("time.entry-date");
        String dateString = timeElement.attr("datetime");
        System.out.println("Ngày và thời gian: " + dateString);       	      	
//        // Lấy ra các thành phần của ngày tháng
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthValue();
//        int day = dateTime.getDayOfMonth();
//        System.out.println("Ngày: " + day +  "Tháng :" + month + "Năm : "+ year);
                
        //select tag
        Elements tagElements = document.select("div.tagcloud a");
        StringBuilder tagStringBuilder = new StringBuilder();
        for (Element tagElement : tagElements) {
            String tagText = tagElement.text();
            tagStringBuilder.append(tagText).append("/");
        }
        // Loại bỏ dấu "/" cuối cùng nếu có
        if (tagStringBuilder.length() > 0) {
            tagStringBuilder.deleteCharAt(tagStringBuilder.length() - 1);
        }
        
        String tagsString = tagStringBuilder.toString();
        System.out.println("Tags: " + tagsString);
        
        //select Author's
        Element aTag = document.selectFirst("a.entry-cat");
        String author = aTag.text();
        System.out.println("Author : " + author);
        
        // Thiết lập các thuộc tính cho đối tượng Article
        ar.setId(i);
        ar.setArticleLink(articleLink);
        ar.setWebsiteSource(webLink);
        ar.setArticleType(articleType);
        ar.setArticleTitle(articleTitle);
        ar.setArticleSummary(summaryText);
        ar.setContent(content);
        ar.setDate(dateString);
        ar.setTagHash(tagsString);
        ar.setAuthor(author);   
//        System.out.println("data : " + ar.toString());
        
        //save data
        csvWriter.dang_AppendData(ar);  
        ar = null;         
	}
	

	
    public static void main(String[] args) {
        String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";   
        String webLink = "https://blockchain.news";
        String articleLink = "https://blockchain.news/news/ethena-ena-labs-integrates-with-major-exchanges-for-reward-program";
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
        // Lấy HTML của trang web đã được tải hoàn toàn
        String html = driver.getPageSource();       

        // Sử dụng Jsoup để phân tích HTML
        Document document = Jsoup.parse(html);
        // Tìm tất cả các phần tử có class là "h5.entry-title"
               
        Article ar = new Article();
        CsvWriter csvWriter = new CsvWriter(dataPath);
        
        //tạo articleType theo hiểu biết về trang lấy dữ liệu
        String articleType = "unknown" ;
        if(webLink == "https://blockchain.news") {
        	articleType = "News Article";
        }
        System.out.println("articleType : " + articleType);
        
        //select title
        Element title = document.selectFirst("h2.title");
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        
        //select summary
        Element summary = document.selectFirst("p.text-size-big");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        
        //select content
        Element div = document.selectFirst("div.textbody");
        String content = div.text();
        System.out.println("Nội dung: " + content);
        
        //select date
        Element timeElement = document.selectFirst("time.entry-date");
        String dateString = timeElement.attr("datetime");
        System.out.println("Ngày và thời gian: " + dateString);       	      	
//        // Lấy ra các thành phần của ngày tháng
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthValue();
//        int day = dateTime.getDayOfMonth();
//        System.out.println("Ngày: " + day +  "Tháng :" + month + "Năm : "+ year);
                
        //select tag
        Elements tagElements = document.select("div.tagcloud a");
        StringBuilder tagStringBuilder = new StringBuilder();
        for (Element tagElement : tagElements) {
            String tagText = tagElement.text();
            tagStringBuilder.append(tagText).append("/");
        }
        // Loại bỏ dấu "/" cuối cùng nếu có
        if (tagStringBuilder.length() > 0) {
            tagStringBuilder.deleteCharAt(tagStringBuilder.length() - 1);
        }
        
        String tagsString = tagStringBuilder.toString();
        System.out.println("Tags: " + tagsString);
        
        //select Author's
        Element aTag = document.selectFirst("a.entry-cat");
        String author = aTag.text();
        System.out.println("Author : " + author);
        
        int i=1;
        // Thiết lập các thuộc tính cho đối tượng Article
        ar.setId(i);
        ar.setArticleLink(articleLink);
        ar.setWebsiteSource(webLink);
        ar.setArticleType(articleType);
        ar.setArticleTitle(articleTitle);
        ar.setArticleSummary(summaryText);
        ar.setContent(content);
        ar.setDate(dateString);
        ar.setTagHash(tagsString);
        ar.setAuthor(author);
        
        System.out.println("data : " + ar.toString());
        
        //save data
        csvWriter.dang_AppendData(ar);

    }
}
