package de.peyrer.indexer;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class ConclusionIndexer extends AbstractIndexer {
    public IArgumentRepository argumentRepository;

    IndexWriterConfig config;

    private AnalyzerModule analyzerModule;

    public ConclusionIndexer(String ... directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory(directory);

        this.analyzerModule = new AnalyzerModule();
        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
    }

    @Override
    public void index() throws IOException {
        Directory fsDirectory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        Iterable<Argument> arguments = argumentRepository.readAll();

        for(Argument argument : arguments){
            String conclusion = analyzerModule.analyze("conclusionText", argument.conclusion);

            Document doc = new Document();

            // A field whose value is stored (not indexed) so that IndexSearcher.doc(int) will return the field and its value.
            doc.add(new StoredField("argumentId", argument.id));

            // A field that is indexed and tokenized, without term vectors. Additionally it is stored without being tokenized.
            doc.add(new TextField("conclusionText", conclusion, Field.Store.YES));

            if (System.getenv().get("DEBUG") != null && System.getenv().get("DEBUG").equals("1")){
                argumentRepository.updateConclusionNormalized(argument.id, conclusion);
            }

            indexWriter.addDocument(doc);
        }

        indexWriter.close();
        fsDirectory.close();
    }
}
