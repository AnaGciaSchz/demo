package utils.json;

import utils.json.managing.SearchTitlesJson;

import java.text.ParseException;
import java.util.Map;

public interface JsonUtilsInterface {
    SearchTitlesJson getOkJson(Map<String, Object> results) throws ParseException;
}
