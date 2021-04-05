package utils.elasticSearch.aggregation;

import io.micronaut.context.annotation.Primary;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that creates and returns aggregations
 *
 * @Author Ana Garcia
 */
@Primary
public class AggregationUtils implements AggregationUtilsInterface {

    /**
     * Method that creates the aggregation for the genres
     *
     * @param genreTerms terms of the response for the genre
     * @return The list of aggregations
     */
    public Map<String, Integer> getGenresAggregation(Terms genreTerms) {
        Map<String, Integer> genres = new HashMap();
        Collection<Terms.Bucket> genreBuckets = (Collection<Terms.Bucket>) genreTerms.getBuckets();

        for (var genre : genreBuckets) {
            String key = (String) genre.getKey();
            if (!key.equals("\\n")) {
                int number = (int) genre.getDocCount();
                genres.put(key, number);
            }
        }
        return genres;
    }

    /**
     * Method that creates the aggregation for the types
     *
     * @param typeTerms terms of the response for the type
     * @return The list of aggregations
     */
    public Map<String, Integer> getTypesAggregation(Terms typeTerms) {
        Map<String, Integer> types = new HashMap();
        Collection<Terms.Bucket> typesBuckets = (Collection<Terms.Bucket>) typeTerms.getBuckets();

        for (var type : typesBuckets) {
            String key = (String) type.getKey();
            int number = (int) type.getDocCount();
            types.put(key, number);
        }
        return types;
    }

    /**
     * Method that creates the aggregation for the dates
     *
     * @param dateRange range of the response for the date
     * @return The list of aggregations
     */
    public Map<String, Integer> getDatesAggregation(Range dateRange) {
        Map<String, Integer> dates = new HashMap();
        Collection<Range.Bucket> datesBuckets = (Collection<Range.Bucket>) dateRange.getBuckets();

        for (var date : datesBuckets) {
            String key = (String) date.getKey();
            int number = (int) date.getDocCount();
            if (number != 0) {
                dates.put(key, number);
            }
        }
        return dates;
    }

    /**
     * Method that creates the aggregation for the dates aggregations
     *
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @return The aggregation
     */
    public DateRangeAggregationBuilder datesAggregation(String name, String field) throws ParseException {
        DateRangeAggregationBuilder aggDates = AggregationBuilders.dateRange(name).field(field).format("yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
            for (int i = 1700; i < 2021; i += 10) {
                aggDates.addRange(""+i,""+(i+10));
            }
        return aggDates;

    }

    /**
     * Method that creates the aggregation of a term aggregation and returns all buckets
     *
     * @param name  Name of the aggregation
     * @param field Field of the aggregation
     * @return The aggregation
     */
    public TermsAggregationBuilder createAggregation(String name, String field) {
        return AggregationBuilders.terms(name).field(field).size(1000);
    }
}
