package utils.elasticSearch.query;

import io.micronaut.context.annotation.Primary;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that creates querys
 *
 * @Author Ana Garcia
 */
@Primary
public class QueryUtils implements QueryUtilsInterface {

    /**
     * Method that creates the query of a term bool search and using should.
     *
     * @param string string with the temrs separated by ,
     * @param field  Field to search the terms
     * @return map with the query (key=query) and aggregation (key=aggregation)
     */
    public BoolQueryBuilder createQuery(String string, String field) {
        String[] array = string.split(",");
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        for (int i = 0; i < array.length; i++) {
            builder.should(QueryBuilders.termQuery(field, array[i]));
        }

        return builder;
    }

    /**
     * Method that creates the query of the dates
     *
     * @param string string with the temrs separated by ,
     * @param field  Field to search the terms
     * @return map with the query (key=query) and aggregation (key=aggregation)
     */
    public BoolQueryBuilder datesQuery(String string, String field) throws ParseException {
        BoolQueryBuilder datesQuery = new BoolQueryBuilder();

        String[] dates = string.split(",");


        for (int i = 0; i < dates.length; i++) {
            String[] decade = dates[i].split("/");
            RangeQueryBuilder rangeDates = new RangeQueryBuilder(field).format("yyyy");
            String to = decade[1];
            String from = decade[0];

            rangeDates.gte(from);
            rangeDates.lte(to);
            datesQuery.should(rangeDates);
        }
        return datesQuery;
    }
}
