package de.peyrer.querybuilder;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class DocValueFieldQueryBuilderTest {

    private DocValueFieldQueryBuilder docValueFieldQueryBuilder;

    @Before
    public void setUp() {
        this.docValueFieldQueryBuilder = new DocValueFieldQueryBuilder(0.42f);
    }

    @Test
    public void testGetQuery() throws ParseException {
        Query query = this.docValueFieldQueryBuilder.getQuery("This iS a teSt Query");
        System.out.println(query.toString());

        Set<Term> referenceTermSet = new HashSet<>();
        referenceTermSet.add(new Term("conclusion", "test"));
        referenceTermSet.add(new Term("conclusion", "query"));
        Set<Term> termSet = new HashSet<>();
        QueryVisitor queryVisitor = QueryVisitor.termCollector(termSet);
        query.visit(queryVisitor);

        Assert.assertEquals(termSet, referenceTermSet);
    }
}
