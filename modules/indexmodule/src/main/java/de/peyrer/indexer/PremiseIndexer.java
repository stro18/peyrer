package de.peyrer.indexer;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class PremiseIndexer extends AbstractIndexer {

    public IArgumentRepository argumentRepository;

    IndexWriterConfig config;

    private AnalyzerModule analyzerModule;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";
    private static final String TFIDF = "TFIDF";
    private static final String TFIDF_WEIGHTED = "TFIDF_WEIGHTED";
    private static final String BM25 = "BM25";

    public PremiseIndexer(String ... directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory(directory);

        this.analyzerModule = new AnalyzerModule();
        Analyzer analyzer = analyzerModule.getAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
    }

    @Override
    public void index() throws IOException, InvalidSettingValueException {
        String matching = System.getenv().get("MATCHING");

        if (matching != null) {
            switch (matching) {
                case TFIDF:
                case TFIDF_WEIGHTED:
                    config.setSimilarity(new ClassicSimilarity());
                    break;
                case BM25:
                    config.setSimilarity(new BM25Similarity());
                    break;
                case AND:
                case PHRASE:
                    break;
                default:
                    throw new InvalidSettingValueException("The setting MATCHING=" + matching + " is not allowed!");
            }
        }

        Directory fsDirectory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        Iterable<Argument> arguments = argumentRepository.readAll();

        for(Argument argument : arguments){
            int premiseId = 0;
            for(String premise : argument.premises){
                premise = analyzerModule.analyze("premiseText", premise);

                Document doc = new Document();

                // A field whose value is stored (not indexed) so that IndexSearcher.doc(int) will return the field and its value.
                doc.add(new StoredField("argumentId", argument.id));
                doc.add(new StoredField("premiseId", Integer.toString(premiseId)));

                // A field that is indexed and tokenized, without term vectors. Additionally it is stored without being tokenized.
                doc.add(new TextField("premiseText", premise, Field.Store.YES));

                indexWriter.addDocument(doc);

                premiseId++;
            }
        }

        indexWriter.close();
        fsDirectory.close();
    }
}
