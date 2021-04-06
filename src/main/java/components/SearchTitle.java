package components;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import utils.elasticSearch.results.SearchElastic;
import utils.json.JsonUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Class with the business logic necessary for the current version of
 * SearchQueryController that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@SuppressWarnings("rawtypes")
@Primary
public class SearchTitle implements BusinessLogicJsonComponent {

    @Inject
    SearchElastic searchElastic;

    @Inject
    JsonUtilsInterface jsonUtils;

    /**
     * Method that answers the petition with a HttpResponse
     *
     * @param parameters List of parameters that need to be in the HttpResponse if there is no problem
     * @return HttpResponse with the parameters if there is no error, if not, it contains a not found
     */
    public HttpResponse getQueryJson(Map<String, String> parameters) throws IOException, ParseException {
            if (parameters.get("query") != null) {
                Map<String, Object> results = searchElastic.searchImdb(parameters);
                return HttpResponse.ok().body(jsonUtils.getOkJson(results));
            }

            throw new IllegalArgumentException("You need to write a query!");
    }
}
