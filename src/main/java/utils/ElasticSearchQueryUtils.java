package utils;

import io.micronaut.context.annotation.Primary;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * Class that has methods to deal with the Business logic related to ElasticSearch in
 * the SearchController
 *
 * @Autrhor Ana Garcia
 */
@Singleton
@Primary
public class ElasticSearchQueryUtils implements ElasticSearchUtilsInterface {

    private static RestHighLevelClient client;

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     * It also contains the specific cluster that we are using.
     * @return RestHighLevelClient
     */
    public RestHighLevelClient getClientInstance(){
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
     public MainResponse getElasticClientResponse(RestHighLevelClient client){
        MainResponse response = null;
        try {
            response = client.info(RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
