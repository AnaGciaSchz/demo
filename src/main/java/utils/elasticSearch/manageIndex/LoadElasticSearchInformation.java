package utils.elasticSearch.manageIndex;

import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.inout.FileUtil;
import utils.parser.TitlesImdbParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that loads information to ElasticSearch
 *
 * @Author Ana Garcia
 */
public class LoadElasticSearchInformation {

    /**
     * Method that has the logic that loads the information from a csv into the imdb database with its format
     *
     * @throws IOException if there's an error
     */
    public void loadImdbInformation(String path) throws IOException {

        FileUtil fileutil = new FileUtil();
        TitlesImdbParser parser = new TitlesImdbParser();
        ElasticSearchUtilsInterface elasticSearchUtils = new ElasticSearchQueryUtils();

        List<String> originalLines = fileutil.readCsv(path);
        List<Object> list = new ArrayList<>();
        for (int i = 1; i < originalLines.size(); i++) {
            list.add(parser.getTitle(originalLines.get(i)));
            if (i % 1000 == 0) {
                elasticSearchUtils.bulkAdd(list);
                list = new ArrayList<>();
            }
        }
        elasticSearchUtils.bulkAdd(list);
    }
}
