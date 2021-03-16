package components;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import jsonManaging.results.*;
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
    public HttpResponse getQueryJson(Map<String,String> parameters){
        JsonError error = new JsonError("There was an error, try again with another parameter");
        try {

            if (parameters.get("query") != null) {
                Map<String,List<Map>> results = searchElastic.searchImdb(parameters);
                 return HttpResponse.ok().body(getOkJson(results));
            }
        }
        catch(IOException e){
            error = new JsonError("There was an error with the petition, try again");

        }

        return HttpResponse.notFound().body(error);
    }

    /**
     * Method to create the ok response for this url
     * @param results contains the search result (list) and the aggregations (list)
     * @return a JSON with the total of results and the results
     */
    private SearchTitlesJson getOkJson(Map<String,List<Map>> results){

        List<Map> hits = results.get("hits");

        TitleJson[] titles = new TitleJson[hits.size()];

        for(int i = 0; i<hits.size();i++){
            Map title = hits.get(i);
            titles[i]=new TitleJson(title.get("index"),title.get("primaryTitle"),
                    title.get("genres"),title.get("titleType"),title.get("start_year"),title.get("end_year"));
        }

        AggregationsJson a = new AggregationsJson(null,null);

        if(results.get("aggregations") != null){
            /**Map<String, Integer> genres = new HashMap<String, Integer>();
             genres.put(parameters[1],4);

             Map<String, Integer> types = new HashMap<String, Integer>();
             types.put(parameters[2],5);

             AggregationsJson a = new AggregationsJson(genres,types);*/
        /**
            Map<String,Object> m = (Map<String, Object>) results.get("aggregations");
            System.out.println("hgsjhag");
         */
        }



        return new SearchTitlesJson(titles.length, titles, a);
    }
}
