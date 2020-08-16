package de.peyrer.indexer;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.graph.AbstractDirectedGraph;
import de.peyrer.indexmodule.IIndexmodule;
import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.CharArrayReader;
import java.io.IOException;

public class RelevanceIndexer extends AbstractIndexer {

    IArgumentRepository argumentRepository;

    IndexWriterConfig config;

    public RelevanceIndexer(String ... directory) throws IOException {
        this.argumentRepository = new ArgumentRepository();

        this.indexPath = this.createIndexDirectory(directory);

        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
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

            // Option1 for scoring relevance: Doc fields are used for efficient value-based sorting, and for faceting, but they are not useful for filtering.
            doc.add(new DoubleDocValuesField("relevance", argument.relevance == 0 ?
                    1-AbstractDirectedGraph.dampingFactor : argument.relevance));
            // Option2 for scoring relevance: Feature fields can be used to store static scoring factors into documents.
            doc.add(new FeatureField("feature", "relevance", argument.relevance == 0 ?
                    (float) (1-AbstractDirectedGraph.dampingFactor) : (float) argument.relevance));

            // A field that is indexed and tokenized, without term vectors. Additionally it is stored without being tokenized.
            doc.add(new TextField("conclusion", argument.conclusion, Field.Store.YES));

            indexWriter.addDocument(doc);
        }

        indexWriter.close();
        fsDirectory.close();
    }
}
