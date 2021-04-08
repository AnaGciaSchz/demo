package controllers;

import components.ImdbRanking;
import components.TitlesId;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
/**
 * Controller that answers to the petition or the /titles url
 *
 * @Author Ana Garcia
 */
@Controller("/titles")
public class IdTitleController {

        @Inject
        TitlesId titlesId;

        /**
         * Method that answers the titles/{titleId} url
         *
         * @return A JSON response with the movie
         */
        @Get("/{titleId}")
        public HttpResponse index(@PathVariable String titleId) throws IOException, ParseException {
            return titlesId.getMovie(titleId);
        }
}

