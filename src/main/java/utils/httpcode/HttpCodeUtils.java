/**
package utils.httpcode;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.annotation.Error;

import javax.inject.Singleton;

@Singleton
public class HttpCodeUtils {

    @Error(global = true)
    public HttpResponse<JsonError> InternalserverError(HttpRequest request, Throwable e) {
        JsonError error = new JsonError("Bad Things Happened: " + e.getMessage()).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError().body(error);
    }

    @Error(status = HttpStatus.NOT_FOUND)
    public HttpResponse<JsonError> titleNotFound(HttpRequest request) {
        JsonError error = new JsonError("Title Not Found").link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound().body(error);

    }
}
 */
