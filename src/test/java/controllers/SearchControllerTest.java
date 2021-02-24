package controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the test about the /search url
 *
 * @Autor Ana Garcia
 */
@MicronautTest
public class SearchControllerTest {

    /**
     * "Client" that will navigate through the page and do the tests
     */
    @Inject
    @Client("/")
    RxHttpClient client;

    /**
     * Test to see if a correct request is correctly handled
     */
    @Test
    public void testCorrectSearch(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=prueba");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"query\":\"prueba\",\"cluster_name\":\"docker-cluster 7.11.1\"}",body);
    }

    /**
     * Test to see if a request with no query parametes produces the correct exception
     */
    @Test
    public void testNoQueryParameter(){
        try {
            HttpRequest<String> request = HttpRequest.GET("/search");
        }
        catch(HttpClientResponseException e) {
            assertEquals("Required QueryValue [query] not specified", e.getMessage());
        }
    }


}
