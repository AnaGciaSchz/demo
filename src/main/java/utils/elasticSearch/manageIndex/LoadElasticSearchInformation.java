package utils.elasticSearch.manageIndex;

import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.inout.FileUtil;
import utils.parser.TitlesImdbParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void loadImdbInformation(String pathTiles, String pathRatings) throws IOException {

        FileUtil fileutil = new FileUtil();
        TitlesImdbParser parser = new TitlesImdbParser();
        ElasticSearchUtilsInterface elasticSearchUtils = new ElasticSearchQueryUtils();

        List<String> titleLines = fileutil.readCsv(pathTiles);
        List<String> ratingsLines = fileutil.readCsv(pathRatings);

        List<Object> list = new ArrayList<>();
        for (int i = 1; i < titleLines.size(); i++) {
            list.add(parser.getTitlesAndRatings(titleLines.get(i).split("\t"),ratingsLines));
            if (i % 100 == 0) {
                elasticSearchUtils.bulkAdd(list);
                list = new ArrayList<>();
            }
        }
        elasticSearchUtils.bulkAdd(list);
    }
}
