package jsonManaging;

import io.micronaut.http.HttpResponse;

public interface JsonManaging {

    HttpResponse getQueryJson(String[] parameters);
}
