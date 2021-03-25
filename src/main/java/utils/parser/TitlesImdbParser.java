package utils.parser;

import utils.elasticSearch.ElasticSearchQueryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to parse the Imdb titles
 *
 * @Author Ana Garcia
 */
public class TitlesImdbParser {

    /**
     * Method that  parse a title into a list with the id of the title.
     *
     * @param title information about the title
     * @return a map with the information
     */
    public Map<String, Object> getTitle(String title) {

        String[] information = title.split("\t");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", information[0]);
        jsonMap.put("primaryTitle", information[2]);
        jsonMap.put("titleType", information[1]);
        jsonMap.put("genres", information[information.length - 1]);
        jsonMap.put("start_year", information[5]);
        if (!("\\N").equals(information[6])) {
            jsonMap.put("end_year", information[6]);
        }

        return jsonMap;

    }
    /**
     * Method that  parse a the titles and its ratings into a list of maps
     *
     * @param title a title to add
     * @param ratings all the read ratings
     * @return a list with all the information
     */
    public Map<String, Object> getTitlesAndRatings(String[] title, List<String> ratings) {
        String[] ratingData;

        for(int i = 1; i<ratings.size();i++){
            ratingData = ratings.get(i).split("\t");
            if (title[0].equals(ratingData[0])){
                ratings.remove(i);
                return returnMapTitle(title,ratingData);
            }
        }

        return returnMapTitle(title,null);

    }

    private Map<String, Object> returnMapTitle(String[] titleData, String[] ratingData){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", titleData[0]);
        jsonMap.put("primaryTitle", titleData[2]);
        jsonMap.put("titleType", titleData[1]);
        jsonMap.put("genres", titleData[titleData.length - 1]);
        jsonMap.put("start_year", titleData[5]);
        if (!("\\N").equals(titleData[6])) {
            jsonMap.put("end_year", titleData[6]);
        }
        if (ratingData !=  null) {
            jsonMap.put("averageRating", ratingData[1]);
            jsonMap.put("numVotes", ratingData[2]);
        }

        return jsonMap;
    }
}
