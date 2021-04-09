package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the test about the current version of the /titles url
 *
 * @Author Ana Garcia
 */
@SuppressWarnings("ALL")
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TitlesIdControllerTest {

    /**
     * "Client" that will navigate through the page and do the tests
     */
    @Inject
    @Client("/")
    RxHttpClient client;

    /**
     * Test to see if the movie that is returned is correct when you write the
     * exact id
     */
    @Test
    public void testExactId() throws JsonProcessingException {
        HttpRequest<String> request = HttpRequest.GET("/titles/tt0000001");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, Map.class);
        ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");

        assertEquals(1, map.get("total"));
        assertEquals("tt0000001", items.get(0).get("id"));
        assertEquals("Carmencita", items.get(0).get("title"));
    }

    /**
     * Test to see if the movie that is returned is correct when you
     * onlye write the numbers of tthe ide (without tt)
     */
    @Test
    public void testNumberId() throws JsonProcessingException {
        HttpRequest<String> request = HttpRequest.GET("/titles/0000001");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, Map.class);
        ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");

        assertEquals(1, map.get("total"));
        assertEquals("tt0000001", items.get(0).get("id"));
        assertEquals("Carmencita", items.get(0).get("title"));
    }
    /**
     * Test to see if it doesnt return a movie if the number is not exact
     */
    @Test
    public void testIncorrectId() throws JsonProcessingException {
        HttpRequest<String> request = HttpRequest.GET("/titles/1");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, Map.class);
        ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");

        assertEquals(0, map.get("total"));
    }
}
