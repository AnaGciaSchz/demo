package controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.server.exceptions.InternalServerException;

import java.io.IOException;
import java.text.ParseException;

public class BasicController {

    @Error(global = true)
    public HttpResponse<JsonError> notFoundError(HttpRequest request,  IllegalArgumentException e) {
        JsonError error = new JsonError("Couldn't find a title, maybe there's an illegal argument: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);
    }
    @Error(global = true)
    public HttpResponse<JsonError> serverError(HttpRequest request,  IOException e) {
        JsonError error = new JsonError("Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError().body(error);
    }
    @Error(global = true)
    public HttpResponse<JsonError> internaServerError(HttpRequest request,  InternalServerException e) {
        JsonError error = new JsonError("Internal Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError().body(error);
    }
    @Error(global = true)
    public HttpResponse<JsonError> parseError(HttpRequest request,  ParseException e) {
        JsonError error = new JsonError("There was an error with the format of the dates, write them like 2000/2010: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>badRequest().body(error);
    }
}
