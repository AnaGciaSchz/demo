package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import components.JsonComponent;

import javax.inject.Inject;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Autor Ana Garcia
 */
@Controller("/search")
public class SearchController {

    @Inject
    JsonComponent searchQuery;

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     * @param query Query that will be used to search using elasticSearch
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query) {
        String[] s = new String[1];
        s[0] = query;
        return searchQuery.getQueryJson(s);
    }

}
