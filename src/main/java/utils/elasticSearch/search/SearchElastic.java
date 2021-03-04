package utils.elasticSearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
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
    public List<Map> search(String index, String fields[], String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        searchRequest.source(searchInEveryfield(fields,query));

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHits(response);
    }

    /**
     * Method to search in eberyfields where the param says
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
    private List<Map> getHits(SearchResponse response){
        List<Map> hits = new ArrayList<>();
        for(SearchHit hit : response.getHits()){
            hits.add(hit.getSourceAsMap());
        }
        return hits;
    }
}
