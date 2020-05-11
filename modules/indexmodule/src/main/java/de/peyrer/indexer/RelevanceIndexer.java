package de.peyrer.indexer;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class RelevanceIndexer extends AbstractIndexer {

    ArgumentRepository argumentRepository;

    IndexWriterConfig config;

    RelevanceIndexer(String directoryName) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory("..", "..", directoryName);

        Analyzer analyzer = new StandardAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
    }

    @Override
    public void index() throws IOException {
        Directory fsDirectory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        Iterable<Argument> arguments = argumentRepository.readAll();

        for(Argument argument : arguments){
            Document doc = new Document();

            // field types: https://lucene.apache.org/core/8_5_1/core/org/apache/lucene/index/package-summary.html#field_types
            // A field whose value is stored (not indexed) so that IndexSearcher.doc(int) will return the field and its value.
            doc.add(new StoredField("argumentId", argument.id));
            doc.add(new StoredField("relevance", argument.relevance));

            // These fields are used for efficient value-based sorting, and for faceting, but they are not useful for filtering.
            doc.add(new DoubleDocValuesField("relevance", argument.relevance));

            // A field that is indexed and tokenized, without term vectors.
            doc.add(new TextField("conclusion", argument.conclusion, Field.Store.YES));

            indexWriter.addDocument(doc);
        }

        indexWriter.close();
        fsDirectory.close();
    }
}
