package de.peyrer.retrievalmodule;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class Results implements Iterable<Result> {
    private ResultIterator resultIterator;

    public Results(TopDocs topDocs, IndexSearcher indexSearcher) {
        this.resultIterator = new ResultIterator(topDocs, indexSearcher);
    }

    @Override
    public Iterator<Result> iterator() {
        return this.resultIterator;
    }

    public class ResultIterator implements Iterator<Result> {

        private TopDocs topDocs;
        private int index;
        private IndexSearcher indexSearcher;
        private ArgumentRepository argumentRepository;

        public ResultIterator(TopDocs topDocs, IndexSearcher indexSearcher) {
            this.topDocs = topDocs;
            this.indexSearcher = indexSearcher;
            Arrays.sort(this.topDocs.scoreDocs, new CompareScoreDocs());
            this.index = 0;
            this.argumentRepository = new ArgumentRepository();
        }

        @Override
        public boolean hasNext() {
            return (this.index >= this.topDocs.scoreDocs.length) ? false : true;
        }

        @Override
        public Result next() {
            if(!this.hasNext()) return null;
            ScoreDoc doc = this.topDocs.scoreDocs[this.index];
            Result result = null;
            try {
                result = new Result(doc, this.index + 1, this.argumentRepository.readById(this.indexSearcher.doc(doc.doc).get("argumentId")));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
            this.index++;
            return result;
        }

        private class CompareScoreDocs implements Comparator<ScoreDoc> {
            @Override
            public int compare(ScoreDoc doc1, ScoreDoc doc2) {
                if(doc1.score > doc2.score) return -1;
                else if(doc1.score < doc2.score) return 1;
                else return 0;
            }
        }
    }
}
