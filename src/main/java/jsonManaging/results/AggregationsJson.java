package jsonManaging.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;

import java.util.Map;

/**
 * Class that represents the json of the aggregations for the /search url
 *
 * @Author Ana Garcia
 */
@Produces(MediaType.APPLICATION_JSON)
public class AggregationsJson {

    private final Map<String, Integer> genres;
    private final Map<String, Integer> types;

    @JsonCreator
    public AggregationsJson (@JsonProperty("genres") Map<String, Integer> genres, @JsonProperty ("types") Map<String, Integer> types){
        this.genres = genres;
        this.types = types;
    }

    public Map<String, Integer> getGenres(){
        return genres;
    }

    public Map<String, Integer> getTypes() {
        return types;
    }
}