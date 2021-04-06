package components;

import io.micronaut.http.HttpResponse;
import utils.elasticSearch.results.RankingElastic;
import utils.json.JsonUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Class that answers to the petitions of the RankingController class
 *
 * @Author Ana Garcia
 */
public class ImdbRanking {

    @Inject
    RankingElastic rankingElastic;

    @Inject
    JsonUtilsInterface jsonUtils;

    /**
     * Methot that retusn the top 10 movies of Imdb
     *
     * @return A response with the movies
     * @throws ParseException if it's necessary
     */
    public HttpResponse getMovieRanking() throws ParseException, IOException {
        Map<String, Object> results = rankingElastic.imdbRankingMovies();
        return HttpResponse.ok().body(jsonUtils.getOkJson(results));

    }
}
