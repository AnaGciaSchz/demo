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
 * Class with the business logic necessary for the current version of
 * SearchQueryController that answers to the petition or the /search url
 *
 * @Author Ana Garcia
 */
@Primary
public class SearchTitle implements BusinessLogicJsonComponent {

    @Inject
    SearchElastic searchElastic;

    /**
     * Method that answers the petition with a HttpResponse
     * @param parameters List of parameters that need to be in the HttpResponse if there is no problem
     * @return HttpResponse with the parameters if there is no error, if not, it contains a not found
     */
    public HttpResponse getQueryJson(String[] parameters){
        JsonError error = new JsonError("There was an error, try again with another parameter");
        try {

            if (parameters.length >= 1) {
                String[] fields = {"index","primaryTitle", "genres", "titleType", "start_year","end_year"};
                List<Map> hits = searchElastic.search("imdb",fields,parameters[0]);
                 return HttpResponse.ok().body(getOkJson(hits));
            }
        }
        catch(IOException e){
            error = new JsonError("There was an error with the petition, try again");

        }

        return HttpResponse.notFound().body(error);
    }

    /**
     * Method to create the ok response for this url
     * @param hits contains the search result (list)
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
