package lighter.youjin.com;


import java.io.IOException;
import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchEngine{
	
public static void search(String indexDir, String q) throws IOException, ParseException{
	IndexReader dir = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
	IndexSearcher searcher  = new IndexSearcher(dir);
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
	QueryParser parser = new QueryParser(Version.LUCENE_4_10_0, "contents", analyzer);
    Query query = parser.parse(q);
    TopDocs results = searcher.search(query,20);
    ScoreDoc[] hits = results.scoreDocs;
    int numTotalHits = results.totalHits;
    System.out.println(numTotalHits + " total matching documents");
    int start = 0;
    int end = 20;
    for (int i = start; i < end; i++) {
    Document doc = searcher.doc(hits[i].doc);
    String path = doc.get("path");
    if (path != null) {
        System.out.println((i+1) + ". " + path);
        String title = doc.get("title");
        if (title != null) {
          System.out.println("   Title: " + doc.get("title"));
        }
      }  
    }
  }
public static void main(String[] args) throws IllegalArgumentException,IOException, ParseException{
	String indexDir = "/home/mia/workspace/index";
	String q = "google";
	search(indexDir,q);	
}

}