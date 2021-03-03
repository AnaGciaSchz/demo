package utils.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to parse the Imdb titles
 *
 * @Author Ana Garcia
 */
public class TitlesImdbParser {

    /**
     * Method that  parse a title into a list with the id of the title in the 0 position and
     *  * the maps for the json in the 1 position.
     * @param title information about the title
     * @return a map with the information
     */
    public Map<String,Object> getTitle(String title){

        String[] information = title.split("\t");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", information[0]);
        jsonMap.put("primaryTitle", information[2]);
        jsonMap.put("titleType", information[1]);
        jsonMap.put("genres", information[information.length-1]);
        jsonMap.put("start_year", information[5]);
        if(!("\\N").equals(information[6])) {
            jsonMap.put("end_year", information[6]);
        }

        return jsonMap;

    }
}
