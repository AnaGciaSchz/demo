package utils.elasticSearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Interface that represents a class to deal with some ElasticSearch functions like getting the
 * client instance or getting a MainResponse.
 *
 * @Author Ana Garcia
 */
public interface ElasticSearchUtilsInterface {

    static final String HOSTNAME= "localhost";
    static final int ELASTICPORT1 = 9200;
    static final int ELASTICPORT2 = 9201;
    static final String SCHEME = "http";

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     * @return RestHighLevelClient
     */
    RestHighLevelClient getClientInstance();

    /**
     * Method to return a response that contains the necessary cluster or null if there's a problem.
     * @return MainResponse
     */
    MainResponse getElasticClientResponse() throws IOException;

    /**
     * Method to close the client when the contex is closed to free the resources.
     */
    void close();
}
