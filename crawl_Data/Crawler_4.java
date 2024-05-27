package crawl_data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.openqa.selenium.interactions.Actions;

import data_interaction.*;



public class Crawler_4 extends Crawler{
    private String fileCSVPath = "D:/Workspace/Java/Pro1/data/4/data.csv";
    
	public void setKey() throws IOException {
		setKey_articleLink("meta[property*='og:url']");
        setKey_websiteSource("https://coindesk.com");
        setKey_articleType("meta[property*='og:type']");
        setKey_articleSummary("meta[name*='description']");
        setKey_articleTitle("meta[property*='og:title']");
        setKey_content("div[class*='at-text'] > p");
        setKey_date("meta[property*='article:modified_time']");
        setKey_tagHash("meta[property*='article:tag']");
        setKey_author("meta[property*='article:author']");
        setKey_category("meta[property*='article:section']");
	}
	
	
    String selectArticleLink(Document document) {
    	return document.selectFirst(key_articleLink).attr("content");
    }
    String selectWebsiteSource(Document document) {
    	return key_websiteSource;
    }
    
    String selectArticleType(Document document) throws IOException {
    	return document.selectFirst(key_articleType).attr("content");
    }
    
    String selectArticleSumary(Document document) throws IOException {
    	return document.selectFirst(key_articleSummary).attr("content");    }
    
    String selectArticleTitle(Document document) throws IOException {
    	return document.selectFirst(key_articleTitle).attr("content");
    }
    
    String selectContent(Document document) throws IOException {
    	String content = "";
    	Elements contents = document.select(key_content);
    	for (Element data : contents) content += data.text();
    	return content;
    }
    
    String selectDate(Document document) throws IOException {
    	String oldDateTime = document.selectFirst(key_date).attr("content");
    	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    	SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	try {
            // Chuyển đổi từ chuỗi sang đối tượng Date
            Date date = inputFormat.parse(oldDateTime);

            // Chuyển đổi đối tượng Date sang chuỗi theo định dạng mới
            String outputString = outputFormat.format(date);

            return (outputString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
    
    String selectTagHash(Document document) throws IOException {   
    	if (document.selectFirst(key_tagHash) == null) return "";
    	return document.selectFirst(key_tagHash).attr("content").replaceAll(",", "/");
    }
    
    String selectAuthor(Document document) throws IOException {
    	return document.selectFirst(key_author).attr("content");
    }
    
    String selectCategory(Document document) throws IOException {
    	return document.selectFirst(key_category).attr("content");
    }
    
    public void cralingAll(Document document, String stt) {
    	Article article = null;
		try {
			article = new Article(stt,selectArticleLink(document),
			selectWebsiteSource(document),
			selectArticleType(document),
			selectArticleSumary(document),
			selectArticleTitle(document),
			selectContent(document),
			selectDate(document),
			selectTagHash(document),
			selectAuthor(document),
			selectCategory(document));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	CsvWriter csvWriter = new CsvWriter(fileCSVPath);
    	csvWriter.appendData(article);
    }
    
    public void runCrawler() {
        try {
            String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";
            String urlToHome = "https://www.coindesk.com/search?s=blockchain";
            ChromeOptions options = new ChromeOptions();
            options.setBinary(chromePath);
            WebDriver driver = new ChromeDriver();
            driver.get(urlToHome);
            Actions act = new Actions(driver);
            WebElement showMore = driver.findElement(By.cssSelector("#queryly_advanced_container > div:nth-child(5) > div.Box-sc-1hpkeeg-0.eiOgYh > button:nth-child(8)"));
            Random random = new Random(); 
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements articleLinks = doc.select("a[class*='searchstyles__ImageContainer']");
            int i = 1;
            Crawler_4 cr = new Crawler_4();
            cr.setKey();
            while(true) {
                for (Element articleLink : articleLinks) {
                    String articleLinkk = "https://www.coindesk.com" + articleLink.attr("href");
                    if(articleLinkk.startsWith("https://www.coindesk.com/podcasts")) continue;
                    System.out.println(i + ": " + articleLinkk);
                    
                    try {
                        Document docArticle = Jsoup.connect(articleLinkk).timeout(10 * 1000).get();  
                        cr.cralingAll(docArticle, String.valueOf(i));
                        i++;
                        Thread.sleep(random.nextInt(500) + 500);
                    } catch (IOException e) {
                        System.out.println("Connection timed out or failed: " + e.getMessage());
                        continue;
                    }
                }
                act.click(showMore).build().perform();
                Thread.sleep(random.nextInt(750));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
		String chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe";
		String urlToHome = "https://www.coindesk.com/search?s=blockchain";
		ChromeOptions options = new ChromeOptions();
        options.setBinary(chromePath);
        WebDriver driver = new ChromeDriver();
        driver.get(urlToHome);
        Actions act = new Actions(driver);
        WebElement showMore = driver.findElement(By.cssSelector("#queryly_advanced_container > div:nth-child(5) > div.Box-sc-1hpkeeg-0.eiOgYh > button:nth-child(8)"));
        Random random = new Random(); 
		String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Elements articleLinks = doc.select("a[class*='searchstyles__ImageContainer']");
        int i = 1;
        Crawler_4 cr = new Crawler_4();
		cr.setKey();
        while(true) {
        for (Element articleLink : articleLinks) {
        	String articleLinkk = "https://www.coindesk.com" + articleLink.attr("href");
        	if(articleLinkk.startsWith("https://www.coindesk.com/podcasts")) continue;
        	System.out.println(i + ": " + articleLinkk);
        	//Document docArticle = Jsoup.connect(articleLinkk).timeout(30 * 1000).get();	
        	
        	try {
                Document docArticle = Jsoup.connect(articleLinkk).timeout(10 * 1000).get();	
                cr.cralingAll(docArticle, String.valueOf(i));
                i++;
                Thread.sleep(random.nextInt(500) + 500);
            } catch (IOException e) {
                System.out.println("Connection timed out or failed: " + e.getMessage());
                continue;
            }
            
        }
        act.click(showMore).build().perform();
        Thread.sleep(random.nextInt(750));
       }
    }
}
