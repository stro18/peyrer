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
import java.nio.file.Files;
import java.nio.file.Path;

public class RelevanceIndexer implements IIndexer {

    ArgumentRepository argumentRepository;

    IndexWriter indexWriter;

    RelevanceIndexer(String directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        Path indexPath = Files.createTempDirectory(directory);
        Directory fsDirectory = FSDirectory.open(indexPath);

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        this.indexWriter = new IndexWriter(fsDirectory, config);
    }

    @Override
    public void indexPrem() throws IOException {
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

    @Override
    public void indexConc() {

    }
}
