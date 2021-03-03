package utils.parser;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import utils.parser.TitlesImdbParser;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the TitlesImdbParser
 *
 * @Author Ana Garcia
 */
@MicronautTest
public class TitlesImdbParserTest {

    /**
     * Test to see if the parser returns a correct map with a line that has no end year
     */
    @Test
    public void testCorrectMapWithoutYear(){
        String linea = "tt0000001	short	Carmencita	Carmencita	0	1894	\\N	Documentary,Short";

        TitlesImdbParser parser = new TitlesImdbParser();

        Map<String,Object> list = parser.getTitle(linea);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", "tt0000001");
        jsonMap.put("primaryTitle", "Carmencita");
        jsonMap.put("titleType", "short");
        jsonMap.put("genres", "Documentary,Short");
        jsonMap.put("start_year", "1894");

        assertEquals(jsonMap,list);
    }

    /**
     * Test to see if the parser returns a correct map with a line that has end year
     */
    @Test
    public void testCorrectMapWithYear(){
        String linea = "tt0000001	short	Carmencita	Carmencita	0	1894	1895	Documentary,Short";

        TitlesImdbParser parser = new TitlesImdbParser();

        Map<String,Object> list = parser.getTitle(linea);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", "tt0000001");
        jsonMap.put("primaryTitle", "Carmencita");
        jsonMap.put("titleType", "short");
        jsonMap.put("genres", "Documentary,Short");
        jsonMap.put("start_year", "1894");
        jsonMap.put("end_year", "1895");

        assertEquals(jsonMap,list);
    }
}
