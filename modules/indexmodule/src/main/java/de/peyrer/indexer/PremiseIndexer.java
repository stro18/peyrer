package de.peyrer.indexer;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

public class PremiseIndexer extends AbstractIndexer {

    ArgumentRepository argumentRepository;

    Path indexPath;

    IndexWriterConfig config;

    PremiseIndexer(String directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory(directory);

        Analyzer analyzer = new StandardAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
    }

    @Override
    public void index() throws IOException {
        Directory fsDirectory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        Iterable<Argument> arguments = argumentRepository.readAll();

        for(Argument argument : arguments){
            for(String premise : argument.premises){
                Document doc = new Document();

                // A field whose value is stored (not indexed) so that IndexSearcher.doc(int) will return the field and its value.
                doc.add(new StoredField("argumentId", argument.id));

                // A field that is indexed and tokenized, without term vectors.
                doc.add(new TextField("premiseText", premise, Field.Store.YES));

                indexWriter.addDocument(doc);
            }
        }

        indexWriter.close();
    }
}
