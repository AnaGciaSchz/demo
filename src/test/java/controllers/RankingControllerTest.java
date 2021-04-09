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
 * Test class for the test about the current version of the /ranking/movie url
 *
 * @Author Ana Garcia
 */
@SuppressWarnings("ALL")
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RankingControllerTest {

    /**
     * "Client" that will navigate through the page and do the tests
     */
    @Inject
    @Client("/")
    RxHttpClient client;

    /**
     * Test to see if the ranking is correctly sorted
     */
    @Test
    public void testCorrectRanking() throws JsonProcessingException {
        HttpRequest<String> request = HttpRequest.GET("/ranking/movie");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, Map.class);
        ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");

        assertEquals(10, map.get("total"));
        assertEquals("The Shawshank Redemption", items.get(0).get("title"));
        assertEquals("The Chaos Class", items.get(1).get("title"));
        assertEquals("The Godfather", items.get(2).get("title"));
        assertEquals("CM101MMXI Fundamentals", items.get(3).get("title"));
        assertEquals("Mirror Game", items.get(4).get("title"));
        assertEquals("Love on a Leash", items.get(5).get("title"));
        assertEquals("Aloko Udapadi", items.get(6).get("title"));
        assertEquals("The Dark Knight", items.get(7).get("title"));
        assertEquals("The Godfather: Part II", items.get(8).get("title"));
        assertEquals("12 Angry Men", items.get(9).get("title"));
    }
}
