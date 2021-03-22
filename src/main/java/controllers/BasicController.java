package controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.server.exceptions.InternalServerException;

import java.io.IOException;

public class BasicController {

    @Error(global = true)
    public HttpResponse<JsonError> notFoundError(HttpRequest request,  IllegalArgumentException e) {
        JsonError error = new JsonError("Couldn't find a title, maybe there's an illegal argument: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);
    }
    @Error(global = true)
    public HttpResponse<JsonError> serverError(HttpRequest request,  IOException e) {
        JsonError error = new JsonError("Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);
    }@Error(global = true)
    public HttpResponse<JsonError> internaServerError(HttpRequest request,  InternalServerException e) {
        JsonError error = new JsonError("Internal Server error, try again: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);
    }
}
