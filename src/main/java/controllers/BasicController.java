package controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.server.exceptions.InternalServerException;

import java.io.IOException;
import java.text.ParseException;

/**
 * Basic super class to cath the error from all the controllers that implements it
 *
 * @Author Ana García
 */
public class BasicController {

    /**
     * Method to show an error if theres an IllegalArgumentException. It means that we couldn't find the
     * title due to an error in an argument.
     *
     * @param request Request to return the error
     * @param e       Error, it contains the exact message
     * @return An HttpResponse with the error to show to the user
     */
    @Error()
    public HttpResponse<JsonError> notFoundError(HttpRequest request, IllegalArgumentException e) {
        JsonError error = new JsonError("Couldn't find a title, maybe there's an illegal argument: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);
    }

    /**
     * Method to show an error if theres an IOException. It means that there was an error in the server
     *
     * @param request Request to return the error
     * @param e       Error, it contains the exact message
     * @return An HttpResponse with the error to show to the user
     */
    @Error()
    public HttpResponse<JsonError> serverError(HttpRequest request, IOException e) {
        JsonError error = new JsonError("Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError().body(error);
    }

    /**
     * Method to show an error if theres an InternalServerException.
     * It means that there was an internal error in the server
     *
     * @param request Request to return the error
     * @param e       Error, it contains the exact message
     * @return An HttpResponse with the error to show to the user
     */
    @Error()
    public HttpResponse<JsonError> internalServerError(HttpRequest request, InternalServerException e) {
        JsonError error = new JsonError("Internal Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError().body(error);
    }

    /**
     * Method to show an error if theres an ParseException. It means that there
     * were an error while trying to parse a date.
     *
     * @param request Request to return the error
     * @param e       Error, it contains the exact message
     * @return An HttpResponse with the error to show to the user
     */
    @Error()
    public HttpResponse<JsonError> parseError(HttpRequest request, ParseException e) {
        JsonError error = new JsonError("There was an error with the format of the dates, write them like 2000/2010: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>badRequest().body(error);
    }
}
