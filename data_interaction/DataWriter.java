package data_interaction;

import java.util.ArrayList;
import java.util.List;

import crawl_data.Article;

public interface DataWriter {
	public abstract void appendData(String id, String articleLink, String websiteSource, String articleType,
            String articleSummary, String articleTitle, String content, String date,
            String tagHash, String author, String category);
	public abstract void appendData(Article article);
	public abstract void appendLink(String linkPath, ArrayList<String> links);
	public abstract void updateLink(List<String> sttList, List<String> linkList, List<String> selectedList);
}
