package controllers;

import components.ImdbRanking;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;

/**
 * Controller that answers the /ranking/movie url with the ranking of the movies
 * Author Ana Garcia
 */
@Controller("/ranking/movie")
public class RankingController extends BasicController {

    @Inject
    ImdbRanking imdbRanking;

    /**
     * Method that answers the ranking/movie url
     *
     * @return A JSON response
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index() throws IOException, ParseException {
        return imdbRanking.getMovieRanking();
    }
}
