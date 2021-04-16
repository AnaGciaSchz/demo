package utils.json.managing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
/**
 * Class that represents the json for the suggestions
 *
 * @Author Ana Garcia
 */
public class SuggestionJson {

    private final ArrayList<String> did_you_mean;

    @JsonCreator
    public SuggestionJson(@JsonProperty("did_you_mean") ArrayList<String> did_you_mean) {
        this.did_you_mean = did_you_mean;
    }

    public ArrayList<String> getDid_you_mean(){
        return this.did_you_mean;
    }
}
