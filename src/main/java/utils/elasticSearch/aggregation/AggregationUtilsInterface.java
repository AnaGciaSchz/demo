package utils.elasticSearch.aggregation;

import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.text.ParseException;
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

    Map<String, Integer> getTypesFilterAggregation(Filter typeTerms);

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
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @return The aggregation
     */
    DateRangeAggregationBuilder datesAggregation(String name, String field) throws ParseException;

    /**
     * Method that creates the aggregation of a term aggregation and returns all buckets
     *
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @return The aggregation
     */
    TermsAggregationBuilder createAggregation(String name, String field);
}
