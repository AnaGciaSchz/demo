package controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class SearchControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testSearch(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=prueba");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\n\"query\":\"prueba\",\n\"cluster_name\":\"7.11.1\"\n}",body);
    }

}
