package lighter.youjin.com;



import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerDemo {

	private static final String[] examples = {"The quick brown fox jumped over the lazy dog",
		"xyz&corporation - xyz@example.com"};
	
	@SuppressWarnings("deprecation")
	private static final Analyzer[] analyzers = {
		new WhitespaceAnalyzer(Version.LUCENE_40),
		new SimpleAnalyzer(Version.LUCENE_40),
		new KeywordAnalyzer(),
		new StopAnalyzer(Version.LUCENE_40),
		new StandardAnalyzer(Version.LUCENE_40)
	};
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException,IllegalStateException {
		// TODO Auto-generated method stub
        String[] strings = examples;
        
        if(args.length > 0){
        	strings = args;
        }
        for(String text : strings){
        	analyze(text);
        }
        
	}
	
	public static void analyze(String text) throws IOException{
		for(Analyzer analyzer : analyzers){
			System.out.println(" "+analyzer.getClass().getSimpleName() + " ");
			AnalyzerUtils.displayTokes(analyzer, text);
			System.out.println();
		}
	}

}
