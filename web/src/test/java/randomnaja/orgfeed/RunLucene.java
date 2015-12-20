package randomnaja.orgfeed;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RunLucene {

    @Test
    public void test() throws Exception {
//        indexDirectory();
//        search("java");

        indexText();
        searchMyIndex("หลังคาไวนิล");
        searchMyIndex("ไวนิล");
        searchMyIndex("ระแนงเหล็ก");

//        List<String> l = tokenizeString(new ThaiAnalyzer(), "หลังคาไวนิล ซ่อนแผ่น ระแนงเหล็ก รอบ");
//        System.out.println(Arrays.toString(l.toArray()));
    }

    public static List<String> tokenizeString(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<String>();
        try {
            TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }

    private static void indexText() throws Exception {
        if (! new File("/tmp/indexes").exists()) {
            new File("/tmp/indexes").mkdir();
        }
        Path path = Paths.get("/tmp/indexes");
        Directory directory = FSDirectory.open(path);
        IndexWriterConfig config = new IndexWriterConfig(new ThaiAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteAll();

        Document doc = new Document();
        doc.add(new TextField("msg", "หลังคาไวนิล ซ่อนแผ่น ระแนงเหล็ก รอบ", Store.YES));
        doc.add(new TextField("id", "1234", Store.YES));

        indexWriter.addDocument(doc);

        indexWriter.close();
        directory.close();
    }

    private static void searchMyIndex(String text) throws Exception {
        System.out.println("Search for " + text);
        Path path = Paths.get("/tmp/indexes");
        Directory directory = FSDirectory.open(path);
        IndexReader indexReader =  DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("msg",  new ThaiAnalyzer());
        Query query = queryParser.parse(text);
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("totalHits " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("id " + document.get("id"));
            System.out.println("msg " + document.get("msg"));
            //System.out.println("content " + document.get("contents"));
        }
        directory.close();
    }

    private static void indexDirectory() {
        //Apache Lucene Indexing Directory .txt files
        try {
            //indexing directory
            Path path = Paths.get("/tmp/indexes");
            Directory directory = FSDirectory.open(path);
            IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
            IndexWriter indexWriter = new IndexWriter(directory, config);
            indexWriter.deleteAll();
            File f = new File("/home/tone/Documents"); // current directory
            for (File file : f.listFiles()) {
                if (! file.isFile()) {
                    continue;
                }
                System.out.println("indexed " + file.getCanonicalPath());
                Document doc = new Document();
                doc.add(new TextField("path", file.getName(), Store.YES));
                FileInputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while((line = reader.readLine())!=null){
                    stringBuffer.append(line).append("\n");
                }
                reader.close();
                doc.add(new TextField("contents", stringBuffer.toString(), Store.YES));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
            directory.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static void search(String text) {
        //Apache Lucene searching text inside .txt files
        try {
            Path path = Paths.get("/tmp/indexes");
            Directory directory = FSDirectory.open(path);
            IndexReader indexReader =  DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            QueryParser queryParser = new QueryParser("contents",  new StandardAnalyzer());
            Query query = queryParser.parse(text);
            TopDocs topDocs = indexSearcher.search(query,10);
            System.out.println("totalHits " + topDocs.totalHits);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("path " + document.get("path"));
                //System.out.println("content " + document.get("contents"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
