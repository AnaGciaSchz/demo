package utils.elasticSearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
        String[] fields = {"index", "primaryTitle", "genres", "titleType", "start_year", "end_year"};

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        if (parameters.keySet().size() == 1) {
            searchRequest.source(searchInEveryfield(fields, parameters.get("query")));
        } else {

            searchRequest.source(searchInEveryfieldWithImdbParameters(fields, parameters));
        }

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

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

        BoolQueryBuilder genre = null;
        BoolQueryBuilder type = null;

        AggregationBuilder aggbuilderGenres = null;
        AggregationBuilder aggbuilderTypes = null;

        if (parameters.get("genre") != null) {
            String[] genres = parameters.get("genre").split(",");
            genre = QueryBuilders.boolQuery();
            for (int i = 0; i < genres.length; i++) {
                genre.should(QueryBuilders.termQuery("genres", genres[i]));
            }

            aggbuilderGenres = AggregationBuilders.terms("genreAggregation").field("genres").size(26);
            sourceBuilder.aggregation(aggbuilderGenres);
            query.should(genre);
        }
        if (parameters.get("type") != null) {
            String[] types = parameters.get("type").split(",");
            type = QueryBuilders.boolQuery();
            for (int i = 0; i < types.length; i++) {
                type.should(QueryBuilders.termQuery("titleType", types[i]));
            }
            aggbuilderTypes = AggregationBuilders.terms("titleTypeAggregation").field("titleType").size(11);
            sourceBuilder.aggregation(aggbuilderTypes);
            query.should(type);
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
        }
        return results;
    }
}
