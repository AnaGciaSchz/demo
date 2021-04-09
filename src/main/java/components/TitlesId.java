package components;

import io.micronaut.http.HttpResponse;
import utils.elasticSearch.results.TitleIdElastic;
import utils.json.JsonUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Logic to answers to the /titles/{titleId} request
 *
 * @Author Ana Garcia
 */
public class TitlesId {

    @Inject
    TitleIdElastic titlesId;

    @Inject
    JsonUtilsInterface jsonUtils;

    /**
     * Methot that return the movie with that id
     *
     * @return A response with the movie
     * @throws ParseException if it's necessary
     */
    public HttpResponse getMovie(String titleId) throws ParseException, IOException {
        Map<String, Object> results = titlesId.imdbTitleId(titleId);
        return HttpResponse.ok().body(jsonUtils.getOkJson(results));

    }
}
