package de.peyrer.analyzermodule;

import opennlp.tools.stemmer.PorterStemmer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyzerModule {

    private final Analyzer analyzer;

    public AnalyzerModule(){
        CharArraySet stopSet = new CharArraySet(getStopwords(), true);
        this.analyzer = new StandardAnalyzer(stopSet);
    }

    public Analyzer getAnalyzer(){
        return new StandardAnalyzer();
    }

    public List<String> getStopwords() {
        return Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        );
    }

    public String analyze(String key, String value) throws IOException {
        List<String> result = new ArrayList<String>();
        TokenStream stream  = this.analyzer.tokenStream(key, value);

        stream.reset();
        while(stream.incrementToken()) {
            result.add(stream.getAttribute(CharTermAttribute.class).toString());
        }
        stream.end();
        stream.close();

        PorterStemmer stemmer = new PorterStemmer();

        String stemmed = result.stream().map(s -> stemmer.stem(s)).collect(Collectors.joining(" "));

        return stemmed;
    }
}
