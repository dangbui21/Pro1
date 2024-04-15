package dataMining;

public class Article {
    private String id;
    private String articleLink;
    private String websiteSource;
    private String articleType;
    private String articleSummary;
    private String articleTitle;
    private String content;
    private String date;
    private String tagHash;
    private String author;
    private String category;

    // Constructor
    public Article() {
    	this.id = "unknown" ;
        this.articleLink = "unknown";
        this.websiteSource = "unknown";
        this.articleType = "unknown";
        this.articleSummary = "unknown";
        this.articleTitle = "unknown";
        this.content = "unknown";
        this.date = "unknown";
        this.tagHash = "unknown";
        this.author = "unknown";
        this.category = "unknown";
    }
    
    public Article(String id, String articleLink, String websiteSource, String articleType,
                   String articleSummary, String articleTitle, String content, String date,
                   String tagHash, String author, String category) {
        this.id = id;
        this.articleLink = articleLink;
        this.websiteSource = websiteSource;
        this.articleType = articleType;
        this.articleSummary = articleSummary;
        this.articleTitle = articleTitle;
        this.content = content;
        this.date = date;
        this.tagHash = tagHash;
        this.author = author;
        this.category = category;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setId(int id) {
        this.id = String.valueOf(id);
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getWebsiteSource() {
        return websiteSource;
    }

    public void setWebsiteSource(String websiteSource) {
        this.websiteSource = websiteSource;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTagHash() {
        return tagHash;
    }

    public void setTagHash(String tagHash) {
        this.tagHash = tagHash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Override toString() method to provide a readable representation of the object
    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", articleLink='" + articleLink + '\'' +
                ", websiteSource='" + websiteSource + '\'' +
                ", articleType='" + articleType + '\'' +
                ", articleSummary='" + articleSummary + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", tagHash='" + tagHash + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
