package utils.elasticSearch.manageIndex;

import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.inout.FileUtil;
import utils.parser.TitlesImdbParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

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
        List<Object> list = new ArrayList<>();

        BufferedReader titleReader = fileutil.getReaderCsv(pathTiles);
        titleReader.readLine();//skip head
        Map ratings = fromListToMapRatings(fileutil.readCsv(pathRatings));
        String line = titleReader.readLine();
        int i = 0;

        while (line != null) {
            String[] title = line.split("\t");
            list.add(parser.getTitlesAndRatings(title, (List<String>) ratings.get(title[0])));
            if (i % 1000 == 0) {
                elasticSearchUtils.bulkAdd(list);
                list = new ArrayList<>();
            }
            i++;
            line = titleReader.readLine();
        }
        titleReader.close();
        elasticSearchUtils.bulkAdd(list);
    }

    private Map fromListToMapRatings (List<String > ratings){
        Map<String, List> map = new HashMap();
        for(int i = 0; i<ratings.size();i++){
            map.put(ratings.get(i).split("\t")[0], Collections.singletonList(ratings.get(i)));
        }
        return map;
    }
}
