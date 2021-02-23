package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import java.io.IOException;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Autor Ana Garcia
 */
@Controller("/search")
public class SearchContoller {

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     * @param query Query that will be used to search using elasticSearch
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query) {

        //Elasticsearch
        RestHighLevelClient client = getHighLevelClient();
        MainResponse response = getElasticClientResponse(client);

        if (response !=null){
            return getOkHttpResponse(response, query);
        }
        return HttpResponse.notFound();
    }

    /**
     * Method to return the rest high level client that is necessary to expose elasticSearch methods.
     * It also contains the specific cluster that we are using.
     * @return RestHighLevelClient
     */
    private RestHighLevelClient getHighLevelClient(){
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

    }
    /**
     * Method to return a response that contains the necessary cluster or null if there's a problem.
     * @return MainResponse
     */
    private MainResponse getElasticClientResponse(RestHighLevelClient client){
        MainResponse response = null;
        try {
            response = client.info(RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Method to create the ok response for this url
     * @param response contains the cluster
     * @param query the url parameter
     * @return a JSON with the query and the cluster name and version of elasticSearch
     */
    private HttpResponse getOkHttpResponse(MainResponse response, String query){
        String clusterVersion = response.getClusterName()+" "+response.getVersion().getNumber();

        return HttpResponse.ok().body("{\n\"query\":\"" + query+"\",\n" +
                "\"cluster_name\":\""+clusterVersion+"\"\n}");
    }

}
