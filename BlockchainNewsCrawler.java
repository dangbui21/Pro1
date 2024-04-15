import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class BlockchainNewsCrawler {
	public static void main(String[] args) {
        String url = "https://blockchain.news";
        try {
            // Kết nối tới trang web và lấy dữ liệu HTML
            Document doc = Jsoup.connect(url).timeout(7000).get();

            // In ra toàn bộ mã HTML của trang web
            System.out.println(doc.html());
            
            Elements titles = doc.select("h5.entry-title");

            // Duyệt qua danh sách các thẻ h5 đã tìm thấy
            for (Element title : titles) {
                // Lấy nội dung của thẻ a trong thẻ h5
                String content = title.select("a").text();
                // In ra nội dung của thẻ a
                System.out.println("Content: " + content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        try {
//            // Kết nối tới trang web và lấy dữ liệu HTML
//            Document document = Jsoup.connect(url).timeout(5000).get();
//
//            // Lấy danh sách các tiêu đề tin tức
//            Elements pages = document.select("a[href=/news/blackrock-expands-bitcoin-etf-operations-with-five-major-wall-street-firms]");
//            
//            System.out.println(pages.text());
//            // In ra các tiêu đề tin tức
//            System.out.println("Blockchain News Headlines:");
//            for (Element page : pages) {
//                System.out.println("- " + page.text());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
       
   
}
