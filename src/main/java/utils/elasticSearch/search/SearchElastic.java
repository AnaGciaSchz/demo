package utils.elasticSearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that has all the logic related with the search in elasticsearch
 */
public class SearchElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    /**
     * Method to search a query in some fields of an index
     * @param index index where we want to search
     * @param fields fields where we want to search
     * @param query Query that we want to search in that index
     * @return A list of the results (size <=10)
     * @throws IOException I there is an error
     */
    public Map<String,List<Map>> search(String index, String fields[], String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        searchRequest.source(searchInEveryfield(fields,query));

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHitsAndAggregations(response);
    }/**
     * Method to search a query in some fields of an index
     * @param parameters Query and parameters that we want to use to search in that index
     * @return A list of the results (size <=10)
     * @throws IOException I there is an error
     */
    public Map<String,List<Map>> searchImdb(Map<String,String> parameters) throws IOException {
        String[] fields = {"index","primaryTitle", "genres", "titleType", "start_year","end_year"};

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        if(parameters.keySet().size()==1){
            searchRequest.source(searchInEveryfield(fields,parameters.get("query")));
        }
        else {

            searchRequest.source(searchInEveryfieldWithImdbParameters(fields, parameters));
        }

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHitsAndAggregations(response);
    }

    /**
     * Method to search in every field where the param says
     * @param fields Fields where to search
     * @param parameters Query and parameters to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfieldWithImdbParameters(String[] fields, Map<String,String> parameters){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder query = new BoolQueryBuilder();
        query.must(QueryBuilders.multiMatchQuery(parameters.get("query"),fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));

        BoolQueryBuilder genre = null;
        BoolQueryBuilder type = null;

        GlobalAggregationBuilder agg = AggregationBuilders.global("agg");

        if(parameters.get("genre") != null) {
            String[] genres = parameters.get("genre").split(",");
            genre = QueryBuilders.boolQuery();
            for (int i = 0; i < genres.length; i++) {
                genre.should(QueryBuilders.termQuery("genres", genres[i]));
                agg.subAggregation(AggregationBuilders.terms(genres[i]).field("genre"));
            }
            query.must(genre);
        }
        if(parameters.get("type") != null) {
            String[] types = parameters.get("type").split(",");
            type = QueryBuilders.boolQuery();
            for (int i = 0; i < types.length; i++) {
                type.should(QueryBuilders.termQuery("titleType", types[i]));
                agg.subAggregation(AggregationBuilders.terms(types[i]).field("titleType"));
            }
            query.must(type);
        }

        sourceBuilder.query(query);
        sourceBuilder.aggregation(agg);
        return sourceBuilder;
    }

    /**
     * Method to search in every field where the param says
     * @param fields Fields where to search
     * @param query Query to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfield(String[] fields, String query){

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery(query, fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        return builder;
    }

    /**
     * Method that creates the list of results
     * @param response Response of the search
     * @return The list of results (size<=10)
     */
    private Map<String,List<Map>> getHitsAndAggregations(SearchResponse response){
        Map<String,List<Map>> results = new HashMap<>();
        List<Map> hits = new ArrayList<>();
        List<Map> aggregations = new ArrayList<>();
        for(SearchHit hit : response.getHits()){
            hits.add(hit.getSourceAsMap());
        }

        results.put("hits",hits);
        if(response.getAggregations()!=null) {
            for (Aggregation a : response.getAggregations()) {
                aggregations.add(a.getMetadata());
            }
            results.put("aggregations", aggregations);
        }

        return results;
    }
}
