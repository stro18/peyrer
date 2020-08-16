package de.peyrer.analyzermodule;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerModule {

    private final Analyzer analyzer;

    public AnalyzerModule(){
        this.analyzer = new StandardAnalyzer();
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

        return String.join(" ", result);
    }
}
