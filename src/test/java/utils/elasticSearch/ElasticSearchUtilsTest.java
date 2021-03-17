package utils.elasticSearch;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the test about the ElasticSearch functionality
 *
 * @Author Ana Garcia
 */
@MicronautTest
public class ElasticSearchUtilsTest {

    /**
     * "Client" that will navigate through the page and do the tests
     */
    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    /**
     * Test to see if the method getClientInstance() returns a client and that
     * client is always the same
     */
    @Test
    public void testGetClient() {
        RestHighLevelClient client1 = elasticSearchUtils.getClientInstance();
        RestHighLevelClient client2 = elasticSearchUtils.getClientInstance();
        RestHighLevelClient client3 = elasticSearchUtils.getClientInstance();

        assertNotNull(client1);
        assertNotNull(client2);
        assertNotNull(client3);

        assertEquals(client1, client2);
        assertEquals(client2, client3);
        assertEquals(client1, client3);
    }
}
