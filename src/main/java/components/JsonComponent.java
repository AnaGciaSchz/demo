package components;

import io.micronaut.http.HttpResponse;

public interface JsonComponent {

    HttpResponse getQueryJson(String[] parameters);
}
