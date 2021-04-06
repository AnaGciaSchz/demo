package utils.json;

import io.micronaut.context.annotation.Primary;
import utils.json.managing.AggregationsJson;
import utils.json.managing.SearchTitlesJson;
import utils.json.managing.TitleJson;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Class with logic to manage jsons in the Imdb index
 *
 * @Author Ana Garcia
 */
@Primary
public class JsonUtilsImdb implements JsonUtilsInterface {

    /**
     * Method to create a json with results
     *
     * @param results contains the search result (list) and the aggregations (list)
     * @return a JSON with the total of results and the results
     */
    public SearchTitlesJson getOkJson(Map<String, Object> results) throws ParseException {

        List<Map> hits = (List<Map>) results.get("hits");

        TitleJson[] titles = new TitleJson[hits.size()];

        for (int i = 0; i < hits.size(); i++) {
            Map title = hits.get(i);
            titles[i] = new TitleJson(title.get("index"), title.get("primaryTitle"),
                    title.get("genres"), title.get("titleType"), title.get("start_year"),
                    title.get("end_year"), title.get("averageRating"), title.get("numVotes"));
        }

        Map<String, Integer> genres = (Map<String, Integer>) results.get("genres");
        Map<String, Integer> types = (Map<String, Integer>) results.get("types");
        Map<String, Integer> dates = (Map<String, Integer>) results.get("dates");

        AggregationsJson a = new AggregationsJson(genres, types, dates);
        return new SearchTitlesJson(titles.length, titles, a);
    }
}
