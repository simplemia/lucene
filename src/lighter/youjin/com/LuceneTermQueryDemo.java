package lighter.youjin.com;

import lighter.youjin.com.createIndex;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;


public class LuceneTermQueryDemo {
	public static final String FILES_TO_INDEX_DIRECTORY = "/home/mia/Documents";
	public static final String INDEX_DIRECTORY = "/home/mia/workspace/index";

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "content";

	public static void main(String[] args) throws Exception {

		new createIndex();
		searchIndex("QQ");
		searchIndexWithTermQuery("QQ");
		searchIndex("google");
		searchIndexWithTermQuery("google");
		searchIndex("QQ google");
		searchIndexWithTermQuery("QQ google");
	}



	public static void searchIndex(String searchString) throws IOException, ParseException {
		System.out.println("\nSearching for '" + searchString + "' using QueryParser"+"1111111111");
		IndexReader directory = DirectoryReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
		QueryParser queryParser = new QueryParser(Version.LUCENE_4_10_0, FIELD_CONTENTS, analyzer);
		Query query = queryParser.parse(searchString);
		System.out.println("Type of query: " + query.getClass().getSimpleName());
		TopDocs results = indexSearcher.search(query,20);
	    ScoreDoc[] hits = results.scoreDocs;
	    int numTotalHits = results.totalHits;
	    System.out.println("hits number"+numTotalHits);

	}

	public static void searchIndexWithTermQuery(String searchString) throws IOException, ParseException {
		System.out.println("\nSearching for '" + searchString + "' using TermQuery"+"22222222222");
		IndexReader directory = DirectoryReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		
		Term term = new Term(FIELD_CONTENTS, searchString);
		Query query = new TermQuery(term);
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
		QueryParser queryParser = new QueryParser(Version.LUCENE_4_10_0, FIELD_CONTENTS, analyzer);
		
		System.out.println("Type of query: " + query.getClass().getSimpleName());
		TopDocs results = indexSearcher.search(query,20);
	    ScoreDoc[] hits = results.scoreDocs;
	    int numTotalHits = results.totalHits;
	    System.out.println("hits number"+numTotalHits);



	}

	
}

