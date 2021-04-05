package utils.elasticSearch.search;

import io.micronaut.http.server.exceptions.InternalServerException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.elasticSearch.aggregation.AggregationUtilsInterface;
import utils.elasticSearch.query.QueryUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Class that has all the logic related with the search in elasticsearch
 */
public class SearchElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    @Inject
    AggregationUtilsInterface aggregationUtils;

    @Inject
    QueryUtilsInterface queryUtils;

    /**
     * Method to search a query in some fields of an index
     *
     * @param parameters Query and parameters that we want to use to search in that index
     * @return A list of the results (size <=10)
     * @throws IOException I there is an error
     */
    public Map<String, Object> searchImdb(Map<String, String> parameters) throws InternalServerException, IOException, ParseException {
        String[] fields = {"index", "primaryTitle", "genres", "titleType","start_yearText","end_yearText"};

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.indices("imdb");

        searchRequest.source(searchInEveryfieldWithImdbParameters(fields, parameters));


        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        SearchResponse response;

        response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHitsAndAggregations(response);
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields     Fields where to search
     * @param parameters Query and parameters to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfieldWithImdbParameters(String[] fields, Map<String, String> parameters) throws ParseException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder resultQuery = createQuery(fields,parameters);

        //filters & aggregations
        if (parameters.get("genre") != null) {
            resultQuery.filter(queryUtils.createQuery(parameters.get("genre"), "genres"));
        }
        sourceBuilder.aggregation(aggregationUtils.createAggregation("genreAggregation", "genres"));
        if (parameters.get("type") != null) {
            resultQuery.filter(queryUtils.createQuery(parameters.get("type"), "titleType"));
        }
        sourceBuilder.aggregation(aggregationUtils.createAggregation("titleTypeAggregation", "titleType"));

        if (parameters.get("date") != null) {
            resultQuery.filter(queryUtils.datesQuery(parameters.get("date"), "start_year"));
        }

        sourceBuilder.aggregation(aggregationUtils.datesAggregation("dateRange", "start_year"));

        sourceBuilder.query(resultQuery);
        return sourceBuilder;
    }

    /**
     * Method that creates the structure of the query
     * @param fields fields to search
     * @param parameters parameters of the query
     * @return boolean query
     */
    private BoolQueryBuilder createQuery(String[] fields, Map<String, String> parameters){
        BoolQueryBuilder query = searchInEveryfield(fields, parameters.get("query"));
        BoolQueryBuilder query2 = new BoolQueryBuilder();
        query2.must(QueryBuilders.matchPhraseQuery("primaryTitle",parameters.get("query")).boost(3)).boost(3);

        BoolQueryBuilder resultQuery = new BoolQueryBuilder();
        DisMaxQueryBuilder disMaxQuery = new DisMaxQueryBuilder();

        disMaxQuery.add(query2);
        FunctionScoreQueryBuilder f = QueryBuilders.functionScoreQuery(query,
                ScoreFunctionBuilders.linearDecayFunction("averageRatingLogic", 10, 5, "0d", 0.5));
        disMaxQuery.add(f);

        resultQuery.must(disMaxQuery);
        resultQuery.should(QueryBuilders.matchQuery("titleType","movie").boost(5));

        return resultQuery;
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields Fields where to search
     * @param query  Query to search
     * @return a builder with the query
     */
    private BoolQueryBuilder searchInEveryfield(String[] fields, String query) {
        BoolQueryBuilder builder = new BoolQueryBuilder();
        builder.should(QueryBuilders.multiMatchQuery(query, fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
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

        results.put("hits", getHits(response));

        if (response.getAggregations() != null) {
            Terms genreTerms = response.getAggregations().get("genreAggregation");

            if (genreTerms != null) {
                results.put("genres", aggregationUtils.getGenresAggregation(genreTerms));
            }

            Terms typeTerms = response.getAggregations().get("titleTypeAggregation");
            if (typeTerms != null) {
                results.put("types", aggregationUtils.getTypesAggregation(typeTerms));
            }

            Range dateRange = response.getAggregations().get("dateRange");
            if (dateRange != null) {
                results.put("dates", aggregationUtils.getDatesAggregation(dateRange));
            }
        }
        return results;
    }

    /**
     * Method that creates the list of hits
     *
     * @param response Response of the search
     * @return The list of hits (size<=10)
     */
    @SuppressWarnings("rawtypes")
    private List<Map> getHits(SearchResponse response) {
        List<Map> hits = new ArrayList<>();

        for (SearchHit hit : response.getHits()) {
            hits.add(hit.getSourceAsMap());
        }
        return hits;
    }
}
