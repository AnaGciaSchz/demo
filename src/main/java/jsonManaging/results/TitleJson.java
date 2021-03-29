package jsonManaging.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that represents the json of the titles for the /search url
 *
 * @Author Ana Garcia
 */
@Produces(MediaType.APPLICATION_JSON)
public class TitleJson {

    private final String id;
    private final String title;
    private final ArrayList<String> genres;
    private final String type;
    private final String start_year;
    private final String end_year;
    private final Double average_rating;
    private final Integer num_votes;

    @JsonCreator
    public TitleJson(@JsonProperty("id") Object id, @JsonProperty("title") Object title,
                     @JsonProperty("genres") Object genres, @JsonProperty("type") Object type,
                     @JsonProperty("start_year") Object start_year, @JsonProperty("end_year") Object end_year,
                     @JsonProperty("average_rating") Object average_rating,@JsonProperty("num_votes") Object num_votes) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        this.id = (String) id;
        this.title = (String) title;
        this.genres = (ArrayList<String>) genres;
        this.type = (String) type;
        this.start_year = (String) start_year;
        this.end_year = (String) end_year;

        if(average_rating!=null) {
            this.average_rating = Double.parseDouble((String) average_rating);
        }
        else{
            this.average_rating = null;
        }


        if(num_votes!=null) {
            this.num_votes = Integer.parseInt((String) num_votes);
        }
        else{
            this.num_votes = null;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getGenres() {
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

    public Double getAverage_rating() { return average_rating;}

    public Integer getNum_votes() { return num_votes;}
}
