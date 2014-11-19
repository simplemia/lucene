package lighter.youjin.com;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class AnalyzerUtils {
    public static void displayTokes(Analyzer analyzer,String text) throws IOException{
    	displayTokes(analyzer.tokenStream("contents", new StringReader(text)));
    }
    
    public static void displayTokes(TokenStream tokenStream) throws IOException{
    	 CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
    	 tokenStream.reset();  //此行，不能少，不然会报 java.lang.ArrayIndexOutOfBoundsException
   	     while(tokenStream.incrementToken()){
   	    	 System.out.print("["+termAttribute.toString()+"]");
    	 }
   	  tokenStream.close();
    }
}