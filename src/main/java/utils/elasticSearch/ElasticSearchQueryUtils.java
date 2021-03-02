package utils.elasticSearch;

import io.micronaut.context.annotation.Primary;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.common.xcontent.XContentType;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that has methods to deal with some ElasticSearch functions
 *
 * @Author Ana Garcia
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
     public MainResponse getElasticClientResponse() throws IOException {
        MainResponse response;

            //Using getClientInstance to make sure I have a client at this point
         response = getClientInstance().info(RequestOptions.DEFAULT);

        return response;
    }

    /**
     * Method to add a lot of information at the same time to elasticsearch
     * @param list list of informattion to add
     * @throws IOException
     */
    public void bulkAdd(List<Object> list) throws IOException {
        BulkRequest bulk = new BulkRequest();
        RestHighLevelClient client = getClientInstance();

        for(int i = 0; i<list.size();i++){
            ArrayList<Object> array = (ArrayList<Object>) list.get(i);
            bulk.add(new IndexRequest("imdb").id((String) array.get(0)).source(array.get(1), XContentType.JSON));
        }
        System.out.println("bulk");
        client.bulk(bulk, RequestOptions.DEFAULT);
    }

    /**
     * Method to close the client when the context is closed to free the resources.
     */
    @PreDestroy
    public void close() {
        try {
            client.close();
        }
        catch(IOException e){
            System.out.println("Problem closing the connection"); //This should be written in a log
        }
    }


}
