package components;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import jsonManaging.SearchTitlesJson;
import jsonManaging.TitleJson;
import utils.elasticSearch.search.SearchElastic;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class with the business logic necessary for the
 * SearchQueryController that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@Primary
public class SearchTitle implements JsonComponent {

    @Inject
    SearchElastic searchElastic;

    /**
     * Method that answers the petition with a HttpResponse
     * @param parameters List of parameters that need to be in the HttpResponse if there is no problem
     * @return HttpResponse with the parameters if there is no error, if not, it contains a not found
     */
    public HttpResponse getQueryJson(String[] parameters){
        try {

            if (parameters.length >= 1) {
                List<Map> hits = searchElastic.searchImdb(parameters[0]);
                 return HttpResponse.ok().body(getOkJson(hits));
            }
        }
        catch(IOException e){

        }
        JsonError error = new JsonError("There was an error with the petition, try again");

        return HttpResponse.notFound().body(error);
    }

    /**
     * Method to create the ok response for this url
     * @param hits contains the search result
     * @return a JSON with the total of results and the results
     */
    private SearchTitlesJson getOkJson(List<Map> hits){
        TitleJson[] titles = new TitleJson[hits.size()];
        for(int i = 0; i<hits.size();i++){
            Map title = hits.get(i);
            titles[i]=new TitleJson(title.get("index"),title.get("primaryTitle"),
                    title.get("genres"),title.get("titleType"),title.get("start_year"),title.get("end_year"));
        }

        return new SearchTitlesJson(titles.length, titles);
    }
}
