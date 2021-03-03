package utils.elasticSearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    public List<Map> searchImdb (String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("primaryTitle", query));
        searchRequest.source(searchSourceBuilder);

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHits(response);
    }

    private List<Map> getHits(SearchResponse response){
        List<Map> hits = new ArrayList<Map>();
        for(SearchHit hit : response.getHits()){
            hits.add(hit.getSourceAsMap());
        }
        return hits;
    }
}
