package utils.elasticSearch.manageIndex;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Class that creates an index in elastic search
 *
 * @Author Ana Garcia
 */
public class CreateElasticSearchIndex {

    /**
     * Method that creates an index using a json file with its properties and settings
     * @param fileName path of the json
     * @throws IOException
     */
    public void createImdbIndex(String fileName) throws IOException {
        ElasticSearchUtilsInterface elasticSearchUtils = new ElasticSearchQueryUtils();

        CreateIndexRequest create = new CreateIndexRequest("imdb");

        Path path = Path.of(fileName);
        String text = Files.readString(path);
        Map<String, Object> map = new ObjectMapper().readValue(text, Map.class);
        create.source(map);

        RestHighLevelClient client = elasticSearchUtils.getClientInstance();

        client.indices().create(create, RequestOptions.DEFAULT);

    }
}
