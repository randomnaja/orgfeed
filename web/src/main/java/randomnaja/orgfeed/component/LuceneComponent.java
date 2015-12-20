package randomnaja.orgfeed.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FieldValueQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import randomnaja.orgfeed.ejbweb.model.PostEntity;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LuceneComponent {

    private static final Logger log = LoggerFactory.getLogger(LuceneComponent.class);

    private static int MAX_TOP_SEARCH = 20;

    private Path indexPath;

    @PostConstruct
    void init() throws Exception {
        String tmpDir = System.getProperty("indexes.data.dir", System.getProperty("java.io.tmpdir", "/tmp"));
        String indexDir = tmpDir + "/indexes";
        if (! new File(indexDir).exists()) {
            new File(indexDir).mkdir();
        }
        indexPath = Paths.get(indexDir);
    }

    public void indexing(List<PostEntity> posts) throws IOException {
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(new ThaiAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, config);

        log.info("Indexing {} posts", posts.size());
        for (PostEntity each : posts) {

            if (StringUtils.isNotBlank(each.getMessage())) {
                Document doc = new Document();
                doc.add(new TextField("msg", each.getMessage(), Field.Store.YES));
                doc.add(new TextField("id", each.getId().toString(), Field.Store.YES));
                doc.add(new TextField("fbPageId", each.getFacebookPageId(), Field.Store.YES));
                log.debug("Indexing message[{}] of id [{}]", each.getMessage(), each.getId());

                indexWriter.addDocument(doc);
            }
        }

        indexWriter.close();
        directory.close();
    }

    public void deleteIndexing(String fbPageId) throws IOException, ParseException {
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(new ThaiAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, config);

        QueryParser parser = new QueryParser("fbPageId", new StandardAnalyzer());
        Query query = parser.parse(fbPageId);
        indexWriter.deleteDocuments(query);

        indexWriter.close();
        directory.close();
    }

    public Set<Long> search(String msg) throws IOException, ParseException {
        Directory directory = FSDirectory.open(indexPath);
        IndexReader indexReader =  DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("msg",  new ThaiAnalyzer());
        Query query = queryParser.parse(msg);
        TopDocs topDocs = indexSearcher.search(query, MAX_TOP_SEARCH);
        log.info("Found, totalHits = {}", topDocs.totalHits);
        Set<Long> found = new HashSet<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            log.debug("hit, PostEntity id[{}] msg[{}]",
                    document.get("id"), document.get("msg"));
            found.add(Long.parseLong(document.get("id")));
        }

        return found;
    }
}
