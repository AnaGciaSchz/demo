package jsonManaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;

/**
 * Class that represents the json of the titles for the /search url
 *
 * @Author Ana Garcia
 */
@Produces(MediaType.APPLICATION_JSON)
public class TitleJson {

    private final String id;
    private final String title;
    private final String[] genres;
    private final String type;
    private final String start_year;
    private final String end_year;

    @JsonCreator
    public TitleJson (@JsonProperty("id") Object id, @JsonProperty ("title") Object title,
                      @JsonProperty ("genres") Object genres,@JsonProperty ("type") Object type,
                      @JsonProperty ("start_year") Object start_year, @JsonProperty ("end_year") Object end_year){
        String[] genresArray = ((String) genres).split(",");
        this.id = (String) id;
        this.title = (String) title;
        this.genres = genresArray;
        this.type = (String) type;
        this.start_year = (String) start_year;
        this.end_year = (String) end_year;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getType() {
        return type;
    }

    public String getStart_year() {
        return start_year;
    }

    public String getEnd_year() {
        return end_year;
    }


}
