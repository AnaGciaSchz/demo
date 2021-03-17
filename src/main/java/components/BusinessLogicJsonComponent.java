package components;

import io.micronaut.http.HttpResponse;

import java.util.Map;

/**
 * Interface for the classes that have business logic and returns a HttpResponse
 *
 * @Author Ana Garcia
 */
public interface BusinessLogicJsonComponent {

    /**
     * Method that answers the petition with a HttpResponse
     *
     * @param parameters List of parameters that need to be in the HttpResponse if there is no problem
     * @return HttpResponse with the parameters if there is no error, if not, it contains a not found
     */
    HttpResponse getQueryJson(Map<String, String> parameters);
}
