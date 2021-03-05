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
        assertEquals("{\"total\":7,\"items\":[{\"id\":\"tt0000001\",\"title\":\"Carmencita\","
                +"\"genres\":[\"Documentary\",\"Short\"],\"type\":\"short\",\"start_year\":\"1894\"},"
                +"{\"id\":\"tt0453643\",\"title\":\"Carmencita\",\"genres\":[\"Short\"],\"type\":\"short\""
                +",\"start_year\":\"1897\"},{\"id\":\"tt13864446\",\"title\":\"Carmencita\",\"genres\":"
                +"[\"Action\",\"Animation\",\"Drama\"],\"type\":\"tvEpisode\",\"start_year\":\"1988\"},"
                +"{\"id\":\"tt0764727\",\"title\":\"Carmencita mia\",\"genres\":[\"Comedy\",\"Musical\","
                +"\"Romance\"],\"type\":\"movie\",\"start_year\":\"1948\"},{\"id\":\"tt7200526\",\"title\""
                +":\"El cumpleaños de Carmencita\",\"genres\":[\"Comedy\",\"Drama\",\"Romance\"],\"type\":"
                +"\"tvEpisode\",\"start_year\":\"2017\"},{\"id\":\"tt0372198\",\"title\":\"Carmencita, "
                +"esta noche vas a ver\",\"genres\":[\"Short\"],\"type\":\"short\",\"start_year\":\"1999\"},"
                +"{\"id\":\"tt7200522\",\"title\":\"Una noche muy especial para Carmencita\",\"genres\":"
                +"[\"Comedy\",\"Drama\",\"Romance\"],\"type\":\"tvEpisode\",\"start_year\":\"2017\"}]}",
                body);
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
        assertEquals("{\"total\":10,\"items\":[{\"id\":\"tt0109830\",\"title\":\"Forrest Gump\","
                +"\"genres\":[\"Drama\",\"Romance\"],\"type\":\"movie\",\"start_year\":\"1994\"},{\"id\":"
                +"\"tt10150562\",\"title\":\"'Forrest Gump'\",\"genres\":[\"Documentary\",\"Short\","
                +"\"Talk-Show\"],\"type\":\"tvEpisode\",\"start_year\":\"2019\"},{\"id\":\"tt1775407\","
                +"\"title\":\"Forrest Gump\",\"genres\":[\"Comedy\",\"Short\"],\"type\":\"tvEpisode\","
                +"\"start_year\":\"2009\"},{\"id\":\"tt1166244\",\"title\":\"Forrest Gump\",\"genres\":"
                +"[\"Documentary\",\"History\"],\"type\":\"tvEpisode\",\"start_year\":\"2008\"},{\"id\":"
                +"\"tt12286596\",\"title\":\"Forrest Gump\",\"genres\":[\"Talk-Show\"],\"type\":\"tvEpisode"
                +"\",\"start_year\":\"2020\"},{\"id\":\"tt13653370\",\"title\":\"Forrest Gump\",\"genres\""
                +":[\"Talk-Show\"],\"type\":\"tvEpisode\",\"start_year\":\"2020\"},{\"id\":\"tt3905238\","
                +"\"title\":\"Forrest Gump\",\"genres\":[\"Comedy\"],\"type\":\"tvEpisode\",\"start_year\""
                +":\"2014\"},{\"id\":\"tt5274860\",\"title\":\"Forrest Gump\",\"genres\":[\"Comedy\"],\"type"
                +"\":\"tvEpisode\",\"start_year\":\"2009\"},{\"id\":\"tt5443092\",\"title\":\"Forrest Gump"
                +"\",\"genres\":[\"Comedy\",\"Short\"],\"type\":\"tvEpisode\",\"start_year\":\"2011\"},"
                +"{\"id\":\"tt5973974\",\"title\":\"Forrest Gump\",\"genres\":[\"Talk-Show\"],\"type\":"
                +"\"tvEpisode\",\"start_year\":\"2015\"}]}",
                body);
    }

    /**
     * Test for the The Avengers question of the guide
     */
    @Test
    public void testTheAvengers(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=The%20Avengers");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":10,\"items\":[{\"id\":\"tt0118661\",\"title\":\"The Avengers"
                +"\",\"genres\":[\"Action\",\"Adventure\",\"Sci-Fi\"],\"type\":\"movie\",\"start_year\":"
                +"\"1998\"},{\"id\":\"tt0054518\",\"title\":\"The Avengers\",\"genres\":[\"Action\","
                +"\"Comedy\",\"Crime\"],\"type\":\"tvSeries\",\"start_year\":\"1961\",\"end_year\":"
                +"\"1969\"},{\"id\":\"tt0034639\",\"title\":\"The Avengers\",\"genres\":[\"Drama\","
                +"\"War\"],\"type\":\"movie\",\"start_year\":\"1942\"},{\"id\":\"tt0848228\",\"title\""
                +":\"The Avengers\",\"genres\":[\"Action\",\"Adventure\",\"Sci-Fi\"],\"type\":\"movie\""
                +",\"start_year\":\"2012\"},{\"id\":\"tt0880282\",\"title\":\"The Avengers\",\"genres\""
                +":[\"Comedy\",\"Drama\",\"Romance\"],\"type\":\"tvEpisode\",\"start_year\":\"2006\"},"
                +"{\"id\":\"tt0507806\",\"title\":\"The Avengers\",\"genres\":[\"Drama\",\"Fantasy\","
                +"\"Mystery\"],\"type\":\"tvEpisode\",\"start_year\":\"1961\"},{\"id\":\"tt0550017\","
                +"\"title\":\"The Avengers\",\"genres\":[\"Western\"],\"type\":\"tvEpisode\",\"start_year\""
                +":\"1974\"},{\"id\":\"tt0167779\",\"title\":\"The Avengers\",\"genres\":[\"Adventure\"],"
                +"\"type\":\"movie\",\"start_year\":\"1950\"},{\"id\":\"tt10960560\",\"title\":\""
                +"The Avengers\",\"genres\":[\"Comedy\",\"Talk-Show\"],\"type\":\"tvEpisode\","
                +"\"start_year\":\"2014\"},{\"id\":\"tt1476236\",\"title\":\"The Avengers\",\"genres\":"
                +"[\"Biography\",\"Documentary\"],\"type\":\"tvEpisode\",\"start_year\":\"1992\"}]}",
                body);
    }

    /**
     * Test for the Spiderman question of the guide
     */
    @Test
    public void testSpiderman(){
        HttpRequest<String> request = HttpRequest.GET("/search?query=Spiderman");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("{\"total\":10,\"items\":[{\"id\":\"tt0100669\",\"title\":\"Spiderman\","
                +"\"genres\":[\"Short\"],\"type\":\"short\",\"start_year\":\"1990\"},{\"id\":\"tt0964012\""
                +",\"title\":\"Spiderman\",\"genres\":[\"Crime\",\"Documentary\",\"Mystery\"],\"type\":"
                +"\"tvEpisode\",\"start_year\":\"2000\"},{\"id\":\"tt1779548\",\"title\":\"Spiderman\","
                +"\"genres\":[\"Comedy\",\"Short\"],\"type\":\"tvEpisode\",\"start_year\":\"2008\"},"
                +"{\"id\":\"tt1785572\",\"title\":\"Spiderman\",\"genres\":[\"Documentary\",\"Short\"],"
                +"\"type\":\"short\",\"start_year\":\"2010\"},{\"id\":\"tt11981408\",\"title\":\"Spiderman\""
                +",\"genres\":[\"Comedy\"],\"type\":\"tvEpisode\",\"start_year\":\"2019\"},{\"id\":"
                +"\"tt5500644\",\"title\":\"Spiderman\",\"genres\":[\"Comedy\"],\"type\":\"tvEpisode\","
                +"\"start_year\":\"2015\"},{\"id\":\"tt2012885\",\"title\":\"Spiderman\",\"genres\":"
                +"[\"Reality-TV\"],\"type\":\"tvEpisode\",\"start_year\":\"2011\"},{\"id\":\"tt2125854\","
                +"\"title\":\"Spiderman\",\"genres\":[\"Comedy\"],\"type\":\"tvEpisode\",\"start_year\":"
                +"\"2011\"},{\"id\":\"tt3449184\",\"title\":\"Spiderman\",\"genres\":[\"News\"],\"type\":"
                +"\"tvEpisode\",\"start_year\":\"2013\"},{\"id\":\"tt6786512\",\"title\":\"Spiderman\","
                +"\"genres\":[\"Comedy\"],\"type\":\"tvEpisode\",\"start_year\":\"2015\"}]}",
                body);
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
}
