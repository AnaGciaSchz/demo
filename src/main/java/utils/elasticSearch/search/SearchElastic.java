package utils.elasticSearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
     * Method to search a query in the imdb cluster
     * @param query Query that we want to search in that cluster
     * @return A list of the results (size <=10)
     * @throws IOException I there is an error
     */
    public List<Map> searchImdb (String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        String[] querys = query.split(" ");
        BoolQueryBuilder queryBuilder = searchInEveryfield(querys);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHits(response);
    }

    private BoolQueryBuilder searchInEveryfield(String[] querys){

        BoolQueryBuilder query = new BoolQueryBuilder();
        MatchQueryBuilder A,B,C,D,E,F;
        for(String q : querys){
            A = QueryBuilders.matchQuery("primaryTitle", q);
            B = QueryBuilders.matchQuery("titleType", q);
            C = QueryBuilders.matchQuery("index", q);
            D = QueryBuilders.matchQuery("genres", q);
            E = QueryBuilders.matchQuery("start_year", q);
            F =QueryBuilders.matchQuery("end_year", q);
            query.should(A).should(B).should(C).should(D).should(E).should(F);
        }

        //use filter
        BoolQueryBuilder filterBuiler = QueryBuilders.boolQuery().must(query);
        return filterBuiler;
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
