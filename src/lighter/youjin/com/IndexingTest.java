package lighter.youjin.com;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase; 

import org.apache.lucene.analysis.core.WhitespaceAnalyzer; 
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.Version;

/**
 * 示例代码：
 * Lucene中document的基本操作
 * (新增、删除、更新)
 */
public class IndexingTest extends TestCase {

	protected String[] ids = { "1", "2" };
	protected String[] unindexed = { "Netherlands", "Italy" };
	protected String[] unstored = { 
			"Amsterdam has lots of bridges",
			"Venice has lots of canals" };
	protected String[] text = { "Amsterdam", "Venice" };

	private Directory directory;

	//---------------------------------------------------------------【新增】
	public void setUp() throws IOException {
		 directory = new RAMDirectory();
		// A setUp()方法会在每一个测试之前运行，它先创建了一个RAMDirectory用来存储索引信息
		// B 参见 D#
		IndexWriter writer = getWriter(); 
		
		// C 遍历所有内容，创建Document及Fields并将Document加入索引
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new Field("id", ids[i], Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("country", unindexed[i], Field.Store.YES,
					Field.Index.NO));
			doc.add(new Field("contents", unstored[i], Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("city", text[i], Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
		}
		writer.close();
	}

	// D 在RAMDirectory上创建IndexWriter
	public IndexWriter getWriter() throws IOException { 
		IndexWriterConfig cfg=new IndexWriterConfig(Version.LUCENE_40,new WhitespaceAnalyzer(Version.LUCENE_40));
	    cfg.setOpenMode(OpenMode.CREATE);
	    Directory dir = directory;
	    IndexWriter writer=new IndexWriter(dir,cfg);
		return writer;
	}

	public int getHitCount(String fieldName, String searchString)
			throws IOException {
		Directory dir = directory;
		// E 创建Searcher
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir)); 
		Term t = new Term(fieldName, searchString);
		// F 创建简单的单一Term的查询
		Query query = new TermQuery(t); 
		// G 获取结果集的数量
		//int hitCount = TestUtil.hitCount(searcher, query); 
		TopDocs results = searcher.search(query,20);
	    ScoreDoc[] hits = results.scoreDocs;
	    int numTotalHits = results.totalHits;
	    System.out.println(numTotalHits+" answer");
		return numTotalHits;
	}

	public void testIndexWriter() throws IOException {
		IndexWriter writer = getWriter();
		// H 验证writer中document的数量
		System.out.println(writer.numDocs());
		System.out.println(ids.length);
		System.out.println("2222222222222");
		assertEquals(ids.length, writer.numDocs()); 
		writer.close();
	}

	public void testIndexReader() throws IOException {
		//Directory dir = FSDirectory.open(new File(directory));
		Directory dir = directory;
		// E 创建Searcher
		IndexReader reader = IndexReader.open(dir);
		
		System.out.println(ids.length);
		System.out.println(reader.maxDoc());
	    System.out.println(reader.numDocs());
	    System.out.println("3333333333333");
		// I 验证searcher中document的数量
		assertEquals(ids.length, reader.maxDoc());
		assertEquals(ids.length, reader.numDocs());
		reader.close();
	}
	
	//---------------------------------------------------------------【删除】
	public void testDeleteBeforeIndexMerge() throws IOException {
		IndexWriter writer = getWriter();
		// 1 索引中有2个document
		assertEquals(2, writer.numDocs());
		// 2 删除第一个document
		writer.deleteDocuments(new Term("id", "1"));
		writer.commit();
		// 3 验证索引中包含了删除操作
		assertTrue(writer.hasDeletions()); 
		// 4 一个被索引的document 一个被删除的document
		assertEquals(2, writer.maxDoc());
		// 5 仅有一个被索引的document
		assertEquals(1, writer.numDocs());
		writer.close();
	}

	public void testDeleteAfterIndexMerge() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2, writer.numDocs());
		writer.deleteDocuments(new Term("id", "1"));
		// 5 优化压缩索引
		writer.commit();
		assertFalse(writer.hasDeletions());
		// 6 合并后仅剩一个被索引的document
		assertEquals(1, writer.maxDoc()); 
		assertEquals(1, writer.numDocs());
		writer.close();
	}
	
	//---------------------------------------------------------------【更新】
	public void testUpdate() throws IOException {
		assertEquals(1, getHitCount("city", "Amsterdam"));
		IndexWriter writer = getWriter();
		
		// 1 创建新的document用来更新, 其city域为
		Document doc = new Document();
		doc.add(new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("country", "Netherlands", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("contents", "Amsterdam has lots of bridges", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("city", "Haag", Field.Store.YES, Field.Index.ANALYZED));
		
		// 2 用新创建的document替换之前的
		writer.updateDocument(new Term("id", "1"), doc);
		writer.close();
		// 3 验证原来的document不存在了
		System.out.println(getHitCount("city", "Amsterdam"));
		System.out.println(getHitCount("city", "Haag"));

	}


}
