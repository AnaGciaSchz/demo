package components;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import jsonManaging.SearchQueryJson;
import org.elasticsearch.client.core.MainResponse;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.elasticSearch.mainResponse.MainResponseUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Class with the business logic necessary for the
 * SearchQueryController that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@Primary
public class SearchQuery implements JsonComponent {

    @Inject
    ElasticSearchUtilsInterface elasticUtils;

    @Inject
    MainResponseUtilsInterface mainResponseUtils;

    /**
     * Method that answers the petition with a HttpResponse
     * @param parameters List of parameters that need to be in the HttpResponse if there is no problem
     * @return HttpResponse with the parameters if there is no error, if not, it contains a not found
     */
    public HttpResponse getQueryJson(String[] parameters){
        try {
            MainResponse response = elasticUtils.getElasticClientResponse();

            if (response != null && parameters.length >= 1) {
                return HttpResponse.ok(getOkJson(response, parameters[0]));
            }
        }
        catch(IOException e){

        }
        JsonError error = new JsonError("There was an error with the petition, try again");

        return HttpResponse.notFound().body(error);
    }

    /**
     * Method to create the ok response for this url
     * @param response contains the cluster
     * @param query the url parameter
     * @return a JSON with the query and the cluster name and version of elasticSearch
     */
    private SearchQueryJson getOkJson(MainResponse response, String query){
        String clusterVersion = mainResponseUtils.getClusterNameAndVersion(response);

        return new SearchQueryJson(query, clusterVersion);
    }
}
