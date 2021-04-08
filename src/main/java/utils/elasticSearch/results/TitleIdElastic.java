package utils.elasticSearch.results;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to returns the elastic search result for the id Titles
 *
 * @Author Ana Garcia
 */
public class TitleIdElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    /**
     * Method that returns the movie that has the id
     * @param id Id of the movie
     * @return The movie (if there is one with that id)
     * @throws IOException
     */
    public Map<String, Object> imdbTitleId(String id) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchPhraseQuery("index",id));
        searchRequest.source(sourceBuilder);


        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        SearchResponse response;

        response = client.search(searchRequest, RequestOptions.DEFAULT);

        Map<String, Object> results = new HashMap<>();
        results.put("hits", elasticSearchUtils.getHits(response));

        return results;
    }
}
