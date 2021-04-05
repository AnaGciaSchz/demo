package utils.elasticSearch.query;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.text.ParseException;

/**
 * Interface for a class that creates querys
 *
 * @Author Ana Garcia
 */
public interface QueryUtilsInterface {

    /**
     * Method that creates the query of a term bool search and using should.
     *
     * @param string string with the temrs separated by ,
     * @param field  Field to search the terms
     * @return map with the query (key=query) and aggregation (key=aggregation)
     */
    TermsQueryBuilder createQuery(String string, String field);

    /**
     * Method that creates the query of the dates
     *
     * @param string string with the temrs separated by ,
     * @param field  Field to search the terms
     * @return map with the query (key=query) and aggregation (key=aggregation)
     */
    BoolQueryBuilder datesQuery(String string, String field) throws ParseException;
}
