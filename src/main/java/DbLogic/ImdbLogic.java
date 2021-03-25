package DbLogic;

import utils.elasticSearch.manageIndex.CreateElasticSearchIndex;
import utils.elasticSearch.manageIndex.DeleteElasticSearchInformation;
import utils.elasticSearch.manageIndex.LoadElasticSearchInformation;

import java.io.IOException;

/**
 * Class to control the logic for the db for imdb
 *
 * @Author Ana Garcia
 */
public class ImdbLogic {

    /**
     * Method that eliminates the imdb db, and creates a new one based on the ./IMDBCreateindex.json scheme and
     * the titles passed
     * @param titlesPath Path with the titles of the db
     * @throws IOException
     */
    public void resetDB(String titlesPath, String ratingsPath) throws IOException {
        DeleteElasticSearchInformation d = new DeleteElasticSearchInformation();
        d.deleteInformation("imdb");

        System.out.println("deleted");

        createDB(titlesPath,ratingsPath);
    }

    /**
     * Method that creates the imdb db based on the ./IMDBCreateindex.json scheme and
     * the titles passed
     * @param titlesPath Path with the titles of the db
     * @throws IOException
     */
    public void createDB(String titlesPath, String ratingsPath) throws IOException {
        CreateElasticSearchIndex c = new CreateElasticSearchIndex();
        c.createImdbIndex("./IMDBCreateIndex.json");

        System.out.println("created");

        LoadElasticSearchInformation l = new LoadElasticSearchInformation();
        l.loadImdbInformation(titlesPath,ratingsPath);

        System.out.println("inserted");
    }
}
