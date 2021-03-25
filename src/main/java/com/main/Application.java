package com.main;

import io.micronaut.runtime.Micronaut;
import utils.elasticSearch.manageIndex.CreateElasticSearchIndex;
import utils.elasticSearch.manageIndex.DeleteElasticSearchInformation;
import utils.elasticSearch.manageIndex.LoadElasticSearchInformation;

import java.io.IOException;

/**
 * Class that runs the application with its main method
 *
 * @Author Ana Garcia
 */
public class Application {

    /**
     * Main method that starts running the application
     *
     * @param args parameters for the application
     */
    public static void main(String[] args) throws IOException {

        Micronaut.run(Application.class, args);
        resetDB("/Users/anamariagarciasanchez/Documents/title.basics.tsv","/Users/anamariagarciasanchez/Documents/title.ratings.tsv");

    }

    /**
     * Method that eliminates the imdb db, and creates a new one based on the ./IMDBCreateindex.json scheme and
     * the titles passed
     * @param titlesPath Path with the titles of the db
     * @throws IOException
     */
    private static void resetDB(String titlesPath, String ratingsPath) throws IOException {
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
    private static void createDB(String titlesPath, String ratingsPath) throws IOException {
        CreateElasticSearchIndex c = new CreateElasticSearchIndex();
        c.createImdbIndex("./IMDBCreateIndex.json");

        System.out.println("created");

        LoadElasticSearchInformation l = new LoadElasticSearchInformation();
        l.loadImdbInformation(titlesPath,ratingsPath);

        System.out.println("inserted");
    }
}
