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
    private final Map<String, Integer> dates;

    @JsonCreator
    public AggregationsJson(@JsonProperty("genres") Map<String, Integer> genres, @JsonProperty("types") Map<String, Integer> types, @JsonProperty("dates") Map<String, Integer> dates) {
        this.genres = genres;
        this.types = types;
        this.dates = dates;
    }

    public Map<String, Integer> getGenres() { return genres; }
    public Map<String, Integer> getTypes() {
        return types;
    }
    public Map<String, Integer> getDates() { return dates; }
}