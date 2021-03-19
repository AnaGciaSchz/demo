package utils.elasticSearch.search;

import io.micronaut.http.server.exceptions.InternalServerException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

/**
 * Class that has all the logic related with the search in elasticsearch
 */
public class SearchElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    /**
     * Method to search a query in some fields of an index
     *
     * @param parameters Query and parameters that we want to use to search in that index
     * @return A list of the results (size <=10)
     * @throws IOException I there is an error
     */
    public Map<String, Object> searchImdb(Map<String, String> parameters) throws IOException {
        String[] fields = {"index", "primaryTitle", "genres", "titleType"}; //, "start_year", "end_year" ????

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        if (parameters.keySet().size() == 1) {
            searchRequest.source(searchInEveryfield(fields, parameters.get("query")));
        } else {
            searchRequest.source(searchInEveryfieldWithImdbParameters(fields, parameters));
        }

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        SearchResponse response;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (InternalServerException e) {
            return new HashMap<String, Object>();
        }

        return getHitsAndAggregations(response);
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields     Fields where to search
     * @param parameters Query and parameters to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfieldWithImdbParameters(String[] fields, Map<String, String> parameters) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder query = new BoolQueryBuilder();
        query.should(QueryBuilders.multiMatchQuery(parameters.get("query"), fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));

        if (parameters.get("genre") != null) {
            Map<String, Object> map = createAggregationAndQuery(parameters.get("genre"), "genreAggregation", "genres", 26);
            sourceBuilder.aggregation((AggregationBuilder) map.get("aggregation"));
            query.must((QueryBuilder) map.get("query"));
        }
        if (parameters.get("type") != null) {
            Map<String, Object> map = createAggregationAndQuery(parameters.get("type"), "titleTypeAggregation", "titleType", 11);
            sourceBuilder.aggregation((AggregationBuilder) map.get("aggregation"));
            query.must((QueryBuilder) map.get("query"));
        }

        if (parameters.get("date") != null) {
            BoolQueryBuilder datesQuery = new BoolQueryBuilder();

            String[] dates = parameters.get("date").split(",");

            RangeAggregationBuilder aggDates = AggregationBuilders.range("dateRange").field("start_year");
            RangeQueryBuilder rangeDates = new RangeQueryBuilder("start_year");

            for (int i = 0; i < dates.length; i++) {
                String[] decade = dates[i].split("/");
                int to = Integer.parseInt(decade[1]);
                int from = Integer.parseInt(decade[0]);

                rangeDates.gte(from);
                rangeDates.lte(to);
                datesQuery.should(rangeDates);
            }
            query.should(datesQuery);

            for(int i = 0; i<2021; i+=10){
                aggDates.addRange(i, i+10);
            }
            sourceBuilder.aggregation(aggDates);
        }

        sourceBuilder.query(query);
        return sourceBuilder;
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields Fields where to search
     * @param query  Query to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfield(String[] fields, String query) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery(query, fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        return builder;

    }

    /**
     * Method that creates the list of results
     *
     * @param response Response of the search
     * @return The list of results (size<=10)
     */
    private Map<String, Object> getHitsAndAggregations(SearchResponse response) {
        Map<String, Object> results = new HashMap<>();
        List<Map> hits = new ArrayList<>();

        for (SearchHit hit : response.getHits()) {
            hits.add(hit.getSourceAsMap());
        }

        results.put("hits", hits);
        if (response.getAggregations() != null) {
            Terms genreTerms = response.getAggregations().get("genreAggregation");

            if (genreTerms != null) {
                Map<String, Integer> g = new HashMap();
                Collection<Terms.Bucket> genreBuckets = (Collection<Terms.Bucket>) genreTerms.getBuckets();

                for (var genre : genreBuckets) {
                    String key = (String) genre.getKey();
                    if (!key.equals("\\n")) {
                        int number = (int) genre.getDocCount();
                        g.put(key, number);
                    }
                }

                results.put("genres", g);
            }

            Terms typeTerms = response.getAggregations().get("titleTypeAggregation");
            if (typeTerms != null) {
                Map<String, Integer> t = new HashMap();
                Collection<Terms.Bucket> typesBuckets = (Collection<Terms.Bucket>) typeTerms.getBuckets();

                for (var type : typesBuckets) {
                    String key = (String) type.getKey();
                    int number = (int) type.getDocCount();
                    t.put(key, number);
                }

                results.put("types", t);

            }

            Range dateRange = response.getAggregations().get("dateRange");
            if (dateRange != null) {
                Map<String, Integer> t = new HashMap();
                Collection<Range.Bucket> datesBuckets = (Collection<Range.Bucket>) dateRange.getBuckets();

                for (var date : datesBuckets) {
                    String key = (String) date.getKey();
                    int number = (int) date.getDocCount();
                    if(number != 0){
                        t.put(key, number);
                    }
                }

                results.put("dates", t);
            }
        }
        return results;
    }

    /**
     * Method that creates the aggregation and query of a term aggregation and using should.
     *
     * @param string string with the temrs separated by ,
     * @param name   Name of the aggregation
     * @param field  Field to search the terms
     * @param size   Size of the results
     * @return map with the query (key=query) and aggregation (key=aggregation)
     */
    private Map<String, Object> createAggregationAndQuery(String string, String name, String field, int size) {
        String[] array = string.split(",");
        Map<String, Object> result = new HashMap<>();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        for (int i = 0; i < array.length; i++) {
            builder.should(QueryBuilders.termQuery(field, array[i]));
        }
        result.put("query", builder);
        result.put("aggregation", AggregationBuilders.terms(name).field(field).size(size));

        return result;
    }
}
