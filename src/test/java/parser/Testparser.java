package parser;

import org.junit.jupiter.api.Test;
import utils.parser.TitlesImdbParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class Testparser {

    @Test
    public void testParaparser(){
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
}
