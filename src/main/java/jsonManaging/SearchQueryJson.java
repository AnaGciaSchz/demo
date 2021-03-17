package jsonManaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;

/**
 * Class that represented the json for the /search url in the first version
 *
 * @Author Ana Garcia
 */
@Produces(MediaType.APPLICATION_JSON)
public class SearchQueryJson {

    private final String query;
    private final String cluster_name;

    @JsonCreator
    public SearchQueryJson(@JsonProperty("query") String query, @JsonProperty("cluster_name") String cluster_name) {
        this.query = query;
        this.cluster_name = cluster_name;
    }

    public String getQuery() {
        return query;
    }

    public String getCluster_name() {
        return cluster_name;
    }


}
