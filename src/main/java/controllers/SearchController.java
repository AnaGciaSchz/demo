package controllers;

import utils.ElasticSearchUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.elasticsearch.client.core.MainResponse;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Autor Ana Garcia
 */
@Controller("/search")
public class SearchController {

    ElasticSearchUtils elasticUtils;

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     * @param query Query that will be used to search using elasticSearch
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query) {

        //Elasticsearch
        MainResponse response = elasticUtils.getElasticClientResponse(elasticUtils.getClientInstance());

        if (response !=null){
            return getOkHttpResponse(response, query);
        }
        return HttpResponse.notFound();
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
