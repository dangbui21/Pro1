package crawl_Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Crawler {
	protected String key_articleLink;
    protected String key_websiteSource;
    protected String key_articleType;
    protected String key_articleSummary;
    protected String key_articleTitle;
    protected String key_content;
    protected String key_date;
    protected String key_tagHash;
    protected String key_author;
    protected String key_category;
    protected final  String chromePath ;   
    protected final  String dataPath ;
    protected final  String linkPath ;
    
    // Constructor
    public Crawler() {
    	this.key_articleLink = "unknow";
        this.key_websiteSource = "unknow";
        this.key_articleType = "unknow";
        this.key_articleSummary = "unknow";
        this.key_articleTitle = "unknow";
        this.key_content = "unknow";
        this.key_date = "unknow";
        this.key_tagHash = "unknow";
        this.key_author = "unknow";
        this.key_category = "unknow";
        this.chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe"; 
        this.dataPath = "D:/Workspace/Java/Pro1/data/1/data.csv";
        this.linkPath = "D:/Workspace/Java/Pro1/data/1/links.csv";
    }
    
    public Crawler(String dataPath, String linkPath) {
    	this.key_articleLink = "unknow";
        this.key_websiteSource = "unknow";
        this.key_articleType = "unknow";
        this.key_articleSummary = "unknow";
        this.key_articleTitle = "unknow";
        this.key_content = "unknow";
        this.key_date = "unknow";
        this.key_tagHash = "unknow";
        this.key_author = "unknow";
        this.key_category = "unknow";
        this.chromePath = "D:/Workspace/Java/Pro1/chrome-win64/chrome.exe"; 
        this.dataPath = dataPath;
        this.linkPath = linkPath;
    }
    
    public Crawler(String key_articleLink, String key_websiteSource, String key_articleType,
            String key_articleSummary, String key_articleTitle, String key_content,
            String key_date, String key_tagHash, String key_author, String key_category,
            String chromePath, String dataPath, String linkPath) {
		 this.key_articleLink = key_articleLink;
		 this.key_websiteSource = key_websiteSource;
		 this.key_articleType = key_articleType;
		 this.key_articleSummary = key_articleSummary;
		 this.key_articleTitle = key_articleTitle;
		 this.key_content = key_content;
		 this.key_date = key_date;
		 this.key_tagHash = key_tagHash;
		 this.key_author = key_author;
		 this.key_category = key_category;
		 this.chromePath = chromePath; 
		 this.dataPath = dataPath; 
		 this.linkPath = linkPath; 
}
   
    //select
    public String dang_select_ArticleLink() {
    	if(key_articleLink == "unkown") return "unkown";
    	System.out.println("Link: " + key_articleLink);
    	return key_articleLink;
    }
    
    public String dang_select_WebsiteSource() {
    	if(key_websiteSource == "unkown") return "unkown";
    	System.out.println("WebSource: " + key_websiteSource);
    	return key_websiteSource;
    }
    
    public String dang_select_ArticleType() {
    	if(key_articleType == "unkown") return "unkown";
    	System.out.println("Type: " + key_articleType);
    	return key_articleType;
	}
	
	public String dang_select_Summary(Document document) {
		if(key_articleSummary == "unkown") return "unkown";
		Element summary = document.selectFirst(key_articleSummary);
        String summaryText = summary.text();
        System.out.println("Nội dung tóm tắt: " + summaryText);
        return summaryText;
	}
	
	public String dang_select_Title(Document document) {
		if(key_articleTitle == "unkown") return "unkown";
		Element title = document.select(key_articleTitle).first();
        String articleTitle = title.text();
        System.out.println("Title : " + articleTitle);
        return articleTitle;
	}
	
	public String dang_select_Content(Document document) {
		if(key_content == "unkown") return "unkown";
		Element div = document.selectFirst(key_content);
        String content = div.text();
        System.out.println("Nội dung: " + content);
        return content;
	}
	
	public String dang_select_Date(Document document) {
		if(key_date == "unkown") return "unkown";
		Element timeElement = document.selectFirst(key_date);
        String dateString = timeElement.text();
        System.out.println("Ngày và thời gian: " + dateString);      	      	
        return dateString;
	}
	
	public String dang_select_Tags(Document document) {
		//select tag
		if(key_tagHash == "unkown") return "unkown";
        Elements tagElements = document.select(key_tagHash);
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
	
	public String dang_select_Author(Document document) {
		//select Author's
		if(key_author == "unkown") return "unkown";
        Element aTag = document.selectFirst(key_author);
        String author = aTag.text();
        System.out.println("Author : " + author);
        return author;
	}
	
	public String dang_select_Category(Document document) {
        if(key_category == "unkown") return "unkown";
		Element cate = document.selectFirst(key_category);
        String category = cate.text();
        System.out.println("Category : " + category);
        return category;
	}
	
	//select link
	public ArrayList<String> selectLinks(Document document) {
		Elements divElements = document.select(key_articleLink);
        // ArrayList để lưu đường link
        ArrayList<String> links = new ArrayList<>();
        // Lặp qua các phần tử div tìm được
        for (Element divElement : divElements) {             
            String link = divElement.attr("href");
            links.add(link);                           
        }
        for (String link : links) {
            System.out.println("Link: " + link);
        }
        return links;
    }
	
	// chuyển đổi định dạng ngày tháng năm theo yy/mm/dd hh:mm:ss
	public static String parseDateTime(String datetime) {
        try {
            // Định dạng của thời gian ban đầu
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            // Định dạng mới
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = originalFormat.parse(datetime);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	
	
    // Getter and setter methods for each attribute
    // You can generate these automatically in most IDEs or write them manually like this:

    public String getKey_articleLink() {
        return key_articleLink;
    }

    public void setKey_articleLink(String key_articleLink) {
        this.key_articleLink = key_articleLink;
    }

    public String getKey_websiteSource() {
        return key_websiteSource;
    }

    public void setKey_websiteSource(String key_websiteSource) {
        this.key_websiteSource = key_websiteSource;
    }

    public String getKey_articleType() {
        return key_articleType;
    }

    public void setKey_articleType(String key_articleType) {
        this.key_articleType = key_articleType;
    }

    public String getKey_articleSummary() {
        return key_articleSummary;
    }

    public void setKey_articleSummary(String key_articleSummary) {
        this.key_articleSummary = key_articleSummary;
    }

    public String getKey_articleTitle() {
        return key_articleTitle;
    }

    public void setKey_articleTitle(String key_articleTitle) {
        this.key_articleTitle = key_articleTitle;
    }

    public String getKey_content() {
        return key_content;
    }

    public void setKey_content(String key_content) {
        this.key_content = key_content;
    }

    public String getKey_date() {
        return key_date;
    }

    public void setKey_date(String key_date) {
        this.key_date = key_date;
    }

    public String getKey_tagHash() {
        return key_tagHash;
    }

    public void setKey_tagHash(String key_tagHash) {
        this.key_tagHash = key_tagHash;
    }

    public String getKey_author() {
        return key_author;
    }

    public void setKey_author(String key_author) {
        this.key_author = key_author;
    }

    public String getKey_category() {
        return key_category;
    }

    public void setKey_category(String key_category) {
        this.key_category = key_category;
    }
    
    public String getChromePath() {
        return chromePath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getLinkPath() {
        return linkPath;
    }
    
}
