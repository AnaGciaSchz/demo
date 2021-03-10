package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the test about the current version of the /search url
 *
 * @Author Ana Garcia
 */
@MicronautTest
public class SearchControllerTestVr2 {

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
        HttpRequest<String> request = HttpRequest.GET("/search?query=Carmencita");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(7,map.get("total"));
            assertEquals("Carmencita",items.get(0).get("title"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if it shows the correct message when there is no results
     */
    @Test
    public void testNoResults(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=sdsadsadads");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":0}",body);
    }

    /**
     * Test to see if a request with no query parameter produces the correct exception
     */
    @Test
    public void testNoQueryParameter(){
        try {
            HttpRequest.GET("/search");
        }
        catch(HttpClientResponseException e) {
            assertEquals("Required QueryValue [query] not specified", e.getMessage());
        }
    }


    /**
     * Test to see if a request with an empty query parameter like "/search?query=" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterWithEqual(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":0}",body);
    }

    /**
     * Test to see if a request with an empty query parameter like "/search?query" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterWithoutEqual(){
        HttpRequest<String> request = HttpRequest.GET("/search?query");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":0}",body);
    }

    /**
     * Test to see if a request with an empty query parameter like "/search?" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterJustInterrogation(){
        try {
            HttpRequest.GET("/search?");
        }
        catch(HttpClientResponseException e) {
            assertEquals("Required QueryValue [query] not specified", e.getMessage());
        }
    }

    /**
     * Test for the Forrest gump question of the guide
     */
    @Test
    public void testForrestGump(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=Forrest%20Gump");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(10,map.get("total"));
            assertEquals("Forrest Gump",items.get(0).get("title"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for the The Avengers question of the guide
     */
    @Test
    public void testTheAvengers(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=The%20Avengers");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(10,map.get("total"));
            assertEquals("The Avengers",items.get(0).get("title"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for the Spiderman question of the guide
     */
    @Test
    public void testSpiderman(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=Spiderman");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(10,map.get("total"));
            assertEquals("Spider-Man",items.get(0).get("title"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the first result is what we are searching for (Title and type)
     */
    @Test
    public void testBestResultTitleAndType(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=The%20Avengers%20tvSeries");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(10,map.get("total"));
            assertEquals("The Avengers",items.get(0).get("title"));
            assertEquals("tvSeries",items.get(0).get("type"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the first result is what we are searching for (Title and start year)
     */
    @Test
    public void testBestResultTitleAndStartYear(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=The%20Avengers%202012");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(10,map.get("total"));
            assertEquals("The Avengers",items.get(0).get("title"));
            assertEquals("2012",items.get(0).get("start_year"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the first result is what we are searching for (Title and start year)
     */
    @Test
    public void testBestResultTitleAndGenre(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=The%20Avengers%20Comedy");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            ArrayList genres = (ArrayList) items.get(0).get("genres");
            assertEquals(10,map.get("total"));
            assertEquals("The Avengers",items.get(0).get("title"));
            assertEquals("Comedy",genres.get(0));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the search by id returns only one result and its correct
     */
    @Test
    public void testId(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=tt2577784");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            ArrayList<LinkedHashMap> items = (ArrayList) map.get("items");
            assertEquals(1,map.get("total"));
            assertEquals("tt2577784",items.get(0).get("id"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the upper and lower cases doesn't affect the search
     */
    @Test
    public void testUpperCase(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=Forrest%20Gump");
        String body1 = client.toBlocking().retrieve(request);
        request = HttpRequest.GET("/search?query=FORREST%20GUMP");
        String body2 = client.toBlocking().retrieve(request);
        request = HttpRequest.GET("/search?query=FOrReST%20GuMp");
        String body3 = client.toBlocking().retrieve(request);

        assertNotNull(body1);
        assertNotNull(body2);
        assertNotNull(body3);

        assertEquals(body1,body2);
        assertEquals(body1,body3);
        assertEquals(body2,body3);



    }

     /**
     * Test to see if the accents doesn't affect the search
     */
    @Test
    public void testAccent(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=Spiderman");
        String body1 = client.toBlocking().retrieve(request);
        request = HttpRequest.GET("/search?query=Sp%C3%ADd%C3%A9rm%C3%A1n");
        String body2 = client.toBlocking().retrieve(request);

        assertNotNull(body1);
        assertNotNull(body2);

        assertEquals(body1,body2);
    }

    /**
     * /**
     *      * Test to see if the - symbol doesn't affect the search
     *      */
     /**
    @Test
     *

    public void testHyphenSymbol() {
     *HttpRequest<String> request = HttpRequest.GET("/search?query=Spiderman");
     *String body1 = client.toBlocking().retrieve(request);
     *request = HttpRequest.GET("/search?query=Spider-Man");
     *String body2 = client.toBlocking().retrieve(request);
     *request = HttpRequest.GET("/search?query=S-p-i-d-e-r-m-a-n");
     *String body3 = client.toBlocking().retrieve(request);
     *
     *assertNotNull(body1);
     *assertNotNull(body2);
     *assertNotNull(body3);
     *
     *assertEquals(body1, body2);
     *assertEquals(body1, body3);
     *assertEquals(body2, body3);
     *}
     */
}
