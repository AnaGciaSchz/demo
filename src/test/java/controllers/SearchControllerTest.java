package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchControllerTest {

    /**
     * "Client" that will navigate through the page and do the tests
     */
    @Inject
    @Client("/")
    RxHttpClient client;

    HttpRequest<String> request;
    String body, body1, body2, body3, body4;
    ObjectMapper mapper;
    Map map;
    ArrayList<LinkedHashMap> items;


    @BeforeAll
    public void beforeAll() {
        mapper = new ObjectMapper();
    }

    /**
     * Test to see if a correct request is correctly handled
     */
    @Test
    public void testCorrectSearch() {
        body = getBody("Carmencita", null, null,null);

        assertNotNull(body);
        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(7, map.get("total"));
            assertEquals("Carmencita", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if it shows the correct message when there is no results
     */
    @Test
    public void testNoResults() {
        request = HttpRequest.GET("/search?query=sdsadsadads");
        body = getBody("sdsadsadads", null, null,null);

        assertNotNull(body);
        assertEquals("{\"total\":0,\"aggregations\":{}}", body);
    }

    /**
     * Test to see if a request with no query parameter produces the correct exception
     */
    @Test
    public void testNoQueryParameter() {
        try {
            HttpRequest.GET("/search");
        } catch (HttpClientResponseException e) {
            assertEquals("Required QueryValue [query] not specified", e.getMessage());
        }
    }


    /**
     * Test to see if a request with an empty query parameter like "/search?query=" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterWithEqual() {
        request = HttpRequest.GET("/search?query=");
        body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":0,\"aggregations\":{}}", body);
    }

    /**
     * Test to see if a request with an empty query parameter like "/search?query" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterWithoutEqual() {
        request = HttpRequest.GET("/search?query");
        body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":0,\"aggregations\":{}}", body);
    }

    /**
     * Test to see if a request with an empty query parameter like "/search?" produces the correct exception
     */
    @Test
    public void testEmptyQueryParameterJustInterrogation() {
        try {
            HttpRequest.GET("/search?");
        } catch (HttpClientResponseException e) {
            assertEquals("Required QueryValue [query] not specified", e.getMessage());
        }
    }

    /**
     * Test for the Forrest gump question of the guide
     */
    @Test
    public void testForrestGump() {
        body = getBody("Forrest Gump", null, null,null);

        assertNotNull(body);
        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("Forrest Gump", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for the The Avengers question of the guide
     */
    @Test
    public void testTheAvengers() {
        body = getBody("The Avengers", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("The Avengers", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for the Spiderman question of the guide
     */
    @Test
    public void testSpiderman() {
        body = getBody("Spiderman", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("Spiderman", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the first result is what we are searching for (Title and type)
     */
    @Test
    public void testBestResultTitleAndType() {
        body = getBody("The Avengers tvSeries", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("The Avengers", items.get(0).get("title"));
            assertEquals("movie", items.get(0).get("type"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the first result is what we are searching for (Title and start year)
     */
    @Test
    public void testBestResultTitleAndStartYear() {
        body = getBody("The Avengers 2012", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("Marvel's The Avengers (2012)", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the first result is what we are searching for (Title and start year)
     */
    @Test
    public void testBestResultTitleAndGenre() {
        body = getBody("The Avengers Comedy", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            ArrayList genres = (ArrayList) items.get(0).get("genres");

            assertEquals(10, map.get("total"));
            assertEquals("The Avengers", items.get(0).get("title"));
                assertEquals("Action", genres.get(0));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the search by id returns only one result and its correct
     */
    @Test
    public void testId() {
        body = getBody("tt2577784", null, null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(1, map.get("total"));
            assertEquals("tt2577784", items.get(0).get("id"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the upper and lower cases doesn't affect the search
     */
    @Test
    public void testUpperCase() {
        body1 = getBody("el lago de los cisnes", null, null,null);
        body2 = getBody("EL LAGO DE LOS CISNES", null, null,null);
        body3 = getBody("El LAgO de Los CiSNEs", null, null,null);

        assertNotNull(body1);
        assertNotNull(body2);
        assertNotNull(body3);

        assertEquals(body1, body2);
        assertEquals(body1, body3);
        assertEquals(body2, body3);

    }

    /**
     * Test to see if the accents affect the search
     */
    @Test
    public void testAccent() {
        body1 = getBody("Spiderman", null, null,null);
        body2 = getBody("Spídérmán", null, null,null);

        assertNotNull(body1);
        assertNotNull(body2);

        assertNotEquals(body1, body2);
    }

    /**
     * Test to see if the - symbol affect the search the way it should
     */
    @Test
    public void testHyphenSymbol() {
        body1 = getBody("Spiderman", null, null,null);
        body2 = getBody("Spider-Man", null, null,null);
        body3 = getBody("Spi-der-man", null, null,null);
        body4 = getBody("S-p-i-d-e-r-m-a-n", null, null,null);

        assertNotNull(body1);
        assertNotNull(body2);
        assertNotNull(body3);
        assertNotNull(body4);

        assertEquals(body1, body3);
        assertEquals(body1, body4);
        assertEquals(body3, body4);


        assertNotEquals(body1, body2);
        assertNotEquals(body3, body2);
        assertNotEquals(body4, body2);

        try {
            map = mapper.readValue(body1, Map.class);
            items = (ArrayList) map.get("items");
            assertEquals(10, map.get("total"));
            assertEquals("Spiderman", items.get(0).get("title"));


            map = mapper.readValue(body2, Map.class);
            items = (ArrayList) map.get("items");
            assertEquals(10, map.get("total"));
            assertEquals("Spider-Man", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the strange symbols ins spanish (like ç) affect the search the way it should
     */
    @Test
    public void testStrangeSymbol() {
        try {
            body1 = getBody("Barca", null, null,null);
            body2 = getBody("Barça", null, null,null);

            assertNotNull(body1);
            assertNotNull(body2);

            assertNotEquals(body1, body2);

            map = mapper.readValue(body1, Map.class);
            items = (ArrayList) map.get("items");
            assertEquals(10, map.get("total"));
            assertEquals("Barca! Barca! Barca!", items.get(0).get("title"));

            map = mapper.readValue(body2, Map.class);
            items = (ArrayList) map.get("items");
            assertEquals(10, map.get("total"));
            assertEquals("Barça Dreams", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the roman numeral is correctly translated
     */
    @Test
    public void testRomanNumbers() {
        body = getBody("Rocky 2", null, null,null);
        body2 = getBody("Rocky II", null, null,null);
        body3 = getBody("I Am Legend", null, null,null);

        assertNotNull(body);
        assertNotNull(body2);
        assertNotNull(body3);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("Rocky II", items.get(0).get("title"));

            map = mapper.readValue(body2, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("Rocky II", items.get(0).get("title"));

            map = mapper.readValue(body3, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));
            assertEquals("I Am Legend", items.get(0).get("title"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the aggregations are shown correctly
     */
    @Test
    public void testAggregations() {
        body = getBody("Hola", "Adult", "tvEpisode",null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            LinkedHashMap genres = (LinkedHashMap) aggregations.get("genres");
            LinkedHashMap types = (LinkedHashMap) aggregations.get("types");
            assertEquals(3, genres.size());
            assertEquals(1, types.size());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test to see if the aggregations are shown correctly when the user doesnt want the genre
     */
    @Test
    public void testAggregationsNoGenre() {
        body = getBody("Hola", null, "tvEpisode",null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            LinkedHashMap genres = (LinkedHashMap) aggregations.get("genres");
            LinkedHashMap types = (LinkedHashMap) aggregations.get("types");
            assertEquals(27, genres.size());
            assertEquals(1, types.size());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the aggregations are shown correctly when the user doesnt want the type
     */
    @Test
    public void testAggregationsNoType() {
        body = getBody("Hola", "Adult", null,null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            LinkedHashMap genres = (LinkedHashMap) aggregations.get("genres");
            LinkedHashMap types = (LinkedHashMap) aggregations.get("types");
            assertEquals(8, genres.size());
            assertEquals(5, types.size());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the aggregations are shown correctly when the user tests various aggregations
     */
    @Test
    public void testVariousAggregations() {
        body = getBody("Hola", "Music,Game", "tvSeries,tvEpisode",null);

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            LinkedHashMap genres = (LinkedHashMap) aggregations.get("genres");
            LinkedHashMap types = (LinkedHashMap) aggregations.get("types");
            assertEquals(12, genres.size());
            assertEquals(2, types.size());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the date filter is correctly working
     */
    @Test
    public void testDateFilter() {
        body = getBody("Hola", null, null,"1990/1999");

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            assertEquals("1997", items.get(0).get("start_year"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to see if the date aggregation is correctly working
     */
    @Test
    public void testDateAggregation() {
        body = getBody("Hola", null, null,"1990/1999");

        assertNotNull(body);

        try {
            map = mapper.readValue(body, Map.class);
            items = (ArrayList) map.get("items");

            assertEquals(10, map.get("total"));

            LinkedHashMap aggregations = (LinkedHashMap) map.get("aggregations");
            LinkedHashMap dates = (LinkedHashMap) aggregations.get("dates");
            assertEquals(1, dates.size());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the body of a search with the search param and possible genre and type aggregations
     *
     * @param searchTerm term to search
     * @param genre      genre to search
     * @param type       type to search
     * @return body of the response
     */
    private String getBody(String searchTerm, String genre, String type, String date) {
        String body = "";
        String uri = "/search?query=";
        try {
            String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.toString());
            uri += encodedSearchTerm;
            if (genre != null) {
                String encodedGenre = URLEncoder.encode(genre, StandardCharsets.UTF_8.toString());
                uri += "&genre=" + encodedGenre;
            }
            if (type != null) {
                String encodedType = URLEncoder.encode(type, StandardCharsets.UTF_8.toString());
                uri += "&type=" + encodedType;
            }if (date != null) {
                String encodedType = URLEncoder.encode(date, StandardCharsets.UTF_8.toString());
                uri += "&date=" + encodedType;
            }
            request = HttpRequest.GET(uri);
            body = client.toBlocking().retrieve(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return body;
        }
    }
}
