package utils.elasticSearch.manageIndex;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.inout.FileUtil;
import utils.parser.TitlesImdbParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that deletes information in ElasticSearch
 *
 * @Author Ana Garcia
 */
public class DeleteElasticSearchInformation {

    /**
     * Method that has the logic that deletes the information from the an elasticsearch index
     *
     * @throws IOException if there's an error
     */
    public void deleteInformation(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        ElasticSearchUtilsInterface elasticSearchUtils = new ElasticSearchQueryUtils();
        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        client.indices().delete(request, RequestOptions.DEFAULT);

    }
}
