package utils;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

public interface ElasticSearchUtilsInterface {

    public static final String HOSTNAME= "localhost";
    public static final int ELASTICPORT1 = 9200;
    public static final int ELASTICPORT2 = 9201;
    public static final String SCHEME = "http";

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     * @return RestHighLevelClient
     */
    RestHighLevelClient getClientInstance();

    /**
     * Method to return a response that contains the necessary cluster or null if there's a problem.
     * @return MainResponse
     */
    MainResponse getElasticClientResponse(RestHighLevelClient client);
}
