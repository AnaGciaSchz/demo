package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import components.JsonComponent;
import io.micronaut.http.hateoas.JsonError;
import org.elasticsearch.action.search.SearchResponse;
import utils.elasticSearch.search.SearchElastic;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@Controller("/search")
public class SearchController {

    @Inject
    JsonComponent searchQuery;

    @Inject
    SearchElastic searchElastic;

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     * @param query Query that will be used to search using elasticSearch
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query) {
/**
        String[] s = new String[1];
        s[0] = query;
        return searchQuery.getQueryJson(s);
 */
        try {
            return (HttpResponse) searchElastic.searchImdb(query).get(0);
        }
        catch(IOException e){

            JsonError error = new JsonError("There was an error with the petition, try again");

            return HttpResponse.notFound().body(error);
        }
    }

}
