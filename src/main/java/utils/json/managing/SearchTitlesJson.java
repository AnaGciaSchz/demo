package utils.json.managing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;

/**
 * Class that represents the json for the /search url
 *
 * @Author Ana Garcia
 */
@Produces(MediaType.APPLICATION_JSON)
public class SearchTitlesJson {

    private final int total;
    private final TitleJson[] items;
    private final AggregationsJson aggregations;
    private final SuggestionJson suggestions;

    @JsonCreator
    public SearchTitlesJson(@JsonProperty("total") int total, @JsonProperty("items") TitleJson[] items,
                            @JsonProperty("aggregations") AggregationsJson aggregations,
                            @JsonProperty ("suggestions") SuggestionJson suggestions) {
        this.total = total;
        this.items = items;
        this.aggregations = aggregations;
        this.suggestions = suggestions;
    }

    public int getTotal() {
        return total;
    }

    public TitleJson[] getItems() {
        return items;
    }

    public AggregationsJson getAggregations() {
        return aggregations;
    }

    public SuggestionJson getSuggestions(){
        return this.suggestions;
    }


}
