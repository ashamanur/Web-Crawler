import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author anusha
 * Web Crawler that starts crawling http://developer.ubuntu.com/start/- BASE.
 * Recursively crawls links on the pages encountered as long as the links contain the BASE. 
 * Stops when runs out of pages.
 * 
 * Output:
 * 1. The number of URLs you found
 * 2. A list of the unique URLs you found, sorted alphabetically
 */

public class Crawl {
	static int count=0;
	
	/**
	 * Grabs all the anchor tags in a given page.
	 * Checks if the links have the BASE URL and adds them to the LinkedHashSet.
	 * @param url
	 * The URL from which links must be grabbed
	 * @return URLset 
	 * The set of links encountered on that page.
	 */
	
	private static LinkedHashSet<String> grabURLs(String url) throws IOException {
		LinkedHashSet<String> URLset = new LinkedHashSet<String>();
        try
        {
        	Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			for(Element eachlink: links){
				String href = eachlink.attr("href");
				String check = "http://developer.ubuntu.com/";
				if(href.contains(check)){
					URLset.add(href);
					count=count+1;
				}
				
			}
		}
        catch (MalformedURLException e) {
            System.err.println("The URL to crawl is invalid.");
        }
        catch (IOException e) {
            System.err.println("An error occured while atempt to fetch content from " + url);
        }
        catch (IllegalArgumentException e){
        	
        }
	return URLset;
    	}
	
	/**
	 * Starts crawling from the BASE URL
	 * @param BaseURL
	 * @return urlList 
	 * The list of URLs found while crawling
	 */
	
	public static List<String> crawl(String BaseURL, int limit) throws IOException{
		List<String> urlList = new ArrayList<String>(limit);
		urlList.add(BaseURL);
		int i=0;
		/*
		 * Add links to a set to make sure we do not crawl the same page over and over.
		 */
		Set<String> crawlURL = new HashSet<String>(urlList);
		while(urlList.size() < limit&&i<urlList.size()){
			String currentURL = urlList.get(i);
			for(String url : grabURLs(currentURL)){
				if(crawlURL.add(url)){
					{
						urlList.add(url);
						if (urlList.size() == limit) {
	                        break;
	                    }
					}
				}
			}
			i++;
		}
		return urlList;
	}
	
	/**
	 * Starts the crawling process from the mentioned BASE URL.
	 * Sorts the list of URL obtained after crawling.
	 * Prints the total number of URLs found and the unique URLs found.
	 */
	
	public static void main(String[] args) throws IOException{
			String BASE = "http://developer.ubuntu.com/";
			
			int limit = 1000;
            long start = System.currentTimeMillis();
            List<String> UniqueURLs = Crawl.crawl(BASE, limit);
			long end = System.currentTimeMillis();
           	Collections.sort(UniqueURLs);
			int i = 1;
			Iterator<String> iterator = UniqueURLs.iterator();
			System.out.println("The number of URLs found in "+ (end-start) +" milli seconds is: "  +count);
			System.out.println("----------------------------------------------------------------------------------------------------------");
			System.out.println("Unique URLs found");
            while (iterator.hasNext()&&i<100) {
                System.out.println(i + ". " + iterator.next());
                i++;
            }
            
    }
}
