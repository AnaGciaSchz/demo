package utils.elasticSearch.aggregation;

import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.Map;

/**
 * Interface for a class that creates and returns aggregations
 */
public interface AggregationUtilsInterface {
    /**
     * Method that creates the aggregation for the genres
     *
     * @param genreTerms terms of the response for the genre
     * @return The list of aggregations
     */
    Map<String, Integer> getGenresAggregation(Terms genreTerms);

    /**
     * Method that creates the aggregation for the types
     *
     * @param typeTerms terms of the response for the type
     * @return The list of aggregations
     */
    Map<String, Integer> getTypesAggregation(Terms typeTerms);

    /**
     * Method that creates the aggregation for the dates
     *
     * @param dateRange range of the response for the date
     * @return The list of aggregations
     */
    Map<String, Integer> getDatesAggregation(Range dateRange);

    /**
     * Method that creates the aggregation for the dates aggregations
     *
     * @param dates Dates to filter, if it's null it shows all decades.
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @return The aggregation
     */
    RangeAggregationBuilder datesAggregation(String dates, String name, String field);

    /**
     * Method that creates the aggregation of a term aggregation
     *
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @param size  Size of the results for the aggregation
     * @return The aggregation
     */
    TermsAggregationBuilder createAggregation(String name, String field, int size);
}
