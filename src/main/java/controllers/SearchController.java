package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.hateoas.JsonError;
import org.elasticsearch.client.core.MainResponse;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import jsonManaging.SearchQueryJson;
import utils.elasticSearch.mainResponse.MainResponseUtilsInterface;

import javax.inject.Inject;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Autor Ana Garcia
 */
@Controller("/search")
public class SearchController {

    @Inject
    ElasticSearchUtilsInterface elasticUtils;

    @Inject
    MainResponseUtilsInterface mainResponseUtils;

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     * @param query Query that will be used to search using elasticSearch
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public SearchQueryJson index(@QueryValue String query) {

        //Elasticsearch
        MainResponse response = elasticUtils.getElasticClientResponse(elasticUtils.getClientInstance());

        if (response !=null){
            return getOkHttpResponse(response, query);
        }

        return new SearchQueryJson(null,null);
    }

    /**
     * Method to create the ok response for this url
     * @param response contains the cluster
     * @param query the url parameter
     * @return a JSON with the query and the cluster name and version of elasticSearch
     */
    private SearchQueryJson getOkHttpResponse(MainResponse response, String query){
        String clusterVersion = mainResponseUtils.getClusterNameAndVersion(response);

        return new SearchQueryJson(query, clusterVersion);
    }

}
