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

import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import dataMining.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Date;

import data_interaction.CsvWriter;

public class Crawl_in_one {	
	private String articleType;
	private String articleTitle;
	private String summaryText;
	private String content;
	private String dateString;
	private String tagsString;
	private String author;
	
	
	public Crawl_in_one() {
		
	}
	
	public String dang_select_ArticleType(String webLink) {
		String articleType = "unknown" ;
        if(webLink == "https://blockchain.news") {
        	articleType = "News Article";
        }
        System.out.println("articleType : " + articleType);
        return articleType;
	}
	
	public String dang_select_Title(String key ,Document document) {
		Element title = document.selectFirst(key);
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        return articleTitle;
	}
	
	public String dang_select_Summary(String key , Document document) {
		Element summary = document.selectFirst("p.text-size-big");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        return summaryText;
	}
	
	public String dang_select_Content(String key , Document document) {
		Element div = document.selectFirst(key);
        String content = div.text();
        System.out.println("Nội dung: " + content);
        return content;
	}
	
	public String dang_select_Date(String key , Document document) {
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
        return dateString;
	}
	
	public String dang_select_Tags(String key , Document document) {
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
        return tagsString;
	}
	
	public String dang_select_Author(String key , Document document) {
		//select Author's
        Element aTag = document.selectFirst("a.entry-cat");
        String author = aTag.text();
        System.out.println("Author : " + author);
        return author;
	}
	
//	public void dang_Crawl_1() {
//		articleType = dang_select_ArticleType(articleTitle) 
//	}
	
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
        Element title = document.selectFirst("h1");
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        
        //select summary
        Element summary = document.selectFirst("p.lead");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        
        //select content
        Element div = document.selectFirst("div.col-lg-7 mb-5");
        String content = div.text();
        System.out.println("Nội dung: " + content);
        
        //select date
        Element timeElement = document.selectFirst("li.list-inline-item d-lg-block my-lg-2");
        String date = timeElement.text();
        System.out.println("Ngày và thời gian: " + dateString);       	      	
//        // Lấy ra các thành phần của ngày tháng
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthValue();
//        int day = dateTime.getDayOfMonth();
//        System.out.println("Ngày: " + day +  "Tháng :" + month + "Năm : "+ year);
                
        //select tag
        Elements tag = document.select("ul.list-inline");
        Elements tagElements = tag.select("a");
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
        Element aTag = document.selectFirst("div.position-relative");
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
	
	public void dang_Crawl_1(int i, String chromePath, String dataPath , String webLink ,String articleLink , String html){

        // Sử dụng Jsoup để phân tích HTML
        Document document = Jsoup.parse(html);
               
        Article ar = new Article();
        CsvWriter csvWriter = new CsvWriter(dataPath);
        
        //tạo articleType theo hiểu biết về trang lấy dữ liệu
        String articleType = "unknown" ;
        if(webLink == "https://blockchain.news") {
        	articleType = "News Article";
        }else if(webLink == "https://www.blockchain.com/blog"){
        	articleType = "Blog";
        }
        System.out.println("articleType : " + articleType);
        
      //select title
        Element title = document.selectFirst("h4.sc-1f19948b-5.fOSzTG");
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        
        //select summary
        Element summary = document.selectFirst("p.sc-1f19948b-6.gTWyeq");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        
        //select content
        Element div = document.selectFirst("div.sc-1f19948b-7.fWlASx");
        String content = div.text();
        System.out.println("Nội dung: " + content);
        
        //select date
        Element timeElement = document.selectFirst("p.sc-cba31b2d-8.bEKtyJ");
        String dateString = timeElement.text();
        System.out.println("Ngày và thời gian: " + dateString);       	      	
//        // Lấy ra các thành phần của ngày tháng
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthValue();
//        int day = dateTime.getDayOfMonth();
//        System.out.println("Ngày: " + day +  "Tháng :" + month + "Năm : "+ year);
                
        //select tag
//        Elements tagElements = document.select("div.tagcloud a");
//        StringBuilder tagStringBuilder = new StringBuilder();
//        for (Element tagElement : tagElements) {
//            String tagText = tagElement.text();
//            tagStringBuilder.append(tagText).append("/");
//        }
//        // Loại bỏ dấu "/" cuối cùng nếu có
//        if (tagStringBuilder.length() > 0) {
//            tagStringBuilder.deleteCharAt(tagStringBuilder.length() - 1);
//        }
//        
//        String tagsString = tagStringBuilder.toString();
//        System.out.println("Tags: " + tagsString);
//        
        //select Author's
        Element aTag = document.selectFirst("span.sc-cba31b2d-7.gPdnYn");
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
//        ar.setTagHash(tagsString);
        ar.setAuthor(author);   
//        System.out.println("data : " + ar.toString());
        
        //save data
        csvWriter.dang_AppendData(ar);  
        ar = null;         
	}
	
	private static String parseDateTime(String datetime) {
        try {
            // Định dạng của thời gian ban đầu
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            // Định dạng mới
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Chuyển đổi định dạng
            Date date = originalFormat.parse(datetime);

            // Chuyển đổi sang định dạng chuẩn
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
	}
	
    public static void main(String[] args) {
        String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";   
        String webLink = "https://www.cnbc.com/blockchain";
        String articleLink = "https://www.cnbc.com/2024/01/31/george-osborne-ex-uk-finance-minister-joins-coinbase-crypto-exchange.html";
        String dataPath = "D:/Workspace/Java/Pro1/data/data.csv";
        
//        // Thiết lập ChromeOptions
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary(chromePath); // Đặt đường dẫn tới chrome.exe
//        // Khởi tạo WebDriver với ChromeOptions
//        WebDriver driver = new ChromeDriver(options);
//        // Mở trang web
//        driver.get(articleLink);  
//        // Wait để đảm bảo trang web được tải hoàn toàn
//        try {
//            Thread.sleep(4000); 
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // Lấy HTML của trang web đã được tải hoàn toàn
//        String html = driver.getPageSource();       
//
//        // Sử dụng Jsoup để phân tích HTML
//        Document document = Jsoup.parse(html);
//        // Tìm tất cả các phần tử có class là "h5.entry-title"
        
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
        CsvWriter csvWriter = new CsvWriter(dataPath);
        
        //tạo articleType theo hiểu biết về trang lấy dữ liệu
        String articleType = "unknown" ;
        if(webLink == "https://blockchain.news") {
        	articleType = "News Article";
        }else if(webLink == "https://www.blockchain.com/blog"){
        	articleType = "Blog";
        }
        System.out.println("articleType : " + articleType);
        
        //select title
        Element title = document.selectFirst("h1.ArticleHeader-headline");
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        
        //select summary
        Element summary = document.selectFirst("div.group");
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
     // Lấy phần tử ul
//        String summaryText;
//        Element ulElement = document.select("ul").first();
//        if (ulElement != null) {
//            StringBuilder summaryBuilder = new StringBuilder();
//            Elements liElements = ulElement.select("li");
//            for (Element liElement : liElements) {
//                String summary = liElement.text();
//                summaryBuilder.append(summary).append("\n");
//            }
//            summaryText = summaryBuilder.toString().trim();
//
//            System.out.println("Tóm tắt của bài viết:");
//            System.out.println(summaryText);
//        } else {
//            System.out.println("Không tìm thấy phần tử ul.");
//        }
        
        
        
        //select content
        Element div = document.selectFirst("div.ArticleBody-articleBody");
        String content = div.text();
        System.out.println("Nội dung: " + content);
        
        //select date
        Element timeElement = document.selectFirst("time[datetime]");
        String datetime = timeElement.attr("datetime");
       // Chuyển đổi định dạng ngày tháng
        String dateString = parseDateTime(datetime);
        
        System.out.println("Ngày và thời gian: " + dateString);       	      	
//        // Lấy ra các thành phần của ngày tháng
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthValue();
//        int day = dateTime.getDayOfMonth();
//        System.out.println("Ngày: " + day +  "Tháng :" + month + "Năm : "+ year);
                
        //select tag
//        Elements tagElements = document.select("div.tagcloud a");
//        StringBuilder tagStringBuilder = new StringBuilder();
//        for (Element tagElement : tagElements) {
//            String tagText = tagElement.text();
//            tagStringBuilder.append(tagText).append("/");
//        }
//        // Loại bỏ dấu "/" cuối cùng nếu có
//        if (tagStringBuilder.length() > 0) {
//            tagStringBuilder.deleteCharAt(tagStringBuilder.length() - 1);
//        }
//        
//        String tagsString = tagStringBuilder.toString();
//        System.out.println("Tags: " + tagsString);
//        
        //select Author's
        Element aTag = document.selectFirst("a.Author-authorName");
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
//        ar.setTagHash(tagsString);
        ar.setAuthor(author);
        
        System.out.println("data : " + ar.toString());
        
        //save data
//        csvWriter.dang_AppendData(ar);

    }
}
