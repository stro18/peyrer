package de.peyrer.indexer;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
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
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class PremiseIndexer extends AbstractIndexer {

    public IArgumentRepository argumentRepository;

    IndexWriterConfig config;

    public PremiseIndexer(String ... directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory(directory);

        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
    }

    @Override
    public void index() throws IOException {
        String matching = System.getenv().get("MATCHING");

        if (matching != null && (matching.equals("PHRASE"))) {
            config.setSimilarity(new ClassicSimilarity());
        }
        Directory fsDirectory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        Iterable<Argument> arguments = argumentRepository.readAll();

        for(Argument argument : arguments){
            int premiseId = 0;
            for(String premise : argument.premises){
                Document doc = new Document();

                // A field whose value is stored (not indexed) so that IndexSearcher.doc(int) will return the field and its value.
                doc.add(new StoredField("argumentId", argument.id));
                doc.add(new StoredField("premiseId", Integer.toString(premiseId)));

                // Necessary for Phrase-Matching with stopwords, see: https://stackoverflow.com/questions/31719249/how-to-query-a-phrase-with-stopwords-in-elasticsearch
                if (matching != null && (matching.equals("PHRASE"))) {
                    premise = new AnalyzerModule().analyze("premiseText",premise);
                }

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
