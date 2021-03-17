package controllers;

import components.SearchTitle;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@Controller("/search")
public class SearchController {

    @Inject
    SearchTitle searchTitle;

    /**
     * Method that answers the /search url that has a "query" param.
     * /search?query={value}
     *
     * @param query Query that will be used to search using elasticSearch
     * @param genre Genre where the movies should belong (optional)
     * @param type  Type of the media (optional)
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query, @Nullable @QueryValue String genre, @Nullable @QueryValue String type) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("query", query);
        if (genre != null && genre != "") {
            parameters.put("genre", genre);
        }
        if (type != null && type != "") {
            parameters.put("type", type);
        }

        return searchTitle.getQueryJson(parameters);
    }

}
