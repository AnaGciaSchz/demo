package utils.parser;

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
        if(ratings != null){
            return returnMapTitle(title,ratings);
        }
        return returnMapTitle(title,null);

    }

    /**
     * Method that returns a map with the necessary content to index the title in the index
     * @param titleData Date of the title
     * @param ratingData Data od the rating od the title (If it's null it means there is no information)
     * @return A Map representing the final data for the json (index, primaryTitle, titleType, genres, start_year.
     * OPTIONAL: end_year, averageRating, numVotes).
     */
    private Map<String, Object> returnMapTitle(String[] titleData, List<String> ratingData){
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
            String[] data = ratingData.get(0).split("\t");
            jsonMap.put("averageRating", data[1]);
            jsonMap.put("numVotes", data[2]);
        }

        return jsonMap;
    }
}
