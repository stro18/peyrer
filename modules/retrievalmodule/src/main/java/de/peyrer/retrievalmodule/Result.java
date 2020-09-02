package de.peyrer.retrievalmodule;

import de.peyrer.model.Argument;
import org.apache.lucene.search.ScoreDoc;

public class Result {

    private ScoreDoc scoreDoc;
    private int rank ;
    private Argument argument;

    public Result(ScoreDoc scoreDoc, int rank, Argument argument) {
        this.scoreDoc = scoreDoc;
        this.rank = rank;
        this.argument = argument;
    }

    public ScoreDoc getScoreDoc() {
        return scoreDoc;
    }

    public void setScoreDoc(ScoreDoc scoreDoc) {
        this.scoreDoc = scoreDoc;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Argument getArgument() {
        return argument;
    }

    public void setArgument(Argument argument) {
        this.argument = argument;
    }

    public float getScore() {
        return this.scoreDoc.score;
    }
}
