package utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import java.io.IOException;

/**
 * Class that has static methods to deal with ElacticSearch and ome business logic for the app
 *
 * @Autrhor Ana Garcia
 */
public class ElasticSearchUtils {

    private static RestHighLevelClient client;
    
    public static final String HOSTNAME= "localhost";
    public static final int ELASTICPORT1 = 9200;
    public static final int ELASTICPORT2 = 9201;
    public static final String SCHEME = "http";

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     * It also contains the specific cluster that we are using.
     * @return RestHighLevelClient
     */
    public static RestHighLevelClient getClientInstance(){
        if(client==null){
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOSTNAME, ELASTICPORT1, SCHEME),
                            new HttpHost(HOSTNAME, ELASTICPORT2, SCHEME)));
        }
        return client;

    }

    /**
     * Method to return a response that contains the necessary cluster or null if there's a problem.
     * @return MainResponse
     */
    public static MainResponse getElasticClientResponse(RestHighLevelClient client){
        MainResponse response = null;
        try {
            response = client.info(RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
