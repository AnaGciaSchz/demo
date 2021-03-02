package utils;

import utils.elasticSearch.ElasticSearchQueryUtils;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.inout.FileUtil;
import utils.parser.TitlesImdbParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that loads titles information to ElasticSearch
 *
 * @Author Ana Garcia
 */
public class LoadElasticSearchInformation {

    /**
     * Method that has the logic that loads the information from the csv
     * @throws IOException
     */
    public void loadInformation() throws IOException {

        FileUtil fileutil = new FileUtil();
        TitlesImdbParser parser = new TitlesImdbParser();
        ElasticSearchUtilsInterface elasticSearchUtils = new ElasticSearchQueryUtils();

        List<String> s = fileutil.readCsv("/Users/anamariagarciasanchez/Documents/title.basics.tsv");
        List<Object> list = new ArrayList<Object>();
        for(int i = 1; i<s.size();i++) {
            list.add(parser.getTitle(s.get(i)));
            if(i%100 == 0){
                elasticSearchUtils.bulkAdd(list);
                list = new ArrayList<Object>();
            }
        }
        elasticSearchUtils.bulkAdd(list);
    }
}
