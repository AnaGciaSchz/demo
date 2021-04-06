package utils.elasticSearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interface that represents a class to deal with some ElasticSearch functions like getting the
 * client instance or getting a MainResponse.
 *
 * @Author Ana Garcia
 */
public interface ElasticSearchUtilsInterface {

    String HOSTNAME = "localhost";
    int ELASTICPORT1 = 9200;
    int ELASTICPORT2 = 9201;
    String SCHEME = "http";

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     *
     * @return RestHighLevelClient
     */
    RestHighLevelClient getClientInstance();

    /**
     * Method to return a response that contains the necessary cluster or null if there's a problem.
     *
     * @return MainResponse
     */
    MainResponse getElasticClientResponse() throws IOException;

    /**
     * Method to add a lot of information at the same time to elasticsearch
     *
     * @param list list of information to add
     * @throws IOException if there's an error
     */
    void bulkAdd(List<Object> list) throws IOException;

    /**
     * Method that creates the list of hits
     *
     * @param response Response of the search
     * @return The list of hits (size<=10)
     */
    @SuppressWarnings("rawtypes")
    List<Map> getHits(SearchResponse response);

    /**
     * Method to close the client when the context is closed to free the resources.
     */
    void close();
}
