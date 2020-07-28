package kr.ac.kaist.ir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.opencsv.CSVWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

public class App {
    public static void main(String[] args) throws IOException, ParseException
    {
        //Create a new index and open a writer
        Directory dir = FSDirectory.open(Paths.get("C:\\Users\\kyoun\\Downloads\\indexes\\lucene-index.robust04.pos+docvectors+raw"));
        Analyzer analyzer = new StandardAnalyzer();

        //Open an IndexSearcher
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        //Create a query
        QueryParser parser = new QueryParser("*", analyzer);
        Query query = parser.parse("*");

        // csv writer
        CSVWriter writer = new CSVWriter(new FileWriter("new.csv"), '\t', '"', '"', "\n");

        //Search for results of the query in the index
        System.out.println("Searching for: \"" + query + "\"");
        Integer total = searcher.count(query);
        System.out.println(total);
        TopDocs results = searcher.search(query, total);
        for (ScoreDoc result : results.scoreDocs) {
            Document resultDoc = searcher.doc(result.doc);
            String id = resultDoc.getField("id").stringValue();
            String contents = resultDoc.getField("contents").stringValue();
            String[] row = new String[]{id, contents};
            writer.writeNext(row);
//            System.out.println("score: " + result.score +
//                    " -- text: " + resultDoc);
        }
        reader.close();
        writer.close();
    }
}